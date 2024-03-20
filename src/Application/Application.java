package Application;

import Capteur.Capteur;
import CentralDeGestion.CentraleGestion;
import Capteur.DataCapteur;
import Capteur.CapteurInterface;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Application {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/java_agriconnect?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "passroot";

    private static CentraleGestion centrale;

    public static void afficherCapteur(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet capteur = statement.executeQuery("SELECT * FROM Capteur;");

        while (capteur.next()) {
            String codeUnique = capteur.getString("codeUnique");
            String coordonneesGPS = capteur.getString("coordonneesGPS");

            System.out.println("Code unique : " + codeUnique + " | coordonneesGPS : " + coordonneesGPS);
        }
    }

    public static void afficherMesures(Connection conn, String codeUnique) throws SQLException {
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Mesure WHERE codeUnique = ?");
        statement.setString(1, codeUnique);
        ResultSet mesures = statement.executeQuery();

        while (mesures.next()) {
            int id = mesures.getInt("id");
            java.sql.Timestamp dateHeure = mesures.getTimestamp("dateHeure");
            double temperature = mesures.getDouble("temperature");
            double humidite = mesures.getDouble("humidite");

            // Affichage des informations de chaque mesure
            System.out.println("Code Unique: " + codeUnique + ", Date/Heure: " + dateHeure +
                    ", Température: " + temperature + "°C, Humidité: " + humidite + "%");
        }
    }

    public static void listerCapteurs() throws RemoteException {
        System.out.println("Liste des capteurs:");
        List<String>  listeCapteur = centrale.getNomCapteurs();
        for(int i=0; i<listeCapteur.size(); i++){
            System.out.println(listeCapteur.get(i));
        }
    }


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            centrale = (CentraleGestion) Naming.lookup("rmi://localhost:1099/CentraleGestion");

            Scanner scanner = new Scanner(System.in);

            boolean quitter = false;
            while (!quitter) {
                System.out.println("Menu:");
                System.out.println("1. Lister les capteurs connecté sur la centrale");
                System.out.println("1. Lister les capteurs enregistré dans la BDD");
                System.out.println("2. Obtenir les mesures d'un capteur");
                System.out.println("3. Modifier intervalle de mesure pour un capteur");
                System.out.println("4. Ajouter un capteur");
                System.out.println("5. Activer un capteur");
                System.out.println("6. Désactiver un capteur");
                System.out.println("7. Calculer la moyenne et la tendance pour un capteur");
                System.out.println("5. Quitter");
                System.out.print("Votre choix: ");

                int choix = scanner.nextInt();
                scanner.nextLine(); // Pour consommer la nouvelle ligne après nextInt()

                switch (choix) {
                    case 1:
                        listerCapteurs();
                        break;
                    case 2:
                        afficherCapteur(conn);
                        break;
                    case 3:
                        System.out.print("Entrez le code unique du capteur: ");
                        String codeCapteur = scanner.nextLine();
                        afficherMesures(conn, codeCapteur);
                        break;
                    case 4:

                        break;
                    case 5:
                        System.out.println("Entrez le nom du capteur :");
                        String nomCapteur = scanner.nextLine(); // Lire la saisie utilisateur

                        break;
                    case 6:
                        quitter = true;
                        break;
                    default:
                        System.out.println("Choix invalide, veuillez réessayer.");
                }
            }

            conn.close(); // Fermer la connexion à la base de données

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}