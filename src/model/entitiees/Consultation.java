package model.entitiees;

import java.sql.Date;
import java.sql.Time;

public class Consultation {
    private int id;
    private Date date;
    private Time heure;
    private boolean retard;
    private int anxiete;
    private String motCles;
    private String postures;
    private String comportements;
    private String prix;
    private String modeReglement;
    private Patient patient;

    public Consultation(Date date, Time heure, Patient patient) {
        this.date = date;
        this.heure = heure;
        this.patient = patient;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setHeure(Time heure) {
        this.heure = heure;
    }

    public void setRetard(boolean retard) {
        this.retard = retard;
    }

    public void setAnxiete(int anxiete) {
        this.anxiete = anxiete;
    }

    public void setMotCles(String motCles) {
        this.motCles = motCles;
    }

    public void setPostures(String postures) {
        this.postures = postures;
    }

    public void setComportements(String comportements) {
        this.comportements = comportements;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    public void setModeReglement(String modeReglement) {
        this.modeReglement = modeReglement;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public int getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public Time getHeure() {
        return heure;
    }

    public boolean isRetard() {
        return retard;
    }

    public int getAnxiete() {
        return anxiete;
    }

    public String getMotCles() {
        return motCles;
    }

    public String getPostures() {
        return postures;
    }

    public String getComportements() {
        return comportements;
    }

    public String getPrix() {
        return prix;
    }

    public String getModeReglement() {
        return modeReglement;
    }

    public Patient getPatient() {
        return patient;
    }

    public Object[] convertToObjectTable() {
        Object[] data = new Object[10];
        data[0] = this.date;
        data[1] = this.heure;
        data[2] = this.patient;
        data[3] = this.retard;
        data[4] = this.anxiete;
        data[5] = this.motCles;
        data[6] = this.postures;
        data[7] = this.comportements;
        data[8] = this.prix;
        data[9] = this.modeReglement;

        return data;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", date=" + date +
                ", heure=" + heure +
                ", retard=" + retard +
                ", anxiete=" + anxiete +
                ", motCles='" + motCles + '\'' +
                ", postures='" + postures + '\'' +
                ", comportements='" + comportements + '\'' +
                ", prix='" + prix + '\'' +
                ", modeReglement='" + modeReglement + '\'' +
                ", patient=" + patient +
                '}';
    }
}
