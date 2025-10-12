# Prérequis

- **Java 17** | Pour l'installer, utilisez SDKMAN : `curl -s "https://get.sdkman.io" | bash`, puis installez Java 17 : `sdk install java 17-tem`
- **Maven 3.9.11** | Pour l'installer avec SDKMAN, exécutez : `sdk install maven 3.9.11`

Pour vérifier que Java et Maven sont bien installés, exécutez : `java -version` et `mvn -version`

# Configuration du projet

1.  Allez dans le répertoire `./client/target/classes` et copiez son chemin d'accès complet
2.  Ouvrez le fichier `./server/src/main/java/org/test/rmi/server/main/Application.java`
3.  À la ligne 20, remplacez le chemin existant par celui que vous venez de copier

# Lancement du projet

1.  **Compiler le projet** : Placez-vous à la racine et exécutez la commande `mvn clean install`
    -   En cas de problème, essayez `mvn clean install -U` pour forcer la mise à jour des dépendances
2.  **Lancer le serveur** : Allez dans le répertoire `/server` et exécutez `mvn spring-boot:run`
3.  **Lancer le(s) client(s)** : Allez dans le répertoire `/client` et exécutez `mvn spring-boot:run`

Vous pouvez désormais interagir avec l'application via la console ou via l'interface graphique accessible à l'adresse suivante : `http://localhost:8080`