package org.test.soap.server.model;

public class Offre {
    private String idChambre;
    private String nomHotel;
    private String adresse;
    private String ville;
    private String pays;
    private String coordonneesGps;
    private int nombreLitsProposes;
    private int etoiles;
    private float prixTotal;

    public Offre(String idChambre, String nomHotel, String adresse, String ville, String pays, String coordonneesGps, int nombreLitsProposes, int etoiles, float prixTotal) {
        this.idChambre = idChambre;
        this.nomHotel = nomHotel;
        this.adresse = adresse;
        this.ville = ville;
        this.pays = pays;
        this.coordonneesGps = coordonneesGps;
        this.nombreLitsProposes = nombreLitsProposes;
        this.etoiles = etoiles;
        this.prixTotal = prixTotal;
    }

    public String getIdChambre() {
        return idChambre;
    }

    public void setIdChambre(String idChambre) {
        this.idChambre = idChambre;
    }

    public String getNomHotel() {
        return nomHotel;
    }

    public void setNomHotel(String nomHotel) {
        this.nomHotel = nomHotel;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPays() {
        return pays;
    }

    public void setPays(String pays) {
        this.pays = pays;
    }

    public String getCoordonneesGps() {
        return coordonneesGps;
    }

    public void setCoordonneesGps(String coordonneesGps) {
        this.coordonneesGps = coordonneesGps;
    }

    public int getNombreLitsProposes() {
        return nombreLitsProposes;
    }

    public void setNombreLitsProposes(int nombreLitsProposes) {
        this.nombreLitsProposes = nombreLitsProposes;
    }

    public int getEtoiles() {
        return etoiles;
    }

    public void setEtoiles(int etoiles) {
        this.etoiles = etoiles;
    }

    public float getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(float prixTotal) {
        this.prixTotal = prixTotal;
    }
}