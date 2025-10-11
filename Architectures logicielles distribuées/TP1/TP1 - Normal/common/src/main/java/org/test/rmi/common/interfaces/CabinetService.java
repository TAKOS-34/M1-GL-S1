package org.test.rmi.common.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface CabinetService extends Remote {
    String addAnimal(String nom, String nomMaitre, String  race, String espece) throws RemoteException;
    String addAnimalSpecial(AnimalService animal) throws RemoteException;
    String deleteAnimalByNom(String nom) throws RemoteException;
    String getAnimalByNom(String nom) throws RemoteException;
    ArrayList<String> getAllAnimals() throws RemoteException;

    String addEspece(String espece, int dureeVie) throws RemoteException;
    String getEspeceByNom(String nom) throws RemoteException;
    String deleteEspeceByNom(String nom) throws RemoteException;
    ArrayList<String> getAllEspeces() throws RemoteException;

    String getDossierByNom(String nom) throws RemoteException;
    ArrayList<String> getAllDossiers() throws RemoteException;
    String setDossier(String nomAnimal, String dossier) throws RemoteException;

    String addAlerte(AlerteService alerte) throws RemoteException;
    String deleteAlerte(AlerteService alerte) throws RemoteException;
}