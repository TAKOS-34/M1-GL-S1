package org.test.soap.server.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.test.soap.server.model.Reservation;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    @Query("SELECT r FROM Reservation r " +
            "JOIN r.client c " +
            "WHERE c.nom = :nom " +
            "AND c.prenom = :prenom")
    List<Reservation> getReservations(
            @Param("nom") String nom,
            @Param("prenom") String prenom
    );

    @Query("SELECT r FROM Reservation r " +
            "JOIN r.client c " +
            "WHERE r.id = :reservationId " +
            "AND c.nom = :nom " +
            "AND c.prenom = :prenom")
    Reservation getReservationsByIdAndClient(
            @Param("reservationId") String reservationId,
            @Param("nom") String nom,
            @Param("prenom") String prenom
    );

    @Query("SELECT COUNT(r) FROM Reservation r " +
            "WHERE r.chambre.id = :chambreId " +
            "AND r.id != :reservationId " +
            "AND (r.dateFin > :dateDebut AND r.dateDebut < :dateFin)")
    int verifyIfConflictExists(
            @Param("reservationId") String reservationId,
            @Param("chambreId") String chambreId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}