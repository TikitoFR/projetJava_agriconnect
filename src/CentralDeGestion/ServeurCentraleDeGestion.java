package CentralDeGestion;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
/**
 * La classe ServeurCentraleDeGestion est responsable de démarrer le serveur RMI pour la centrale de gestion.
 * Elle lie l'implémentation de la centrale de gestion à un registre RMI.
 */
public class ServeurCentraleDeGestion {
    public static void main(String[] args) {
        /**
     * Méthode principale permettant de démarrer le serveur RMI pour la centrale de gestion.
     * @param args Arguments de la ligne de commande (non utilisés dans cette application).
     */
        CentraleGestionimpl centraleGestionimpl;
        try {
            centraleGestionimpl = new CentraleGestionimpl();
            try {
                java.rmi.registry.LocateRegistry.createRegistry(1099);
                java.rmi.Naming.bind("rmi://localhost:1099/CentraleGestion", centraleGestionimpl);
                System.out.println("Serveur prêt");

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
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}