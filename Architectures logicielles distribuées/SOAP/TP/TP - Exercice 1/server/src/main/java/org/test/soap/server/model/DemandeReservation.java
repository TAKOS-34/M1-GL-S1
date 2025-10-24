package org.test.soap.server.model;

public class DemandeReservation {
    private String nom;
    private String prenom;
    private String dateDebut;
    private String dateFin;
    private CarteCredit carteCredit;
    private int nombrePersonnes;
    private String chambreId;

    public DemandeReservation() {}

    public DemandeReservation(String nom, String prenom, String dateDebut, String dateFin, CarteCredit carteCredit, int nombrePersonnes, String chambreId) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.carteCredit = carteCredit;
        this.nombrePersonnes = nombrePersonnes;
        this.chambreId = chambreId;
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

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public CarteCredit getCarteCredit() {
        return carteCredit;
    }

    public void setCarteCredit(CarteCredit carteCredit) {
        this.carteCredit = carteCredit;
    }

    public int getNombrePersonnes() {
        return nombrePersonnes;
    }

    public void setNombrePersonnes(int nombrePersonnes) {
        this.nombrePersonnes = nombrePersonnes;
    }

    public String getChambreId() {
        return chambreId;
    }

    public void setChambreId(String chambreId) {
        this.chambreId = chambreId;
    }
}