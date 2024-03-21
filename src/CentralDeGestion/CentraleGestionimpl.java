package CentralDeGestion;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Capteur.DataCapteur;
import Capteur.CapteurInterface;

public class CentraleGestionimpl extends UnicastRemoteObject implements CentraleGestion{

    private HashMap<String, CapteurInterface> capteurs;

    private DataCapteur derniereData;

    public CentraleGestionimpl() throws RemoteException {
        super();
        this.capteurs = new HashMap<>();
    }

    public void ajouterCapteur(String nomCapteur, CentraleGestion centrale, int intervalle) {
        try {
            CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteurs.put(nomCapteur, capteur);
            capteur.parametrerCapteur(centrale, intervalle);
            capteur.demarrerMesure();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void retirerCapteur(String nomCapteur) {
        try {
            CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteurs.remove(nomCapteur);
            capteur.retirerCapteur();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
    public void demarrerMesure(String nomCapteur) {
        CapteurInterface capteur = null;
        try {
            capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteur.demarrerMesure();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void arreterMesure(String nomCapteur) {
        CapteurInterface capteur = null;
        try {
            capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteur.arreterMesure();
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public void modifierIntervalle(String nomCapteur, int intervalle) {
        try {
            CapteurInterface capteur = (CapteurInterface) Naming.lookup("rmi://localhost:1099/" + nomCapteur);
            capteur.modifierIntervalle(intervalle);
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String> afficherInformationsCapteur(String nomCapteur) throws RemoteException {
        List<String> listeCapteurs = new ArrayList<String>();
        this.capteurs.get(nomCapteur);
        if (this.capteurs.containsKey(nomCapteur)) {
            if (this.capteurs.get(nomCapteur).getStatus()) {
                listeCapteurs.add(nomCapteur + " - GPS : " + this.capteurs.get(nomCapteur).getCoordonneesGPS() + " - Intervalle : " + this.capteurs.get(nomCapteur).getIntervalle() +  " - Actif");
            } else {
                listeCapteurs.add(nomCapteur + " - GPS : " + this.capteurs.get(nomCapteur).getCoordonneesGPS() + " - Intervalle : " + this.capteurs.get(nomCapteur).getIntervalle() +  " - Inactif");
            }
        }
        return listeCapteurs;
    }

    public void afficherMesures(DataCapteur data) {
        //System.out.println(data);
        this.derniereData = data;
        System.out.println(derniereData);
    }

    public DataCapteur getMesures() {
        try {
            wait(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return derniereData;
    }

    public void enregistrerMesures(DataCapteur data) {
//        System.out.println(data);
//        capteurs.get(data.codeUnique).add(data);
//
//        try (FileWriter writer = new FileWriter("result.txt", StandardCharsets.UTF_8, true)) {
//            writer.write(data + "\n");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public List<String> getCapteurs() throws RemoteException {
        List<String> listeCapteurs = new ArrayList<String>();
        this.capteurs.forEach((nomCapteur, capteur)->{
            try {
                if (capteur.getStatus()) {
                    listeCapteurs.add(capteur.getNom() + " - GPS : " + capteur.getCoordonneesGPS() + " - Intervalle : " + capteur.getIntervalle() + " - Actif");
                } else {
                    listeCapteurs.add(capteur.getNom() + " - GPS : " + capteur.getCoordonneesGPS() + " - Intervalle : " + capteur.getIntervalle() + " - Inactif");
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
        return listeCapteurs;
    }
}
