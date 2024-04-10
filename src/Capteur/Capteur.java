package Capteur;

import java.rmi.RemoteException;

import CentralDeGestion.CentraleGestion;
/**
 * Cette classe représente un capteur dans le système.
 * Elle implémente l'interface Runnable pour permettre l'exécution dans un thread.
 */
public class Capteur implements Runnable {
    public String codeUnique;
    public String coordonneesGPS;
    public CentraleGestion centrale;
    public int intervalle;

    double temperature = 25 + Math.round(Math.random() * 15 * 10) / 10.0;
    double humidite = Math.round(Math.random() * 100 * 10) / 10.0;

    private int tempsEcouleEnSecondes = 0;

    public boolean statusCapteur;
  /**
     * Récupère le nom du capteur.
     * @return Le nom du capteur.
     */
    public String getNom() {
        return this.codeUnique;
    }
  /**
     * Récupère les coordonnées GPS du capteur.
     * @return Les coordonnées GPS du capteur.
     */
    public String getCoordonneesGPS() {return this.coordonneesGPS;}
/**
     * Récupère l'intervalle de mesure du capteur.
     * @return L'intervalle de mesure du capteur.
     */
    public int getIntervalle() {return this.intervalle;}
 /**
     * Récupère le statut du capteur.
     * @return Le statut du capteur (activé ou désactivé).
     */
    public boolean getStatus() {return this.statusCapteur;}
 /**
     * Constructeur de la classe Capteur.
     * @param codeUnique Le code unique du capteur.
     * @param coordonneesGPS Les coordonnées GPS du capteur.
     */
    public Capteur(String codeUnique, String coordonneesGPS) {
        this.codeUnique = codeUnique;
        this.coordonneesGPS = coordonneesGPS;
    }
 /**
     * Effectue une mesure de température et d'humidité, puis enregistre et affiche les mesures.
     * @param temperature La température mesurée.
     * @param humidite L'humidité mesurée.
     * @throws RemoteException En cas d'erreur lors de la communication distante avec la centrale.
     */
    public void mesurer(double temperature, double humidite) throws RemoteException {
        centrale.enregistrerMesures(new DataCapteur(temperature, humidite, this.codeUnique));
        centrale.afficherMesures(new DataCapteur(temperature, humidite, this.codeUnique));
    }
   /**
     * Mesure la température et met à jour la valeur de température du capteur.
     * @return La nouvelle valeur de température.
     */
    private double measureTemp() {
        temperature += (Math.random() * 6 - 3);
        temperature = Math.min(Math.max(temperature, 0), 30);
        temperature = Math.round(temperature * 100) / 100.0;
        return temperature;
    }
    /**
     * Mesure l'humidité et met à jour la valeur d'humidité du capteur.
     * @return La nouvelle valeur d'humidité.
     */
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
 /**
     * Implémentation de la méthode run() de l'interface Runnable.
     * Effectue en continu les mesures de température et d'humidité lorsque le capteur est activé.
     */
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
            while (!statusCapteur){
                System.out.println("Mesure arretée");
            }
        }
    }
}