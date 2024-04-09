package Capteur;

import CentralDeGestion.CentraleGestion;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Capteurimpl extends UnicastRemoteObject implements CapteurInterface {

    private Capteur capteur;
    private Thread mesure;

    public Capteurimpl(Capteur capteur) throws RemoteException {
        super();
        this.capteur=capteur;
    }

    public String getNom() throws RemoteException{
        return capteur.getNom();
    }

    public String getCoordonneesGPS() throws RemoteException{
        return capteur.getCoordonneesGPS();
    }

    public int getIntervalle() throws RemoteException{
        return capteur.getIntervalle();
    }

    public boolean getStatus() throws RemoteException{
        return capteur.getStatus();
    }

    public void setHumidite(int humidite) {
        capteur.humidite += humidite;
    }

    @Override
    public void parametrerCapteur(CentraleGestion centrale, int intervalle) throws RemoteException {
        capteur.centrale=centrale;
        capteur.intervalle=intervalle;
    }

    @Override
    public void demarrerMesure() throws RemoteException {
        if (mesure==null) {
            System.out.println("Mesure démarré pour : " + capteur.codeUnique);
            capteur.statusCapteur = true;
            Thread mesure = new Thread(capteur);
            this.mesure=mesure;
            mesure.start();
        }else if (!capteur.statusCapteur) {
            capteur.statusCapteur = true;
            System.out.println("Mesure reprise pour : " + capteur.codeUnique);
        }
    }

    @Override
    public void arreterMesure() throws RemoteException {
        capteur.statusCapteur = false;
        System.out.println("Mesure arrété pour : " + capteur.codeUnique);
    }

    @Override
    public void modifierIntervalle(int intervalle) throws RemoteException {
        capteur.intervalle=intervalle;
    }
    @Override
    public void retirerCapteur() throws RemoteException {
        capteur.statusCapteur=false;
        capteur.centrale=null;
        System.out.println("Capteur retiré : " + capteur.codeUnique);
    }
}
