package org.test.soap.server.model;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Reservation {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "chambre_id")
    private Chambre chambre;

    private String tokenPaiement;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int nombreNuits;
    private int nombrePersonnes;
    private float prix;

    public Reservation() {}

    public Reservation(Client client, Chambre chambre, String tokenPaiement, LocalDate dateDebut, LocalDate dateFin, int nombreNuits, int nombrePersonnes, float prix) {
        this.id = UUID.randomUUID().toString();
        this.client = client;
        this.chambre = chambre;
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

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Chambre getChambre() {
        return chambre;
    }

    public void setChambre(Chambre chambre) {
        this.chambre = chambre;
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