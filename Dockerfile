# =============================================================================
# Fichier     : Dockerfile
# Rôle        : construire l'image de l'API Spring Boot (Workshop Organizer)
#               en deux étages : compilation avec JDK + Gradle,
#               exécution avec JRE
# Utilise     : gradlew, gradle/, build.gradle, settings.gradle, src/
# Utilisé par : docker-compose.yml (exercice 1),
#               pipeline GitLab CI (exercice 2)
# =============================================================================

# ---------- Étage 1 : construction (l'atelier) ----------
# JDK 21 Temurin (Adoptium), LTS supportée jusqu'au 31/12/2029 (endoflife.date)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /build

# Gradle Wrapper + description du projet : cette couche reste en cache
# tant que les dépendances (build.gradle) ne changent pas
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
RUN ./gradlew --no-daemon dependencies > /dev/null

# Code source, puis compilation et empaquetage (commande du README)
# → /build/build/libs/workshop-organizer-<version>.war
# Les tests ne sont pas lancés ici : c'est le rôle du job de test de la CI
COPY src ./src
RUN ./gradlew --no-daemon bootWar

# ---------- Étage 2 : exécution (le moteur seul) ----------
# JRE 21 : de quoi exécuter Java, sans le compilateur ni les outils du JDK
FROM eclipse-temurin:21-jre-alpine

# Sécurité : applique les correctifs Alpine publiés depuis la sortie de
# l'image officielle (même version d'Alpine, correctifs seulement)
RUN apk upgrade --no-cache

# Sécurité : un utilisateur système dédié, sans droits root, avec un numéro
# FIXE (10001) : Kubernetes (runAsNonRoot) ne sait vérifier qu'un uid numérique
RUN addgroup -S -g 10001 app && adduser -S -u 10001 -G app app
WORKDIR /app

# On ne récupère QUE l'archive de l'application (Tomcat est inclus dedans)
COPY --from=build --chown=10001:10001 /build/build/libs/*.war app.war

# L'API ne tourne pas en root
USER 10001:10001

# Documentation : l'API écoute sur le port 8080
EXPOSE 8080

# Démarrage (forme exec : Java reçoit directement les signaux d'arrêt)
ENTRYPOINT ["java", "-jar", "/app/app.war"]
