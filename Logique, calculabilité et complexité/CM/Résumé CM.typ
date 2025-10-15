= Calculabilité

== Glossaire

Calculable : Une fonction (ou un problème) est calculable si il existe un algorithme / procédure qui pour chaque entrée, produit une réponse en temps fini / nombre fini d’étapes

Non calculable : Une fonction est non calculable si aucun algorithme / procédure permet de calculer sa valeur pour chaque entrée ne donne pas de résultat en temps fini

Décidable / Récursif : Un algorithme peut donner une réponse oui / non en temps fini. La fonction caractéristique du problème est calculable

Semi-décidable / Récursivement énumérable : Un algorithme peut donner la réponse oui / non pour tout entrée appartenant à l’ensemble, mais peut ne jamais s’arrêter pour les entrées n’appartenant pas à l’ensemble

Indécidable : Aucuns algorithmes ne peut, pour toute entrée, déterminer en temps fini si celle-ci appartient ou non à l’ensemble. Sa fonction caractéritique est non calculable

Triviale : Soit toujours vrai, soit toujours faux

Fonction total : Fonction dont le domaine de définition est N tout entier

Fonction non total :  Fonction dont le domaine de définition n’est pas N tout entier. Exemple 1/x

Fonction caractéristique : 

X(x) = {\
    1 si x ∈ E,\
    0 sinon\
}

Fonction semi-caractéristique : 

X(x) = {\
    1 si x ∈ E,\
    non définie sinon\
}

Avec E ensemble des entiers. E est décidable si sa fonction caractéristique est calculable

Ensemble dénombrable : Ensemble dont les éléments peuvent être mis en correspondance bijective avec l’ensemble N

Ensemble indénombrable : Ensemble dont les éléments ne peuvent pas être mis en correspondance bijective avec l’ensemble N. Exemple : ensemble R

Ensemble énumérable : Ensemble du domaine d’une fonction calculable

Ensemble récusrif : Ensemble admettant sa fonction caractéristique total et calculable

== Notions

Fonctions de N → N partielles (ou total)

Une fonction partielle est calculable

Ensemble vide énumérable (ex : programme qui return 0 tout le temps)

A recursif => A enumerable
A recursif => compl(A) recursif
A enumerable et compl(A) enumerable => A recursif (POST)
A < B <=> ∃f calculable ∀x x appartient A <=> f(x) appartient a B
Si A < B alors B rec/enum => A rec/enum
Si A < B alors A pas enum => B pas enum
Rice : Si A = [x | [x | . ] appartient C ] (C prop sur les fonctions) et A non trivial => A non recursif

== Notions de base

=== Encodage entrées sorties

Transformer suite de lettre (x1, x2, …) en entier → 1 devant (1, x1, x2, …) soit nombre binaire et -1 pour revenir au mot

Exemple : 24 + 1 = 25 et 25 en binaire on enlève le 1 devant = 24

=== Programme

a un programme valide / non valide

[ a | x ]  = y Execution a sur entrée x et renvoie de y

[ a | x ] ↓ Converge (finti et renvoie une valeur)

[ a | x ] ↑ Diverge (plante, erreur ou boucle infinie)

[ a | . ] : x → { si [ a | x ] ↓ alors [ a | x ] sinon non définie }

=== Théorème de Post

Si E est énumérable et compl(E) énumérable alors E est récursif

E = dom [ a | . ] = Wa

compl(E) = Wb

=== Temps

Step <\a, x, t> = {\
    0 si on n’obtient pas de convergence de [ a | x ] dans un temps t\
    1 + [ a | x ] sinon\
}