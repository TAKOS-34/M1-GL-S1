# ------------------------------------------------------------
# Étape 1 : Build Maven (compile commons + server)
# ------------------------------------------------------------
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build

# --- Copier les POM pour profiter du cache Docker ---
COPY ./pom.xml .
COPY ./common/pom.xml common/
COPY ./server/pom.xml server/
COPY ./common common/
COPY ./server server/

# Pré-télécharger les dépendances
RUN mvn install -N -f pom.xml
RUN mvn -f common/pom.xml clean install -DskipTests
RUN mvn -f server/pom.xml dependency:go-offline -B
RUN mvn -f server/pom.xml clean install -DskipTests

# ------------------------------------------------------------
# Étape 2 : Image d'exécution légère
# ------------------------------------------------------------
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copier le JAR compilé du serveur
COPY --from=builder /build/server/target/*.jar app.jar

# Expose le port HTTP Spring Boot et le port RMI
EXPOSE 8081
EXPOSE 1099

# Variables d'environnement pour RMI
ENV RMI_SERVER_HOSTNAME=rmi-server
ENV RMI_PORT=1099

# Volume NFS pour le module commons (optionnel)
VOLUME /app/commons

# Lancer le serveur
#ENTRYPOINT ["java", "-jar", "./app.jar"]
# Dockerfile du SERVEUR
# ...
ENTRYPOINT ["java", "-Djava.security.manager=allow", "-Djava.rmi.server.hostname=${RMI_SERVER_HOSTNAME}", "-jar", "app.jar"]