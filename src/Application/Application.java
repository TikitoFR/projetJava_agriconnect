package Application;

import Capteur.DataCapteur;
import CentralDeGestion.CentraleGestion;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.List;
import java.util.Objects;
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
        List<String>  listeCapteur = centrale.getCapteurs();
        for(int i=0; i<listeCapteur.size(); i++){
            System.out.println(listeCapteur.get(i));
        }
    }

//    public static void getMesures() throws RemoteException {
//
//        Scanner scanner = new Scanner(System.in);
//        boolean continuer = true;
//
//        System.out.println("Appuyez sur une touche pour arrêter la récupération des mesures...");
//        String input = scanner.nextLine();
//        while (input.isEmpty()) {
//            DataCapteur data = centrale.getMesures();
//            System.out.println(data);
//        }
//    }

    public static void getMesures() throws RemoteException {

        Scanner scanner = new Scanner(System.in);
        boolean continuer = true;
        String dernierData = null;

        System.out.println("Appuyez sur Entrée pour actualiser la récupération des mesures...");
        System.out.println("Entrer un caractere et appuyer sur entrée pour quitter");
        String input = scanner.nextLine();
        while (input.isEmpty()) {
            DataCapteur data = centrale.getMesures();
            String nouvelleData = data.toString();

            // Vérifier si la nouvelle data est différente de la dernière
            if (!Objects.equals(nouvelleData, dernierData)) {
                System.out.println(nouvelleData);
                dernierData = nouvelleData; // Mettre à jour le dernierData
            }

            input = scanner.nextLine(); // Attendre la prochaine entrée
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
                System.out.println("-------------------------------------------------");
                System.out.println("1. Lister les capteurs connecté sur la centrale");
                System.out.println("-------------------------------------------------");
                System.out.println("2. Lister les mesures qui arrive sur la centrale");
                System.out.println("-------------------------------------------------");
                System.out.println("3. Lister les capteurs enregistré dans la BDD");
                System.out.println("-------------------------------------------------");
                System.out.println("4. Obtenir les mesures d'un capteur depuis la BDD");
                System.out.println("-------------------------------------------------");
                System.out.println("5. Afficher les infos d'un capteur depuis la centrale");
                System.out.println("-------------------------------------------------");
                System.out.println("6. Modifier intervalle de mesure pour un capteur");
                System.out.println("-------------------------------------------------");
                System.out.println("7. Ajouter un capteur");
                System.out.println("-------------------------------------------------");
                System.out.println("8. Activer un capteur");
                System.out.println("-------------------------------------------------");
                System.out.println("9. Retirer un capteur");
                System.out.println("-------------------------------------------------");
                System.out.println("10. Désactiver un capteur");
                System.out.println("-------------------------------------------------");
                //System.out.println("10. Calculer la moyenne et la tendance pour un capteur"); // A faire
                System.out.println("12. Ajouter un arroseur");
                System.out.println("-------------------------------------------------");
                System.out.println("13. Quitter");
                System.out.println("-------------------------------------------------");
                System.out.print("Votre choix: ");

                int choix = scanner.nextInt();
                scanner.nextLine();

                switch (choix) {
                    case 1:
                        listerCapteurs();
                        break;
                    case 2:
                        getMesures();
                        break;
                    case 3:
                        afficherCapteur(conn);
                        break;
                    case 4:
                        System.out.print("Entrez le code unique du capteur: ");
                        String codeCapteur = scanner.nextLine();
                        afficherMesures(conn, codeCapteur);
                        break;
                    case 5:
                        System.out.print("Entrez le code unique du capteur : ");
                        String nomCapteur = scanner.nextLine();
                        List<String> listeCapteur = centrale.afficherInformationsCapteur(nomCapteur);
                        System.out.println(listeCapteur.get(0));
                        break;
                    case 6:
                        System.out.print("Entrez le code unique du capteur : ");
                        nomCapteur = scanner.nextLine();
                        System.out.print("Entrez l'intervalle de mesure : ");
                        int intervalle = Integer.parseInt(scanner.nextLine());
                        centrale.modifierIntervalle(nomCapteur, intervalle);
                        break;
                    case 7:
                        System.out.print("Entrez le code unique du capteur : ");
                        nomCapteur = scanner.nextLine();
                        System.out.print("Entrez l'intervalle de mesure : ");
                        intervalle = Integer.parseInt(scanner.nextLine());
                        centrale.ajouterCapteur(nomCapteur, centrale, intervalle);
                        break;
                    case 8:
                        System.out.print("Entrez le code unique du capteur : ");
                        nomCapteur = scanner.nextLine();
                        centrale.demarrerMesure(nomCapteur);
                        break;
                    case 9:
                        System.out.print("Entrez le code unique du capteur : ");
                        nomCapteur = scanner.nextLine();
                        centrale.retirerCapteur(nomCapteur);
                        break;
                    case 10:
                        System.out.print("Entrez le code unique du capteur : ");
                        nomCapteur = scanner.nextLine();
                        centrale.arreterMesure(nomCapteur);
                        break;
                    case 11:
                        // A faire
                        break;
                    case 12:
                        System.out.print("Entrez l'ID du nouvel arroseur: ");
                        int id = scanner.nextInt();
                        System.out.print("Entrez la latitude de l'arroseur: ");
                        double latitude = scanner.nextDouble();
                        System.out.print("Entrez la longitude de l'arroseur: ");
                        double longitude = scanner.nextDouble();
                        String coordonneesGPS = String.format("%.1f, %.1f", latitude, longitude);
                        centrale.ajouterArroseur(id, coordonneesGPS);
                        System.out.println("Nouveau capteur avec ID " + id + " a été ajouté et activé.");
                        break;
                    case 13:
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