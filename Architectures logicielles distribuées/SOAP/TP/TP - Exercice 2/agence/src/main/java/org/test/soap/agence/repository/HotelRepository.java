package org.test.soap.agence.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.test.soap.agence.model.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {}