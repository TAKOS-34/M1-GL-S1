= Exercice 1
- 11
- 126
- 30
- 80
- 30
- 16
- Erreur de paramètres
- Erreur de paramètres | 32
- Erreur de paramètres

= Exercice 2
- 65
- F : variable V has no value
- ```lisp
(defun fact (n) 
	(if (<= n 1) 
		1 
		(* n (fact (- n 1)))))
```
- ```lisp
(defun fibo (n)
	(if (<= n 1)
		1
		(+ (fibo (- n 1)) (fibo (- n 2)))))
```

= Exercice 3
- CAR() = NILL
- CAR() = NILL
- Pour le reste = erreur : Arbot debug loop

- (1 2 3 4) = 4 cells
- (1 (2 3) 4) = 5 cells
- (1 (2) (3) 4) = 6 cells