package view;

import model.entitiees.Profession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class AddProfessionFrame extends JFrame {

    private JTextField txtProfession;
    private JTextField txtDateDebut;
    private JTextField txtDateFin;

    private Profession profession;

    /**
     * Constructeur de la classe.
     *
     * @param profession La profession à compléter.
     */
    public AddProfessionFrame(Profession profession) {
        this.profession = profession;
        this.setTitle("Ajout d'une profession");
        this.setSize(400, 200);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.loadComponents();
        this.setVisible(true);
    }

    /**
     * Chargement des composants de la Frame.
     */
    private void loadComponents() {
        this.add(new JLabel("Profession :"));
        this.txtProfession = new JTextField();
        this.add(this.txtProfession);

        this.add(new JLabel("Date de début :"));
        this.txtDateDebut = new JTextField();
        this.txtDateDebut.setText("2019-01-01");
        this.add(this.txtDateDebut);

        this.add(new JLabel("Date de fin :"));
        this.txtDateFin = new JTextField();
        this.txtDateFin.setText("");
        this.add(this.txtDateFin);

        JButton btnSubmit = new JButton();
        btnSubmit.setText("Ajouter");
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createProfession();
            }
        });
        this.add(btnSubmit);
    }

    /**
     * Méthode de complétion de la profession.
     */
    private void createProfession() {
        try {
            String libelleProfession = this.txtProfession.getText();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf1.parse(this.txtDateDebut.getText());
            Date dateDebut = new java.sql.Date(date.getTime());
            Date dateFin = null;
            if (!this.txtDateFin.getText().equals("")) {
                date = sdf1.parse(this.txtDateFin.getText());
                dateFin = new java.sql.Date(date.getTime());
            }

            if (libelleProfession != "" && dateDebut != null) {
                this.profession.setLibelle(libelleProfession);
                this.profession.setDateDebut(dateDebut);
                this.profession.setDateFin(dateFin);
                this.setVisible(false);
            } else {
                JOptionPane.showMessageDialog(this, "Une erreur est survenue, la profession n'a pas pu être ajoutée");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, String.format("Une erreur est survenue, la profession n'a pas pu être ajoutée : \n%s", e.getMessage()));
            e.printStackTrace();
        }
    }
}
