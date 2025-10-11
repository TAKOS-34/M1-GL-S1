FROM maven:3.9.9-eclipse-temurin-17 AS builder
WORKDIR /build

COPY ./pom.xml .
COPY ./common/pom.xml common/
COPY ./client/pom.xml client/
COPY ./common common/
COPY ./client client/

# Pré-télécharger les dépendances
RUN mvn install -N -f pom.xml
RUN mvn -f common/pom.xml clean install -DskipTests
RUN mvn -f client/pom.xml dependency:go-offline -B
RUN mvn -f client/pom.xml clean package -DskipTests

# Étape runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY --from=builder /build/client/target/*.jar client.jar


EXPOSE 8081
EXPOSE 1099
EXPOSE 6060

# Variables d'environnement pour RMI
ENV RMI_SERVER_HOSTNAME=rmi-server
ENV RMI_PORT=1099

# Volume NFS pour le module commons (optionnel)
VOLUME /app/commons

# Lancer le serveur
ENTRYPOINT ["java", "-Dloader.path=/app/commons/", "--add-opens=java.base/java.lang=ALL-UNNAMED", "-jar", "client.jar"]
#ENTRYPOINT ["sh", "-c", "until nc -z rmi-server 1099; do echo waiting for RMI server; sleep 1; done; java -jar /app/client.jar"]
