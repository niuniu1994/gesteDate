package launcher;

import controller.LoginFrameController;
import model.bdd.BddConnector;
import model.bdd.Dao;
import view.LoginFrame;

public class Main {
    public static void main(String[] args) {
        // Création du connecteur
        BddConnector bddConnector = new BddConnector();
        try {
            // On crée la connexion
            bddConnector.connect();
        } catch (Exception   e) {
            e.printStackTrace();
        }

        // Si la connexion est établie, on lance l'appli
        if (bddConnector.getConnection() != null) {
            Dao dao = new Dao(bddConnector);
            LoginFrameController loginFrameController = new LoginFrameController(dao);
            LoginFrame loginFrame = new LoginFrame(loginFrameController);
            loginFrame.showFrame();
        }
    }
}
