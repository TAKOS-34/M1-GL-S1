package org.test.rmi.client.impl;
import org.test.rmi.common.interfaces.AnimalService;
import org.test.rmi.common.interfaces.DossierService;
import java.io.Serializable;

public class Chat implements AnimalService, Serializable {
    private String nom;
    private String nomMaitre;
    private String race;
    private String couleurPelage;

    public Chat(String nom, String nomMaitre, String race, String couleurPelage) {
        this.nom = nom;
        this.nomMaitre = nomMaitre;
        this.race = race;
        this.couleurPelage = couleurPelage;
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public String getEspece() {
        return "";
    }

    @Override
    public DossierService getDossier() {
        return null;
    }

    @Override
    public String formatedToString() {
        return "Nom : " + nom + ", Maitre : " + nomMaitre + ", Race : " + race + ", Couleur du pelage : " + couleurPelage;
    }
}