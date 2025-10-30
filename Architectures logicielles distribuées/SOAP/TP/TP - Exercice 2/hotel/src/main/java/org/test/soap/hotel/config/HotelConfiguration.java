package org.test.soap.hotel.config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.test.soap.hotel.service.Hotel;

@Configuration
public class HotelConfiguration {
    private Logger logger = LoggerFactory.getLogger(HotelConfiguration.class);

    @Value("${hotel.instance.nom}")
    private String nom;

    @Value("${hotel.instance.marque}")
    private String marque;

    @Value("${hotel.instance.pays}")
    private String pays;

    @Value("${hotel.instance.ville}")
    private String ville;

    @Value("${hotel.instance.adresse}")
    private String adresse;

    @Value("${hotel.instance.coordonneeGps}")
    private String coordonneesGps;

    @Value("${hotel.instance.etoile}")
    private int etoile;

    @Bean
    public Hotel currentHotel() {
        Hotel h = new Hotel(nom, marque, pays,  ville, adresse, coordonneesGps, etoile);

        logger.info("Hôtel " + nom + " dans la ville de " + ville + " de la marque " + marque + " à été créer");

        return h;
    }
}