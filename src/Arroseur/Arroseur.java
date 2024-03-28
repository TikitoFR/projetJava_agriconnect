package Arroseur;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class Arroseur {
    public static void main(String[] args) throws RemoteException {
        System.out.print("Entrez l'ID du nouvelle arroseur : ");
        int id = Math.random() > 0 ? (int) (Math.random() * 1000) : (int) (Math.random() * 1000) * -1;
        id = id + 1;
        ArroseurInterface nouveauArroseur = new ArroseurImpl(id);
        try {
            Naming.rebind("rmi://localhost:1099/arroseur" + id , nouveauArroseur);
            System.out.println("Arroseur " + id + " démarré.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
