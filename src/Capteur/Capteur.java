package Capteur;
import java.rmi.RemoteException;

import CentralDeGestion.CentraleGestion;

public class Capteur implements Runnable {
    public String codeUnique;
    public String coordonneesGPS;
    public CentraleGestion centrale;
    public int intervalle;

    public boolean statusCapteur;

    public String getNom() {
        return this.codeUnique;
    }

    public String getCoordonneesGPS() {return this.coordonneesGPS;}

    public int getIntervalle() {return this.intervalle;}

    public boolean getStatus() {return this.statusCapteur;}

    public Capteur(String codeUnique, String coordonneesGPS) {
        this.codeUnique = codeUnique;
        this.coordonneesGPS = coordonneesGPS;
    }

    public void mesurer(double temperature, double humidite) throws RemoteException {
        centrale.enregistrerMesures(new DataCapteur(temperature, humidite, this.codeUnique));
    }

    @Override
    public void run() {
        while(true) {
            while (statusCapteur) {
                try {
                    mesurer(10, 10);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                try {
                    Thread.sleep(intervalle);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restaure l'état d'interruption
                    System.out.println("Thread interrompu");
                }
            }

//            if(statusCapteur) {
//                this.notify();
//            } else {
//                try {
//                    this.wait();
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//            }

        }
    }
}