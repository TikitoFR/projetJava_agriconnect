package CentralDeGestion;
import Capteur.CapteurInterface;
import Capteur.DataCapteur;

import java.util.List;

public interface CentraleGestion extends java.rmi.Remote {
    public void enregistrerMesures(DataCapteur data) throws java.rmi.RemoteException;
    public List<String> getCapteurs() throws java.rmi.RemoteException;
    public void ajouterCapteur(String nomCapteur, CentraleGestion centrale, int intervalle) throws java.rmi.RemoteException;
    public void ajouterArroseur(int id, String coordonneesGPS) throws java.rmi.RemoteException;
    public void retirerCapteur(String nomCapteur) throws java.rmi.RemoteException;
    public void demarrerMesure(String nomCapteur) throws java.rmi.RemoteException;
    public void demarrerArroseur(String idArroseur, String nomCapteur) throws java.rmi.RemoteException;
    public void arreterMesure(String nomCapteur) throws java.rmi.RemoteException;
    public void modifierIntervalle(String nomCapteur, int intervalle) throws java.rmi.RemoteException;
    public List<String> afficherInformationsCapteur(String nomCapteur) throws java.rmi.RemoteException;
    public void afficherMesures(DataCapteur data) throws java.rmi.RemoteException;
    public DataCapteur getMesures() throws java.rmi.RemoteException;
}