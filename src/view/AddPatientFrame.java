package view;

import controller.PatientController;
import model.entitiees.Patient;
import model.entitiees.Profession;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AddPatientFrame extends JFrame {

    private PatientController controller;

    private JTextField txtNom;
    private JTextField txtPrenom;
    private JTextField txtMoyenConnaissance;
    private JComboBox<String> cmbClassification;

    private Profession profession;

    /**
     * Constructeur de la classe.
     *
     * @param controller Le controlleur de la classe.
     */
    public AddPatientFrame(PatientController controller) {
        this.controller = controller;

        this.setTitle("Ajout d'un nouveau patient");
        this.setSize(400, 400);
        this.setResizable(false);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        this.loadComponents();
        this.setVisible(true);
    }

    /**
     * Chargement des composants de la Frame.
     */
    private void loadComponents() {
        this.add(new JLabel("Nom :"));
        this.txtNom = new JTextField();
        this.add(this.txtNom);
        this.add(new JLabel("Prénom :"));
        this.txtPrenom = new JTextField();
        this.add(this.txtPrenom);
        this.add(new JLabel("Moyen de connaissance :"));
        this.txtMoyenConnaissance = new JTextField();
        this.add(this.txtMoyenConnaissance);
        this.add(new JLabel("Classification :"));
        this.cmbClassification = new JComboBox<>();
        for (String classification : this.controller.getListeClassifications()) {
            this.cmbClassification.addItem(classification);
        }
        this.add(this.cmbClassification);
        JButton btnProfession = new JButton();
        btnProfession.setText("Ajouter profession");
        this.profession = new Profession();
        btnProfession.addActionListener(e -> new AddProfessionFrame(profession));
        this.add(btnProfession);

        JButton btnSubmit = new JButton("CREER");
        btnSubmit.addActionListener(e -> createPatient());
        this.add(btnSubmit);

    }

    /**
     * Méthode de création d'un nouveau patient.
     */
    private void createPatient() {
        try {
            String nom = this.txtNom.getText();
            String prenom = this.txtPrenom.getText();
            String moyenConnaissance = this.txtMoyenConnaissance.getText();
            String classification = (String) this.cmbClassification.getSelectedItem();
            List<Profession> lstProfession = new ArrayList<>();
            lstProfession.add(this.profession);

            Patient patient = new Patient(nom, prenom, moyenConnaissance, classification, lstProfession);

            if (this.controller.addNewPatient(patient)) {
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue, le patient n'a pas pu être ajouté");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, String.format("Une erreur est survenue, le patient n'a pas pu être ajouté : \n%s", e.getMessage()));
            e.printStackTrace();
        }

    }
}
