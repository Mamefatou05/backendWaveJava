# Étape 1 : Construire l'application
FROM maven:3.8.4-openjdk-17 AS build

# Déclaration d'un répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et settings.xml pour les dépendances
COPY pom.xml .

# Copier tout le projet dans le conteneur
COPY . .

# Étape 2 : Construire l'image à partir de l'artefact généré
FROM openjdk:17-jdk-slim

# Créer un répertoire pour l'application
WORKDIR /app

# Copier l'artefact généré depuis l'étape de build
COPY --from=build /app/target/backendwave-0.0.1-SNAPSHOT.jar /app/backendwave.jar

# Exposer le port utilisé par Spring Boot
EXPOSE 9000

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "/app/backendwave.jar"]
