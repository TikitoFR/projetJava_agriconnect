package Capteur;

import java.rmi.RemoteException;

import CentralDeGestion.CentraleGestion;

public class Capteur implements Runnable {
    public String codeUnique;
    public String coordonneesGPS;
    public CentraleGestion centrale;
    public int intervalle;

    double temperature = 25 + Math.round(Math.random() * 15 * 10) / 10.0;
    double humidite = Math.round(Math.random() * 100 * 10) / 10.0;

    private int tempsEcouleEnSecondes = 0;

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
        centrale.afficherMesures(new DataCapteur(temperature, humidite, this.codeUnique));
    }

    private double measureTemp() {
        temperature += (Math.random() * 6 - 3);
        temperature = Math.min(Math.max(temperature, 0), 30);
        temperature = Math.round(temperature * 100) / 100.0;
        return temperature;
    }

    private double measureHumi() {
//        humidite += (Math.random() * 6 - 3);
//        humidite = Math.min(Math.max(humidite, 0), 100);
//        humidite = Math.round(humidite * 100) / 100.0;
        tempsEcouleEnSecondes++;
        if (tempsEcouleEnSecondes % 60 == 0) {
            humidite -= 1; // réduction de 1% toutes les 60 secondes
        }
        return humidite;
    }

    @Override
    public void run() {
        while(true) {

            while (statusCapteur) {
                try {
                    mesurer(measureTemp(), measureHumi());
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

            //System.out.println(statusCapteur);

            while (!statusCapteur){
                System.out.println("Mesure arretée");
            }
        }
    }
}