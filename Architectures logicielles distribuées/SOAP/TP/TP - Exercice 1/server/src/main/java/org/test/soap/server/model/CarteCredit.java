package org.test.soap.server.model;

public class CarteCredit {
    private String numero;
    private String dateExpiration;
    private String nom;
    private String ccv;

    public CarteCredit() {}

    public CarteCredit(String numero, String dateExpiration, String nom, String ccv) {
        this.numero = numero;
        this.dateExpiration = dateExpiration;
        this.nom = nom;
        this.ccv = ccv;
    }

    public void setNumero(String numero) {
        if (numero == null || !numero.matches("\\d{16}")) {
            throw new IllegalArgumentException("Numéro de carte invalide");
        }
        this.numero = numero;
    }

    public void setDateExpiration(String dateExpiration) {
        if (dateExpiration == null || !dateExpiration.matches("(0[1-9]|1[0-2])/\\d{2}")) {
            throw new IllegalArgumentException("Date d'expiration invalide");
        }
        this.dateExpiration = dateExpiration;
    }

    public void setNom(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Nom invalide");
        }
        this.nom = nom;
    }

    public void setCcv(String ccv) {
        if (ccv == null || !numero.matches("\\d{3}")) {
            throw new IllegalArgumentException("Code de sécurité invalide");
        }
        this.ccv = ccv;
    }

    public String getNumero() {
        return numero;
    }

    public String getDateExpiration() {
        return dateExpiration;
    }

    public String getNom() {
        return nom;
    }

    public String getCcv() {
        return ccv;
    }
}
