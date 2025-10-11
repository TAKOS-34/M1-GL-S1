#import "@preview/modern-report-umfds:0.1.2": umfds

#show: umfds.with(
    title: [
        *M1 Génie Logiciel* \ \ 
        HAI704I : Architectures Logicielles Distribuées \
        TP1 : RMI \ \ \
    ],
    authors: (
        "DURAND Elliot",
    ),
    date: "12 Octobre 2025",
    lang: "fr",
)

#outline()
#pagebreak()

= Structure du projet
La toute première étape dans la création du projet était d'avoir une structure propre et modulable. De ce fait la structure global est la suivante :
\
#figure(
    image("f1.jpeg", width: 100%),
    caption: "Schéma simplifié de la communication Client - Serveur"
)<f1>
\
Nous pouvons voir ici trois projets Spring Boot : server, client et common, avec chacuns leurs pom. Ces trois projets héritent d'un pom parent, à la racine. Ce pom permet de compiler les trois projets en même temps, grâce à la balise ```<modules>```. Il permet également d'avoir une version commune de Java et Spring Boot grâce à la basile ```<properties>```. Les pom dans les projets précisent leurs ```<artifactId>``` pour les identifiers ainsi que la version de Java / Spring Boot hériter du parent.\
Comme leur noms l'indique, common contient les interfaces avec les méthodes que le client pourra utiliser sur les objets distants. Server contient les implémentations réels des classes ainsi que des méthodes déclarées dans les interfaces. Le client récupère le stub des objets afin d'appeler les méthodes déclaré dans common et manipuler les objets à distance.

#pagebreak()

= Conception de la solution

== Une première version simple

=== Implémentation de Animal en tant que classe distante

La première étape du projet était la mise en place d'une version simple, avec une classe animal distribué, contenant des attributs simple, tels que le nom, le nom du maître, la race et l'espèce. Une méthode simple permettant l'affichage des informations de la classe animal coté client doit être réalisé. Le serveur doit donc créer un animal, le rendre distribuer, donc envoyer le stub au client, pour que ce dernier puisse afficher les informations de l'objet. Voici un exemple d'implémentation :

- Interface AnimalService dans le projet common :
```java
public interface AnimalService extends Remote {
    String formatedToString() throws RemoteException;
}
```
\

- Implémention réel de la classe Animal dans le projet server :
```java
public class Animal implements AnimalService {
    private String nom;
    private String nomMaitre;
    [ ... ]

    @Override
    public String formatedToString() throws RemoteException {
        return "Nom : " + nom + ", Maitre : " + nomMaitre + ", Race : " + race + ", Espece : " + espece + ", Dossier : " + dossier;
    }
}
```
\
On peut donc voir ici une méthode formatedToString() déclaré dans le projet common, elle sera utilisable par le client selon l’implantation réel coté server. Le serveur va donc créer un animal, le client récupère le stub du service Animal, et peut ensuite appeler la méthode formatedToString() pour obtenir les informations sur l'animal crée. Nous pouvons remarquer que l'interface étend `Remote` et toutes les méthodes peuvent lever une exception de type `RemoteException`, cela est spécifique à RMI et permet de gérer les erreurs liés au réseau, comme par exemple si le serveur est coupé. Nous conserverons cette implémentation tout au long du TP.

- Récupération du stub (même si avec Spring c'est un proxy) et appel de la méthode distante coté client :
```java
AnimalService service = (AnimalService) proxy.getObject();
service.formatedToString();
```
\
- Exemple de ce qui peut être affiché coté client :
```text
Nom : Kyojin, Maitre : Elliot, Race : Aucune, Espece : Chat de gouttière, Dossier : Dossier_Kyojin
```
\
On voit ici une utilisation typique de RMI, des méthodes utilisable par le client (ici une affichage) sont déclarées dans une interface partagé par le client et le serveur, cette même interface est implémenté par le serveur dans une classe réel. La classe animal est donc manipulé à distance par le client.

=== Ajout d'un gestionnaire de sécurité

On ajoute ensuite un gestionnaire de sécurité, il permet principalement de gérer la sécurité (ici accès en lecteur de fichiers) des JVM client serveur. Dans notre cas on va donner toutes les autorisations possibles, pour permettre au client et au serveur de récupérer les interfaces dans le projet common, mais aussi plus tard pour quand on integrera la codebase dans le projet.

- Implémentation coté serveur et client :
```java
String policyUrl = Objects.requireNonNull(Application.class.getClassLoader().getResource("security.policy"))
    .toString();
System.setProperty("java.security.policy", policyUrl);
System.setSecurityManager(new SecurityManager());
```
\
- Fichier `security.policy` :
```text
grant {
    permission java.security.AllPermission;
};
```
\
Le fichier `security.policy` est chargé par le client et le serveur et il définit aucunes restrictions aux JVM.

=== Ajout du dossier de suivie à l'animal

Nous allons définir une nouvelle classe Dossier qui sera un attribut de la classe Animal, que l'on modifiera au préalable, on créer également une interface DossierService qui sera utile pour l'implémentation de Cabinet plus tard.
- Implémentation de Dossier coté serveur :
```java
public class Dossier implements DossierService {
    private String nom;
    
    public String getNom() throws RemoteException { return nom; }

    public void setNom(String nom) throws RemoteException { this.nom = nom; }
}
```
\

=== Espèce devient une classe

Nous allons faire a peu près la même chose que Dossier implémenté à la question précédente mais faire en sorte de transmettre des copies d'espèces et non des références distantes. Nous allons donc étendre la classe `Serializable` dans l’interface de Espèce.

- Interface de Espèce :
```java
public interface EspeceService extends Serializable {
    String formatedToString();
}
```
- Implémentation coté serveur de l'interface Espèce :
```java
public class Espece implements EspeceService {
    private String nom;
    private int dureeVie;

    [ ... ]
}
```

== Classe Cabinet

Nous allons ici modifié l'architecture et faire en sorte que seule la classe Cabinet soit distribué et gère les patient (les animaux) ainsi que toutes les autres classes implémenté coté serveur (Animal, Dossier et Espèce). Nous allons donc mettre en place deux dictionnaires, qui contiendrons le couple \<Nom, Animaux\> et \<Nom, Espece\>. Le client appelera donc des méthodes pour créer, modifier et supprimer des animaux et des espèces, qui seront stockés coté serveur.
- Interface de Cabinet :
```java
public interface CabinetService extends Remote {
    String getAnimalByNom(String nom) throws RemoteException;
    [ ... ]
}
```
- Implémentation coté serveur de la classe Cabinet :
```java
public class Cabinet implements CabinetService {
    private HashMap<String, Animal> animals = new HashMap<>();
    private HashMap<String, Espece> especes = new HashMap<>();

    @Override
    public String getAnimalByNom(String nom) throws RemoteException {
        if (animals.containsKey(nom)) {
            return animals.get(nom).formatedToString();
        }
        return "Erreur : aucuns animaux avec ce nom n'existe";
    }

    [ ... ]
}
```
\
Nous avons encore ici l'utilisation basique de RMI, des méthodes utilisables par le client dans l'interface et implémenté coté serveur. Nous pouvons remarquer que les classes implémenté depuis le début du TP (Animal, Dossier) coté serveur n'étendent pas `UnicastRemoteObject`, car lors du lancement du serveur, l'objet `RmiServiceExporter` est créer et s'occupe d'exporter automatiquement l'objet.

== Création d'animaux

Nous allons donc déclarer une fonction d'ajout dans l'interface et l'implémenter coté serveur :
- Implémentation de la classe d'ajout d'animaux coté serveur :
```java
public String addAnimal(String nom, String nomMaitre, String race, String espece) throws RemoteException {
    [ ... ]
    Espece realEspece = getEspeceByNomCabinet(espece);
    Animal newAnimal = new Animal(nom, nomMaitre, race, realEspece, dossier);
    animals.put(nom, newAnimal);
    return "Animal ajouté";
}
```
Le client va donc donner le nom de l'animal, le nom du maître, la race ainsi que l'espèce. Pour trouver l'espèce, le serveur appellera une méthode pour trouver l'espèce dans le dictionnaire dédié aux espèces et renverra une erreur si jamais l'espèce n'existe pas. Le serveur créera ensuite l'objet Animal et l'ajoutera dans le dictionnaire dédié aux animaux.

== Téléchargement de code

Nous allons maintenant créer une classe coté client, nommé Chat, implémentant l'interface `AnimalService` et faire en sorte que le serveur puisse récupérer cette classe grâce à la codebase. Nous allons donc mélanger dans notre dictionnaire la classe Animal implémenté coté serveur et la classe Chat coté client, il faudra que le dictionnaire d'Animaux soit du type `AnimalService`. Nous allons également créer une nouvelle méthode d'ajout dans le serveur (et donc dans l'interface), comme montré ci dessous :
- Méthode d'ajout d'animal spéciale coté serveur :
```java
@Override
public String addAnimalSpecial(AnimalService animal) throws RemoteException {
    [ ... ]
    animals.put(animal.getNom(), animal);
    return "Animal spéciale ajouté";
}
```
- Classe Chat coté client :
```java
public class Chat implements AnimalService, Serializable {
    private String nom;
    private String nomMaitre;
    [ ... ]

    @Override
    public String formatedToString() {
        return "Nom : " + nom + ", Maitre : " + nomMaitre + ", Race : " + race + ", Couleur du pelage : " + couleurPelage;
    }
}
```
\
Nous pouvons remarquer que l'on utilise encore `Serializable`, car nous allons passer des copies de notre objet au serveur, et non des références distantes. Notre interface AnimalService étend `Remote` et donc par défaut, la classe chat implémentant cette interface passera des références distances au serveur, ce que nous ne voulons pas. En effet, nous voulons recevoir une copie afin de la stocker dans le dictionnaire, afin que le serveur garde la main sur les objets que le client va manipuler à distance. 
\
Il nous reste à implémenter le principe de codebase coté serveur, afin que ce dernier puisse connaitre l'implémentation de Chat.
- Main coté Serveur :
```java
String codebase = Paths.get("/home/username/TP1/client/target/classes")
  .toUri().toString();
System.setProperty("java.rmi.server.codebase", codebase);
```
Grâce à ce code, nous récupérons les classes implémentés dans le client et nous pouvons passé notre animal au serveur.

== Les alertes

Pour la dernière étapes du TP, nous allons faire l'inverse de ce que nous faisions. Le client va être alerté lorsqu'un certain seuil d'animaux coté serveur sera franchi. Nous allons donc créer une nouvelle interface, cette fois ci implémenté coté client, et le serveur appellera la méthode de l'interface pour envoyé l'alerte au client. Le client va donc créer un objet qui provient de la classe Alerte qu'il aura implémenté, puis enverra cet objet au serveur qui le stockera dans une liste à l'aide d'une méthode `AddAlerte`.

- Classe Alerte coté client :
```java
public class Alerte extends UnicastRemoteObject implements AlerteService {
    [ ... ]

    @Override
    public void message(String message) throws RemoteException {
        System.out.println(message);
    }
}
```
Nous pouvons voir ici que Alerte étend `UnicastRemoteObject` afin de passer des références distantes pour que le serveur puisse appeler la méthode message().
- Utilisation de Alerte coté serveur :
```java
public class Cabinet implements CabinetService {
    ArrayList<AlerteService> alertes = new ArrayList<>();

    [ ... ]
    public void checkAlerte() throws RemoteException {
        int size = animals.size();
        if (size == 100 ||  size == 500 || size == 1000) {
            for (AlerteService alerte : alertes) {
                alerte.message("Le cabinet à maintenant : " + size + " animaux\n");
            }
        }
    }
}
```
La méthode `checkAlerte` sera appelé lors de l'ajout ou de la suppression d'un animal. Le serveur va donc parcourir la liste d'objet d'alerte et appeler la méthode `message` afin d'envoyé l'alerte a chaque client. Nous avons ici un modèle de type Observer Pattern, avec le serveur comme Subject et les clients comme Obeserver. Afin d'éviter les erreurs si un client se déconnecte et que le serveur appel une méthode sur un objet qui n'existe plus, on appellera une méthode `deleteAlerte` afin de supprimer l'objet Alerte de la liste correspondant au client voulant se déconnecter. On considère ici pouvoir gérer plusieurs client manipulant le cabinet en même temps, ce qui est le but de notre implémentation.
#pagebreak()

= Architecture global

== Schéma simplifié de la communication Client - Serveur

L'architecture global de notre projet implémente RMI, avec un client qui peut appeler des méthodes provenant d'une interface commune entre le client et le serveur, sur un objet que le serveur aura implémenté de son coté. Le client manipule donc l'objet à distance simplement en appelant des méthodes sans en connaître l'implémentation. Voici un schéma simplifié de la communication entre le client et le serveur :
\
#figure(
    image("f2.png", width: 100%),
    caption: "Schéma simplifié de la communication Client - Serveur"
)<f2>

Nous avons un échange d'informations préalable entre le client et le serveur afin qu'ils puissent communiquer, dans notre cas, la récupération du stub de Cabinet par le client, l'envoie de l'objet Alerte et le serveur qui télécharge la classe Chat via la codebase

== Méthodes à disposition du client

Le client pourra utiliser les méthodes mis a sa disposition afin de manipulet l'objet cabinet à distance en effectuant les actions suivantes :

- Ajouter un animal
- Ajouter un animal avec sa classe créer dans le client 
- Récupérer les informations d'un animal à partir de son nom
- Récupérer la liste de tous les animaux
- Ajouter une espèce
- Récupérer les informations d'une espèce à partir de son nom
- Récupérer la liste de toutes les espèces
- Récupérer un dossier à partir de son nom
- Récupérer la liste de tous les dossiers

#pagebreak()

= Implémentations supplémentaires

== Ajouts de méthodes pour manipuler le cabinet

Plusieurs méthodes pour manipuler le cabinet on été ajouté, telle que :

- Suppression d'un animal à partir de son nom
- Suppression d'une espèce à partir de son nom
- Mise à jour du dossier à partir de son nom
- Se retirer de la liste d'alertes
- S'ajouter à la liste d'alertes

== Command Line Interface (CLI)

Lorsque l'on démarre le client, l'on arrive sur une interface CLI avec toutes les méthodes disponibles
- Extrait du CLI :
```text
---------------------------------------------------------
> Menu :
- 1 : Ajouter un animal
- 2 : Ajouter une espèce
- 3 : Ajouter un chat

- 4 : Chercher un animal par nom

[ ... ]

- quit : Sortir du programme
---------------------------------------------------------
```


#pagebreak()
= README
== Prérequis

- Java 17 | Pour l'installer, utilisez SDKMAN : `curl -s "https://get.sdkman.io" | bash`, puis installez Java 17 : `sdk install java 17-tem`
- Maven 3.9.11 | Pour l'installer avec SDKMAN, faites : `sdk install maven 3.9.11`

Pour vérifier que Java et Maven sont bien installés, faites : `java -version` et `mvn -version`

== Lancer le projet

- Compiler le projet : placez-vous à la racine et faites : `mvn clean install`
- S'il y a des problèmes de compilation, essayez : `mvn clean install -U` pour purger complètement le projet avant de le compiler
- Lancer le serveur : placez-vous dans `/server` et faites : `mvn spring-boot:run`
- Lancer le/les client(s) : placez-vous dans `/client` et faites : `mvn spring-boot:run`