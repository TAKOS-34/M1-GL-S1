package org.test.rmi.client.impl;
import org.test.rmi.common.interfaces.AlerteService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Alerte extends UnicastRemoteObject implements AlerteService {
    public Alerte() throws RemoteException {
        super();
    }

    @Override
    public void message(String message) throws RemoteException {
        System.out.println(message);
    }
}