package Capteur;

import CentralDeGestion.CentraleGestion;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Capteurimpl extends UnicastRemoteObject implements CapteurInterface {

    private Capteur capteur;
    private Thread mesure;

    public Capteurimpl() throws RemoteException {
        super();
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
        }
    }

    @Override
    public void arreterMesure() throws RemoteException {
        capteur.statusCapteur=false;
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
