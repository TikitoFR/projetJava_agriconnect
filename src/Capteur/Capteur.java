package Capteur;
import java.rmi.RemoteException;
import java.util.Random;

import CentralDeGestion.CentraleGestion;

public class Capteur implements Runnable {
    public String codeUnique;
    public String coordonneesGPS;
    public CentraleGestion centrale;
    public int intervalle;

    public boolean statusCapteur;

    public Capteur(String codeUnique, String coordonneesGPS) {
        this.codeUnique = codeUnique;
        this.coordonneesGPS = coordonneesGPS;
    }

    public void mesurer(double temperature, double humidite) throws RemoteException {
        centrale.enregistrerMesures(new DataCapteur(temperature, humidite, this.codeUnique));
        //chose a imaginé a renvoyé au main pour tester plusieur fois la fonction
    }

    @Override
    public void run() {
        while(statusCapteur){
            try {
                Random rand = new Random();
                        // La plage de valeurs désirée est de 375 (100 - (-274) + 1)
                int plage = 100 - (-274) + 1;
    
                mesurer(rand.nextInt(plage) - 274,rand.nextInt(101) );
                
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
    }
}