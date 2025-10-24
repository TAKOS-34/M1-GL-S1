package org.test.soap.server.model;
import javax.persistence.*;
import java.util.List;

@Entity
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "agence_id")
    private Agence agence;

    private String nom;
    private String marque;
    private String pays;
    private String ville;
    private String adresse;
    private String coordonneesGps;
    private int etoile;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Chambre> chambres;

    public Hotel() {}

    public Hotel(Agence agence, String nom, String marque, String pays, String ville, String adresse, String coordonneesGps, int etoile) {
        this.agence = agence;
        this.nom = nom;
        this.marque = marque;
        this.pays = pays;
        this.ville = ville;
        this.adresse = adresse;
        this.coordonneesGps = coordonneesGps;
        this.etoile = etoile;
    }

    public int getId() {
        return id;
    }

    public Agence getAgence() {
        return agence;
    }

    public String getNom() {
        return nom;
    }

    public String getMarque() {
        return marque;
    }

    public String getPays() {
        return pays;
    }

    public String getVille() {
        return ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getCoordonneesGps() {
        return coordonneesGps;
    }

    public int getEtoile() {
        return etoile;
    }
}