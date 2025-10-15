= Introduction

Un compilateur traduit efficacement un langage de haut niveau (adapté à l’esprit humain) vers un langage de bas niveau (conçu pour être exécuté efficacement par une machine)

= MIPS

== Registres généraux

r0 à r31

- r0 = 0

- a0 - a3, ra = Passage d’arguments
- v0 - v1 = Renvoi de résultats

- s0 - s7 = Sauvgardés par l’appelé
- t0 - t9 = Non sauvargdés par l’appelé (appels de fonctions)

- sp, fp = Pointeurs vers la pile
- gp = Pointeur vers les données

- k0 - k1, at = Réservé par le système

== Instructions

- *lw* dest, offset : Lecture : Transfert de offset a dest
- *sw* source, offset : Ecriture : Transfert de offset dans le registre de base et dans source

- *li* dest, constant : La constante constant est transféré vers registre dest

- *addi* dest, source, constant : Somme de constant et de source dans dest
- *move* dest source : Transfert de source dans dest
- *neg* dest, source : Negation de source dans dest

- *add* dest, source1, source2 : Somme de source1 source2 dans dest (sub, mul, div)
- *slt* dest, source1, source2 : Comparaison : 1 si le source1 < source2, 0 sinon (sle, sgt, sge, seq, sne)

- *j* adress : Saut à adress
- *bgtz* source, adress : Comparaison : si source > 0 (bgez, blez, bltz)
- *beq* source1, source2, address : Comparaison : si source1 = source2 j to address (blt, bne)
- *jal* address : Saute et sauvgarde l’adresse dans ra
- *jr* target : Saut vers adresse variable

= PP → UPP

- Suppression des types
    - Allocation de taille fixe : 4 octets
- Variables global désignées par leurs adresses, 2 choix :
    - Les variables gardent leurs noms, elles seront traduites par des labels MIPS dans la zones de données (facile)
    - Les variables sont désignées par un « offset » (déplacement) dans la zone de données, qui a un seul label MIPS
- Accès au tableau
    - Accès en lecture de la forme e1[e2] : lw (e1 + 4 x e2)
    - Accès en écriture de la forme e1[e2] = e3 : sw (e1 + 4 x e2) e3
    - Nouveau tableau de la forme T[e] : (4 x e)
- Remplacement des opérations arithmétiques par ceux de MIPS
    - Sauf le - unaire, qui sera traduit : -e → 0 - e
    - Optimisation a faire : e + 0 → e, |e + 1 → addi(e, 1)

= RTL

- Instructions élémentaires traduit en graphe de flot de contrôle. Il représente les chemins possible à l’execution
- Variables locales remplacées par des pseudos registres

```bash
while 2 x b < a do
	a := a - 1;
b := 3
```

```bash
entry f1
exit f0
f1 : sll %4, %1, 1 -> f2
f2 : slt %3, %4, %2  -> f3
f3 : bgtz %3, f4, f5
f4 : sub %2, %2, 1 -> f2
f5 : li %1, 3 -> f0
```

= ERTL

Intégration de la convention d’appel (r0, a0 …, etc)

Paramètres, resultat de procédures et fonctions → registres / emplacements de pile

```bash
procedure f(1)
var %0, %1, %2, %3, %4, %5, %6
entry f11
f11 : newframe → f10
f10 : move %6, $ra → f9
f9 : move %5, $s1 → f8
f8 : move %4, $s0 → f7
f7 : move %0, $a0 → f6
f6 : li %1, 0 → f5
f5 : blez %0 → f4, f3
f3 : addiu %3, %0, -1 → f2
f2 : j → f20
f20 : move $a0, %3 → f19
f19 : call f (1) → f18
f18 : move %2, $v0 → f1
f1 : mul %1, %0, %2 → f0
f0 : j → f17
f17 : move $v0, %1 → f16
f16 : move $ra, %6 → f15
f15 : move $s1, %5 → f14
f14 : move $s0, %4 → f13
f13 : delframe → f12
f12 : jr $ra
f4 : li %1, 1 → f0
```

Move d’un registre physique vers un pseudo registre

Paramètree sur la pile (a0 - a3)

Chaque procédure à sa propre vision de la pile, trois sous régions distinct : paramètres entrants, sortant et données locales

= LISP

