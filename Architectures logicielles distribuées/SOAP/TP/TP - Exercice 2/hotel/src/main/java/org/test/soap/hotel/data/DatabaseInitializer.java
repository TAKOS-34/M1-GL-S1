package org.test.soap.hotel.data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test.soap.hotel.service.Hotel;
import org.test.soap.hotel.repository.ChambreRepository;
import org.test.soap.hotel.repository.ReservationRepository;
import org.test.soap.hotel.service.HotelService;
import org.test.soap.hotel.service.HotelServiceImpl;

@Configuration
public class DatabaseInitializer {
	private Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

    @Autowired
    private Hotel hotel;

	@Bean
	CommandLineRunner initDataBase(ChambreRepository chambreRepo, ReservationRepository reservationRepo) {
		return args -> {
            logger.info("Initialisation terminée");
        };
    }

	@Bean
    HotelService initHotelService() {
		return new HotelServiceImpl();
	}
}
