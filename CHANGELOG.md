# Changelog

## [0.4.0](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/compare/v0.3.0...v0.4.0) (2026-10-25)

### Features

* **helm:** ajoute les valeurs des environnements dev et staging ([03f4fa1](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/commit/03f4fa13bf61cb9ccee04fb1e3c35c1854dffa6b))
* **release:** synchronise l'appVersion des charts Helm ([f714946](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/commit/f714946b39868b82702b5ae089dc6f93655d07ce))

### Bug Fixes

* **config:** lit le mot de passe de la base dans SPRING_DATASOURCE_PASSWORD au lieu d'une valeur en clair ([1734653](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/commit/1734653bd1535de81a30acb81327ef732c0e48a3))
* **deps:** migre l'API vers Spring Boot 4.1.2 (CVE-2026-47884, CVE-2026-47890) ([e1f5409](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/commit/e1f5409cb77c5f686f17316fafeb5c88bde57bcb)), closes [#1](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/issues/1)
* **deps:** passe log4j-api en 2.25.5 pour corriger CVE-2026-49844 ([97e18ff](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/commit/97e18ff9a927fb0b181e4c825c85b6cf06af6b0a)), closes [#2](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/issues/2)

## [0.3.0](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/compare/v0.2.5...v0.3.0) (2026-10-22)

### Features

* **helm:** ajoute le chart Helm workshop-api-chart avec PostgreSQL ([3420823](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/commit/34208231a9588ba12ae14bc85c2a6b28098abe1c))
* **k8s:** complète les manifestes Kubernetes de l'API avec stockage persistant ([1f998ff](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/commit/1f998ffb947ea39ee304bcc584c22364b8fee055))

## [0.2.5](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/compare/v0.2.4...v0.2.5) (2026-10-21)

### Bug Fixes

* **deps:** met à jour Spring Boot 3.5 et corrige les vulnérabilités ([3660e0c](https://gitlab.com/web-device-34-bouchet/workshop-organizer-api/commit/3660e0c4fbbc0fb2fa43b0e155789b56084fcc8c))
