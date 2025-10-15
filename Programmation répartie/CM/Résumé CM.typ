= Rappel

#link("https://www.notion.so/R-sum-CM-181f3211144380959378e7497e8f3d3f?pvs=21")[Lien Notion L3]

== Espace d’adressage

- Code
- Données statiques : variables globales, constantes
- Tas : mémoire dynamique (malloc)
- Pile : variables locales, appels de fonctions

Compteur ordinal : registre de l’adresse de la prochaine instruction

== Processus

```c
// Création de processus
pid_t fork(void);
// 0 dans le fils, PID fils dans le parent (> 0)

// Recouvrement
int execve(const char *pathname, char *const argv[], char *const envp[]);
// Remplace le code et les données du processus courant par un nouveau programm
// Réutilise le PID courant

// Attente
pid_t wait(int *wstatus);
// Permet à un processus parent d’attendre la terminaison d’un fils
// Retourne le PID du fils terminé

// Terminaison
void _exit(int status);
// Termine un processus proprement et renvoie un code d’état à son parent
// Etat de sortie est récupérable par wait() du parent
```

== Communication inter - processus

IPC Locales : signaux, tubes, mémoire partagée, fils de messages

IPC Distantes : socket (TCP / UDP)

=== Signaux

SIGKILL, SIGTERM, SIGINT, SIGHUP, SIGCHLD

=== Tubes

Mécanisme simple de communication unidirectionnelle entre deux processus

pipe() : crée un canal avec une extrémité lecture et une écriture

Héritage via fork() : communication parent–fils

=== Mémoire partagée et files de messages

Voir cours L3

=== Socket

Voir cours L3

= Systèmes répartis

== Définitions

Définition : ensemble de processus autonomes, pouvant être séparé, qui coopèrent dans un objectif commun, sans mémoire centrale ni horloge global, en échangeant des messages en utilisant un système de communication

Taches :

- Diffusion de messages : garantir que tous les processus reçoivent la même information
- Accord / consensus : décider collectivement d’une même valeur
- Exclusion mutuelle : un seul processus à la fois accède à une ressource critique
- Élection d’un coordinateur : choisir un « chef » pour organiser certaines décisions
- Ordonnancement des messages : livraison en respectant la causalité ou un ordre tota
- Réplication cohérente : maintenir des copies identiques d’une donnée malgré les pannes

Avantages :

Tolérance aux pannes, évolutivité, performances, disponibilité, partage de ressources, robustesse et flexibilité

Algorithme distribué :

- Chaque entité exécute le même algorithme que les autres enttiés du système réparti
- Chaque entité du système réparti peut communiquer uniquement avec ses voisins

Contrôle répartie :

- Aucune entité n’a de rôle privilégié
- Tous les sites sont égaux en droit et en devoir
- Chaque site agit à partir d’une connaissance locale et partielle du système
- La coordination se fait via des échanges de messages entre voisins

Problématiques :

Absence d’horloge globale, pannes et tolérance aux fautes, synchronisation, communication incertaine, sécurité, scalabilité

== Modélisation

Syncrone, asyncrone, partiellement syncrone

Un système réparti peut être représenté sous forme de graphe

Chaque noeux :

- Représentent les entités de calcul
- Chaque nœud peut avoir un identifiant unique

Chaque arêtes :

- Représentent les liens de communication
- Une arête (Pi, Pj) signifie qu’un échange de messages est possible entre Pi et Pj

![image.png](attachment:9d06aee1-baa9-4c9c-be1a-1353dd0136b9:image.png)

== Multiplexage

Comment travailler avec plusieurs socket en même temps sans connaitre l’ordre d’arrivé des messages

On peut :

- Connaitre l’ordre d’arrivé
- Modifié le comportement des opérations entrée sortie
- Mécanisme de surveillance des entrée sortie → multiplexage des entrées sortie

Définition : Un moyen de scruter plusieurs descripteurs de fichier ouverts, en attendant qu’un événement se produise sur au moins l’un de ces descripteurs. 

Objectif : Développer un serveur capable de gérer plusieurs clients simultanément sans devoir créer plusieurs processus

=== Mise en place

1. Définition de l’ensemble des sockets a structer en précisent les entrées sorties
2. On scrute les événements définis en une seule opération : opération bloquante
3. Si déblocage, on vérifie pour chaque socket si un événement s’est produit
    - Si oui :
        - on sélectionne un descripteur pour le traiter
        - on effectue l’opération associée (exemple : recv pour lire un message reçu)
        - on réinitialise le descripteur ; on le remet dans la liste à structer

=== fd_set

fd_set représente un ensemble de descripteurs de fichier

```c
void FD_ZERO(fd_set *set) // Initialise à faux les éléments de l’ensemble set

void FD_SET(int desc, fd_set *set) // Ajoute le descripteur desc à la liste des
// descripteurs de *set à scruter, i.e. positionne l’élément à l’indice 
// desc à vrai

select() // se met en attente jusqu’à ce qu’un événement se produise sur au
// moins un descripteur scruté
// Dans ce cas, elle modifie les ensembles passés en paramètres pour ne
// garder que les descripteurs passés à l’état ”prêt” et retirer les autres

int FD_ISSET(int desc, fd_set *set) // Teste si le descripteur desc est dans la
// liste des descripteurs de *set, et si l’indice desc est positionné à vrai

void FD_CLR(int desc, fd_set *set) // Supprime le descripteur desc de la liste 
// des descripteurs de *set à scruter, i.e. positionne l’élément à 
// l’indice desc à faux
```