package org.test.soap.client.ui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.soap.client.generated.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Component
public class AgenceServiceCLI implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AgenceServiceCLI.class);

    @Autowired
    private AgenceService agenceService;

    @Override
    public void run(String... args) throws Exception {

        logger.info("--- [CLIENT] Démarrage des tests ---");
        String reservationIdPourTests = null; // Pour stocker l'ID de la réservation réussie

        try {
            String ville = "Paris";
            int nbrPersonnes = 2;

            LocalDate dateDebutObj = LocalDate.now().plusMonths(2);
            LocalDate dateFinObj = dateDebutObj.plusDays(2);
            String dateDebutStr = dateDebutObj.toString();
            String dateFinStr = dateFinObj.toString();

            // --- TEST 1: RECHERCHE D'OFFRES ---
            logger.info("\n--- [TEST 1] Recherche d'offres (Ville: {}, Dates: {} à {}) ---", ville, dateDebutStr, dateFinStr);
            List<Offre> offres = agenceService.getOffres(ville, dateDebutStr, dateFinStr, 0.0f, 5000.0f, 0, nbrPersonnes);

            if (offres == null || offres.isEmpty()) {
                logger.warn("Résultat Test 1: Aucune offre trouvée. Arrêt des tests.");
                return;
            }
            logger.info("Résultat Test 1: {} offre(s) trouvée(s).", offres.size());

            Offre offreAReserver = offres.get(0);
            String idChambreAReserver = offreAReserver.getIdChambre();
            logger.info("   -> Utilisation de l'ID de chambre: {}", idChambreAReserver);

            CarteCredit carte = new CarteCredit();
            carte.setNom("M. Test Client");
            carte.setNumero("1234567890123456");
            carte.setDateExpiration("12/26");
            carte.setCcv("123");

            // --- TEST 2: RÉSERVATION VALIDE ---
            logger.info("\n--- [TEST 2] Tentative de réservation valide ---");
            DemandeReservation demandeValide = new DemandeReservation();
            demandeValide.setNom("Test");
            demandeValide.setPrenom("Client");
            demandeValide.setDateDebut(dateDebutStr);
            demandeValide.setDateFin(dateFinStr);
            demandeValide.setCarteCredit(carte);
            demandeValide.setNombrePersonnes(nbrPersonnes);
            demandeValide.setChambreId(idChambreAReserver);

            boolean premiereReservationReussie = false;
            try {
                boolean succes = agenceService.reserverChambre(demandeValide);
                logger.info("RÉUSSITE DU TEST 2: Réservation effectuée (Retour: {})", succes);
                premiereReservationReussie = succes;
            } catch (ChambreException_Exception e) {
                logger.error("ERREUR DU TEST 2: La réservation valide a échoué: {}", e.getFaultInfo().getMessage());
            }

            // --- TEST 3: RÉSERVATION DUPLICATA (ÉCHEC ATTENDU) ---
            logger.info("\n--- [TEST 3] Tentative de double réservation (doit échouer) ---");
            if (premiereReservationReussie) {
                try {
                    agenceService.reserverChambre(demandeValide);
                    logger.error("ERREUR DU TEST 3: La double réservation aurait dû échouer.");
                } catch (ChambreException_Exception e) {
                    logger.info("SUCCÈS DU TEST 3: Réservation échouée comme prévu. Raison: {}", e.getFaultInfo().getMessage());
                }
            } else {
                logger.warn("   -> Test 3 ignoré car la réservation initiale (Test 2) a échoué.");
            }

            // --- TEST 4: RÉSERVATION CHAMBRE INEXISTANTE (ÉCHEC ATTENDU) ---
            logger.info("\n--- [TEST 4] Tentative de réservation d'une chambre inexistante (doit échouer) ---");
            String idChambreInexistante = UUID.randomUUID().toString();
            DemandeReservation demandeInvalide = new DemandeReservation();
            demandeInvalide.setNom("Test");
            demandeInvalide.setPrenom("Client");
            demandeInvalide.setDateDebut(dateDebutStr);
            demandeInvalide.setDateFin(dateFinStr);
            demandeInvalide.setCarteCredit(carte);
            demandeInvalide.setNombrePersonnes(nbrPersonnes);
            demandeInvalide.setChambreId(idChambreInexistante);

            try {
                agenceService.reserverChambre(demandeInvalide);
                logger.error("ERREUR DU TEST 4: La réservation pour une chambre inexistante aurait dû échouer.");
            } catch (ChambreException_Exception e) {
                logger.info("SUCCÈS DU TEST 4: Réservation échouée comme prévu. Raison: {}", e.getFaultInfo().getMessage());
            }

            // --- TEST 5: RÉCUPÉRATION DE TOUTES LES RÉSERVATIONS ---
            logger.info("\n--- [TEST 5] Récupération de l'historique des réservations (pour Test Client) ---");
            try {
                List<DetailsReservation> detailsReservations = agenceService.getReservations("Test", "Client");

                if (detailsReservations == null || detailsReservations.isEmpty()) {
                    logger.info("Résultat Test 5: Aucune réservation trouvée pour ce client.");
                } else {
                    logger.info("Résultat Test 5: {} réservation(s) trouvée(s):", detailsReservations.size());
                    if (!detailsReservations.isEmpty()) {
                        reservationIdPourTests = detailsReservations.get(0).getId();
                    }
                    for (DetailsReservation r : detailsReservations) {
                        logger.info(
                                String.format(
                                        "  -> Hôtel: %s (%d*) à %s\n" +
                                                "     Chambre: N°%s (Lits: %d)\n" +
                                                "     Séjour: du %s au %s (%d nuits)\n" +
                                                "     Prix payé: %.2f €\n" +
                                                "     --------------------",
                                        r.getNomHotel(), r.getEtoile(), r.getVille(),
                                        r.getNumeroChambre(), r.getNombreLits(),
                                        r.getDateDebut(), r.getDateFin(), r.getNombreNuits(),
                                        r.getPrix()
                                )
                        );
                    }
                }
            } catch (Exception e) {
                logger.error("[CLIENT] ERREUR LORS DE LA RÉCUPÉRATION DES RÉSERVATIONS :", e);
            }

            // --- TEST 6: CONSULTATION D'UNE RÉSERVATION EXISTANTE ---
            logger.info("\n--- [TEST 6] Consultation d'une réservation existante ---");
            if (reservationIdPourTests != null) {
                logger.info("   -> Utilisation de l'ID: {}", reservationIdPourTests);
                try {
                    DetailsReservation detail = agenceService.getReservation(reservationIdPourTests);
                    logger.info("RÉUSSITE DU TEST 6: Détails trouvés:");
                    logger.info(
                            String.format(
                                    "     -> Hôtel: %s, Chambre: N°%s, Séjour: %s à %s",
                                    detail.getNomHotel(), detail.getNumeroChambre(), detail.getDateDebut(), detail.getDateFin()
                            )
                    );
                } catch (ReservationException_Exception e) {
                    logger.error("ERREUR DU TEST 6: getReservation a échoué pour un ID valide: {}", e.getFaultInfo().getMessage());
                }
            } else {
                logger.warn("   -> Test 6 ignoré car aucun ID de réservation n'a été récupéré du Test 5.");
            }

            // --- TEST 7: CONSULTATION D'UNE RÉSERVATION INEXISTANTE (ÉCHEC ATTENDU) ---
            logger.info("\n--- [TEST 7] Tentative de consultation d'une réservation inexistante ---");
            String idReservationInexistant = UUID.randomUUID().toString();
            logger.info("   -> Utilisation de l'ID inexistant: {}", idReservationInexistant);
            try {
                agenceService.getReservation(idReservationInexistant);
                logger.error("ERREUR DU TEST 7: La consultation d'une réservation inexistante aurait dû échouer.");
            } catch (ReservationException_Exception e) {
                logger.info("SUCCÈS DU TEST 7: Consultation échouée comme prévu.");
                logger.info("   Raison (SOAP Fault): {}", e.getFaultInfo().getMessage());
            }

        } catch (Exception e) {
            logger.error("\n[CLIENT] ERREUR INATTENDUE LORS DE L'APPEL WEB SERVICE :", e);
        }

        logger.info("\n--- [CLIENT] Fin de tous les tests ---");
    }
}