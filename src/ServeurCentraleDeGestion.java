import java.util.ArrayList;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
public class ServeurCentraleDeGestion {
    public static void main(String[] args) {
        
        CentraleGestionimpl dfg;
        try {
            dfg = new CentraleGestionimpl();

            try {
                java.rmi.registry.LocateRegistry.createRegistry(1099);
                java.rmi.Naming.bind("rmi://localhost:1099/CentraleGestion", dfg);
                System.out.println("Serveur prêt");
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (AlreadyBoundException e) {
                try {
                    java.rmi.Naming.rebind("rmi://localhost:1099/CentraleGestion", dfg);
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