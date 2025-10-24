package org.test.soap.server.data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test.soap.server.model.Agence;
import org.test.soap.server.model.Chambre;
import org.test.soap.server.model.Hotel;
import org.test.soap.server.repository.AgenceRepository;
import org.test.soap.server.repository.ChambreRepository;
import org.test.soap.server.repository.HotelRepository;
import org.test.soap.server.service.AgenceService;
import org.test.soap.server.service.AgenceServiceImpl;
import java.util.Arrays;
import java.util.List;

@Configuration
public class DatabaseInitializer {
	private Logger logger = LoggerFactory.getLogger(DatabaseInitializer.class);

	@Bean
	CommandLineRunner initDataBase(AgenceRepository agenceRepo,
                                   HotelRepository hotelRepo,
                                   ChambreRepository chambreRepo) {
		return args -> {
            Agence agenceAccor = agenceRepo.save(new Agence("Groupe Accor"));

            List<String> villes = Arrays.asList("Paris", "Marseille", "Lyon", "Montpellier", "Nice");
            String[] marques = {"Ibis", "Mercure", "Accor"};
            int[] etoiles = {3, 4, 5};
            float[] prixBase = {85.0f, 130.0f, 210.0f};

            for (String ville : villes) {
                for (int m = 0; m < marques.length; m++) {

                    Hotel hotel = new Hotel(
                            agenceAccor,
                            marques[m] + " " + ville + " Centre",
                            marques[m],
                            "France",
                            ville,
                            "10 Avenue de la République, " + ville,
                            "45.123, 3.456",
                            etoiles[m]
                    );
                    hotelRepo.save(hotel);

                    for (int i = 1; i <= 5; i++) {
                        int lits = (i <= 2) ? 1 : 2;
                        float prixNuit = prixBase[m] + (lits * 10.0f);

                        String numeroChambre;
                        if (lits == 1) {
                            numeroChambre = "10" + i;
                        } else {
                            numeroChambre = "20" + (i - 2);
                        }

                        Chambre chambre = new Chambre(
                                hotel,
                                numeroChambre,
                                lits,
                                prixNuit
                        );
                        chambreRepo.save(chambre);
                    }
                }
            }
            logger.info("Database initialized");
        };
    }

	@Bean
    AgenceService initAgenceService() {
		return new AgenceServiceImpl();
	}
}
