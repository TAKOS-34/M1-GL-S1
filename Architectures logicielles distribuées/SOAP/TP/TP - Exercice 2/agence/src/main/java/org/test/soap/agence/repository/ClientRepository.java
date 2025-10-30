package org.test.soap.agence.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.test.soap.agence.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Integer> {
    @Query("SELECT c FROM Client c " +
            "WHERE c.nom = :nom " +
            "AND c.prenom = :prenom")
    Client findByNomPrenom(
            @Param("nom") String nom,
            @Param("prenom") String prenom
    );
}