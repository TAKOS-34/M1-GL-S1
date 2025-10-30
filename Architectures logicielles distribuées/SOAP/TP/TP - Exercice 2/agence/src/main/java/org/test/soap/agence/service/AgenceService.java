package org.test.soap.agence.service;
import org.test.soap.agence.exceptions.ChambreException;
import org.test.soap.agence.exceptions.ReservationException;
import org.test.soap.agence.model.*;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface AgenceService {

    @WebMethod
    List<Offre> getOffres(DemandeOffre demandeOffre);

    @WebMethod
    String reserverChambre(DemandeReservation reservation) throws ChambreException;

    @WebMethod
    List<DetailsReservation> getReservations(String nom, String prenom);

    @WebMethod
    DetailsReservation getReservation(String reservationId) throws ReservationException;

    @WebMethod
    String updateReservation(String reservationId, DemandeReservation demandeReservation) throws ReservationException, ChambreException;

    @WebMethod
    boolean deleteReservation(String reservationId, String nom, String prenom) throws ReservationException;
}