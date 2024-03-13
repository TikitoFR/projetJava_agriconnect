package CentralDeGestion;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertData {
    public static void main(String[] args) {
        // Paramètres de connexion
        String url = "jdbc:mysql://localhost:3306/projet";
        String user = "root";
        String password = "azer";

        try {

            // La requête SQL pour l'insertion

            try (Connection conn = DriverManager.getConnection(url, user, password)) {
                String query = "INSERT INTO Capteur (codeUnique, coordonneesGPS, messureTemperature, messureHumidite) VALUES (?, ?, ?, ?)";
    
                try (PreparedStatement preparedStatement = conn.prepareStatement(query)) {
                    preparedStatement.setString(1, "Code123");
                    preparedStatement.setString(2, "48.8566, 2.3522"); // Exemple de coordonnées GPS pour Paris
                    preparedStatement.setBigDecimal(3, new java.math.BigDecimal("23.00")); // Température
                    preparedStatement.setBigDecimal(4, new java.math.BigDecimal("75.00")); // Humidité
    
                    int result = preparedStatement.executeUpdate();
    
                    if (result > 0) {
                        System.out.println("Une ligne insérée avec succès.");
                    } else {
                        System.out.println("Aucune ligne insérée.");
                    }
                }
            } catch (SQLException e) {
                System.out.println("Erreur d'insertion: " + e.getMessage());
            }
    }
    catch (Exception e) {
        System.out.println("Erreur de connexion: " + e.getMessage());
    }
}
}
