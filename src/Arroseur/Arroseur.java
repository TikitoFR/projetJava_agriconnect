package Arroseur;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class Arroseur {
    /**
     * Méthode principale pour démarrer un nouvel arroseur.
     * @param args Les arguments de la ligne de commande.
     * @throws RemoteException En cas d'erreur lors de l'accès distant.
     */
    public static void main(String[] args) throws RemoteException {
        // Génération d'un numero d'arroseur aléatoire
        int id = Math.random() > 0 ? (int) (Math.random() * 1000) : (int) (Math.random() * 1000) * -1;
        id = id + 1;
        ArroseurInterface nouveauArroseur = new ArroseurImpl(id);
        try {
            // Bind de l'arroseur crée
            Naming.rebind("rmi://localhost:1099/arroseur" + id , nouveauArroseur);
            System.out.println("Arroseur " + id + " démarré.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
