import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
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
        String result = "Mesures du capteur " + capteur.codeUnique + " - Température : " + temperature + ", Humidité : " + humidite;
        System.out.println(result);

        try (FileWriter writer = new FileWriter("result.txt", true)) {
            writer.write(result + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}