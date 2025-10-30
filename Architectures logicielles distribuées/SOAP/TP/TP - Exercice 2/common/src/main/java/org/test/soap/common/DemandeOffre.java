package org.test.soap.common;

public class DemandeOffre {
    String nomVille;
    String dateDebut;
    String dateFin;
    float prixMin;
    float prixMax;
    int etoileMin;
    int nbrPersonnes;

    public DemandeOffre() {}

    public DemandeOffre(String nomVille, String dateDebut, String dateFin, float prixMin, float prixMax, int etoileMin, int nbrPersonnes) {
        this.nomVille = nomVille;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixMin = prixMin;
        this.prixMax = prixMax;
        this.etoileMin = etoileMin;
        this.nbrPersonnes = nbrPersonnes;
    }

    public String getNomVille() {
        return nomVille;
    }

    public void setNomVille(String nomVille) {
        this.nomVille = nomVille;
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

    public float getPrixMin() {
        return prixMin;
    }

    public void setPrixMin(float prixMin) {
        this.prixMin = prixMin;
    }

    public float getPrixMax() {
        return prixMax;
    }

    public void setPrixMax(float prixMax) {
        this.prixMax = prixMax;
    }

    public int getEtoileMin() {
        return etoileMin;
    }

    public void setEtoileMin(int etoileMin) {
        this.etoileMin = etoileMin;
    }

    public int getNbrPersonnes() {
        return nbrPersonnes;
    }

    public void setNbrPersonnes(int nbrPersonnes) {
        this.nbrPersonnes = nbrPersonnes;
    }
}
