package org.test.soap.server.model;

public class DetailsReservation {
    private String id;
    private String nom;
    private String prenom;
    private String nomHotel;
    private String pays;
    private String ville;
    private String adresse;
    private int etoile;
    private String numeroChambre;
    private int nbrPersonnes;
    private int nombreLits;
    private int nombreNuits;
    private String dateDebut;
    private String dateFin;
    private float prix;

    public DetailsReservation() {}

    public DetailsReservation(String id, String nom, String prenom, String nomHotel, String pays, String ville, String adresse, int etoile, String numeroChambre, int nbrPersonnes, int nombreLits, int nombreNuits, String dateDebut, String dateFin, float prix) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.nomHotel = nomHotel;
        this.pays = pays;
        this.ville = ville;
        this.adresse = adresse;
        this.etoile = etoile;
        this.numeroChambre = numeroChambre;
        this.nbrPersonnes = nbrPersonnes;
        this.nombreLits = nombreLits;
        this.nombreNuits = nombreNuits;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prix = prix;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNomHotel() {
        return nomHotel;
    }

    public void setNomHotel(String nomHotel) {
        this.nomHotel = nomHotel;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public int getEtoile() {
        return etoile;
    }

    public void setEtoile(int etoile) {
        this.etoile = etoile;
    }

    public String getNumeroChambre() {
        return numeroChambre;
    }

    public void setNumeroChambre(String numeroChambre) {
        this.numeroChambre = numeroChambre;
    }

    public int getNbrPersonnes() {
        return nbrPersonnes;
    }

    public void setNbrPersonnes(int nbrPersonnes) {
        this.nbrPersonnes = nbrPersonnes;
    }

    public int getNombreLits() {
        return nombreLits;
    }

    public void setNombreLits(int nombreLits) {
        this.nombreLits = nombreLits;
    }

    public int getNombreNuits() {
        return nombreNuits;
    }

    public void setNombreNuits(int nombreNuits) {
        this.nombreNuits = nombreNuits;
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

    public float getPrix() {
        return prix;
    }

    public void setPrix(float prix) {
        this.prix = prix;
    }
}
