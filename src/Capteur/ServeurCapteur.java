package Capteur;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import CentralDeGestion.CentraleGestion;

public class ServeurCapteur {
    public static void main(String[] args) {
        Capteur capteur1 = new Capteur("Capteur1", "48.8566, 2.3522");
        try {
            Capteurimpl capteurImpl = new Capteurimpl(capteur1);
            try {
                java.rmi.Naming.bind("rmi://localhost:1099/Capteur1", capteurImpl);
                System.out.println("Serveur prêt");
            } catch (MalformedURLException | RemoteException e) {
                throw new RuntimeException(e);
            } catch (AlreadyBoundException e) {
                try {
                    java.rmi.Naming.rebind("rmi://localhost:1099/Capteur1", capteurImpl);
                } catch (MalformedURLException | RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}