import java.util.Random;

public class ApplicationAgriconnect {
    public static void main(String[] args) {
        CentraleDeGestion centrale = new CentraleDeGestion();

        // Création de 5 capteurs
        Capteur capteur1 = new Capteur("C001", "GPS1");
        Capteur capteur2 = new Capteur("C002", "GPS2");
        Capteur capteur3 = new Capteur("C003", "GPS3");
        Capteur capteur4 = new Capteur("C004", "GPS4");
        Capteur capteur5 = new Capteur("C005", "GPS5");

        // Démarrage des capteurs
        capteur1.demarrer(centrale);
        capteur2.demarrer(centrale);
        capteur3.demarrer(centrale);
        capteur4.demarrer(centrale);
        capteur5.demarrer(centrale);

        // Intervalle de mesure
        int intervalleMesure = 5000; // 5 secondes

        // Boucle de temporisation pour effectuer les mesures à intervalles réguliers
        try {
            for (int i = 0; i < 4; i++) {
            //while (true) {
                Thread.sleep(intervalleMesure);

                // Simulation des mesures
                Random random = new Random();
                capteur1.mesurer(centrale, random.nextDouble() * 30, random.nextDouble() * 100);
                capteur2.mesurer(centrale, random.nextDouble() * 30, random.nextDouble() * 100);
                capteur3.mesurer(centrale, random.nextDouble() * 30, random.nextDouble() * 100);
                capteur4.mesurer(centrale, random.nextDouble() * 30, random.nextDouble() * 100);
                capteur5.mesurer(centrale, random.nextDouble() * 30, random.nextDouble() * 100);
            }

            // Retirer les capteurs
            capteur1.retirer(centrale);
            capteur2.retirer(centrale);
            capteur3.retirer(centrale);
            capteur4.retirer(centrale);
            capteur5.retirer(centrale);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
