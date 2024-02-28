package CentralDeGestion;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Capteur.DataCapteur;

import java.nio.charset.StandardCharsets;

public class CentraleGestionimpl extends UnicastRemoteObject implements CentraleGestion{

    private HashMap<String,List<DataCapteur>> capteurs;

    public CentraleGestionimpl() throws RemoteException {
        super();
        this.capteurs = new HashMap<>();
    }

    public void ajouterCapteur(String capteur) {
        capteurs.put(capteur, new ArrayList<>());
    }

    public void retirerCapteur(String capteur) {
        capteurs.remove(capteur);
    }

    public void afficherInformationsCapteur(String capteur) {
        System.out.println("Informations du capteur " + capteur);
        // Afficher d'autres informations si nécessaire
    }

    public void enregistrerMesures(DataCapteur data) {
        System.out.println(data);
        capteurs.get(data.codeUnique).add(data);

        try (FileWriter writer = new FileWriter("result.txt", StandardCharsets.UTF_8, true)) {
            writer.write(data + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
