package org.test.rmi.server.impl;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import org.test.rmi.common.interfaces.AnimalService;
import org.test.rmi.common.interfaces.CabinetService;
import org.test.rmi.common.interfaces.AlerteService;

@Service
public class Cabinet implements CabinetService {
    private HashMap<String, AnimalService> animals = new HashMap<>();
    private HashMap<String, Espece> especes = new HashMap<>();
    private HashMap<String, Dossier> dossiers = new HashMap<>();
    ArrayList<AlerteService> alertes = new ArrayList<>();

    public Cabinet() {}

    // ----------------------- Animals

    @Override
    public boolean addAnimal(String nom, String nomMaitre, String race, String espece) throws RemoteException {
        Espece realEspece = this.getEspeceByNomCabinet(espece);
        if (realEspece == null) {
            return false;
        }
        Dossier dossier = this.addDossier(nom);
        Animal newAnimal = new Animal(nom, nomMaitre, race, realEspece, dossier);
        animals.put(nom, newAnimal);
        checkAlerte();
        System.out.println("Nouvel animal ajouté, nombre actuel d'animaux : " + animals.size());
        return true;
    }

    @Override
    public boolean addAnimalSpecial(AnimalService animal) throws RemoteException {
        if (!animals.containsKey(animal.getNom())) {
            animals.put(animal.getNom(), animal);
            checkAlerte();
            System.out.println("Nouvel animal ajouté, nombre actuel d'animaux : " + animals.size());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteAnimalByNom(String nom) throws RemoteException {
        boolean deleted = animals.remove(nom) != null;
        if (deleted) {
            checkAlerte();
            System.out.println("Animal supprimé, nombre actuel d'animaux : " + animals.size());
        }
        return deleted;
    }

    @Override
    public String getAnimalByNom(String nom) {
        if (animals.containsKey(nom)) {
            return animals.get(nom).formatedToString();
        }
        return "Aucun animal avec ce nom n'existe";
    }

    @Override
    public ArrayList<String> getAllAnimals() {
        ArrayList<String> res = new ArrayList<>();
        animals.forEach((k, v) -> {
           res.add(v.formatedToString()); 
        });
        return res;
    }

    // ----------------------- Especes

    @Override
    public boolean addEspece(String nom, int dureeVie) {
        if (!especes.containsKey(nom) && dureeVie > 0) {
            especes.put(nom, new Espece(nom, dureeVie));
            return true;
        }
        return false;
    }

    @Override
    public String getEspeceByNom(String nom) {
        if (especes.containsKey(nom)) {
            return especes.get(nom).formatedToString();
        }
        return "Aucune espece avec ce nom n'existe";
    }

    @Override
    public boolean deleteEspeceByNom(String nom) throws RemoteException {
        for (AnimalService animal : animals.values()) {
            if (animal.getEspece().equals(nom)) {
                return false;
            }
        }
        boolean deleted = especes.remove(nom) != null;
        if (deleted) checkAlerte();
        return deleted;
    }

    public Espece getEspeceByNomCabinet(String nom) {
        if (especes.containsKey(nom)) {
            return especes.get(nom);
        }
        return null;
    }

    @Override
    public ArrayList<String> getAllEspeces() {
        ArrayList<String> res = new ArrayList<>();
        especes.forEach((k, v) -> {
            res.add(v.formatedToString());
        });
        return res;
    }

    // ----------------------- Dossiers

    public Dossier addDossier(String nom) {
        if (!dossiers.containsKey(nom)) {
            Dossier newDossier = new Dossier("Dossier_" + nom);
            dossiers.put(nom, newDossier);
            return newDossier;
        }
        return null;
    }

    @Override
    public String getDossierByNom(String nom) {
        if (dossiers.containsKey(nom)) {
            return dossiers.get(nom).formatedToString();
        }
        return "Aucune dossier avec ce nom n'existe";
    }

    @Override
    public ArrayList<String> getAllDossiers() {
        ArrayList<String> res = new ArrayList<>();
        dossiers.forEach((k, v) -> {
            res.add(v.formatedToString());
        });
        return res;
    }

    // ----------------------- Alertes

    @Override
    public boolean addAlerte(AlerteService alerte) {
        if (!alertes.contains(alerte)) {
            System.out.println("Un vétérinaire s'est connecté, nombre actuel de vétérinaires : " + (alertes.size() + 1));
            return alertes.add(alerte);
        }
        return false;
    }

    @Override
    public boolean supprimerAlerte(AlerteService alerte) throws RemoteException {
        for (int i = 0; i < alertes.size(); i++) {
            if (alertes.get(i).equals(alerte)) {
                alertes.remove(i);
                System.out.println("Un vétérinaire s'est déconnecté, nombre actuel de vétérinaires : " + alertes.size());
                return true;
            }
        }
        return false;
    }

    public void checkAlerte() throws RemoteException {
        int size = animals.size();
        if (size == 1 || size == 3 || size == 100 ||  size == 500 || size == 1000) {
            for (AlerteService alerte : alertes) {
                alerte.message("Le cabinet à maintenant : " + size + " animaux\n");
            }
        }
    }
}
