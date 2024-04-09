package Capteur;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class ServeurCapteur {
    public static void main(String[] args) {
        int id = Math.random() > 0 ? (int) (Math.random() * 1000) : (int) (Math.random() * 1000) * -1;
        id = id + 1;
        Capteur capteur1 = new Capteur("Capteur" + id, "48.8566, 2.3522");
        try {
            Capteurimpl capteurImpl = new Capteurimpl(capteur1);
            try {
                java.rmi.Naming.bind("rmi://localhost:1099/Capteur" + id, capteurImpl);
                System.out.println("Capteur " + id + " démarré.");
            } catch (MalformedURLException | RemoteException e) {
                throw new RuntimeException(e);
            } catch (AlreadyBoundException e) {
                try {
                    java.rmi.Naming.rebind("rmi://localhost:1099/Capteur" + id, capteurImpl);
                } catch (MalformedURLException | RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}