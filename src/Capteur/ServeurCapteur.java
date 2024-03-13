package Capteur;
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Scanner; // Importation de la classe Scanner

import CentralDeGestion.CentraleGestion;

public class ServeurCapteur {
    public static void main(String[] args) {
        Random rand = new Random(); // Création d'un objet Random
        String chaineAleatoire = genererChaineAleatoire(3);
        System.out.println("Chaine aléatoire générée : " + chaineAleatoire);
        Capteur capteur1 = new Capteur(chaineAleatoire, "48.8566, 2.3522");
        try {
            Capteurimpl capteurImpl = new Capteurimpl(capteur1);

            try {
                java.rmi.Naming.bind("rmi://localhost:1099/" + chaineAleatoire, capteurImpl);
                System.out.println("Serveur prêt");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (AlreadyBoundException e) {
                try {
                    java.rmi.Naming.rebind("rmi://localhost:1099/Capteur1", capteurImpl);
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    
public static String genererChaineAleatoire(int longueur) {
    // Définition des caractères qui peuvent être présents dans la chaîne
    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    
    // Utilisation de StringBuilder pour construire la chaîne
    StringBuilder sb = new StringBuilder();
    
    Random random = new Random();
    
    // Boucle pour ajouter des caractères aléatoires à la chaîne jusqu'à atteindre la longueur désirée
    for(int i = 0; i < longueur; i++) {
        // Sélection aléatoire d'un caractère
        int index = random.nextInt(caracteres.length());
        char caractereAleatoire = caracteres.charAt(index);
        
        // Ajout du caractère à la chaîne
        sb.append(caractereAleatoire);
    }
    
    return sb.toString();
}
}