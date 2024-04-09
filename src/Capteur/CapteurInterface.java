package Capteur;

import CentralDeGestion.CentraleGestion;

public interface CapteurInterface extends java.rmi.Remote {
    public void parametrerCapteur (CentraleGestion centrale, int intervalle) throws java.rmi.RemoteException;
    public void demarrerMesure () throws java.rmi.RemoteException;
    public void arreterMesure () throws java.rmi.RemoteException;
    public void modifierIntervalle (int intervalle) throws java.rmi.RemoteException;
    public void retirerCapteur () throws java.rmi.RemoteException;
    public String getNom() throws java.rmi.RemoteException;
    public String getCoordonneesGPS() throws java.rmi.RemoteException;
    public boolean getStatus() throws java.rmi.RemoteException;
    public int getIntervalle() throws java.rmi.RemoteException;
    void setHumidite(int humidite) throws java.rmi.RemoteException;
}
