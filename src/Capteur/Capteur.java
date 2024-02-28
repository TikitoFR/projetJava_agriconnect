package Capteur;
import java.rmi.RemoteException;

import CentralDeGestion.CentraleGestion;

public class Capteur {
    public String codeUnique;
    public String coordonneesGPS;
    public CentraleGestion centrale;

    public Capteur(String codeUnique, String coordonneesGPS, CentraleGestion centrale) {
        this.codeUnique = codeUnique;
        this.coordonneesGPS = coordonneesGPS;
        this.centrale = centrale;
    }

    public void demarrer() throws RemoteException {
        centrale.ajouterCapteur(this.codeUnique);
        System.out.println("Capteur démarré : " + codeUnique);
    }

    public void retirer() throws RemoteException {
        centrale.retirerCapteur(this.codeUnique);
        System.out.println("Capteur retiré : " + codeUnique);
    }

    public void mesurer(double temperature, double humidite) throws RemoteException {
        centrale.enregistrerMesures(new DataCapteur(temperature, humidite, this.codeUnique));
        //chose a imaginé a renvoyé au main pour tester plusieur fois la fonction
    }
}