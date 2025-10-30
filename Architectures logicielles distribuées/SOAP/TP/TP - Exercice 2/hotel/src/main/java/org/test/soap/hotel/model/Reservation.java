package org.test.soap.hotel.model;
import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Reservation {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;

    private String nom;
    private String prenom;
    private String tokenPaiement;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int nombreNuits;
    private int nombrePersonnes;
    private float prix;

    public Reservation() {}

    public Reservation(String id, Chambre chambre, String nom, String prenom, String tokenPaiement, LocalDate dateDebut, LocalDate dateFin, int nombreNuits, int nombrePersonnes, float prix) {
        this.id = id;
        this.chambre = chambre;
        this.nom = nom;
        this.prenom = prenom;
        this.tokenPaiement = tokenPaiement;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombreNuits = nombreNuits;
        this.nombrePersonnes = nombrePersonnes;
        this.prix = prix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTokenPaiement() {
        return tokenPaiement;
    }

    public void setTokenPaiement(String tokenPaiement) {
        this.tokenPaiement = tokenPaiement;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public int getNombreNuits() {
        return nombreNuits;
    }

    public void setNombreNuits(int nombreNuits) {
        this.nombreNuits = nombreNuits;
    }

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
}