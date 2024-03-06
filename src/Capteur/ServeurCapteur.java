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
            Capteurimpl capteurImpl = new Capteurimpl();
            java.rmi.registry.LocateRegistry.createRegistry(10500);
            try {
                java.rmi.Naming.bind("rmi://localhost:10500/Capteur", capteurImpl);

                
            } catch (MalformedURLException | AlreadyBoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println("Capteur enregistré");
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
