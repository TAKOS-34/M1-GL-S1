package org.test.rmi.server.impl;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.test.rmi.common.interfaces.CabinetService;

@Service
public class Cabinet implements CabinetService {
    private ArrayList<Animal> animals = new ArrayList<>();
    private ArrayList<Espece> especes = new ArrayList<>();
    private ArrayList<Dossier> dossiers = new ArrayList<>();

    public Cabinet() {}

    @Override
    public boolean addAnimal(String nom, String nomMaitre, String race, String espece, String dossier) {
        Espece realEspece = getEspecebyNom(espece);
        Dossier realDossier = getDossierbyNom(dossier);
        Animal newAnimal = new Animal(nom, nomMaitre, race, realEspece, realDossier);

        return animals.add(newAnimal);
    }

    @Override
    public String getAnimalByNom(String nom) {
        for (Animal animal : animals) {
            if (animal.getNom().equals(nom)) {
                return animal.afficher();
            }
        }
        return "Aucun animal avec ce nom n'existe";
    }

    @Override
    public ArrayList<String> getAllAnimals() {
        ArrayList<String> allAnimals = new ArrayList<>();
        for (Animal animal : animals) {
            allAnimals.add(animal.afficher());
        }
        return allAnimals;
    }

    @Override
    public boolean addEspece(String nom, int dureeVie) {
        Espece espece = new Espece(nom, dureeVie);
        return especes.add(espece);
    }

    @Override
    public boolean addDossier(String nom) {
        Dossier dossier = new Dossier(nom);
        return dossiers.add(dossier);
    }

    public Espece getEspecebyNom(String nom) {
        for (Espece espece : especes) {
            if (espece.equals(nom)) {
                return espece;
            }
        }
        return null;
    }

    public Dossier getDossierbyNom(String nom) {
        for (Dossier dossier : dossiers) {
            if (dossier.equals(nom)) {
                return dossier;
            }
        }
        return null;
    }
}
