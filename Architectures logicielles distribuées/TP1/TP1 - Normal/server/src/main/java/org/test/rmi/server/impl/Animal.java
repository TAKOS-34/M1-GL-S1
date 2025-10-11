package org.test.rmi.server.impl;
import org.springframework.stereotype.Service;
import org.test.rmi.common.impl.Espece;
import org.test.rmi.common.interfaces.AnimalService;

@Service
public class Animal implements AnimalService {
    private String nom;
    private String nomMaitre;
    private String race;
    private Espece espece;
    private Dossier dossier;

    public Animal() {}

    public Animal(String nom, String nomMaitre, String race, Espece espece, Dossier dossier) {
        this.nom = nom;
        this.nomMaitre = nomMaitre;
        this.race = race;
        this.espece = espece;
        this.dossier = dossier;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getEspece() {
        return espece.getNom();
    }

    public Dossier getDossier() {
        return dossier;
    }

    @Override
    public String formatedToString() {
        return "Nom : " + nom + ", Maitre : " + nomMaitre + ", Race : " + race + ", Espece : " + espece.getNom() + ", Dossier : " + dossier.getNom();
    }
}