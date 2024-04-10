package Capteur;

import CentralDeGestion.CentraleGestion;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
/**
 * Implémentation de l'interface CapteurInterface pour le capteur.
 * Cette classe permet de gérer les opérations liées au capteur à distance via RMI.
 */
public class Capteurimpl extends UnicastRemoteObject implements CapteurInterface {

    private Capteur capteur;
    private Thread mesure;
/**
     * Constructeur de la classe Capteurimpl.
     * @param capteur L'instance du capteur associée à cette implémentation.
     * @throws RemoteException En cas d'erreur lors de la communication distante.
     */
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


    // Methode permettant de parametrer le capteur depuis la centrale
    @Override
    public void parametrerCapteur(CentraleGestion centrale, int intervalle) throws RemoteException {
        capteur.centrale=centrale;
        capteur.intervalle=intervalle;
    }


    // Methode perettant le demarrage des mesures sur le capteur
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
