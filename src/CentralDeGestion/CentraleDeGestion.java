package CentralDeGestion;
import java.util.ArrayList;
import java.util.List;

import Capteur.Capteur;
public class CentraleDeGestion {
    private List<Capteur> capteurs = new ArrayList<>();

    public void ajouterCapteur(Capteur capteur) {
        capteurs.add(capteur);
    }

    public void retirerCapteur(Capteur capteur) {
        capteurs.remove(capteur);
    }

    public void afficherInformationsCapteur(Capteur capteur) {
        System.out.println("Informations du capteur, code unique : " + capteur.codeUnique + ", Coordonnées GPS : " + capteur.coordonneesGPS);
        // Afficher d'autres informations si nécessaire
    }

    public void enregistrerMesures(Capteur capteur, double temperature, double humidite) {
        System.out.println("Mesures du capteur " + capteur.codeUnique + " - Température : " + temperature + ", Humidité : " + humidite);
    }
}