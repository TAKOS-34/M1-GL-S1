package org.test.rmi.server.impl;
import org.springframework.stereotype.Service;
import org.test.rmi.common.interfaces.AnimalService;

@Service
public class Animal implements AnimalService {
    private static String nom;
    private static String nomMaitre;
    private static String espece;
    private static String race;

    static {
        nom = "test1";
        nomMaitre = "test2";
        espece = "test3";
        race = "test4";
    }

    @Override
    public String afficher() {
        return "Nom : " + nom + ", Maitre : " + nomMaitre + ", Espece : " + espece + ", Race : " + race;
    }
}