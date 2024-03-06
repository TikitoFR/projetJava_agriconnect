package CentralDeGestion;
import Capteur.CapteurInterface;
import Capteur.Capteurimpl;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Scanner; // Importer la classe Scanner
public class ServeurCentraleDeGestion {
    public static void main(String[] args) {
        
        CentraleGestionimpl centraleGestionimpl;
        try {
            centraleGestionimpl = new CentraleGestionimpl();
            try {
                java.rmi.registry.LocateRegistry.createRegistry(1099);
                java.rmi.Naming.bind("rmi://localhost:1099/CentraleGestion", centraleGestionimpl);
                CentraleGestion centrale = (CentraleGestion) Naming.lookup("rmi://localhost:1099/CentraleGestion");
                System.out.println("Serveur prêt");

                Scanner scanner = new Scanner(System.in); // Créer un objet Scanner

                System.out.println("Entrez le nom du capteur :");
                String nomCapteur = scanner.nextLine(); // Lire la saisie utilisateur

                try {
                    CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);

                    capteur.parametrerCapteur(centrale,5000);
                    centraleGestionimpl.ajouterCapteur(nomCapteur);
                    capteur.demarrerMesure();

                } catch (NotBoundException e) {
                    throw new RuntimeException(e);
                }

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (AlreadyBoundException e) {
                try {
                    java.rmi.Naming.rebind("rmi://localhost:1099/CentraleGestion", centraleGestionimpl);
                } catch (RemoteException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}