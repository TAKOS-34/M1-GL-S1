package org.test.soap.server.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.test.soap.server.model.Agence;

@Repository
public interface AgenceRepository extends JpaRepository<Agence, Integer> {}