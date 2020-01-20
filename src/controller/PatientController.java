package controller;

import model.bdd.Dao;
import model.entitiees.Patient;

import java.util.List;

/**
 * Controller de la classe AddPatientFrame.
 */
public class PatientController {

    // La dao
    private Dao dao;

    /**
     * Le constructeur de la classe.
     *
     * @param dao La dao.
     */
    public PatientController(Dao dao) {
        this.dao = dao;
    }

    /**
     * Permet d'obtenir la liste des classifications.
     *
     * @return La liste des classifications.
     */
    public List<String> getListeClassifications() {
        return this.dao.getListeClassification();
    }

    /**
     * Permet d'ajouter un nouveau patient.
     *
     * @param patient Le patient à ajouter.
     * @return Success ou non.
     */
    public boolean addNewPatient(Patient patient) {
        boolean success = false;

        // Le nom et le prénom ne peuvent pas être nulls
        if (patient.getNom() != "" && patient.getPrenom() != "") {
            success = this.dao.createPatient(patient);
        }

        return success;
    }
}
