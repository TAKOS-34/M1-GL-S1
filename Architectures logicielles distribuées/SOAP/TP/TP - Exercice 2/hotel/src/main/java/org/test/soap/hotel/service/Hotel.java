package org.test.soap.hotel.service;
import org.springframework.stereotype.Service;

@Service
public class Hotel {
    private String nom;
    private String marque;
    private String pays;
    private String ville;
    private String adresse;
    private String coordonneesGps;
    private int etoile;

    public Hotel() {}

    public Hotel(String nom, String marque, String pays, String ville, String adresse, String coordonneesGps, int etoile) {
        this.nom = nom;
        this.marque = marque;
        this.pays = pays;
        this.ville = ville;
        this.adresse = adresse;
        this.coordonneesGps = coordonneesGps;
        this.etoile = etoile;
    }

    public int getEtoile() {
        return etoile;
    }

    public String getCoordonneesGps() {
        return coordonneesGps;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getVille() {
        return ville;
    }

    public String getPays() {
        return pays;
    }

    public String getMarque() {
        return marque;
    }

    public String getNom() {
        return nom;
    }
}