# Prérequis

- Docker | Pour l'installer, faites : `sudo snap install docker`

# Lancer le projet

- Compiler le projet : placez-vous à la racine et faites : `mvn clean install`
- Exécutez le fichier setup NFS avec la commande suivante : `sudo ./setupNFS.sh`
- Exécutez le fichier setupCodebase avec la commande suivante : `sudo ./setupCodebase.sh`
- Lancez un build Docker avec la commande suivante : `sudo docker-compose build`
- Lancez le client et le serveur avec la commande suivante : `sudo docker-compose up`

- Si des problèmes surviennent, arrêtez Docker : `sudo docker-compose down`
- Si les problèmes persistent : `sudo docker rm -f rmi-server` et `sudo docker rm -f rmi-client`

Vous pouvez désormais interagir avec l'application via l'interface graphique accessible à l'adresse suivante : `http://localhost:6060`
