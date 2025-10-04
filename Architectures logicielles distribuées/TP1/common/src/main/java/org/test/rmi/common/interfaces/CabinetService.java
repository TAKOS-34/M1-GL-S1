package org.test.rmi.common.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface CabinetService extends Remote {
    boolean addAnimal(String nom, String nomMaitre, String  race, String espece) throws RemoteException;
    boolean addAnimalSpecial(AnimalService animal) throws RemoteException;
    boolean deleteAnimalByNom(String nom) throws RemoteException;
    String getAnimalByNom(String nom) throws RemoteException;
    ArrayList<String> getAllAnimals() throws RemoteException;

    boolean addEspece(String espece, int dureeVie) throws RemoteException;
    String getEspeceByNom(String nom) throws RemoteException;
    boolean deleteEspeceByNom(String nom) throws RemoteException;
    ArrayList<String> getAllEspeces() throws RemoteException;

    String getDossierByNom(String nom) throws RemoteException;
    ArrayList<String> getAllDossiers() throws RemoteException;

    boolean addAlerte(AlerteService alerte) throws RemoteException;
    boolean supprimerAlerte(AlerteService alerte) throws RemoteException;
}
