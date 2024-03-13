package CentralDeGestion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectionbdd {

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/projet?serverTimezone=UTC"; // Remplacer nomDeLaBaseDeDonnees par le nom de votre base de données
        String user = "root"; // Remplacer par votre nom d'utilisateur MySQL
        String password = "azer"; // Remplacer par votre mot de passe MySQL

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connecté à la base de données MySQL!");
        } catch (SQLException e) {
            System.out.println("Erreur de connexion: " + e.getMessage());
        }
    }
}

}
