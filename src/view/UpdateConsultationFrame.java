package view;

import controller.ConsultationController;
import model.entitiees.Consultation;
import model.entitiees.Patient;

import javax.swing.*;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UpdateConsultationFrame extends JFrame {

    private ConsultationController controller;
    private Consultation consultation;

    private JTextField txtDate;
    private JTextField txtHeure;
    private JComboBox<Patient> cmbPatients;
    private JComboBox<String> cmbRetard;
    private JComboBox<Integer> cmbAnxiete;
    private JTextArea areaMotsCles;
    private JTextArea areaPostures;
    private JTextArea areaComportements;
    private JTextField txtPrix;
    private JComboBox<String> cmbModeReglement;

    /**
     * Constructeur de la classe.
     *
     * @param controller Le controlleur de la classe.
     */
    public UpdateConsultationFrame(ConsultationController controller, Consultation consultation) {
        this.controller = controller;
        this.consultation = consultation;

        this.setTitle("Mise à jour de la consultation");
        this.setSize(400, 500);
        this.setResizable(false);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        this.loadComponents();
        this.setVisible(true);
    }

    /**
     * Chargement des composants de la Frame.
     */
    private void loadComponents() {
        this.add(new JLabel("Date du rendez-vous :"));
        this.txtDate = new JTextField(this.consultation.getDate().toString());
        this.add(this.txtDate);

        this.add(new JLabel("Heure du rendez-vous :"));
        this.txtHeure = new JTextField(this.consultation.getHeure().toString());
        this.add(this.txtHeure);

        this.add(new JLabel("Patient concerné :"));
        this.cmbPatients = new JComboBox<>();
        for (Patient patient : this.controller.getPatients()) {
            this.cmbPatients.addItem(patient);
        }
        this.cmbPatients.setSelectedItem(this.consultation.getPatient());
        this.add(this.cmbPatients);

        this.add(new JLabel("Patient arrivé en retard :"));
        this.cmbRetard = new JComboBox<>();
        this.cmbRetard.addItem("OUI");
        this.cmbRetard.addItem("NON");
        if (this.consultation.isRetard()) {
            this.cmbRetard.setSelectedItem("OUI");
        } else {
            this.cmbRetard.setSelectedItem("NON");
        }
        this.add(this.cmbRetard);

        this.add(new JLabel("Anxieté du patient (si nécessaire) :"));
        this.cmbAnxiete = new JComboBox<>();
        for (int i = 0; i < 11; i++) {
            this.cmbAnxiete.addItem(i);
        }
        this.cmbAnxiete.setSelectedItem(0);
        this.add(this.cmbAnxiete);

        this.add(new JLabel("Mots-Clés :"));
        this.areaMotsCles = new JTextArea(this.consultation.getMotCles());
        this.add(this.areaMotsCles);

        this.add(new JLabel("Postures :"));
        this.areaPostures = new JTextArea(this.consultation.getPostures());
        this.add(this.areaPostures);

        this.add(new JLabel("Comportements :"));
        this.areaComportements = new JTextArea(this.consultation.getComportements());
        this.add(this.areaComportements);

        this.add(new JLabel("Prix :"));
        this.txtPrix = new JTextField(this.consultation.getPrix());
        this.add(this.txtPrix);

        this.add(new JLabel("Mode de règlement :"));
        this.cmbModeReglement = new JComboBox<>();
        for (String mode : this.controller.getListeModeReglement()) {
            this.cmbModeReglement.addItem(mode);
        }
        this.cmbModeReglement.setSelectedItem(this.consultation.getModeReglement());
        this.add(this.cmbModeReglement);

        JButton btnSubmit = new JButton();
        btnSubmit.setText("Modifier");
        btnSubmit.addActionListener(event -> {
            modifierConsultation();
        });
        this.add(btnSubmit);
    }

    /**
     * Méthode de mise à jour de la consultation.
     */
    private void modifierConsultation() {
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf1.parse(this.txtDate.getText());
            this.consultation.setDate(new java.sql.Date(date.getTime()));

            sdf1 = new SimpleDateFormat("hh:mm:ss");
            date = sdf1.parse(this.txtHeure.getText());
            Time time = new java.sql.Time(date.getTime());
            this.consultation.setHeure(time);

            this.consultation.setPatient((Patient) this.cmbPatients.getSelectedItem());
            this.consultation.setRetard(((String) this.cmbRetard.getSelectedItem()).equals("OUI"));
            this.consultation.setAnxiete((Integer) this.cmbAnxiete.getSelectedItem());
            this.consultation.setMotCles(this.areaMotsCles.getText());
            this.consultation.setPostures(this.areaPostures.getText());
            this.consultation.setComportements(this.areaComportements.getText());
            this.consultation.setPrix(this.txtPrix.getText());
            this.consultation.setModeReglement((String) this.cmbModeReglement.getSelectedItem());

            if (this.controller.modifierConsultation(this.consultation)) {
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue, la consultation n'a pas pu être mise à jour");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, String.format("Une erreur est survenue, la consultation n'a pas pu être mise à jour : \n%s", e.getMessage()));
            e.printStackTrace();
        }
    }
}
