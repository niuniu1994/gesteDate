package controller;

import model.bdd.Dao;
import model.entitiees.Consultation;
import model.entitiees.Patient;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlleur principal.
 */
public class MainFrameController {

    // La dao
    private Dao dao;

    // L'id de l'utilisateur connecté
    private int idPersonne;

    /**
     * Constructeur de la classe.
     *
     * @param dao        La dao.
     * @param idPersonne L'id de l'utilisateur connecté.
     */
    public MainFrameController(Dao dao, int idPersonne) {
        this.dao = dao;
        this.idPersonne = idPersonne;
    }

    /**
     * Permet d'obtenir la dao.
     *
     * @return La dao.
     */
    public Dao getDao() {
        return this.dao;
    }

    /**
     * Permet d'obtenir la liste des consultations en fonction d'une période et des noms d'un patient.
     *
     * @param dateDebutPeriode La date de début de la période.
     * @param dateFinPeriode   La date de fin de la période.
     * @param patientName      Le nom + prenom du patient.
     * @return La liste des consultations.
     */
    public List<Consultation> getListeConsultation(Date dateDebutPeriode, Date dateFinPeriode, String patientName) {
        List<Consultation> result;

        if (patientName.equals("TOUS")) {
            result = this.dao.getListeToutesConsultationsPeriode(dateDebutPeriode, dateFinPeriode);
        } else {
            result = this.dao.getListeConsultationsPatientPeriode(this.getPatientFromNames(patientName), dateDebutPeriode, dateFinPeriode);
        }

        return result;
    }

    /**
     * Permet d'annuler une consultation.
     *
     * @param consultation La consultation a annuler.
     * @return Success ou non.
     */
    public boolean annulerConsultation(Consultation consultation) {
        return this.dao.deleteConsultation(consultation);
    }

    /**
     * Permet d'obtenir la liste des patients.
     *
     * @return La liste des patients.
     */
    public List<Patient> getListePatient() {
        return this.dao.getListePatients();
    }

    /**
     * Permet d'obtenir un patient  en fonction d'un nom + prenom.
     *
     * @param names Le nom + prenom.
     * @return Le patient.
     */
    public Patient getPatientFromNames(String names) {

        String nom = names.split(" ")[0].toLowerCase();
        String prenom = names.split(" ")[1].toLowerCase();
        return this.dao.getPatient(nom, prenom);
    }

    /**
     * Permet d'obtenir un patient  en fonction d'un id.
     *
     * @return Le patient.
     */
    public String getPatientFomatedNamesFromID() {
        Patient patient = this.dao.getPatient(this.idPersonne);
        return String.format("%s %s", patient.getNom().toUpperCase(), patient.getPrenom().toLowerCase());
    }
}
