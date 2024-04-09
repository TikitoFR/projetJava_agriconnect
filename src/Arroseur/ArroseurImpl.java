package Arroseur;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import Capteur.CapteurInterface;

public class ArroseurImpl extends UnicastRemoteObject implements ArroseurInterface {
    // Identifiant unique du capteur.
    private int id;
    // Coordonnées géographiques du capteur.
    private String coordonneesGPS;
    private String zone;
    private double seuilTemp;
    private double seuilHumi;
    private boolean status;
    private ScheduledExecutorService executor; // Référence au niveau de la classe

    

    // Constructeur du capteur implémentant les propriétés nécessaires.
    public ArroseurImpl(int id) throws RemoteException {
        super();
        this.id = id;
        this.seuilTemp = 30.0;
        this.seuilHumi = 35.0;
        this.status = false;
        
    }

    // Getters pour les propriétés du capteur.
    public int getId() throws RemoteException{
        return id;
    }

    public String getCoordonneesGPS() throws RemoteException{
        return coordonneesGPS;
    }

    public double getSeuilTemp() throws RemoteException{
        return seuilTemp;
    }

    public double getSeuilHumi() throws RemoteException{
        return seuilHumi;
    }

    public boolean getStatus() throws RemoteException{
        return status;
    }

    public String getZone() throws RemoteException{
        return zone;
    }

    public void setCoordonneesGPS(String coordonneesGPS) throws RemoteException{
        this.coordonneesGPS = coordonneesGPS;
    }


//    public void setSeuilTemp(double seuilTemp) throws RemoteException{
//        this.seuilTemp = seuilTemp;
//    }
//
//    public void setSeuilHumi(double seuilHumi) throws RemoteException{
//        this.seuilHumi = seuilHumi;
//    }

    public void setZone(String zone) throws RemoteException{
        this.zone = zone;
    }

    @Override
    public void activer() throws RemoteException {
        if (!status) {
            status = true;
        }
    }

    @Override
    public void desactiver() throws RemoteException {
        if (status) {
            stopArrosage();// Arrête l'arrosage lors de la désactivation

            status = false;
        }
    }

    @Override
    public void arroser(CapteurInterface capteur) throws RemoteException {
        System.out.println("Arrosage de la plante...");
        // Crée un ScheduledExecutorService avec 1 thread.
        executor = Executors.newScheduledThreadPool(1);

        // Crée une tâche qui incrémente la variable i toutes les 10 secondes.
        // Utilise un tableau pour permettre la modification dans l'expression lambda
        Runnable task = () -> {
            try {
                capteur.setHumidite(1);
                System.out.println("Augmentation de l'humidité sur le capteur :" + capteur.getNom());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };

        // Planifie la tâche pour s'exécuter toutes les 10 secondes
        executor.scheduleAtFixedRate(task, 0, 10, TimeUnit.SECONDS);

        // Crée une autre tâche pour stopper l'executor après 2 minutes.
        executor.schedule(() -> {
            executor.shutdown();
            try {
                // Attend la terminaison de toutes les tâches planifiées
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    executor.shutdownNow(); // Annule toutes les tâches en cours si elles ne terminent pas
                }
            } catch (InterruptedException ex) {
                executor.shutdownNow();
                Thread.currentThread().interrupt(); // Rétablit le statut d'interruption
            }
            System.out.println("Fin de l'exécution après 2 minutes.");
        }, 2, TimeUnit.MINUTES);
    }


    @Override
    public void stopArrosage() throws RemoteException {
        if (executor != null) {
            executor.shutdownNow(); // Essaye d'arrêter toutes les tâches actives
            try {
                // Attend que les tâches terminent
                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Des tâches n'ont pas terminé après l'arrêt.");
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            } finally {
                executor = null; // Aide au nettoyage
            }
            System.out.println("Arrosage arrêté.");
        }

}
}
