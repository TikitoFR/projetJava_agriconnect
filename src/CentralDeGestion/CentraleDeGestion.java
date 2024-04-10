package CentralDeGestion;
import java.util.ArrayList;
import java.util.List;

import Capteur.Capteur;

/**
 * La classe CentraleDeGestion représente un système de gestion centralisé pour les capteurs.
 * Elle permet d'ajouter, de supprimer et d'afficher des informations sur les capteurs, ainsi que
 * d'enregistrer des mesures provenant des capteurs.
 */
public class CentraleDeGestion {
        /** Liste pour stocker les capteurs gérés par le système. */

    private List<Capteur> capteurs = new ArrayList<>();
   /**
     * Ajoute un capteur à la liste des capteurs gérés.
     * @param capteur Le capteur à ajouter.
     */
    public void ajouterCapteur(Capteur capteur) {
        capteurs.add(capteur);
    }

    /**
     * Supprime un capteur de la liste des capteurs gérés.
     * @param capteur Le capteur à supprimer.
     */
    public void retirerCapteur(Capteur capteur) {
        capteurs.remove(capteur);
    }
  /**
     * Affiche les informations concernant un capteur spécifique.
     * @param capteur Le capteur dont les informations doivent être affichées.
     */
    public void afficherInformationsCapteur(Capteur capteur) {
        System.out.println("Informations du capteur, code unique : " + capteur.codeUnique + ", Coordonnées GPS : " + capteur.coordonneesGPS);
        // Afficher d'autres informations si nécessaire
    }
    /**
     * Enregistre les mesures provenant d'un capteur spécifique.
     * @param capteur Le capteur à partir duquel les mesures sont enregistrées.
     * @param temperature La mesure de température.
     * @param humidite La mesure d'humidité.
     */
    public void enregistrerMesures(Capteur capteur, double temperature, double humidite) {
        System.out.println("Mesures du capteur " + capteur.codeUnique + " - Température : " + temperature + ", Humidité : " + humidite);
    }
}