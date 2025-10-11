package org.test.rmi.common.interfaces;

public interface AnimalService {
    String getNom();
    String getEspece();
    DossierService getDossier();
    String formatedToString();
}