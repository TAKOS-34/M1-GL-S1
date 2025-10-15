= Définitions de base

- Extensibilité : capacité à se voir ajouter de nouvelles fonctionnalités pour de nouveaux contextes d’utilisation
- Adaptabilité : capacité à voir ses fonctionnalités adaptées à de nouveaux contextes d’utilisation
- Entité générique : entité apte à être utilisée dans, ou adaptée à, différents contextes
- Variabilité : néologisme dénotant la façon dont un système est susceptible de fournir des fonctionnalités pouvant varier dans le respect de ses spécifications
- Paramètre : nom dénotant un élément variable d’un système ou d’un calcul
- Abstraction : Fonction / procédure (avec ou sans paramètres)
- Application : Application d’une fonction / procédure
- Composition : Enchaînement d’appels à des fonctions / procédures
- Polymorphisme :  Langage ou variables et valeurs peuvent avoir (ou appartenir a) plusieurs types, ce qui est polymorphe peut être réutilisé dans de nouveaux contextes
- Entités d’ordre supérieur : Entités qui peuvent prendre en paramètre une autre entité de même nature. Exemple : fonction d'ordre supérieur, peut recevoir une autre fonction en argument ou en retourner une comme résultat
- Fonction d’ordre supérieur : Fonction acceptant une fonction en argument et/ou rend une fonction en valeur
- Paramétrage par un objet : Passer un objet en argument d'une méthode revient à passer tout l'ensemble de ses propres méthodes
- Typage static : identificateurs typés dans le texte du programme (C, Java)
- Typage dynamnique : identificateurs non typés dans le texte du programme (Python, JS)

= Shémas de réutilisation PPO

== Définitions

- Envoie de message : autre nom pour une méthode de classe, faisant apparaiîre le receveur
comme un argument distingué de la méthode
- Receveur courant : au sein d’une méthode M, le receveur courant, accessible via l’identificateur self (ou this), est l’objet auquel a été envoyé le message ayant conduit à l’exécution de M. this ne peut varier durant l’exécution d’une méthode
- Paramètre Implicite : this (ou self) est un paramètre implicite (n’ayant pas besoin d’être déclaré explicitement) de toute méthode. Ceci est vrai dans tous les langages à objets à classes. Ce paramètre est lié au receveur courant à chaque invocation de la méthode
- Liaison dynamique (ou tardive) : l’appel de méthode, ou donc l’envoi de message, se distingue de l’appel de fonction (ou de procédure) en ce que savoir quelle méthode invoquer suite à un appel de méthode donné n’est pas décidable par analyse statique du code (à la compilation), ceci nécessite la connaissance du type du receveur, qui n’est connu qu’à l’exécution

== Description différentielle

Définition d’une sous-classe par expression des différences (propriétés supplémentaires)

```java
class Point3D extends Point {
	private float z;
	public float getZ() { return z; }
	public void setZ(float z) { this.z = z; }
	// ...
}
```

== Spécialisation / redéfinition

Ajout, sur une nouvelle sous-classe, de nouvelles propriétés et la spécialisation de propriétés existantes, en particulier des méthodes

```java
class Point {
	void scale(float factor) {
		x = x * factor;
		y = y * factor;
	}
}

class Point3D extends Point {
	// ...
	void scale(float factor) {
		x = x * factor;
		y = y * factor;
		z = z * factor;
	}
}
```

== Spécialisation partielle

Redéfinition faisant appel à la méthode redéfinie (super)

```java
class Point3D extends Point{
	// ...
	void scale(float factor) {
	super.scale(factor);
	z = z ∗ factor;}}
```

== Paramétrage par spécialisation (Pattern “Template Method”)

Adaptation d’une méthode à de nouveaux contexte sans modification ni duplication de son code

Utilisation de this uniquement en paramètre

Implémentation possible par une sous classe

Accessibles par envoi de message avec liaison dynamique

```java
abstract class Produit {
    protected float TVA;
    float prixTTC() { return prixHT() * (1 + getTVA()); } // méthode adaptable
    abstract float prixHT();
    float getTVA() { return TVA; }
}

class Voiture extends Produit {
    float prixHT() { return prixCaisse() + prixAccessoires(); }
}

class Livre extends Produit {
    protected boolean tauxSpecial = true;
    float prixHT() { ... } // adaptation
    float getTVA() { return tauxSpecial ? 0.055f : 0.196f; }
}
```

Affectation polymorphique ou transtypage ascendant : En présence de polymorphisme d’inclusion, où un type peut être définit comme un sous-type d’un autre, affectation d’une valeur d’un type ST, sous-type de T, à une variable de type statique T

== Paramétrage par composition

Une classe n’instancie pas elle-même ses dépendances, mais les reçoit en paramètres, le client peut donc fournir n’importe quelle sous classe. Les méthodes des objets injectés (parse, generate) deviennent accessibles via la composition

```java
class Compiler {
    Parser p;
    CodeGenerator c;

    Compiler(Parser p, CodeGenerator c) {
        this.p = p;
        this.c = c;
    }

    public void compile(SourceText st) {
        AST a = p.parse(st);
        GeneratedText gt = c.generate(a);
    }
}
```

= Spécificités du typage statique en présence de polymorphisme d’inclusion

Problème global : rendre effective et contrôler la substituabilité

```java
class A{
public T f(X x) {...} }

class B extends A{
public U f(Y y) {...}
...
A a = new B();
X x = new Y() ;
T t = a.f(x);
```

Faire en sorte que a.f(x) s’execute correctement, donc que la méthode f redefinie dans B soit compatible avec l’architecture

== Problème 1 : Réaliser des spécialisations compatibles

Règle de spécialisation de Liskov

- Pas de paramètres additionnels
- Redéfinition contra-variante (inverse à l’ordre de sous-typage) des types des paramètres
- Redéfinition co-variante (respectant l’ordre de sous-typage) des types de retour, y compris les cas d’exceptions

== Problème 2 : Spécialisation conforme versus spécialisation conceptuelle

Solution : invariance des types des paramètres

Exemple :

```java
class A {
	public void f(X x) {...}
}

class B extends A {
	public void f(X x) {...}
}

Y y = new Y();
Z z = new Z();
A a = new B();
a.f(z); −> invoque f de la classe B
```

Autre exemple : Redéfinition de la méthode equals

== Problème 3 : Accès aux membres d’un paramètre dans une spécialisation conforme

La combinaison des solutions aux problèmes #1 et #2 nécessite un mécanisme de transtypage descendant ou “downcast-ing”

```java
if (o instanceof Point) ((Point)o).getx()); else ...
```

== Distinguer spécialisation et surcharge

Surcharge : (en général) une surcharge M’ d’une méthode M est une méthode de même nom externe que M mais possédant des paramètres de types non comparables (non liés par la relation de sous-typage)

Surcharge sur une sous-classe une surcharge M’ sur SC (sous-classe de C) d’une méthode M de C, est une méthode de même nom externe que M qui n’est pas une redéfinition de M, par exemple parce que ne respectant pas la règle de contra-variance

Exemple :

```java
class A {
	public void f(X x) {...} 
}

class B extends A {
	public void f(Y y) {...}
	public void f(Z z) {...} 
}

class X {}
class Y {}
class Z extends X {}

A a = new B(); // Affectation polymorphique
a.f(new Y()); // −> cas 1 : erreur de compilation
a.f(new Z()); // −> cas 2 : invoque f de la classe A
```

== Implantation de l’envoi de message en typage statique faible

```c
A a = ...;
X x = ...;
T t = a.f(x)
```

1. A la compilation :
    1. Recherche d’une méthode f(X) sur A
    2. Recherche de toutes les spécialisations (selon les regles propres au langage) de f(X) sur les sous-types (selon les regles propres au langage) de A
    3. Indexation dans une table, selon le type elle est d´efinie, de toutes les spécialisations
2. A l’exécution : sélection dans la table selon le type dynamique de a, de la méthode f a invoquer (le type dynamique de x est nécessairement compatible avec X)

== Spécialisation d’attribut

Impossible de spécialiser un attribut, cela créer un nouvel attribut, ce n’est pas une redéfinition

= Application aux “Frameworks” et “Lignes de produits”

Framework (Application logicielle partielle) : Dédié à la réalisation de nouvelles applications du domaine visé. Doté de code générique, extensible et adaptable

== Framework versus Bibliothèque

- Une bibliotheque s’utilise, un framework s’étend ou se parametre
- Avec une bibliotheque, le code d’une nouvelle application invoque le code de la bibliotheque
- Le code d’un framework appelle le code d’une nouvelle application

Inversion de contrôle : Le framework appel un code exterieur non nécessairement connu à la compilation

Injection de dépendance : Une injection est une association d’une extension à un point d’extension, c’est un renseignement d’un paramétrage