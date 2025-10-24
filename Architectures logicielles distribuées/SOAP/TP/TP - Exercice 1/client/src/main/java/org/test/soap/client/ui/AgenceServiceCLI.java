package org.test.soap.client.ui;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.test.soap.client.generated.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

@Component
public class AgenceServiceCLI implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(AgenceServiceCLI.class);
        private final Scanner scanner = new Scanner(System.in);

        @Autowired
        private AgenceService agenceService;

        @Override
        public void run(String... args) throws Exception {
            while (true) {
                printMenu();
                String choiceStr = scanner.nextLine();
                int choice;
                try {
                    choice = Integer.parseInt(choiceStr);
                } catch (NumberFormatException e) {
                    logger.warn("Choix invalide");
                    continue;
                }

                switch (choice) {
                    case 1:
                        handleSearchOffers();
                        break;
                    case 2:
                        handleReserveRoom();
                        break;
                    case 3:
                        handleGetReservationById();
                        break;
                    case 4:
                        handleGetReservationsByClient();
                        break;
                    case 5:
                        handleUpdateReservation();
                        break;
                    case 6:
                        handleDeleteReservation();
                        break;
                    case 7:
                        tests();
                        break;
                    case 0:
                        logger.info("[CLIENT] Bye!");
                        return;
                    default:
                        logger.warn("Choix non reconnu");
                }
            }
        }

            private void printMenu() {
                System.out.println("\n------------------ MENU ------------------");
                System.out.println("1. Chercher des offres d'hôtels");
                System.out.println("2. Réserver une chambre");
                System.out.println("3. Consulter une réservation par ID");
                System.out.println("4. Consulter mes réservations");
                System.out.println("5. Modifier une réservation");
                System.out.println("6. Annuler une réservation");
                System.out.println("7. Lancer les tests automatisés");
                System.out.println("0. Quitter");
                System.out.println("------------------------------------------");
                System.out.print("Votre choix : ");
            }

            private void handleSearchOffers() {
                try {
                    String ville = readStringInput("Ville : ");
                    LocalDate dateDebutObj = readDateInput("Date d'arrivée (YYYY-MM-DD) : ");
                    LocalDate dateFinObj = readDateInput("Date de départ (YYYY-MM-DD) : ");
                    int nbrPersonnes = readIntInput("Nombre de personnes : ");
                    float prixMin = 0.0f;
                    float prixMax = 9999.0f;
                    int etoileMin = 0;

                    DemandeOffre demande = new DemandeOffre();
                    demande.setNomVille(ville);
                    demande.setDateDebut(dateDebutObj.toString());
                    demande.setDateFin(dateFinObj.toString());
                    demande.setPrixMin(prixMin);
                    demande.setPrixMax(prixMax);
                    demande.setEtoileMin(etoileMin);
                    demande.setNbrPersonnes(nbrPersonnes);

                    List<Offre> offres = agenceService.getOffres(demande);

                    if (offres == null || offres.isEmpty()) {
                        logger.info("Aucune offre trouvée pour ces critères");
                    } else {
                        logger.info("{} offre(s) trouvée(s) :", offres.size());
                        offres.forEach(offre -> logger.info("  -> ID Chambre : {}, Hôtel : {}, Lits : {}, Prix : {:.2f} €",
                                offre.getIdChambre(), offre.getNomHotel(), offre.getNombreLitsProposes(), offre.getPrixTotal()));
                    }
                } catch (Exception e) {
                    logger.error("Erreur lors de la recherche d'offres : {}", e.getMessage(), e);
                }
            }

            private void handleReserveRoom() {
                try {
                    String idChambre = readStringInput("ID de la chambre à réserver : ");
                    LocalDate dateDebutObj = readDateInput("Date d'arrivée (YYYY-MM-DD) : ");
                    LocalDate dateFinObj = readDateInput("Date de départ (YYYY-MM-DD) : ");
                    int nbrPersonnes = readIntInput("Nombre de personnes : ");
                    String nom = readStringInput("Votre nom : ");
                    String prenom = readStringInput("Votre prénom : ");

                    CarteCredit carte = new CarteCredit();
                    String numero = readStringInput("Numero de carte : ");
                    String nomCarte = readStringInput("Nom sur la carte : ");
                    String dateExpiration = readStringInput("Date expiration de la carte : ");
                    String ccv = readStringInput("Code de sécurité : ");
                    carte.setNumero(numero);
                    carte.setNom(nomCarte);
                    carte.setDateExpiration(dateExpiration);
                    carte.setCcv(ccv);

                    DemandeReservation demande = new DemandeReservation();
                    demande.setNom(nom);
                    demande.setPrenom(prenom);
                    demande.setDateDebut(dateDebutObj.toString());
                    demande.setDateFin(dateFinObj.toString());
                    demande.setCarteCredit(carte);
                    demande.setNombrePersonnes(nbrPersonnes);
                    demande.setChambreId(idChambre);

                    String reservationId = agenceService.reserverChambre(demande);
                    logger.info("Réservation réussie ! Votre ID de réservation est : {}", reservationId);

                } catch (ChambreException_Exception e) {
                    logger.error("Échec de la réservation : {}", e.getFaultInfo().getMessage());
                } catch (Exception e) {
                    logger.error("Erreur lors de la réservation : {}", e.getMessage(), e);
                }
            }

            private void handleGetReservationById() {
                try {
                    String reservationId = readStringInput("ID de la réservation : ");

                    DetailsReservation detail = agenceService.getReservation(reservationId);
                    logReservationDetails(detail);

                } catch (ReservationException_Exception e) {
                    logger.error("Impossible de trouver la réservation : {}", e.getFaultInfo().getMessage());
                } catch (Exception e) {
                    logger.error("Erreur lors de la consultation : {}", e.getMessage(), e);
                }
            }

            private void handleGetReservationsByClient() {
                try {
                    String nom = readStringInput("Votre nom : ");
                    String prenom = readStringInput("Votre prénom : ");

                    List<DetailsReservation> listeResas = agenceService.getReservations(nom, prenom);

                    if (listeResas == null || listeResas.isEmpty()) {
                        logger.info("Aucune réservation trouvée pour {} {}", prenom, nom);
                    } else {
                        logger.info("{} réservation(s) trouvée(s) pour {} {} :", listeResas.size(), prenom, nom);
                        listeResas.forEach(this::logReservationDetails);
                    }
                } catch (Exception e) {
                    logger.error("Erreur lors de la consultation des réservations : {}", e.getMessage(), e);
                }
            }

            private void handleUpdateReservation() {
                try {
                    String reservationId = readStringInput("ID de la réservation à modifier : ");
                    String nom = readStringInput("Votre nom (pour vérification) : ");
                    String prenom = readStringInput("Votre prénom (pour vérification) : ");
                    LocalDate newDateDebutObj = readDateInput("Nouvelle date d'arrivée (YYYY-MM-DD) : ");
                    LocalDate newDateFinObj = readDateInput("Nouvelle date de départ (YYYY-MM-DD) : ");
                    int nbrPersonnes = readIntInput("Nouveau nombre de personnes : ");

                    DemandeReservation demandeModif = new DemandeReservation();
                    demandeModif.setNom(nom);
                    demandeModif.setPrenom(prenom);
                    demandeModif.setDateDebut(newDateDebutObj.toString());
                    demandeModif.setDateFin(newDateFinObj.toString());
                    demandeModif.setNombrePersonnes(nbrPersonnes);

                    String updatedId = agenceService.updateReservation(reservationId, demandeModif);
                    logger.info("Modification réussie pour la réservation ID : {}", updatedId);

                } catch (ReservationException_Exception | ChambreException_Exception e) {
                    logger.error("Échec de la modification : {}", e.getMessage());
                } catch (Exception e) {
                    logger.error("Erreur lors de la modification : {}", e.getMessage(), e);
                }
            }

            private void handleDeleteReservation() {
                try {
                    String reservationId = readStringInput("ID de la réservation à annuler : ");
                    String nom = readStringInput("Votre nom (pour vérification) : ");
                    String prenom = readStringInput("Votre prénom (pour vérification) : ");

                    boolean deleted = agenceService.deleteReservation(reservationId, nom, prenom);

                    if (deleted) {
                        logger.info("Réservation {} annulée avec succès.", reservationId);
                    } else {
                        logger.warn("Impossible d'annuler la réservation {}", reservationId);
                    }
                } catch (Exception e) {
                    logger.error("Erreur lors de l'annulation : {}", e.getMessage(), e);
                }
            }

            private String readStringInput(String prompt) {
                System.out.print(prompt);
                return scanner.nextLine();
            }

            private LocalDate readDateInput(String prompt) {
                while (true) {
                    System.out.print(prompt);
                    String input = scanner.nextLine();
                    try {
                        return LocalDate.parse(input);
                    } catch (DateTimeParseException e) {
                        logger.warn("Format de date invalide");
                    }
                }
            }

            private int readIntInput(String prompt) {
                while (true) {
                    System.out.print(prompt);
                    String input = scanner.nextLine();
                    try {
                        return Integer.parseInt(input);
                    } catch (NumberFormatException e) {
                        logger.warn("Entrée invalide");
                    }
                }
            }

            private void logReservationDetails(DetailsReservation r) {
                if (r == null) return;
                logger.info(
                        String.format(
                                "  -> Résa ID: %s\n" +
                                        "     Client : %s %s\n" +
                                        "     Hôtel : %s (%d*) à %s\n" +
                                        "     Chambre : N°%s (Lits: %d) pour %d personne(s)\n" +
                                        "     Séjour : du %s au %s (%d nuits)\n" +
                                        "     Prix payé : %.2f €\n" +
                                        "     --------------------",
                                r.getId(),
                                r.getPrenom(), r.getNom(),
                                r.getNomHotel(), r.getEtoile(), r.getVille(),
                                r.getNumeroChambre(), r.getNombreLits(), r.getNbrPersonnes(),
                                r.getDateDebut(), r.getDateFin(), r.getNombreNuits(),
                                r.getPrix()
                        )
                );
            }

        private void tests() {
            logger.info("--- [CLIENT] Démarrage des tests ---");
            String reservationIdPourTests = null;
            String idChambreAReserver = null;

            try {
                String ville = "Paris";
                int nbrPersonnes = 2;
                LocalDate dateDebutObj = LocalDate.now().plusMonths(2);
                LocalDate dateFinObj = dateDebutObj.plusDays(2);
                String dateDebutStr = dateDebutObj.toString();
                String dateFinStr = dateFinObj.toString();
                CarteCredit carte = new CarteCredit();
                carte.setNom("M. Test Client");
                carte.setNumero("1234567890123456");
                carte.setDateExpiration("12/26");
                carte.setCcv("123");

                logger.info("\n--- [TEST 1] Recherche d'offres ---");
                DemandeOffre demandeOffre = new DemandeOffre();
                demandeOffre.setNomVille(ville);
                demandeOffre.setDateDebut(dateDebutStr);
                demandeOffre.setDateFin(dateFinStr);
                demandeOffre.setPrixMin(0.0f);
                demandeOffre.setPrixMax(5000.0f);
                demandeOffre.setEtoileMin(0);
                demandeOffre.setNbrPersonnes(nbrPersonnes);
                List<Offre> offres = agenceService.getOffres(demandeOffre);
                if (offres == null || offres.isEmpty()) { logger.warn("Résultat Test 1: Aucune offre trouvée."); return; }
                logger.info("Résultat Test 1: {} offre(s) trouvée(s).", offres.size());
                idChambreAReserver = offres.get(0).getIdChambre();
                logger.info("   -> Utilisation ID chambre: {}", idChambreAReserver);

                logger.info("\n--- [TEST 2] Réservation valide ---");
                DemandeReservation demandeValide = new DemandeReservation();
                demandeValide.setNom("Test"); demandeValide.setPrenom("Client"); demandeValide.setDateDebut(dateDebutStr); demandeValide.setDateFin(dateFinStr); demandeValide.setCarteCredit(carte); demandeValide.setNombrePersonnes(nbrPersonnes); demandeValide.setChambreId(idChambreAReserver);
                try {
                    reservationIdPourTests = agenceService.reserverChambre(demandeValide);
                    logger.info("RÉUSSITE TEST 2: Réservation OK (ID: {})", reservationIdPourTests);
                } catch (ChambreException_Exception e) { logger.error("ERREUR TEST 2: Résa valide échouée: {}", e.getFaultInfo().getMessage()); }

                logger.info("\n--- [TEST 3] Réservation duplicata (échec attendu) ---");
                if (reservationIdPourTests != null) {
                    try { agenceService.reserverChambre(demandeValide); logger.error("ERREUR TEST 3: Double résa aurait dû échouer."); }
                    catch (ChambreException_Exception e) { logger.info("SUCCÈS TEST 3: Échec résa duplicata OK: {}", e.getFaultInfo().getMessage()); }
                } else { logger.warn("   -> Test 3 ignoré."); }

                logger.info("\n--- [TEST 4] Réservation chambre inexistante (échec attendu) ---");
                DemandeReservation demandeInvalideChambre = new DemandeReservation(); // Renommé pour clarté
                demandeInvalideChambre.setNom("Test"); demandeInvalideChambre.setPrenom("Client"); demandeInvalideChambre.setDateDebut(dateDebutStr); demandeInvalideChambre.setDateFin(dateFinStr); demandeInvalideChambre.setCarteCredit(carte); demandeInvalideChambre.setNombrePersonnes(nbrPersonnes); demandeInvalideChambre.setChambreId(UUID.randomUUID().toString());
                try { agenceService.reserverChambre(demandeInvalideChambre); logger.error("ERREUR TEST 4: Résa chambre inexistante aurait dû échouer."); }
                catch (ChambreException_Exception e) { logger.info("SUCCÈS TEST 4: Échec résa chambre inexistante OK: {}", e.getFaultInfo().getMessage()); }

                logger.info("\n--- [TEST 5] Consultation de toutes les réservations ---");
                try {
                    List<DetailsReservation> listeResas = agenceService.getReservations("Test", "Client");
                    if (listeResas == null || listeResas.isEmpty()) { logger.info("Résultat Test 5: Aucune résa trouvée."); }
                    else {
                        logger.info("Résultat Test 5: {} réservation(s) trouvée(s):", listeResas.size());
                        if (reservationIdPourTests == null && !listeResas.isEmpty()) { reservationIdPourTests = listeResas.get(0).getId(); logger.info("   -> ID résa récupéré pour tests suivants: {}", reservationIdPourTests); }
                        listeResas.forEach(r -> logger.info("   -> Résa ID: {}, Hôtel: {}, Chambre: {}, Dates: {}->{}", r.getId(), r.getNomHotel(), r.getNumeroChambre(), r.getDateDebut(), r.getDateFin()));
                    }
                } catch (Exception e) { logger.error("ERREUR TEST 5: ", e); }

                logger.info("\n--- [TEST 6] Consultation réservation existante ---");
                if (reservationIdPourTests != null) {
                    try { DetailsReservation detail = agenceService.getReservation(reservationIdPourTests); logger.info("RÉUSSITE TEST 6: Résa trouvée: Hôtel {}, Chambre {}", detail.getNomHotel(), detail.getNumeroChambre()); }
                    catch (ReservationException_Exception e) { logger.error("ERREUR TEST 6: Consultation résa existante échouée: {}", e.getFaultInfo().getMessage()); }
                } else { logger.warn("   -> Test 6 ignoré."); }

                logger.info("\n--- [TEST 7] Consultation réservation inexistante (échec attendu) ---");
                String idResaInexistant = UUID.randomUUID().toString();
                try { agenceService.getReservation(idResaInexistant); logger.error("ERREUR TEST 7: Consultation résa inexistante aurait dû échouer."); }
                catch (ReservationException_Exception e) { logger.info("SUCCÈS TEST 7: Échec consultation résa inexistante OK: {}", e.getFaultInfo().getMessage()); }

                logger.info("\n--- [TEST 8] Consultation résa avec mauvais client (échec attendu) ---");
                if (reservationIdPourTests != null) {
                    logger.warn("   -> Test 8 ignoré (l'API getReservation ne vérifie pas le client).");
                } else { logger.warn("   -> Test 8 ignoré."); }

                logger.info("\n--- [TEST 9] Modification valide (décalage 2j) ---");
                if (reservationIdPourTests != null) {
                    LocalDate newDateDebutObj = dateDebutObj.plusDays(2);
                    LocalDate newDateFinObj = dateFinObj.plusDays(2);
                    DemandeReservation demandeModif = new DemandeReservation();
                    demandeModif.setNom("Test"); demandeModif.setPrenom("Client"); demandeModif.setDateDebut(newDateDebutObj.toString()); demandeModif.setDateFin(newDateFinObj.toString()); demandeModif.setCarteCredit(carte); demandeModif.setNombrePersonnes(nbrPersonnes);
                    try { String updatedId = agenceService.updateReservation(reservationIdPourTests, demandeModif); logger.info("RÉUSSITE TEST 9: Résa modifiée (ID: {})", updatedId); }
                    catch (ReservationException_Exception | ChambreException_Exception e) { logger.error("ERREUR TEST 9: Modif valide échouée: {}", e.getMessage()); }
                } else { logger.warn("   -> Test 9 ignoré."); }

                logger.info("\n--- [TEST 10] Vérification de la modification ---");
                try {
                    List<DetailsReservation> listeResas = agenceService.getReservations("Test", "Client");
                    if (listeResas == null || listeResas.isEmpty()) { logger.info("Résultat Test 10: Aucune résa trouvée."); }
                    else {
                        logger.info("Résultat Test 10: {} réservation(s) trouvée(s):", listeResas.size());
                        if (reservationIdPourTests == null && !listeResas.isEmpty()) { reservationIdPourTests = listeResas.get(0).getId(); }
                        listeResas.forEach(r -> logger.info("   -> Résa ID: {}, Hôtel: {}, Chambre: {}, Dates: {}->{}", r.getId(), r.getNomHotel(), r.getNumeroChambre(), r.getDateDebut(), r.getDateFin()));
                    }
                } catch (Exception e) { logger.error("ERREUR TEST 10: ", e); }

                logger.info("\n--- [TEST 11] Modification résa inexistante (échec attendu) ---");
                DemandeReservation demandeModifInvalide = new DemandeReservation();

                demandeModifInvalide.setNom("Test"); demandeModifInvalide.setPrenom("Client"); demandeModifInvalide.setDateDebut(dateDebutObj.plusDays(5).toString()); demandeModifInvalide.setDateFin(dateFinObj.plusDays(5).toString()); demandeModifInvalide.setCarteCredit(carte); demandeModifInvalide.setNombrePersonnes(nbrPersonnes);
                try { agenceService.updateReservation(idResaInexistant, demandeModifInvalide); logger.error("ERREUR TEST 10: Modif résa inexistante aurait dû échouer."); }
                catch (ReservationException_Exception e) { logger.info("SUCCÈS TEST 10: Échec modif résa inexistante OK: {}", e.getFaultInfo().getMessage()); }
                catch (ChambreException_Exception e) { logger.error("ERREUR TEST 10: Modif résa inexistante a levé mauvaise exception (Chambre): {}", e.getFaultInfo().getMessage());}


                logger.info("\n--- [TEST 12] Modification créant un conflit (échec attendu) ---");
                if (reservationIdPourTests != null && idChambreAReserver != null) {
                    String deuxiemeResaId = null;
                    LocalDate dateDebut2 = dateFinObj.plusDays(3);
                    LocalDate dateFin2 = dateDebut2.plusDays(1);
                    DemandeReservation demandeValide2 = new DemandeReservation();
                    demandeValide2.setNom("Test"); demandeValide2.setPrenom("Client"); demandeValide2.setDateDebut(dateDebut2.toString()); demandeValide2.setDateFin(dateFin2.toString()); demandeValide2.setCarteCredit(carte); demandeValide2.setNombrePersonnes(nbrPersonnes); demandeValide2.setChambreId(idChambreAReserver);
                    try {
                        deuxiemeResaId = agenceService.reserverChambre(demandeValide2);
                        logger.info("   -> Deuxième réservation créée (ID: {}) pour test de conflit.", deuxiemeResaId);

                        DemandeReservation demandeConflit = new DemandeReservation();
                        demandeConflit.setNom("Test"); demandeConflit.setPrenom("Client");
                        demandeConflit.setDateDebut(dateDebutObj.plusDays(2).toString());
                        demandeConflit.setDateFin(dateDebut2.plusDays(1).toString());
                        demandeConflit.setCarteCredit(carte); demandeConflit.setNombrePersonnes(nbrPersonnes);
                        try {
                            agenceService.updateReservation(reservationIdPourTests, demandeConflit);
                            logger.error("ERREUR TEST 11: Modification conflictuelle aurait dû échouer.");
                        } catch (ChambreException_Exception e) {
                            logger.info("SUCCÈS TEST 11: Échec modif conflictuelle OK: {}", e.getFaultInfo().getMessage());
                        } catch (ReservationException_Exception e) {
                            logger.error("ERREUR TEST 11: Modif conflictuelle a levé mauvaise exception (Reservation): {}", e.getFaultInfo().getMessage());
                        }
                    } catch (ChambreException_Exception e) {
                        logger.error("   -> Erreur lors de la création de la 2e réservation pour le test 11: {}", e.getFaultInfo().getMessage());
                    } finally {
                        if (deuxiemeResaId != null) {
                            try { agenceService.deleteReservation(deuxiemeResaId, "Test", "Client"); } catch (Exception ignored) {}
                        }
                    }
                } else { logger.warn("   -> Test 11 ignoré."); }

                logger.info("\n--- [TEST 13] Suppression valide ---");
                if (reservationIdPourTests != null) {
                    try { boolean deleted = agenceService.deleteReservation(reservationIdPourTests, "Test", "Client"); logger.info("RÉUSSITE TEST 12: Suppression effectuée (Retour: {})", deleted); }
                    catch (Exception e) { logger.error("ERREUR TEST 12: Suppression valide échouée:", e); }
                } else { logger.warn("   -> Test 12 ignoré."); }

                logger.info("\n--- [TEST 14] Suppression inexistante (échec attendu) ---");
                if (reservationIdPourTests != null) {
                    try { boolean deletedAgain = agenceService.deleteReservation(reservationIdPourTests, "Test", "Client"); if (!deletedAgain) { logger.info("SUCCÈS TEST 13: Suppression échouée OK (Retour: false)."); } else { logger.error("ERREUR TEST 13: Suppression inexistante aurait dû retourner false."); } }
                    catch (Exception e) { logger.error("ERREUR TEST 13: Suppression inexistante a levé exception:", e); }
                } else { logger.warn("   -> Test 13 ignoré."); }


            } catch (Exception e) {
                logger.error("\n[CLIENT] ERREUR INATTENDUE GLOBALE:", e);
            }

            logger.info("\n--- [CLIENT] Fin de tous les tests ---");
        }
}