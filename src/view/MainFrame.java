package view;

import controller.PatientController;
import controller.ConsultationController;
import controller.MainFrameController;
import model.entitiees.Consultation;
import model.entitiees.Patient;
import model.entitiees.references.TypeCompte;
import oracle.sql.DATE;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MainFrame extends JFrame {

    private JTextField dateDebut;
    private JTextField dateFin;
    private JComboBox<String> cmbPatient;
    private JTable tableau;

    private ArrayList<Consultation> listeConsultations;

    private MainFrameController controller;
    private TypeCompte typeCompte;

    private Panel pageStartPanel;
    private Panel centerPanel;
    private Panel pageEndPanel;

    private String actualPatientName;

    /**
     * Constructeur de la classe.
     *
     * @param controller Le controlleur de la classe.
     */
    public MainFrame(MainFrameController controller) {

        this.controller = controller;

        this.setTitle("PSY - Gestionnaire de consultation");
        this.setSize(1200, 500);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.actualPatientName = "TOUS";
    }

    /**
     * Chargement des composants de la Frame.
     */
    public void loadComponents() {
        this.loadPageStartPanel();
        this.loadCenterPanel();
        this.loadPageEndPanel();
    }

    /**
     * Chargement de la partie supérieure de la Frame.
     */
    private void loadPageStartPanel() {
        this.dateDebut = new JTextField("2019-01-31");
        this.dateDebut.setColumns(20);
        this.dateFin = new JTextField("2019-03-31");
        this.dateFin.setColumns(20);
        this.pageStartPanel = new Panel();
        this.pageStartPanel.setLayout(new FlowLayout());
        this.pageStartPanel.add(new JLabel("Date début :"));
        this.pageStartPanel.add(this.dateDebut);
        this.pageStartPanel.add(new JLabel("Date fin :"));
        this.pageStartPanel.add(this.dateFin);
        JButton btnRefresh = new JButton();
        btnRefresh.setText("Rafraichir");
        btnRefresh.addActionListener(event -> {
            refresh();
        });
        this.pageStartPanel.add(btnRefresh);
        this.add(this.pageStartPanel, BorderLayout.PAGE_START);
    }

    /**
     * Chargement de la partie centrale de la Frame.
     */
    private void loadCenterPanel() {
        this.centerPanel = new Panel();
        this.centerPanel.setLayout(new BorderLayout());

        this.createJTable();
        this.setUpTableData();
        this.centerPanel.add(tableau.getTableHeader(), BorderLayout.NORTH);
        this.centerPanel.add(tableau, BorderLayout.CENTER);

        this.add(this.centerPanel, BorderLayout.CENTER);
    }

    /**
     * Création du tableau des consultations.
     */
    private void createJTable() {
        String[] colName = {"date", "heure", "patient", "retard", "anxiete", "mots clés", "postures", "comportements", "prix", "mode de règlement"};
        if (this.tableau == null) {
            this.tableau = new JTable() {
                public boolean isCellEditable(int nRow, int nCol) {
                    return false;
                }
            };
        }
        DefaultTableModel tableModel = new DefaultTableModel(colName, 0);
        tableModel.setColumnIdentifiers(colName);
        this.tableau.setModel(tableModel);
        this.tableau.setRowHeight(30);
        this.tableau.setShowGrid(true);
        this.tableau.setComponentPopupMenu(this.createPopupMenu());
        this.tableau.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (SwingUtilities.isRightMouseButton(me) == true) {
                    try {
                        int row = tableau.rowAtPoint(me.getPoint());
                        tableau.clearSelection();
                        tableau.addRowSelectionInterval(row, row);
                    } catch (Exception e) {

                    }
                }
            }
        });
        this.tableau.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * Création du menu de click droit.
     */
    private JPopupMenu createPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemAnnuler = new JMenuItem("Annuler");
        menuItemAnnuler.addActionListener(event -> {
            int row = this.tableau.getSelectedRow();

            DefaultTableModel tableModel = (DefaultTableModel) this.tableau.getModel();
            String date = String.valueOf(tableModel.getValueAt(row, 0));
            String heure = String.valueOf(tableModel.getValueAt(row, 1));

            Consultation consultation = null;

            for (Consultation c : this.listeConsultations) {
                if (c.getDate().toString().equals(date) && c.getHeure().toString().equals(heure)) {
                    consultation = c;
                }
            }

            this.controller.annulerConsultation(consultation);
        });
        popupMenu.add(menuItemAnnuler);
        if (this.typeCompte == TypeCompte.ADMIN) {
            JMenuItem menuItemModifier = new JMenuItem("Modifier");
            menuItemModifier.addActionListener(event -> {
                int row = this.tableau.getSelectedRow();

                DefaultTableModel tableModel = (DefaultTableModel) this.tableau.getModel();
                String date = String.valueOf(tableModel.getValueAt(row, 0));
                String heure = String.valueOf(tableModel.getValueAt(row, 1));

                Consultation consultation = null;

                for (Consultation c : this.listeConsultations) {
                    if (c.getDate().toString().equals(date) && c.getHeure().toString().equals(heure)) {
                        consultation = c;
                    }
                }

                new UpdateConsultationFrame(new ConsultationController(controller.getDao()), consultation);
            });
            popupMenu.add(menuItemModifier);
        }
        return popupMenu;
    }

    /**
     * Chargement des données de la table.
     */
    public void setUpTableData() {
        Date dateDebutPeriode = Date.valueOf(this.dateDebut.getText());
        Date dateFinPeriode = Date.valueOf(this.dateFin.getText());
        DefaultTableModel tableModel = (DefaultTableModel) this.tableau.getModel();
        if (tableModel.getRowCount() > 0) {
            for (int i = tableModel.getRowCount() - 1; i > -1; i--) {
                tableModel.removeRow(i);
            }
        }
        if (this.typeCompte == TypeCompte.ADMIN) {
            this.listeConsultations = (ArrayList<Consultation>) this.controller.getListeConsultation(dateDebutPeriode, dateFinPeriode, this.actualPatientName);
        } else {
            this.listeConsultations = (ArrayList<Consultation>) this.controller.getListeConsultation(dateDebutPeriode, dateFinPeriode, this.controller.getPatientFomatedNamesFromID());
        }

        for (Consultation consultation : this.listeConsultations) {
            tableModel.addRow(consultation.convertToObjectTable());
        }
        this.tableau.setModel(tableModel);
        tableModel.fireTableDataChanged();
    }

    /**
     * Ré-actualisation des données.
     */
    private void refresh() {
        if (this.typeCompte == TypeCompte.ADMIN) {
            this.actualPatientName = (String) this.cmbPatient.getSelectedItem();
        }
        this.setUpTableData();
        if (this.typeCompte == TypeCompte.ADMIN) {
            this.refreshCmbPatient();
        }
    }

    /**
     * Ré-actualisation du combo de choix du patient.
     */
    private void refreshCmbPatient() {
        DefaultComboBoxModel model = (DefaultComboBoxModel) this.cmbPatient.getModel();

        for (Patient patient : this.controller.getListePatient()) {

            String formatedName = String.format("%s %s", patient.getNom().toUpperCase(), patient.getPrenom());

            if (model.getIndexOf(formatedName) == -1) {
                model.addElement(formatedName);
            }
        }
        this.cmbPatient.setSelectedItem(this.actualPatientName);
    }

    /**
     * Chargement de la partie inférieure de la Frame.
     */
    private void loadPageEndPanel() {
        this.pageEndPanel = new Panel();
        this.pageEndPanel.setLayout(new FlowLayout());

        if (this.typeCompte == TypeCompte.ADMIN) {
            JButton btnAddPatient = new JButton();
            btnAddPatient.setText("Ajouter un nouveau patient");
            btnAddPatient.addActionListener(event -> {
                PatientController patientController = new PatientController(controller.getDao());
                new AddPatientFrame(patientController);
            });
            this.pageEndPanel.add(btnAddPatient);
        }

        if (this.typeCompte == TypeCompte.ADMIN) {
            JButton btnAddRdv = new JButton();
            btnAddRdv.setText("Ajouter un rendez-vous");
            btnAddRdv.addActionListener(event -> {
                ConsultationController consultationController = new ConsultationController(controller.getDao());
                new AddConsultationFrame(consultationController);
            });
            this.pageEndPanel.add(btnAddRdv);
        }

        if (this.typeCompte == TypeCompte.ADMIN) {
            this.cmbPatient = new JComboBox<>();
            this.cmbPatient.addItem("TOUS");
            this.refreshCmbPatient();
            this.pageEndPanel.add(this.cmbPatient);
        }

        this.add(this.pageEndPanel, BorderLayout.PAGE_END);
    }

    /**
     * Affichage de la Frame.
     */
    public void showFrame() {
        this.setVisible(true);
    }

    /**
     * Définition du type de compte utilisateur connecté.
     *
     * @param typeCompte Le type de compte.
     */
    public void setTypeCompte(TypeCompte typeCompte) {
        this.typeCompte = typeCompte;
    }
}
