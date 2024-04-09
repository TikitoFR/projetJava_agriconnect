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
    double getSeuilTemp()throws RemoteException;
    double getSeuilHumi()throws RemoteException;
    void arroser(CapteurInterface capteur) throws RemoteException;
    void setCoordonneesGPS(String coordonneesGPS) throws RemoteException;
    void setZone(String terrain) throws RemoteException;
    String getZone() throws RemoteException;
    void stopArrosage() throws RemoteException;
}
