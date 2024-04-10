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
import java.util.concurrent.ScheduledExecutorService;

import Capteur.DataCapteur;
import Capteur.CapteurInterface;
import Arroseur.ArroseurInterface;

public class CentraleGestionimpl extends UnicastRemoteObject implements CentraleGestion{

    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/java_agriconnect?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = "passroot";

    private HashMap<String, CapteurInterface> capteurs;
    private HashMap<Integer, ArroseurInterface> arroseurs;
    private DataCapteur derniereData;

    /**
     * Constructeur par défaut de la classe.
     * Initialise les structures de données pour stocker les capteurs et les arroseurs.
     * @throws RemoteException En cas d'erreur lors de la création de l'objet distant.
     */
    public CentraleGestionimpl() throws RemoteException {
        super();
        this.capteurs = new HashMap<>();
        this.arroseurs = new HashMap<>();
    }
  /**
     * Ajoute un capteur à la centrale de gestion.
     * @param nomCapteur Le nom du capteur.
     * @param centrale La centrale de gestion à laquelle le capteur est ajouté.
     * @param intervalle L'intervalle de mesure du capteur.
     */
    public void ajouterCapteur(String nomCapteur, CentraleGestion centrale, int intervalle) {
        try {
            // lookup permettant de contacter le capteur distant
            CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/Capteur" + nomCapteur);
            capteurs.put(nomCapteur, capteur);
            capteur.parametrerCapteur(centrale, intervalle);
            capteur.demarrerMesure();

            // Requête SQL pour insérer des données
            String query = "INSERT INTO Capteur (codeUnique, coordonneesGPS) VALUES (?, ?)";

            try (Connection connection = DriverManager.getConnection(DATABASE_URL, USER, PASSWORD);
                 PreparedStatement preparedStatement = connection.prepareStatement(query)) {

                preparedStatement.setString(1, "Capteur" + nomCapteur); // Code unique
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
   /**
     * Ajoute un arroseur à la centrale de gestion.
     * @param id L'identifiant de l'arroseur.
     * @param coordonneesGPS Les coordonnées GPS de l'arroseur.
     */
    public void ajouterArroseur(int id, String coordonneesGPS) {
        ArroseurInterface arroseur = null;
        try {
            // Permet de contacter l'arroseur passé en parametre
            arroseur = (ArroseurInterface) Naming.lookup("rmi://localhost:1099/arroseur" + id);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        arroseurs.put(id, arroseur);
        try {
            arroseur.setCoordonneesGPS(coordonneesGPS);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        String message = "L'arroseur " + id + " a été ajouté à la centrale" + "\n";
        System.out.println(message);
    }

    /**
     * Retire un capteur de la centrale de gestion.
     * @param nomCapteur Le nom du capteur à retirer.
     */
    public void retirerCapteur(String nomCapteur) {
        try {
            CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/Capteur" + nomCapteur);
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
    /**
     * Retire un arroseur de la centrale de gestion.
     * @param idArroseurStr L'identifiant de l'arroseur à retirer.
     * @throws RemoteException En cas d'erreur de communication avec l'arroseur distant.
     */

    public void retirerArroseur(String idArroseurStr) throws RemoteException   {
        ArroseurInterface arroseur = null;
        try {
            // Conversion de la chaîne en entier
            int idArroseur = Integer.parseInt(idArroseurStr);
            arroseur = (ArroseurInterface) Naming.lookup("rmi://localhost:1099/arroseur" + idArroseur);
            // Utiliser l'entier comme clé pour retirer l'arroseur
            arroseurs.remove(idArroseur);
            
            System.out.println(arroseurs);
            System.out.println("Arroseur " + idArroseur + " retiré de la centrale");
            arroseur.retirerArroseur();
        } catch (NumberFormatException e) {
            System.err.println("Le format de l'ID de l'arroseur est incorrect.");
            throw new RuntimeException(e);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * Démarre la mesure d'un capteur spécifié.
     * @param nomCapteur Le nom du capteur dont la mesure doit démarrer.
     */
    public void demarrerMesure(String nomCapteur) {
        CapteurInterface capteur = null;
        try {
            capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/Capteur" + nomCapteur);
            capteur.demarrerMesure();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
 /**
     * Démarre un arroseur spécifié.
     * @param idArroseur L'identifiant de l'arroseur à démarrer.
     * @param nomCapteur Le nom du capteur associé à l'arroseur.
     * @throws RemoteException En cas d'erreur de communication avec l'arroseur distant.
     */
    public void demarrerArroseur(String idArroseur, String nomCapteur) throws RemoteException {
        ArroseurInterface arroseur = null;
        try {
            arroseur = (ArroseurInterface) Naming.lookup("rmi://localhost:1099/arroseur" + idArroseur);
            arroseur.activer();
            capteurs.get(nomCapteur);
            arroseur.arroser(capteurs.get(nomCapteur));
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
 /**
     * Arrête la mesure d'un capteur spécifié.
     * @param nomCapteur Le nom du capteur dont la mesure doit être arrêtée.
     */
    public void arreterMesure(String nomCapteur) {
        CapteurInterface capteur = null;
        try {
            capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/Capteur" + nomCapteur);
            capteur.arreterMesure();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
     /**
     * Arrête un arroseur spécifié.
     * @param idArroseur L'identifiant de l'arroseur à arrêter.
     * @throws RemoteException En cas d'erreur de communication avec l'arroseur distant.
     */
    public void areterArroseur(String idArroseur) throws RemoteException  {
        ArroseurInterface arroseur = null;
        try {
            arroseur = (ArroseurInterface) Naming.lookup("rmi://localhost:1099/arroseur" + idArroseur);
            arroseur.desactiver();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    

    /**
     * Modifie l'intervalle de mesure d'un capteur spécifié.
     * @param nomCapteur Le nom du capteur dont l'intervalle doit être modifié.
     * @param intervalle Le nouvel intervalle de mesure.
     */
    public void modifierIntervalle(String nomCapteur, int intervalle) {
        try {
            CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/Capteur" + nomCapteur);
            capteur.modifierIntervalle(intervalle);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
 /**
     * Affiche les informations d'un capteur spécifié.
     * @param nomCapteur Le nom du capteur dont les informations doivent être affichées.
     * @return Une liste contenant les informations du capteur.
     * @throws RemoteException En cas d'erreur de communication avec le capteur distant.
     */
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
 /**
     * Affiche les dernières mesures reçues par la centrale de gestion.
     * @param data Les données de capteur contenant les mesures à afficher.
     */
    public void afficherMesures(DataCapteur data) {
        this.derniereData = data;
    }

    public DataCapteur getMesures() {
        return derniereData;
    }
 /**
     * Enregistre les mesures dans la base de données.
     * @param data Les données de capteur contenant les mesures à enregistrer.
     */

    public void enregistrerMesures(DataCapteur data ) {
        System.out.println("Temp : " + data.getTemperature() + " | Humi : " + data.getHumidite() + " | Nom : " + data.getCodeUnique());

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

    /**
     * Récupère la liste des capteurs enregistrés dans la centrale de gestion.
     * @return La liste des capteurs avec leurs informations.
     * @throws RemoteException En cas d'erreur de communication avec les capteurs distants.
     */
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
 /**
     * Récupère la liste des arroseurs enregistrés dans la centrale de gestion.
     * @return La liste des arroseurs avec leurs informations.
     * @throws RemoteException En cas d'erreur de communication avec les arroseurs distants.
     */
    public List<String> getArroseurs() throws RemoteException {
        List<String> listeArroseurs = new ArrayList<>();
        this.arroseurs.forEach((idArroseur, arroseur) ->{
            try {
                if (arroseur.getStatus()) {
                    listeArroseurs.add("Arroseur" + arroseur.getId() + " - GPS : " + arroseur.getCoordonneesGPS() + " - Actif");
                } else {
                    listeArroseurs.add("Arroseur" + arroseur.getId() + " - GPS : " + arroseur.getCoordonneesGPS() + " - Inactif");
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        return listeArroseurs;
    }
}