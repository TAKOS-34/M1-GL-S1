package org.test.soap.hotel.service;
import org.test.soap.hotel.exceptions.ChambreException;
import org.test.soap.hotel.exceptions.ReservationException;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;
import org.test.soap.common.*;

@WebService
public interface HotelService {

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