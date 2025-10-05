# Prérequis

- Java 17 | Pour l'installer, utilisez SDKMAN : `curl -s "https://get.sdkman.io" | bash`, puis installez Java 17 : `sdk install java 17-tem`
- Maven 3.9.11 | Pour l'installer avec SDKMAN, faites : `sdk install maven 3.9.11`

Pour vérifier que Java et Maven sont bien installés, faites : `java -version` et `mvn -version`

# Lancer le projet

- Compiler le projet : placez-vous à la racine et faites : `mvn clean install`
- S'il y a des problèmes de compilation, essayez : `mvn clean install -U` pour purger complètement le projet avant de le compiler
- Lancer le serveur : placez-vous dans `/server` et faites : `mvn spring-boot:run`
- Lancer le/les client(s) : placez-vous dans `/client` et faites : `mvn spring-boot:run`