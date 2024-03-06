package CentralDeGestion;
import java.util.ArrayList;
import java.util.List;

import Capteur.Capteur;
import Capteur.CapteurInterface;
import Capteur.Capteurimpl;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
public class ServeurCentraleDeGestion {
    public static void main(String[] args) {
        
        CentraleGestionimpl central;
        Capteurimpl capteur;
        try {
            capteur = new Capteurimpl();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            central = new CentraleGestionimpl();
            

            try {
                java.rmi.registry.LocateRegistry.createRegistry(1099);
                java.rmi.Naming.bind("rmi://localhost:1099/CentraleGestion", central);
                System.out.println("Serveur prêt");

                try {
                    capteur = (Capteurimpl) Naming.lookup("rmi://localhost:10500/Capteur");
                    capteur.parametrerCapteur(central, 1000);
                } catch (NotBoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
               
               
               




            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (AlreadyBoundException e) {
                try {
                    java.rmi.Naming.rebind("rmi://localhost:1099/CentraleGestion", central);
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