package controller;

import model.bdd.Dao;
import model.entitiees.Consultation;
import model.entitiees.Patient;

import java.util.List;

/**
 * Controlleur de la classe AddConsultationFrame et UpdateConsultationFrame
 */
public class ConsultationController {

    // La dao
    private Dao dao;

    /**
     * Constructeur de la classe.
     *
     * @param dao La dao.
     */
    public ConsultationController(Dao dao) {
        this.dao = dao;
    }

    /**
     * Permet d'obtenir la liste des patients.
     *
     * @return La liste des patients.
     */
    public List<Patient> getPatients() {
        return this.dao.getListePatients();
    }

    /**
     * Permet de créer une consultation.
     *
     * @param consultation La consultation à créer.
     * @return Un booléen correspondant au success de l'execution.
     */
    public boolean creerConsultation(Consultation consultation) {
        boolean moins10Heures = this.dao.underTenHoursDay(consultation.getDate());
        boolean moins3Consultations = this.dao.underThreeRDVDay(consultation.getPatient(), consultation.getDate());

        if (moins10Heures && moins3Consultations) {
            return this.dao.createConsultation(consultation);
        } else {
            return false;
        }
    }

    /**
     * Permet de modifier une consultation.
     *
     * @param consultation La consultation à créer.
     * @return Un booléen correspondant au success de l'execution.
     */
    public boolean modifierConsultation(Consultation consultation) {
        return this.dao.updateConsultation(consultation);
    }

    /**
     * Permet d'obtenir la liste des modes de règlement.
     *
     * @return La liste des modes de règlement.
     */
    public List<String> getListeModeReglement() {
        return this.dao.getListeModeReglements();
    }
}
