package model.bdd;

import model.bdd.utils.JDBUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BddConnector {

    // La connexion
    private Connection connection;

    public BddConnector() {

    }

    /**
     * Etablissement de la connexion à la bdd.
     */
    public void connect() {
        try {
            JDBUtils.loadDriver();
            connection = JDBUtils.getConnection();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Permet d'obtenir la connexion en cours.
     *
     * @return La connexion.
     */
    public Connection getConnection() {
        return this.connection;
    }
}
