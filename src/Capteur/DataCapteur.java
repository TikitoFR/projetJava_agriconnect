package Capteur;
public class DataCapteur implements java.io.Serializable{
    public double temperature;
    public double humidite;
    public String codeUnique;

    public DataCapteur(double temperature, double humidite, String codeUnique) {
        this.temperature = temperature;
        this.humidite = humidite;
        this.codeUnique = codeUnique;
    }
    public String toString() {
        return "Mesures du capteurs " + codeUnique + " - Température : " + temperature + ", Humidité : " + humidite;
    }
}