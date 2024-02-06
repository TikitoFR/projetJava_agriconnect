import java.util.ArrayList;
import java.util.List;
public class CentraleDeGestion {
    private List<Capteur> capteurs = new ArrayList<>();

    public void ajouterCapteur(Capteur capteur) {
        capteurs.add(capteur);
    }

    public void retirerCapteur(Capteur capteur) {
        capteurs.remove(capteur);
    }

    public void afficherInformationsCapteur(Capteur capteur) {
        System.out.println("Informations du capteur " + capteur.codeUnique);
        // Afficher d'autres informations si nécessaire
    }

    public void enregistrerMesures(Capteur capteur, double temperature, double humidite) {
        System.out.println("Mesures du capteur " + capteur.codeUnique + " - Température : " + temperature + ", Humidité : " + humidite);
    }
}