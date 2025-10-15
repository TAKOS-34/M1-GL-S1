# Prérequis
- sdk install java 8.0.462-tem
- sdk use java 8.0.462-tem
- sudo snap install docker

# Lancer le serveur
- cd server/

### Sans Docker
- mvn clean install
- mvn spring-boot:run

### Avec Docker
- docker build -t soap-server .
- docker run --name soap-server-container -p 8080:8080 soap-server

# Lancer le client
- cd client/

### Sans Docker
- mvn clean install
- mvn spring-boot:run

### Avec Docker
- docker build -t soap-client .
- docker run --name soap-client-container soap-client
