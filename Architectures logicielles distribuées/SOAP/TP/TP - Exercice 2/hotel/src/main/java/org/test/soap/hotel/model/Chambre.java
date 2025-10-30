package org.test.soap.hotel.model;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Chambre {
    @Id
    private String id;

    private String numero;
    private int nombreLits;
    private float prixParNuit;
    private float superficie;
    private boolean terrasse;

    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    public Chambre() {}

    public Chambre(String numero, int nombreLits, float prixParNuit, float superficie, boolean terrasse) {
        this.id = UUID.randomUUID().toString();
        this.numero = numero;
        this.nombreLits = nombreLits;
        this.prixParNuit = prixParNuit;
        this.superficie = superficie;
        this.terrasse = terrasse;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getNombreLits() {
        return nombreLits;
    }

    public void setNombreLits(int nombreLits) {
        this.nombreLits = nombreLits;
    }

    public float getPrixParNuit() {
        return prixParNuit;
    }

    public void setPrixParNuit(float prixParNuit) {
        this.prixParNuit = prixParNuit;
    }

    public float getSuperficie() {
        return superficie;
    }

    public void setSuperficie(float superficie) {
        this.superficie = superficie;
    }

    public boolean isTerrasse() {
        return terrasse;
    }

    public void setTerrasse(boolean terrasse) {
        this.terrasse = terrasse;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}