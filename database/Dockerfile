# =============================================================================
# Fichier     : database/Dockerfile
# Rôle        : image PostgreSQL de l'API, l'officielle SANS l'outil « gosu ».
#               gosu ne sert qu'au démarrage, pour passer de root à postgres ;
#               trivy y trouve 25 failles (bibliothèque Go avec laquelle il est
#               compilé). Ici, le conteneur démarre DIRECTEMENT sous postgres :
#               gosu devient inutile, on le supprime.
#               Résultat : 0 faille haute ou critique (trivy), jamais de root.
# Utilisé par : docker-compose.yml (service db)
# =============================================================================

# Image officielle, version précise : PostgreSQL 18.6 sur Alpine 3.24
# (PostgreSQL 18 supporté jusqu'au 14/11/2030 ; la 13 fournie est en fin de vie)
FROM postgres:18.6-alpine3.24

# Suppression de gosu (inutile sans démarrage en root)
RUN rm /usr/local/bin/gosu

# Utilisateur postgres de l'image, désigné par son numéro (70) : le script de
# démarrage, lancé sans root, n'appelle pas gosu
USER 70:70