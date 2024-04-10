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

    
    /**
     * Affiche tous les capteurs enregistrés dans la base de données.
     * @param conn La connexion à la base de données.
     * @throws SQLException En cas d'erreur SQL.
     */

    public static void afficherCapteur(Connection conn) throws SQLException {
        Statement statement = conn.createStatement();
        ResultSet capteur = statement.executeQuery("SELECT * FROM Capteur;");

        while (capteur.next()) {
            String codeUnique = capteur.getString("codeUnique");
            String coordonneesGPS = capteur.getString("coordonneesGPS");

            System.out.println("Code unique : " + codeUnique + " | coordonneesGPS : " + coordonneesGPS);
        }
    }
    /**
     * Affiche les mesures associées à un capteur spécifique.
     * @param conn La connexion à la base de données.
     * @param codeUnique Le code unique du capteur.
     * @throws SQLException En cas d'erreur SQL.
     */
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
    /**
     * Affiche la liste des capteurs connectés à la centrale de gestion.
     * @throws RemoteException En cas d'erreur lors de l'accès distant.
     */
    public static void listerCapteurs() throws RemoteException {
        System.out.println("Liste des capteurs:");
        List<String>  listeCapteur = centrale.getCapteurs();
        for(int i=0; i<listeCapteur.size(); i++){
            System.out.println(listeCapteur.get(i));
        }
    }
    /**
     * Affiche la liste des arroseurs connectés à la centrale de gestion.
     * @throws RemoteException En cas d'erreur lors de l'accès distant.
     */
    public static void listerArroseurs() throws RemoteException {
        System.out.println("Liste des arroseurs:");
        List<String>  listeArroseur = centrale.getArroseurs();
        for(int i=0; i<listeArroseur.size(); i++){
            System.out.println(listeArroseur.get(i));
        }
    }
    /**
     * Récupère les mesures depuis la centrale de gestion et les affiche.
     * @throws RemoteException En cas d'erreur lors de l'accès distant.
     */
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
    /**
     * Calcule et affiche la moyenne et la tendance des mesures d'un capteur pour une période donnée.
     * @param codeUnique Le code unique du capteur.
     * @param periode La période pour laquelle calculer la moyenne et la tendance.
     * @param conn La connexion à la base de données.
     * @throws SQLException En cas d'erreur SQL.
     */
    public static void getMoyenne(String codeUnique, String periode, Connection conn) throws SQLException {
        StringBuilder result = new StringBuilder();

        // Appel de la procédure stockée
        CallableStatement moyenne = conn.prepareCall("{call GetSensorStats(?, ?)}");
        moyenne.setString(1, "Capteur" + codeUnique);
        moyenne.setString(2, periode);

        // Exécution de la requête
        ResultSet rs = moyenne.executeQuery();

        // Traitement des résultats
        while (rs.next()) {
            double avgTemperature = rs.getDouble("avg_temperature");
            double avgHumidity = rs.getDouble("avg_humidity");
            String temperatureTrend = rs.getString("temperature_trend");
            String humidityTrend = rs.getString("humidity_trend");

            result.append("Moyenne de température: ").append(avgTemperature).append("\n");
            result.append("Moyenne d'humidité: ").append(avgHumidity).append("\n");
            result.append("Tendance de température: ").append(temperatureTrend).append("\n");
            result.append("Tendance d'humidité: ").append(humidityTrend).append("\n");
        }

        // Affichage du résultat
        System.out.println(result.toString());

        // Fermeture des ressources
        rs.close();
        moyenne.close();
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
                System.out.println("11. Ajouter un arroseur");
                System.out.println("-------------------------------------------------");
                System.out.println("12. Activer un arroseur");
                System.out.println("-------------------------------------------------");
                System.out.println("13. Desactiver un arroseur");
                System.out.println("-------------------------------------------------");
                System.out.println("14. retire un arroseur");
                System.out.println("-------------------------------------------------");
                System.out.println("15. Lister les arroseurs connecté sur la centrale");
                System.out.println("-------------------------------------------------");
                System.out.println("16. Obtenir moyenne et tendance d'un capteur");
                System.out.println("-------------------------------------------------");
                System.out.println("17. Quitter");
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
                        String nomCapteur = scanner.nextLine();
                        afficherMesures(conn, nomCapteur);
                        break;
                    case 5:
                        System.out.print("Entrez le code unique du capteur : ");
                        nomCapteur = scanner.nextLine();
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
                        System.out.print("Entrez le code unique du capteur (ex : 1) : ");
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
                    case 12:
                        System.out.print("Entrez le code unique de l'arroseur : ");
                        String idArroseur = scanner.nextLine();
                        System.out.print("Entrez le code unique du capteur a arroser (orientation de l'arrosage) : ");
                        nomCapteur = scanner.nextLine();
                        centrale.demarrerArroseur(idArroseur, nomCapteur);
                        break;
                    case 13:
                        System.out.print("Entrez le code unique du capteur : ");
                        idArroseur = scanner.nextLine();
                        centrale.areterArroseur(idArroseur);
                        break;
                    case 14:
                        System.out.print("Entrez le code unique de l'aroseur : ");
                        idArroseur = scanner.nextLine();
                        centrale.retirerArroseur(idArroseur);
                        System.out.println("Arroseur" +idArroseur+"retiré avec succès.");
                        break;
                    case 15:
                        listerArroseurs();
                        break;
                    case 16:
                        System.out.print("Entrez le code unique du capteur: ");
                        nomCapteur = scanner.nextLine();
                        System.out.print("Sur la derniere heure -> 1 \n");
                        System.out.print("Sur la derniere journée -> 2 \n");
                        choix = scanner.nextInt();
                        scanner.nextLine();
                        switch (choix){
                            case 1 :
                                getMoyenne(nomCapteur, "last_hour", conn);
                                break;
                            case 2 :
                                getMoyenne(nomCapteur, "last_day", conn);
                                break;
                        }
                        break;
                    case 17:
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