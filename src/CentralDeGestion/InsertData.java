package CentralDeGestion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InsertData {
    public static void main(String[] args) {
        // Paramètres de connexion
        String url = "jdbc:mysql://localhost:3306/CapteursDB";
        String user = "root";
        String password = "azerazer";

        try {
            // Établir la connexion
            Class.forName(".idea/ojdbc11.jar");

            Connection conn = DriverManager.getConnection(url, user, password);

            // La requête SQL pour l'insertion
            String sql = "INSERT INTO nom_de_la_table (nom_du_capteur, temperature, humidite, intervalle) VALUES (?, ?, ?, ?)";

            // Préparer la requête
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, "NomDuCapteurUnique");
            statement.setFloat(2, 25.5f); // Température exemple
            statement.setFloat(3, 60.0f); // Humidité exemple
            statement.setInt(4, 10); // Intervalle exemple

            // Exécuter la requête
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Une nouvelle entrée a été insérée avec succès !");
            }

            // Fermer la connexion
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
