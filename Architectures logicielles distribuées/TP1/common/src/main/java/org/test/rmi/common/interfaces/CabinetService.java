package org.test.rmi.common.interfaces;
import java.util.ArrayList;

public interface CabinetService {
    boolean addAnimal(String nom, String nomMaitre, String  race, String espece, String dossier);
    String getAnimalByNom(String nom);
    ArrayList<String> getAllAnimals();

    boolean addEspece(String espece, int dureeVie);

    boolean addDossier(String nom);
}
