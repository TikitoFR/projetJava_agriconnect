public class Capteur {
    public String codeUnique;
    public String coordonneesGPS;

    public Capteur(String codeUnique, String coordonneesGPS) {
        this.codeUnique = codeUnique;
        this.coordonneesGPS = coordonneesGPS;
    }

    public void demarrer(CentraleDeGestion centrale) {
        centrale.ajouterCapteur(this);
        System.out.println("Capteur démarré : " + codeUnique + ", Coordonnées GPS : " + coordonneesGPS);
    }

    public void retirer(CentraleDeGestion centrale) {
        centrale.retirerCapteur(this);
        System.out.println("Capteur retiré : " + codeUnique + ", Coordonnées GPS : " + coordonneesGPS);
    }

    public void mesurer(CentraleDeGestion centrale, double temperature, double humidite) {
        centrale.enregistrerMesures(this, temperature, humidite);
    }
}