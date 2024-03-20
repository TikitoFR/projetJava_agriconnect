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
    
   
    public double getTemperature() {
        return temperature;
    }

    public double getHumidite() {
        return humidite;
    }

    public String getCodeUnique() {
        return codeUnique;
    }
}