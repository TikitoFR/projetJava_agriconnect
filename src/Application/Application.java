package Application;

import Capteur.Capteur;

import java.sql.*;

public class Application {
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/java_agriconnect?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "passroot";

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


    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);

            afficherCapteur(conn);
            afficherMesures(conn, "Capteur2");

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}