package org.test.rmi.common.interfaces;
import java.io.Serializable;

public interface EspeceService extends Serializable {
    EspeceService getEspece();
    String formatedToString();
}