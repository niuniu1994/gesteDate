package view;

import controller.LoginFrameController;
import controller.MainFrameController;
import model.entitiees.references.TypeCompte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class LoginFrame extends JFrame implements ActionListener {

    private JPanel panel;
    private JLabel labelmail, labelPassword, labelMessage;
    private JTextField inputMail;
    private JPasswordField inputPassword;
    private JButton btnLogin;

    private LoginFrameController controller;

    /**
     * Constructeur de la classe.
     *
     * @param controller Le controlleur de la classe.
     */
    public LoginFrame(LoginFrameController controller) {

        this.controller = controller;

        // User Label
        this.labelmail = new JLabel();
        this.labelmail.setText("Email :");
        this.inputMail = new JTextField();

        // Password
        this.labelPassword = new JLabel();
        this.labelPassword.setText("Mot de passe :");
        this.inputPassword = new JPasswordField();

        // Submit

        this.btnLogin = new JButton("SE CONNECTER");

        this.panel = new JPanel(new GridLayout(3, 1));

        this.panel.add(labelmail);
        this.panel.add(inputMail);
        this.panel.add(labelPassword);
        this.panel.add(inputPassword);

        this.labelMessage = new JLabel();
        this.panel.add(labelMessage);
        this.panel.add(btnLogin);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Adding the listeners to components..
        this.btnLogin.addActionListener(this);
        this.add(panel, BorderLayout.CENTER);
        this.setTitle("PSY - Connexion");
        this.setSize(300, 100);
        this.setResizable(false);
    }

    /**
     * Affichage de la Frame.
     */
    public void showFrame() {
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        String email = this.inputMail.getText();
        String password = this.inputPassword.getText();

        int idPersonne = this.controller.connect(email, password);

        if (idPersonne >= 0) {

            MainFrameController mainFrameController = new MainFrameController(this.controller.getDao(), idPersonne);
            MainFrame mainFrame = new MainFrame(mainFrameController);
            if (email.equals("admin")) {
                mainFrame.setTypeCompte(TypeCompte.ADMIN);
            } else {
                mainFrame.setTypeCompte(TypeCompte.CLIENT);
            }
            mainFrame.loadComponents();
            this.setVisible(false);
            mainFrame.showFrame();
        } else {
            this.labelMessage.setText(" Utilisateur invalide ");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginFrame that = (LoginFrame) o;
        return Objects.equals(panel, that.panel) &&
                Objects.equals(labelmail, that.labelmail) &&
                Objects.equals(labelPassword, that.labelPassword) &&
                Objects.equals(labelMessage, that.labelMessage) &&
                Objects.equals(inputMail, that.inputMail) &&
                Objects.equals(inputPassword, that.inputPassword) &&
                Objects.equals(btnLogin, that.btnLogin) &&
                Objects.equals(controller, that.controller);
    }

    @Override
    public int hashCode() {
        return Objects.hash(panel, labelmail, labelPassword, labelMessage, inputMail, inputPassword, btnLogin, controller);
    }

    @Override
    public String toString() {
        return "LoginFrame{" +
                "panel=" + panel +
                ", labelmail=" + labelmail +
                ", labelPassword=" + labelPassword +
                ", labelMessage=" + labelMessage +
                ", inputMail=" + inputMail +
                ", inputPassword=" + inputPassword +
                ", btnLogin=" + btnLogin +
                ", controller=" + controller +
                '}';
    }
}
