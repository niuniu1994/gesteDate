package model.entitiees;

import java.util.List;
import java.util.Objects;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private String moyenConnaissance;
    private String classification;
    private List<Profession> lstProfession;

    public Patient(String nom, String prenom, String moyenConnaissance, String classification, List<Profession> lstProfession) {
        this.nom = nom;
        this.prenom = prenom;
        this.moyenConnaissance = moyenConnaissance;
        this.classification = classification;
        this.lstProfession = lstProfession;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getMoyenConnaissance() {
        return moyenConnaissance;
    }

    public String getClassification() {
        return classification;
    }

    public List<Profession> getLstProfession() {
        return lstProfession;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return id == patient.id &&
                Objects.equals(nom, patient.nom) &&
                Objects.equals(prenom, patient.prenom) &&
                Objects.equals(moyenConnaissance, patient.moyenConnaissance) &&
                Objects.equals(classification, patient.classification) &&
                Objects.equals(lstProfession, patient.lstProfession);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, moyenConnaissance, classification, lstProfession);
    }

    @Override
    public String toString() {
        return String.format("%s %s", this.nom.toUpperCase(), this.prenom);
    }
}
