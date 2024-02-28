package Capteur;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import CentralDeGestion.CentraleGestion;

public class ServeurCapteur {
    public static void main(String[] args) {

        CentraleGestion centrale;
        try {
            centrale = (CentraleGestion) Naming.lookup("rmi://localhost:1099/CentraleGestion");
            Capteur capteur1 = new Capteur("Capteur1", "48.8566, 2.3522", centrale);
            capteur1.demarrer();
            capteur1.mesurer(20, 50);
            capteur1.retirer();
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NotBoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        


    }
}
