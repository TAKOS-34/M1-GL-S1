package org.test.soap.server.service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.jws.WebService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.test.soap.server.exceptions.ReservationException;
import org.test.soap.server.model.*;
import org.test.soap.server.repository.ChambreRepository;
import org.test.soap.server.repository.ClientRepository;
import org.test.soap.server.repository.ReservationRepository;
import org.test.soap.server.exceptions.ChambreException;

@WebService(endpointInterface = "org.test.soap.server.service.AgenceService")
public class AgenceServiceImpl implements AgenceService {

    private static final Logger logger = LoggerFactory.getLogger(AgenceServiceImpl.class);

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServicePaiement servicePaiement;

    @Override
    public List<Offre> getOffres(String nomVille, String dateDebut, String dateFin, float prixMin, float prixMax, int etoileMin, int nbrPersonnes) {
        LocalDate dateDebutFormated = LocalDate.parse(dateDebut);
        LocalDate dateFinFormated = LocalDate.parse(dateFin);
        int nombreNuits = (int) ChronoUnit.DAYS.between(dateDebutFormated, dateFinFormated);
        if (nombreNuits <= 0) {
            return Collections.emptyList();
        }

        List<Chambre> chambres = chambreRepository.findAvailableChambres(nomVille, dateDebutFormated, dateFinFormated, prixMin, prixMax, etoileMin, nbrPersonnes);
        if (chambres.isEmpty()) {
            return Collections.emptyList();
        }

        List<Offre> offres = new ArrayList<>();
        for (Chambre c : chambres) {
            Hotel h = c.getHotel();
            Offre offre = new Offre(c.getId().toString(), h.getNom(), h.getAdresse(), h.getVille(), h.getPays(), h.getCoordonneesGps(), c.getNombreLits(), h.getEtoile(), (c.getPrixParNuit() * nombreNuits));
            offres.add(offre);
        }

        return offres;
    }

    @Override
    public boolean reserverChambre(DemandeReservation reservation) throws ChambreException {
        LocalDate dateDebutFormated;
        LocalDate dateFinFormated;
        try {
            dateDebutFormated = LocalDate.parse(reservation.getDateDebut());
            dateFinFormated = LocalDate.parse(reservation.getDateFin());
        } catch (Exception e) {
            throw new ChambreException("Dates invalides");
        }

        int nombreNuits = (int) ChronoUnit.DAYS.between(dateDebutFormated, dateFinFormated);
        if (nombreNuits <= 0) {
            return false;
        }

        Chambre chambre = chambreRepository.isChambreAvailable(reservation.getChambreId(), dateDebutFormated, dateFinFormated, reservation.getNombrePersonnes());
        if (chambre == null) {
            logger.info("ÉCHEC : isChambreAvailable a renvoyé NULL.");
            throw new ChambreException("La chambre n'est pas disponible ou elle n'existe pas");
        }

        Client client = clientRepository.findByNomPrenom(reservation.getNom(), reservation.getPrenom());
        if (client == null) {
            client = new Client(reservation.getNom(), reservation.getPrenom());
            clientRepository.save(client);
        }

        String token = servicePaiement.genererTokenPaiement(reservation.getCarteCredit());

        Reservation realReservation = new Reservation(client, chambre, token, dateDebutFormated, dateFinFormated, nombreNuits, reservation.getNombrePersonnes(), nombreNuits * chambre.getPrixParNuit());
        reservationRepository.save(realReservation);
        return true;
    }

    @Override
    public List<DetailsReservation> getReservations(String nom, String prenom) {
        Client client = clientRepository.findByNomPrenom(nom, prenom);
        if (client == null) {
            return Collections.emptyList();
        }

        List<Reservation> reservations = reservationRepository.getReservations(nom, prenom);
        if (reservations.isEmpty()) {
            return Collections.emptyList();
        }

        List<DetailsReservation> res = new ArrayList<>();
        for (Reservation r : reservations) {
            Chambre chambre = r.getChambre();
            Hotel hotel = chambre.getHotel();

            String dateDebutStr = r.getDateDebut().toString();
            String dateFinStr = r.getDateFin().toString();

            DetailsReservation detailsReservation = new DetailsReservation(
                    r.getId(),
                    client.getNom(),
                    client.getPrenom(),
                    hotel.getNom(),
                    hotel.getPays(),
                    hotel.getVille(),
                    hotel.getAdresse(),
                    hotel.getEtoile(),
                    chambre.getNumero(),
                    r.getNombrePersonnes(),
                    chambre.getNombreLits(),
                    r.getNombreNuits(),
                    dateDebutStr,
                    dateFinStr,
                    r.getPrix()
            );

            res.add(detailsReservation);
        }

        return res;
    }

    @Override
    public DetailsReservation getReservation(String reservationId) throws ReservationException {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException("La reservation n'existe pas"));

        Chambre chambre = reservation.getChambre();
        Hotel hotel = reservation.getChambre().getHotel();
        String dateDebutStr = reservation.getDateDebut().toString();
        String dateFinStr = reservation.getDateFin().toString();

        return new DetailsReservation(
                reservation.getId(),
                reservation.getClient().getNom(),
                reservation.getClient().getPrenom(),
                hotel.getNom(),
                hotel.getPays(),
                hotel.getVille(),
                hotel.getAdresse(),
                hotel.getEtoile(),
                chambre.getNumero(),
                reservation.getNombrePersonnes(),
                chambre.getNombreLits(),
                reservation.getNombreNuits(),
                dateDebutStr,
                dateFinStr,
                reservation.getPrix()
        );
    }
}