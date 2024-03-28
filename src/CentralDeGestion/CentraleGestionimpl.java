package CentralDeGestion;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Capteur.DataCapteur;
import Capteur.CapteurInterface;

public class CentraleGestionimpl extends UnicastRemoteObject implements CentraleGestion{

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/java_agriconnect?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "passroot";

    private HashMap<String, CapteurInterface> capteurs;

    private DataCapteur derniereData;

    public CentraleGestionimpl() throws RemoteException {
        super();
        this.capteurs = new HashMap<>();
    }

    public void ajouterCapteur(String nomCapteur, CentraleGestion centrale, int intervalle) {
        try {
            CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteurs.put(nomCapteur, capteur);
            capteur.parametrerCapteur(centrale, intervalle);
            capteur.demarrerMesure();

            //System.out.println(capteur.getCoordonneesGPS());

            // Requête SQL pour insérer des données
            String query = "INSERT INTO Capteur (codeUnique, coordonneesGPS) VALUES (?, ?)";

            try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, nomCapteur); // Code unique
                preparedStatement.setString(2, capteur.getCoordonneesGPS()); // Coordonnées GPS

                // Exécuter l'insertion
                int rowsAffected = preparedStatement.executeUpdate();
                System.out.println(rowsAffected + " ligne(s) insérée(s).");

            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void retirerCapteur(String nomCapteur) {
        try {
            CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteurs.remove(nomCapteur);
            capteur.retirerCapteur();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public void demarrerMesure(String nomCapteur) {
        CapteurInterface capteur = null;
        try {
            capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteur.demarrerMesure();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void arreterMesure(String nomCapteur) {
        CapteurInterface capteur = null;
        try {
            capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteur.arreterMesure();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifierIntervalle(String nomCapteur, int intervalle) {
        try {
            CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteur.modifierIntervalle(intervalle);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> afficherInformationsCapteur(String nomCapteur) throws RemoteException {
        List<String> listeCapteurs = new ArrayList<String>();
        this.capteurs.get(nomCapteur);
        if (this.capteurs.containsKey(nomCapteur)) {
            if (this.capteurs.get(nomCapteur).getStatus()) {
                listeCapteurs.add(nomCapteur + " - GPS : " + this.capteurs.get(nomCapteur).getCoordonneesGPS() + " - Intervalle : " + this.capteurs.get(nomCapteur).getIntervalle() +  " - Actif");
            } else {
                listeCapteurs.add(nomCapteur + " - GPS : " + this.capteurs.get(nomCapteur).getCoordonneesGPS() + " - Intervalle : " + this.capteurs.get(nomCapteur).getIntervalle() +  " - Inactif");
            }
        }
        return listeCapteurs;
    }

    public void afficherMesures(DataCapteur data) {
        //System.out.println(data);
        this.derniereData = data;
        //System.out.println(derniereData);
    }

    public DataCapteur getMesures() {
        return derniereData;
    }


    public void enregistrerMesures(DataCapteur data ) {
        //System.out.println(data.getTemperature() + " " + data.getHumidite() + " " + data.getCodeUnique());

        // Requête SQL pour insérer des données, y compris la date et l'heure actuelles
        String query = "INSERT INTO Mesure (codeUnique, dateHeure, temperature, humidite) VALUES (?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Récupérer la date et l'heure actuelles
            LocalDateTime now = LocalDateTime.now();
            // Formater la date et l'heure au format attendu par votre base de données
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            preparedStatement.setString(1, data.getCodeUnique()); // Code unique
            preparedStatement.setString(2, formattedDateTime); // Date et heure actuelles formatées
            preparedStatement.setBigDecimal(3, new java.math.BigDecimal(data.getTemperature())); // Température
            preparedStatement.setBigDecimal(4, new java.math.BigDecimal(data.getHumidite())); // Humidité

            // Exécuter l'insertion
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println(rowsAffected + " ligne(s) insérée(s).");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getCapteurs() throws RemoteException {
        List<String> listeCapteurs = new ArrayList<String>();
        this.capteurs.forEach((nomCapteur, capteur)->{
            try {
                if (capteur.getStatus()) {
                    listeCapteurs.add(capteur.getNom() + " - GPS : " + capteur.getCoordonneesGPS() + " - Intervalle : " + capteur.getIntervalle() + " - Actif");
                } else {
                    listeCapteurs.add(capteur.getNom() + " - GPS : " + capteur.getCoordonneesGPS() + " - Intervalle : " + capteur.getIntervalle() + " - Inactif");
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        return listeCapteurs;
    }
}