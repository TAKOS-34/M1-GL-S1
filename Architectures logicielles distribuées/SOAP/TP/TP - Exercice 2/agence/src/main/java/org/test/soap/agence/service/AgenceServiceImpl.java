package org.test.soap.agence.service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import javax.jws.WebService;

import org.springframework.beans.factory.annotation.Autowired;
import org.test.soap.agence.exceptions.ReservationException;
import org.test.soap.agence.model.*;
import org.test.soap.agence.repository.ChambreRepository;
import org.test.soap.agence.repository.ClientRepository;
import org.test.soap.agence.repository.ReservationRepository;
import org.test.soap.agence.exceptions.ChambreException;

@WebService(endpointInterface = "org.test.soap.server.service.AgenceService")
public class AgenceServiceImpl implements AgenceService {

    @Autowired
    private ChambreRepository chambreRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ServicePaiement servicePaiement;

    @Override
    public List<Offre> getOffres(DemandeOffre demandeOffre) {
        LocalDate dateDebutFormated = LocalDate.parse(demandeOffre.getDateDebut());
        LocalDate dateFinFormated = LocalDate.parse(demandeOffre.getDateFin());

        int nombreNuits = (int) ChronoUnit.DAYS.between(dateDebutFormated, dateFinFormated);
        if (nombreNuits <= 0) {
            return Collections.emptyList();
        }

        List<Chambre> chambres = chambreRepository.findAvailableChambres(demandeOffre.getNomVille(), dateDebutFormated, dateFinFormated, demandeOffre.getPrixMin(), demandeOffre.getPrixMax(), demandeOffre.getEtoileMin(), demandeOffre.getNbrPersonnes());
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
    public String reserverChambre(DemandeReservation reservation) throws ChambreException {
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
            throw new ChambreException("Dates invalides");
        }

        Chambre chambre = chambreRepository.isChambreAvailable(reservation.getChambreId(), dateDebutFormated, dateFinFormated, reservation.getNombrePersonnes());
        if (chambre == null) {
            throw new ChambreException("La chambre n'est pas disponible ou elle n'existe pas");
        }

        Client client = clientRepository.findByNomPrenom(reservation.getNom(), reservation.getPrenom());
        if (client == null) {
            client = new Client(reservation.getNom(), reservation.getPrenom());
            clientRepository.save(client);
        }

        String token = servicePaiement.genererTokenPaiement(reservation.getCarteCredit());
        String id = UUID.randomUUID().toString();

        Reservation realReservation = new Reservation(id, client, chambre, token, dateDebutFormated, dateFinFormated, nombreNuits, reservation.getNombrePersonnes(), nombreNuits * chambre.getPrixParNuit());
        reservationRepository.save(realReservation);
        return id;
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

    @Override
    public String updateReservation(String reservationId, DemandeReservation demandeReservation) throws ReservationException, ChambreException {
        Reservation reservation = reservationRepository.getReservationsByIdAndClient(reservationId, demandeReservation.getNom(), demandeReservation.getPrenom());
        if (reservation == null) {
            throw new ReservationException("La reservation n'existe pas ou n'est pas a votre nom");
        }

        LocalDate dateDebutFormated;
        LocalDate dateFinFormated;

        try {
            dateDebutFormated = LocalDate.parse(demandeReservation.getDateDebut());
            dateFinFormated = LocalDate.parse(demandeReservation.getDateFin());
        } catch (Exception e) {
            throw new ChambreException("Dates invalides");
        }

        int nombreNuits = (int) ChronoUnit.DAYS.between(dateDebutFormated, dateFinFormated);
        if (nombreNuits <= 0) {
            throw new ChambreException("Dates invalides");
        }

        int conflicts = reservationRepository.verifyIfConflictExists(reservationId, reservation.getChambre().getId(), dateDebutFormated, dateFinFormated);
        if (conflicts > 0) {
            throw new ChambreException("La chambre n'est pas disponible à cette date");
        }

        if (demandeReservation.getNombrePersonnes() > reservation.getChambre().getNombreLits()) {
            throw new ChambreException("Le nombre de personnes dépasse la capacité de la chambre");
        }

        reservation.setDateDebut(dateDebutFormated);
        reservation.setDateFin(dateFinFormated);
        reservation.setNombreNuits(nombreNuits);
        reservation.setNombrePersonnes(demandeReservation.getNombrePersonnes());
        reservation.setPrix(reservation.getChambre().getPrixParNuit() * nombreNuits);
        reservation.setTokenPaiement(servicePaiement.genererTokenPaiement(demandeReservation.getCarteCredit()));

        reservationRepository.deleteById(reservationId);
        reservationRepository.save(reservation);

        return reservation.getId();
    }

    @Override
    public boolean deleteReservation(String reservationId, String nom, String prenom) {
        Reservation reservation = reservationRepository.getReservationsByIdAndClient(reservationId, nom, prenom);
        if (reservation == null) {
            return false;
        }

        reservationRepository.deleteById(reservationId);
        return true;
    }
}