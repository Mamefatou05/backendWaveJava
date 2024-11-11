# Étape 1 : Construire l'application
FROM maven:3.8.4-openjdk-17 AS build

# Créer un répertoire de travail
WORKDIR /app

# Copier le fichier pom.xml et télécharger les dépendances
COPY pom.xml .


# Télécharger les dépendances sans construire le projet
RUN mvn dependency:go-offline

# Copier tout le projet dans le conteneur
COPY . .

# Construire le projet
RUN mvn clean package -DskipTests

# Étape 2 : Construire l'image à partir de l'artefact généré
FROM openjdk:17-jdk-slim

# Créer un répertoire pour l'application
WORKDIR /app

# Copier l'artefact généré du conteneur de build
COPY --from=build /app/target/backendwave-0.0.1-SNAPSHOT.jar /app/backendwave.jar

# Exposer le port utilisé par Spring Boot
EXPOSE 8007

# Commande pour lancer l'application
ENTRYPOINT ["java", "-jar", "/app/backendwave.jar"]
