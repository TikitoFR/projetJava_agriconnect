package CentralDeGestion;
import Capteur.DataCapteur;

import java.util.HashMap;
import java.util.List;

public interface CentraleGestion extends java.rmi.Remote {
    public void enregistrerMesures(DataCapteur data) throws java.rmi.RemoteException;

    public HashMap<String, List<DataCapteur>> getCapteurs() throws java.rmi.RemoteException;
}