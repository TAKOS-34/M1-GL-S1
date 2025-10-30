package org.test.soap.agence.service;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.test.soap.agence.model.CarteCredit;

@Service
public class ServicePaiement {
    public String genererTokenPaiement(CarteCredit carte) {
        if (carte == null) { // On peut simuler ici un vrai appel API
            throw new IllegalArgumentException("La carte de crédit n'est pas valide");
        }
        return UUID.randomUUID().toString();
    }
}