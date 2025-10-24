package org.test.soap.server.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.test.soap.server.model.Chambre;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ChambreRepository extends JpaRepository<Chambre, UUID> {

    @Query("SELECT c FROM Chambre c JOIN c.hotel h " +
            "WHERE h.ville = :nomVille " +
            "AND h.etoile >= :etoileMin " +
            "AND c.nombreLits >= :nbrPersonnes " +
            "AND c.prixParNuit BETWEEN :prixMin AND :prixMax " +
            "AND NOT EXISTS (" +
            "    SELECT r FROM Reservation r " +
            "    WHERE r.chambre = c " +
            "    AND (r.dateFin > :dateDebut AND r.dateDebut < :dateFin)" +
            ")")
    List<Chambre> findAvailableChambres(
            @Param("nomVille") String nomVille,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("prixMin") float prixMin,
            @Param("prixMax") float prixMax,
            @Param("etoileMin") int etoileMin,
            @Param("nbrPersonnes") int nbrPersonnes
    );

    @Query("SELECT c FROM Chambre c " +
            "WHERE c.id = :chambreId " +
            "AND c.nombreLits >= :nbrPersonnes " +
            "AND NOT EXISTS (" +
            "    SELECT r FROM Reservation r " +
            "    WHERE r.chambre = c " +
            "    AND (r.dateFin > :dateDebut AND r.dateDebut < :dateFin)" +
            ")")
    Chambre isChambreAvailable(
            @Param("chambreId") String chambreId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin,
            @Param("nbrPersonnes") int nbrPersonnes
    );
}