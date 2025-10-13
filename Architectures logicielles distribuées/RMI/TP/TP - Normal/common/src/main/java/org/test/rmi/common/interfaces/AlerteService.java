package org.test.rmi.common.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AlerteService extends Remote {
    void message(String message) throws RemoteException;
}