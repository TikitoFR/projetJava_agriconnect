package CentralDeGestion;
import Capteur.DataCapteur;

public interface CentraleGestion extends java.rmi.Remote {
    public void ajouterCapteur(String capteur) throws java.rmi.RemoteException;
    public void retirerCapteur(String capteur) throws java.rmi.RemoteException;
    public void enregistrerMesures(DataCapteur data) throws java.rmi.RemoteException;
    
}