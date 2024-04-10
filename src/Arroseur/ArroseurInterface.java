package Arroseur;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Capteur.CapteurInterface;

public interface ArroseurInterface extends Remote {
    void activer() throws RemoteException;
    void desactiver() throws RemoteException;
    String getCoordonneesGPS()throws RemoteException;
    int getId()throws RemoteException;
    boolean getStatus()throws RemoteException;
    void arroser(CapteurInterface capteur) throws RemoteException;
    void setCoordonneesGPS(String coordonneesGPS) throws RemoteException;
    void stopArrosage() throws RemoteException;
    void retirerArroseur() throws RemoteException;
}
