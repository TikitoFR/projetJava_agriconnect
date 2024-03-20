package CentralDeGestion;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class ServeurCentraleDeGestion {
    public static void main(String[] args) {
        
        CentraleGestionimpl centraleGestionimpl;
        try {
            centraleGestionimpl = new CentraleGestionimpl();
            try {
                java.rmi.registry.LocateRegistry.createRegistry(1099);
                java.rmi.Naming.bind("rmi://localhost:1099/CentraleGestion", centraleGestionimpl);
                //CentraleGestion centrale = (CentraleGestion) Naming.lookup("rmi://localhost:1099/CentraleGestion");
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