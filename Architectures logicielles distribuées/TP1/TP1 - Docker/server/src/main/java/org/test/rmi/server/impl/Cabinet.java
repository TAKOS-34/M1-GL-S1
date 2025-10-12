package org.test.rmi.server.impl;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import org.springframework.stereotype.Service;
import org.test.rmi.common.impl.Espece;
import org.test.rmi.common.interfaces.AnimalService;
import org.test.rmi.common.interfaces.CabinetService;
import org.test.rmi.common.interfaces.AlerteService;
import org.test.rmi.common.interfaces.DossierService;

@Service
public class Cabinet implements CabinetService {
    private HashMap<String, AnimalService> animals = new HashMap<>();
    private HashMap<String, Espece> especes = new HashMap<>();
    ArrayList<AlerteService> alertes = new ArrayList<>();

    public Cabinet() throws RemoteException {}

    // ----------------------- Animals

    @Override
    public String addAnimal(String nom, String nomMaitre, String race, String espece) throws RemoteException {
        Espece realEspece = this.getEspeceByNomCabinet(espece);
        if (realEspece == null) {
            return "Erreur : espèce non existante";
        }
        if (animals.containsKey(nom)) {
            return "Erreur : un animal avec ce nom existe déjà";
        }
        Animal newAnimal = new Animal(nom, nomMaitre, race, realEspece, new Dossier(nom));
        animals.put(nom, newAnimal);
        checkAlerte();
        System.out.println("> Nouvel animal ajouté, nombre actuel d'animaux : " + animals.size());
        return "Animal ajouté";
    }

    @Override
    public String addAnimalSpecial(AnimalService animal) throws RemoteException {
        if (animals.containsKey(animal.getNom())) {
            return "Erreur : un animal avec ce nom existe déjà";
        }
        animals.put(animal.getNom(), animal);
        checkAlerte();
        System.out.println("> Nouvel animal ajouté, nombre actuel d'animaux : " + animals.size());
        return "Animal spéciale ajouté";
    }

    @Override
    public String deleteAnimalByNom(String nom) throws RemoteException {
        if (animals.remove(nom) != null) {
            checkAlerte();
            System.out.println("> Animal supprimé, nombre actuel d'animaux : " + animals.size());
            return "Animal supprimé";
        }
        return "Erreur : l'animal n'a pas été supprimé";
    }

    @Override
    public String getAnimalByNom(String nom) throws RemoteException {
        if (animals.containsKey(nom)) {
            return animals.get(nom).formatedToString();
        }
        return "Erreur : aucuns animaux avec ce nom n'existe";
    }

    @Override
    public ArrayList<String> getAllAnimals() throws RemoteException {
        ArrayList<String> res = new ArrayList<>();
        animals.forEach((k, v) -> {
            res.add(v.formatedToString());
        });
        return res;
    }

    // ----------------------- Especes

    @Override
    public String addEspece(String nom, int dureeVie) throws RemoteException {
        if (especes.containsKey(nom)) {
            return "Erreur : Une espèce avec ce nom existe déjà";
        }
        if (dureeVie <= 0) {
            return "Erreur : la durée de vie doit être > 0";
        }
        especes.put(nom, new Espece(nom, dureeVie));
        return "Espèce ajouté";
    }

    @Override
    public Espece getEspeceByNom(String nom) {
        if (especes.containsKey(nom)) {
            return especes.get(nom).getEspece();
        }
        return null;
    }

    @Override
    public String deleteEspeceByNom(String nom) throws RemoteException {
        for (AnimalService animal : animals.values()) {
            if (animal.getEspece().equals(nom)) {
                return "Erreur : l'espèce est déjà attribuer à l'animal : " + animal.getNom();
            }
        }
        if (especes.remove(nom) != null) {
            return "Espèce supprimé";
        }
        return "Erreur : l'espèce n'a pas été supprimer";
    }

    public Espece getEspeceByNomCabinet(String nom) {
        if (especes.containsKey(nom)) {
            return especes.get(nom);
        }
        return null;
    }

    @Override
    public ArrayList<String> getAllEspeces() throws RemoteException {
        ArrayList<String> res = new ArrayList<>();
        especes.forEach((k, v) -> {
            res.add(v.formatedToString());
        });
        return res;
    }

    // ----------------------- Dossiers

    @Override
    public String getDossierByNom(String nom) throws RemoteException {
        for (AnimalService animal : animals.values()) {
            if (animal.getDossier() != null) {
                if (animal.getDossier().formatedToString().equals(nom)) {
                    return animal.getDossier().formatedToString();
                }
            }
        }
        return "Erreur : aucuns dossier avec ce nom n'existe";
    }

    @Override
    public ArrayList<String> getAllDossiers() throws RemoteException {
        ArrayList<String> res = new ArrayList<>();
        animals.forEach((k, v) -> {
            DossierService dossier = v.getDossier();
            if (dossier != null) {
                res.add(v.getDossier().formatedToString());
            }
        });
        return res;
    }

    @Override
    public String setDossier(String nom, String dossier) throws RemoteException {
        if (animals.containsKey(nom)) {
            DossierService d = animals.get(nom).getDossier();
            if (d != null) {
                animals.get(nom).getDossier().setNom(dossier);
                return "Dossier mis à jour";
            } else {
                return "Erreur : l'animal n'a pas de dossier";
            }
        }
        return "Erreur : aucuns animaux avec ce nom n'existe";
    }

    // ----------------------- Alertes

    @Override
    public String addAlerte(AlerteService alerte) throws RemoteException {
        if (!alertes.contains(alerte)) {
            System.out.println("> Un vétérinaire s'est ajouté à la liste d'alertes, taille de la liste : " + (alertes.size() + 1));
            boolean res = alertes.add(alerte);
            if (res) {
                return "Vous êtes maintenant dans la liste d'alertes";
            }
            return "Erreur : erreur lors de l'insertion dans la liste d'alertes";
        }
        return "Erreur : Vous êtes déjà dans la liste d'alertes";
    }

    @Override
    public String deleteAlerte(AlerteService alerte) throws RemoteException {
        for (int i = 0; i < alertes.size(); i++) {
            if (alertes.get(i).equals(alerte)) {
                alertes.remove(i);
                System.out.println("> Un vétérinaire s'est retiré de la liste, taille de la liste : " + alertes.size());
                return "Vous n'êtes plus dans la liste d'alertes";
            }
        }
        return "Erreur : vous n'êtes déjà pas dans la liste d'alertes";
    }

    public void checkAlerte() throws RemoteException {
        int size = animals.size();
        if (size == 1 || size == 3 || size == 100 ||  size == 500 || size == 1000) {
            for (AlerteService alerte : alertes) {
                alerte.message("> Alerte - Le cabinet à maintenant : " + size + " animaux\n");
            }
        }
    }
}