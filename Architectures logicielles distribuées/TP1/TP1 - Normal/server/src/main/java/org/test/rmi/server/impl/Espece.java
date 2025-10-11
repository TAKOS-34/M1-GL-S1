package org.test.rmi.server.impl;
import org.springframework.stereotype.Service;
import org.test.rmi.common.interfaces.EspeceService;

@Service
public class Espece implements EspeceService {
    private String nom;
    private int dureeVie;

    public Espece() {}

    public Espece(String nom, int dureeVie) {
        this.nom = nom;
        this.dureeVie = dureeVie;
    }

    public String getNom() {
        return nom;
    }

    public String formatedToString() {
        return "Nom : " + nom + ", Durée de vie moyenne : " + dureeVie;
    }
}