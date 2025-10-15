= Structure réel d’une base de données

Databse

- Fichiers
    - Header (type, nombre de blocs)

- Blocs

- Header (N : nombres de records, place restante dans le bloc)
- Free space
- Records [ Record 1, …, …, Record N ]

- Header

- Values [ 2309, Alice, Pars ]

= Optimisation de requête

Plan d’execution en deux parties : execution logique (algèbre) et execution physique (opérateurs)

== PEL (Plan execution logique)

Role : Optimiser les requête en faisant des PEL et choisir le plus optimisé

#figure(image("images/f1.png", width: 60%))

#figure(image("images/f2.png"))

== PEP (Plan execution physique)

Role : Choisir comment accéder aux données

PEL est un arbe d’itérateurs

- Un itérateur consomme des n-uplets / produit des n-uplets à la demande

=== Calcul du coût

Temps d’exécution (obtenir tous les uplets) ou temps de réponse (obtenir le premier uplet)

=== Index

Structure de données pour acceder rapidement à une donnée avec un couple : valeur de l’attribut / pointeur vers la donnée

Recherche de l’index via Arbres B+ dans Oracle ou via Bitmap

Accès 

=== Accéder aux données

- Parcours séquentiel (FullScan)
- Parcours d’index (IndexScan)
- Accès par adresse (DirectAccess)
- Test de la condition (filter)

Jointure avec index :

- Index join
    - Jointure naturelle sur la condition de jointure :
        - Parcours séquentiel de la 1ère table contenant la clé étrangère
        - Utilisation de la clé étrangère pour un accès par index à la clé primaire de la 2ème table
        - Utilisation de l’adresse avec un accès direct pour récupérer les informations de la 2ème table

Jointure sans index :

- Boucle imbriquées (Nested loop join)
    - Enumeration de toutes les solutions possibles, acceptables pour deux petites tables
- Tri-fusion (Merge sort join)
    - Très couteut
- Hachage (Hash join)
    - Hachage de la table principales et pour chaque ligne de la table secondaire, on hache la colonne de la jointure et verifie si ça match avec la table principale. Très rapide quand une des deux tables est petites

=== Bonnes pratiques

Eviter :

- select \*, tri, distinct, order / group by, filtrer le plus vite possible
- indexer inutilement des colonnes

Favoriser :

- Union / Union All, Exists par rapport au In, Attention In / Not in quand valeurs null le sgbd les sautes

= Entrepôt de données

Les db relationnels sont insuffisant pour stocker beaucoup de données

Séparation des données d’une db vers un entrepôt de données

Permet de stocker beaucoup de données pour faire des analyses

Souvent appelé avec des fonctions d’agrégations (group by)

Construit pour un usage spécifique et non général (pour des requêtes spécifiques)

== Shéma étoile

#figure(image("images/f3.png"))

Fait : Evenement dans le monde réel (sale), 95% des lignes, composé de clé étrangère en int

Dimension : Attributs décrivant le fait (city, date), 5% des lignes, beaucoup de description textuelle

== ROLLUP / CUBE

#figure(image("images/f4.png"))

#figure(image("images/f5.png"))

== Transactionnels / snapshots

Transactionnel (évènement ponctuel) :

- Niveau de granularité fin (chaque événement/transaction est enregistré)
- Exemple : une ligne de ticket de caisse, une commande passée, un appel téléphonique
- Sert pour des analyses détaillées (nombre de ventes, montant total, etc)

Snapshot (état mesuré régulièrement) :

- Capture l’état d’un système à un moment donné
- Exemple : solde d’un compte bancaire en fin de journée, stock disponible chaque soir
- Sert pour analyser l’évolution d’un état au fil du temps (tendances, inventaire, suivi)

Les mesure :

- Additif : la mesure peut être additionnée sur toutes les dimensions (temps, produit, client…)
    - Exemple : montant des ventes, nombre d’articles vendus
- Semi-additif : la mesure peut être additionnée sur certaines dimensions seulement, pas toutes
    - Exemple : solde d’un compte → on peut additionner par client, par agence, mais pas sur le temps (additionner les soldes journaliers n’a pas de sens)
- Non-additif : la mesure ne peut pas être additionnée
    - Exemple : taux de marge, pourcentage, ratio → il faut faire des moyennes pondérées ou d’autres calculs, mais pas de somme brute

== Les tables ponts

Sert à résoudre une relation many-to-many entre deux tables de dimension dans un entrepôt de données

Exemple :

#figure(image("images/f6.png"))

Ici il y un problème potentiel lors du GROUP BY, le livre C sera vendu 1M de fois au lieu de 500K

Solution, création d’une table PONT :

#figure(image("images/f7.png"))

```sql
SELECT SUM(copies* percentage)
FROM BookSales, Bridge
WHERE book = Bridge.book
AND Bridge.author = author
GROUP BY book
```

![image.png](attachment:970a1492-8b9f-4fcb-9127-68f890369b58:image.png)

== Traiter de très grandes tables

- Normalisation (3FN)
- Partitionner :
    - Par ligne, exemple : par pays (France, USA, …)
    - Par colonne, exemple : avec les colonnes jamais changer, moyennement changé et souvent changé
- Partitionnement hybride : mélange entre le partitionnement par ligne et par colonne

== Update

- Reécrire sur la valeur en brut, simple et rapide mais ne conserve pas d’historique
- Versionning, exemple : ajouter deux valeurs (ValidFrom, ValidTo), très lourd
- Duppliquer les attribus, exemple : ajouter Rating et OldRating, peut avoir n valeurs

On peut combiner les trois méthodes en fonction des besoins

== Insertion de sources de données

- Si nouvelles attribue dimensions ou faits : valeur par défaut