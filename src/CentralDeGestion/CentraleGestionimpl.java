package CentralDeGestion;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Capteur.DataCapteur;

import java.nio.charset.StandardCharsets;

public class CentraleGestionimpl extends UnicastRemoteObject implements CentraleGestion{

    private HashMap<String,List<DataCapteur>> capteurs;

    public CentraleGestionimpl() throws RemoteException {
        super();
        this.capteurs = new HashMap<>();
    }

    public void ajouterCapteur(String capteur) {
        capteurs.put(capteur, new ArrayList<>());
    }

    public void retirerCapteur(String capteur) {
        capteurs.remove(capteur);
    }

    public void afficherInformationsCapteur(String capteur) {
        System.out.println("Informations du capteur " + capteur);
        // Afficher d'autres informations si nécessaire
    }




    public void enregistrerMesures(DataCapteur data ) {
        System.out.println(data.getTemperature() + " " + data.getHumidite() + " " + data.getCodeUnique());
        
        String url = "jdbc:mysql://localhost:3306/java_agriconnect?serverTimezone=UTC"; // Assurez-vous d'inclure le paramètre de fuseau horaire si nécessaire
        // Utilisateur de la base de données
        String user = "root";
        // Mot de passe de l'utilisateur de la base de données
        String password = "passroot";

        // Requête SQL pour insérer des données, y compris la date et l'heure actuelles
        String query = "INSERT INTO Mesure (codeUnique, dateHeure, temperature, humidite) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Récupérer la date et l'heure actuelles
            LocalDateTime now = LocalDateTime.now();
            // Formater la date et l'heure au format attendu par votre base de données
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            // Remplacer les placeholders par les valeurs réelles, y compris la date et l'heure formatées
            preparedStatement.setString(1, data.getCodeUnique()); // Exemple de code unique
            preparedStatement.setString(2, formattedDateTime); // Date et heure actuelles formatées
            preparedStatement.setBigDecimal(3, new java.math.BigDecimal(data.getTemperature())); // Température
            preparedStatement.setBigDecimal(4, new java.math.BigDecimal(data.getHumidite())); // Humidité

            // Exécuter l'insertion
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " ligne(s) insérée(s).");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    
}}
