package CentralDeGestion;
import Capteur.DataCapteur;

public interface CentraleGestion extends java.rmi.Remote {
    public void enregistrerMesures(DataCapteur data) throws java.rmi.RemoteException;
}