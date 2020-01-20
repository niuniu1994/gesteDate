package model.entitiees;

import java.time.LocalDate;
import java.sql.Date;
import java.util.Objects;

public class Profession {
    private String libelle;
    private Date dateDebut;
    private Date dateFin;

    public Profession() {
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setDateDebut(Date dateDebut) {
        this.dateDebut = dateDebut;
    }

    public void setDateFin(Date dateFin) {
        this.dateFin = dateFin;
    }

    public Date getDateDebut() {
        return dateDebut;
    }

    public Date getDateFin() {
        return dateFin;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profession that = (Profession) o;
        return Objects.equals(libelle, that.libelle) &&
                Objects.equals(dateDebut, that.dateDebut) &&
                Objects.equals(dateFin, that.dateFin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(libelle, dateDebut, dateFin);
    }

    @Override
    public String toString() {
        return "Profession{" +
                "libelle='" + libelle + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                '}';
    }
}
