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

= Introduction

== Technologies utilisées

Dans ce TP, nous utiliserons plusieurs technologies. La première, et la plus importante, est bien évidement *Java RMI*, qui nous permet de manipuler des objets présent sur un serveur à distance via un client. Cette technologie était très utilisés dans le passé mais à souffert de sa dépendance au langage Java, car il lui est exclusif. Nous encapsulerons nos projets RMI dans des projets *Spring Boot*, la principale technologie Web de Java, qui offre une architecture propre grâce à *Maven*, la gestion simple des dépendances, s'occupe nativement de certaines contraintes réseaux en plus d'offrir facilement l'ajout d'un controller pour une interface graphique. Nous utiliserons également *Git* pour versionné le code et le mettre en ligne, il est un outil indispensable en tant que développeur. Ce rapport à été écrit #link("https://typst.app/")[*Typst*], une alternative très solide à Latex, qui fusionnent ce dernier avec Markdown. Les schémas ont été dessiné sur le site #link("https://excalidraw.com/")[*Excalidraw*] et #link("https://mermaid.live")[*Mermaid*].

== Architecture global du projet
La toute première étape dans la création du projet était d'avoir une structure propre et modulable. De ce fait la structure global est la suivante :
\
#figure(
    image("structure_global.png", width: 100%),
    caption: "Shéma de la structure global du projet"
)<structure_global>
\
Nous pouvons voir ici trois projets Spring Boot : server, client et common, avec chacuns leurs pom. Ces trois projets héritent d'un pom parent, à la racine. Ce pom permet de compiler les trois projets en même temps, grâce à la balise `<modules>`. Il permet également d'avoir une version commune de Java et Spring Boot grâce à la basile `<properties>`. Les pom dans les projets précisent leurs `<artifactId>` pour les identifiers ainsi que la version de Java / Spring Boot hériter du parent.\
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
On peut donc voir ici une méthode `formatedToString` déclaré dans le projet common, elle sera utilisable par le client selon l’implantation réel coté server. Le serveur va donc créer un animal, le client récupère le stub du service Animal, et peut ensuite appeler la méthode `formatedToString` pour obtenir les informations sur l'animal crée. Nous pouvons remarquer que l'interface étend `Remote` et toutes les méthodes peuvent lever une exception de type `RemoteException`, cela est spécifique à RMI et permet de gérer les erreurs liés au réseau, comme par exemple si le serveur est coupé. Nous conserverons cette implémentation tout au long du TP.

- Récupération du stub (même si avec Spring c'est un proxy) et appel de la méthode distante coté client :
```java
AnimalService service = (AnimalService) proxy.getObject();
service.formatedToString();
```

On voit ici une utilisation typique de RMI, des méthodes utilisable par le client (ici une affichage) sont déclarées dans une interface partagé par le client et le serveur, cette même interface est implémenté par le serveur dans une classe réel. La classe animal est donc manipulé à distance par le client. Voici un schéma visuel la communication entre le client et le serveur pour cette implémentation :

#figure(
    image("structure_basique.png", width: 100%),
    caption : "Schéma d'une communication Client - Serveur avec RMI"
)

Ce schéma montre la base de la communication Client Serveur en RMI et sera réutiliser tout au long du projet.


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

Nous allons devoir faire en sorte que `Espece`, on doit également transmettre une copie d'espèce et non une référence. Il faut donc que le client puisse appeler une méthode sans modifier l'objet stocker côté serveur (il ne modifira que sa copie local). Pour rendre cela possible, nous allons créer l'interface et la classe dans le module `common`. L'interface de `Espece` va donc étendre `Serializable` afin que le transfère d'espèce soit une copie et non une référence distante.

- Interface de Espèce :
```java
public interface EspeceService extends Serializable {
    EspeceService getEspece();
    void setNom(String nom); // Méthode créer simplement pour faire des tests et retiré après
    [ ... ]
}
```
- Implémentation de l'interface Espèce :
```java
public class Espece implements EspeceService {
    private String nom;
    private int dureeVie;
    [ ... ]

    @Override
    public Espece getEspece() { return this; }

}
```

Dans cette implémentation, le client peut récupérer l'espèce d'un animal et le stocker dans une variable de type `Espece`, puisqu'il connait son implémentation, appeler la méthode `formatedToString` pour consulter l'espèce, mais lors de l'appel de la méthode `setNom`, seul l'objet coté client sera modifié, si l'on rappel la méthode `formatedToString`, le nom restera inchangé. Ci dessous un schéma montrant la différence si `Espece` est une copie ou bien une référence distante :

\
#figure(
    image("copie_vs_ref_distante.png", width: 100%),
    caption: "Schéma de comparaison entre copie et référence distante"
)<copie_vs_ref_distante>
\

== Classe Cabinet

Nous allons ici modifié l'architecture et faire en sorte que seule la classe Cabinet soit distribué et gère les patient (les animaux) ainsi que toutes les autres classes implémenté coté serveur. Nous allons donc mettre en place deux dictionnaires, qui contiendrons le couple \<Nom, Animaux\> et \<Nom, Espece\>. Le client appelera donc des méthodes pour créer, modifier et supprimer des animaux et des espèces, qui seront stockés coté serveur.
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
    [ ... ]

    @Override
    public String getAnimalByNom(String nom) throws RemoteException {
        if (animals.containsKey(nom)) {
            return animals.get(nom).formatedToString();
        }
        return "Erreur : aucuns animaux avec ce nom n'existe";
    }
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

Nous allons maintenant créer une classe coté client, nommé Chat, implémentant l'interface `AnimalService` et faire en sorte que le serveur puisse récupérer cette classe grâce à la codebase. Nous allons donc mélanger dans notre dictionnaire la classe Animal implémenté coté serveur et la classe Chat coté client, il faudra que le dictionnaire d'Animaux soit du type `<String, AnimalService>`. Nous allons également créer une nouvelle méthode d'ajout dans le serveur (et donc dans l'interface), comme montré ci dessous :
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
Grâce à ce code, nous récupérons les classes implémentés dans le client et nous pouvons passé notre animal au serveur. Ci dessous un schéma illustrant le fonctionnement de la codebase :

#figure(
    image("codebase.png", width: 100%),
    caption: "Schéma illustrant la codebase"
)<codebase>

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
La méthode `checkAlerte` sera appelé lors de l'ajout ou de la suppression d'un animal. Le serveur va donc parcourir la liste d'objet d'alerte et appeler la méthode `message` afin d'envoyé l'alerte a chaque client. Nous avons ici un modèle de type Observer Pattern, avec le serveur comme Subject et les clients comme Obeserver. Afin d'éviter les erreurs si un client se déconnecte et que le serveur appel une méthode sur un objet qui n'existe plus, on appellera une méthode `deleteAlerte` afin de supprimer l'objet Alerte de la liste correspondant au client voulant se déconnecter. On considère ici pouvoir gérer plusieurs client manipulant le cabinet en même temps, ce qui est le but de notre implémentation. Ci dessous un schéma illustrant les alertes :

#figure(
    image("alerte.png", width: 100%),
    caption: "Schéma illustrant les alertes"
)

= Architecture global

== Schéma global simplifié de la communication Client - Serveur

L'architecture global de notre projet implémente RMI, avec un client qui peut appeler des méthodes provenant d'une interface commune entre le client et le serveur, sur un objet que le serveur aura implémenté de son coté. Le client manipule donc l'objet à distance simplement en appelant des méthodes sans en connaître l'implémentation (excepté Espece). Voici un schéma simplifié de la communication entre le client et le serveur :
\
#figure(
    image("communication_client_server.png", width: 100%),
    caption: "Schéma global simplifié de la communication Client - Serveur"
)<communication_client_server>
\
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

= Implémentations supplémentaires

== Ajouts de méthodes pour manipuler le cabinet

Plusieurs méthodes pour manipuler le cabinet on été ajouté, telle que :

- Suppression d'un animal à partir de son nom
- Suppression d'une espèce à partir de son nom
- Mise à jour du dossier à partir de son nom
- Se retirer de la liste d'alertes
- S'ajouter à la liste d'alertes

== Command Line Interface (CLI)

Lorsque l'on démarre le client, l'on arrive sur une interface CLI avec toutes les commandes disponibles. Nous avons une boucle infinie contenant : l'affichage d'un menu, la récupération de l'entrée standard de l'utilisateur et un switch case pour déterminer la méthode à appeler. Cette méthode est très rapide a mettre en place et évite ainsi de d'avoir taper en brut dans le main chaque méthode / paramètres a appeler.

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

== Interface graphique

=== L'ajout du controller

A l'aide de Spring Boot, nous pouvons assez rapidement mettre en place une interface graphique. Il suffit d'ajouter un controller Spring Boot ainsi qu'une template Thymleaf. Thymleaf est un moteur de template qui permet des rendus côté serveur à l'aide de Spring Boot. Notre controlleur pourra, grâce à l'annotation `Autowired`, récuper le stub de Cabinet puis assigné des API routes à des appeles de méthodes distantes. Ci dessous un schéma illustrant la communication entre le controller, le client et le serveur :
\
#figure(
    image("web_controller.png", width: 100%),
    caption: "Schéma global simplifié de la communication Client - Serveur"
)<web_controller>
\
Une fois que le client soumet un des formulaire HTTP, voici un extrait de ce qu'il se passe dans le controller :

- Extrait du controller :
```java
@Controller
public class ClientWebController {

    @Autowired
    private CabinetService cabinetService;

    @GetMapping("/")
    public String index(Model model) throws RemoteException {
        model.addAttribute("animaux", cabinetService.getAllAnimals());
        model.addAttribute("especes", cabinetService.getAllEspeces());
        return "index";
    }
    [ ... ]
}
```
Dans l'extrait de code, nous voyons que `cabinetService` sert de stub et appel la méthode pour afficher la liste des animaux et des espèces présentent dans le cabinet, à l'aide des méthodes `getAllAnimals` et `getAllEspeces`. Cette appel est fait lors lors du chargement de la page d'accueil, car la requête est un `GET` sur `/`. Voici un extrait ou l'on utilise cette fois ci des paramètres pour par exemple ajouter un animal :

- Extrait de la méthode `addAnimal` :
```java
@PostMapping("/addAnimal")
public String addAnimal(
        @RequestParam String nom, @RequestParam String nomMaitre,
        @RequestParam String race, @RequestParam String espece,
        RedirectAttributes redirectAttributes) throws RemoteException {

    String serverResponse = cabinetService.addAnimal(nom, nomMaitre, race, espece);

    [ ... ]
    return "redirect:/";
}
```

Lorsque l'utilisateur a remplie le formulaire, on récupère les informations grâce à `@RequestParam` puis cabinetService appel la méthode `addAnimal` avec les paramètres précédement récupérer, ensuite, la page est recharger.

=== La page HTML
Maintenant que nous avons notre controller, il ne manque qu'un fichier HTML afin d'afficher les formulaires, les listes d'animaux et d'espèces avec si possible une belle interface. Après avoir envoyé les fichiers du controlleur, l'implémentation des méthodes coté serveur de Cabinet, de la classe Chat / Dossier à Google Gemini, nous avons une belle et simple interface graphique dont voici un aperçue :

#figure(
    image("ui.png", width: 100%),
    caption: "Apercue de l'interface utilisateur"
)<ui>

== Conteuneurisation du projet avec Docker

Docker est une révolution dans le monde informatique, il permet de résoudre les problèmes de compatibilités entre machine. Avec Docker installé sur la machine et un fichier de configuration pour Docker, nous pouvons lancer n'importe quel projet sous forme de conteuneur isolé et indépendant des autres. Docker est pertinant dans le cadre de notre projet, car nous utilisons un certains nombres de technologie necessitant des dépendances, Docker nous facilitera la tache pour lancer notre projet n'importe ou. Le principale défi ici était de réaliser la codebase, nous allons pour cela monter un volume NFS afin de partagé les classes entre le client et le serveur. Ci dessous un schéma de la structure de fichier avec Docker :

#figure(
    image("structure_docker.png", width: 100%),
    caption: "Shéma de la structure global du projet avec Docker"
)<structure_docker>


Nous avons 3 nouveaux fichiers et deux `Dockerfile` dans les modules client et serveur. Le fichier `setupNFS` et `setupCodebase` permettent de monter un volume NFS et de mettre les fichiers compilés dans le répertoire `/srv/nfs/commons`, c'est une étape manuel a faire avant d'executer les commandes Docker. Nous pouvons ensuite lancer la commande `docker-compose build` afin de créer les images Docker à partir des Dockerfile du client et du server. Finalement, nous lançons la commande `docker-compose up` pour démarrer nos conteneurs à partir des images créer, c'est a cet étapes que les conteuneurs récupères les fichiers présents dans le volume NFS. Le projet est donc accessible via l'interface graphique. Ci dessous un schéma illustrant les différentes étapes lors du lancement des commandes :

#figure(
    image("docker.png", width: 80%),
    caption: "Schéma illustrant "
)

Nous voyons ici toute la puissance de Docker, en quelques commandes nous sommes sur que notre projet peut se lancer peu importe la machine.

#pagebreak()

= Conclusion

Durant ce projet, nous avons abordés plusieurs technologies qui sont certes plus utilisées aujourd'hui, mais ces dernières constitues la base de ce qui est aujourd'hui utilisé. Nous avons appris à réutiliser un paradigme que nous connaissions déjà (la programmation orienté objet) pour un autre type d'utilisation. Cela peut également nous apporter un esprit critique sur les Technologies d'aujourd'hui maintenant que nous savons ce qui été précédement utilisé, avec leurs qualitées mais également leurs défauts. Ce projet consolide également notre manière de nous organisé pour apprendre quelque chose de nouveau, concevoir une solution tout en pensant à la structure d'un rapport et d'une vidéo avec une date limite. Ces points peuvent être cruciaux car ils pourront nous être demandé en entreprise surtout dans le cadre du génie logiciel. Pour conclure, l'implémentation que nous aurions avoir en plus serait l'intégration d'une base de données, qui prévaut largement sur le stockage des informations dans des dictionnaires sur le serveur, ce qui est actuellement fait. Cette implémentation n'a pas été faites par manque de temps, surtout a cause de la sous estimation du temps d'implémentation et d'écriture du rapport, mais aurais été très pertinante dans le cadre de ce projet.

#pagebreak()

= README - Version Normal
== Prérequis

- *Java 17* | Pour l'installer, utilisez SDKMAN : `curl -s "https://get.sdkman.io" | bash`, puis installez Java 17 : `sdk install java 17-tem`
- *Maven 3.9.11* | Pour l'installer avec SDKMAN, exécutez : `sdk install maven 3.9.11`

Pour vérifier que Java et Maven sont bien installés, exécutez : `java -version` et `mvn -version`

== Configuration du projet

+  Allez dans le répertoire `./client/target/classes` et copiez son chemin d'accès complet
+  Ouvrez le fichier `./server/src/main/java/org/test/rmi/server/main/Application.java`
+  À la ligne 20, remplacez le chemin existant par celui que vous venez de copier

== Lancement du projet

+  *Compiler le projet* : Placez-vous à la racine et exécutez la commande `mvn clean install`
    -   En cas de problème, essayez `mvn clean install -U` pour forcer la mise à jour des dépendances
+  *Lancer le serveur* : Allez dans le répertoire `/server` et exécutez `mvn spring-boot:run`
+  *Lancer le(s) client(s)* : Allez dans le répertoire `/client` et exécutez `mvn spring-boot:run`

Vous pouvez désormais interagir avec l'application via la console ou via l'interface graphique accessible à l'adresse suivante : `http://localhost:8080`

= README - Version Docker
== Prérequis

- *Docker* | Pour l'installer, faites : `sudo snap install docker`

== Lancer le projet

+ *Compiler le projet* : placez-vous à la racine et faites : `mvn clean install`
+ *Exécutez le fichier setup NFS* avec la commande suivante : `sudo ./setupNFS.sh`
+ *Exécutez le fichier setupCodebase* avec la commande suivante : `sudo ./setupCodebase.sh`
+ *Lancez un build Docker* avec la commande suivante : `sudo docker-compose build`
+ *Lancez le client et le serveur* avec la commande suivante : `sudo docker-compose up`

- Si des problèmes surviennent, arrêtez Docker : `sudo docker-compose down`
- Si les problèmes persistent : `sudo docker rm -f rmi-server` et `sudo docker rm -f rmi-client`

Vous pouvez désormais interagir avec l'application via l'interface graphique accessible à l'adresse suivante : `http://localhost:6060`