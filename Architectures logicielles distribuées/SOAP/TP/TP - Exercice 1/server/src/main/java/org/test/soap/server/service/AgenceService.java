package org.test.soap.server.service;
import org.test.soap.server.exceptions.ChambreException;
import org.test.soap.server.exceptions.ReservationException;
import org.test.soap.server.model.DemandeReservation;
import org.test.soap.server.model.DetailsReservation;
import org.test.soap.server.model.Offre;
import java.util.List;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService
public interface AgenceService {

    @WebMethod
    List<Offre> getOffres(String nomVille, String dateDebut, String dateFin, float prixMin, float prixMax, int etoileMin, int nbrPersonnes);

    @WebMethod
    boolean reserverChambre(DemandeReservation reservation) throws ChambreException;

    @WebMethod
    List<DetailsReservation> getReservations(String nom, String prenom);

    @WebMethod
    DetailsReservation getReservation(String reservationId) throws ReservationException;
}