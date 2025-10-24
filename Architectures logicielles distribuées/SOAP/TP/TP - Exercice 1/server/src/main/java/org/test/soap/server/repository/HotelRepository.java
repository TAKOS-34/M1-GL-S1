package org.test.soap.server.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.test.soap.server.model.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {}