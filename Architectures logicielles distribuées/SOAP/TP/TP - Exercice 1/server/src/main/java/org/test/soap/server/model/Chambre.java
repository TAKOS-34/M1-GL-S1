package org.test.soap.server.model;
import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class Chambre {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private String numero;
    private int nombreLits;
    private float prixParNuit;

    @OneToMany(mappedBy = "chambre", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Reservation> reservations;

    public Chambre() {}

    public Chambre(Hotel hotel, String numero, int nombreLits, float prixParNuit) {
        this.id = UUID.randomUUID().toString();
        this.hotel = hotel;
        this.numero = numero;
        this.nombreLits = nombreLits;
        this.prixParNuit = prixParNuit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
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

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}