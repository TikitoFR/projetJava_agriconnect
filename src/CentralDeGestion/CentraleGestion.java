package CentralDeGestion;
import Capteur.Capteur;
import Capteur.DataCapteur;
import Capteur.CapteurInterface;

import java.util.HashMap;
import java.util.List;

public interface CentraleGestion extends java.rmi.Remote {
    public void enregistrerMesures(DataCapteur data) throws java.rmi.RemoteException;
    public List<String> getNomCapteurs() throws java.rmi.RemoteException;
    public void ajouterCapteur(String nomCapteur, CentraleGestion centrale, int intervalle) throws java.rmi.RemoteException;
}