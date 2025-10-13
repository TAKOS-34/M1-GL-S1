package org.test.rmi.common.impl;
import org.test.rmi.common.interfaces.EspeceService;

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

    @Override
    public Espece getEspece() {
        return this;
    }

    @Override
    public String formatedToString() {
        return "Nom : " + nom + ", Durée de vie moyenne : " + dureeVie;
    }
}