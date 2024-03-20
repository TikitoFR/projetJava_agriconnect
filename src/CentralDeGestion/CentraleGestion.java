package CentralDeGestion;
import Capteur.Capteur;
import Capteur.DataCapteur;
import Capteur.CapteurInterface;

import java.util.HashMap;
import java.util.List;

public interface CentraleGestion extends java.rmi.Remote {
    public void enregistrerMesures(DataCapteur data) throws java.rmi.RemoteException;
    public List<String> getCapteurs() throws java.rmi.RemoteException;
    public void ajouterCapteur(String nomCapteur, CentraleGestion centrale, int intervalle) throws java.rmi.RemoteException;
    public void retirerCapteur(String nomCapteur) throws java.rmi.RemoteException;
    public void demarrerMesure(String nomCapteur) throws java.rmi.RemoteException;
    public void arreterMesure(String nomCapteur) throws java.rmi.RemoteException;
    public void modifierIntervalle(String nomCapteur, int intervalle) throws java.rmi.RemoteException;
    public List<String> afficherInformationsCapteur(String nomCapteur) throws java.rmi.RemoteException;
}