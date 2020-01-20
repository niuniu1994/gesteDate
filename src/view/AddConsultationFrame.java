package view;

import controller.ConsultationController;
import model.entitiees.Consultation;
import model.entitiees.Patient;

import javax.swing.*;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class AddConsultationFrame extends JFrame {

    private ConsultationController controller;

    private JTextField txtDate;
    private JTextField txtHeure;
    private JComboBox<Patient> cmbPatients;

    /**
     * Constructeur de la classe.
     *
     * @param controller Le controlleur de la classe.
     */
    public AddConsultationFrame(ConsultationController controller) {
        this.controller = controller;

        this.setTitle("Ajout d'un rendez-vous");
        this.setSize(400, 200);
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
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        this.txtDate = new JTextField(dtf.format(now));
        this.add(this.txtDate);
        this.add(new JLabel("Heure du rendez-vous :"));
        this.txtHeure = new JTextField("12:00");
        this.add(this.txtHeure);
        this.add(new JLabel("Patient concerné :"));
        this.cmbPatients = new JComboBox<>();
        for (Patient patient : this.controller.getPatients()) {
            this.cmbPatients.addItem(patient);
        }
        this.add(this.cmbPatients);
        JButton btnSubmit = new JButton();
        btnSubmit.setText("Ajouter");
        btnSubmit.addActionListener(event -> {
            ajouterConsultation();
        });
        this.add(btnSubmit);
    }

    /**
     * Méthode d'ajout de consultation.
     */
    private void ajouterConsultation() {
        try {

            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf1.parse(this.txtDate.getText());
            Date dateRdv = new java.sql.Date(date.getTime());

            sdf1 = new SimpleDateFormat("hh:mm");
            date = sdf1.parse(this.txtHeure.getText());
            Time time = new java.sql.Time(date.getTime());
            Patient p = (Patient) this.cmbPatients.getSelectedItem();
            if (this.controller.creerConsultation(new Consultation(dateRdv, time, p))) {
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue, la consultation n'a pas pu être ajoutée");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, String.format("Une erreur est survenue, la consultation n'a pas pu être ajoutée : \n%s", e.getMessage()));
            e.printStackTrace();
        }
    }
}
