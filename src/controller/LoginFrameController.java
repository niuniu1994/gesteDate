package controller;

import model.bdd.Dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Controlleur de la classe LoginFrame
 */
public class LoginFrameController {

    // La dao
    private Dao dao;

    /**
     * Constructeur de la classe.
     *
     * @param dao La dao.
     */
    public LoginFrameController(Dao dao) {
        this.dao = dao;
    }

    /**
     * Connecter un utilisateur avec le hash du mot de passe.
     *
     * @param email    L'email.
     * @param password Le mot de passe.
     * @return L'id de l'utilisateur.
     */
    public int connect(String email, String password) {
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(password.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            password = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return this.dao.loginUser(email, password);
    }

    /**
     * Permet d'obtenir la dao.
     * @return La dao.
     */
    public Dao getDao() {
        return dao;
    }
}
