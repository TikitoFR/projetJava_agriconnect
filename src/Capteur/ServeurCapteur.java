package Capteur;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import CentralDeGestion.CentraleGestion;

public class ServeurCapteur {
    public static void main(String[] args) {
        Capteur capteur1 = new Capteur("Capteur1", "48.8566, 2.3522");
        try {
            Capteurimpl capteurImpl = new Capteurimpl();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
