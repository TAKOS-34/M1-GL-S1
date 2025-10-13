# Prérequis

- **Docker** | Pour l'installer, faites : `sudo snap install docker`

# Lancer le projet

1. **Compiler le projet** : Placez-vous à la racine et exécutez la commande `mvn clean install`
2. **Exécutez le fichier setup NFS** avec la commande suivante : `sudo ./setupNFS.sh`
3. **Exécutez le fichier setupCodebase** avec la commande suivante : `sudo ./setupCodebase.sh`
4. **Lancez un build Docker** avec la commande suivante : `sudo docker-compose build`
5. **Lancez le client et le serveur** avec la commande suivante : `sudo docker-compose up`

- Si des problèmes surviennent, arrêtez Docker : `sudo docker-compose down`
- Si les problèmes persistent : `sudo docker rm -f rmi-server` et `sudo docker rm -f rmi-client`

Vous pouvez désormais interagir avec l'application via l'interface graphique accessible à l'adresse suivante : `http://localhost:6060`