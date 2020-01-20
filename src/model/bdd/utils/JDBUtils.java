package model.bdd.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;


public class JDBUtils {

    private static final String DRIVER;
    private static final String URL;
    private static final String NAME;
    private static final String PASSWORD;

    static {
        //load document de properties
        Properties pp = new Properties();
        InputStream is = JDBUtils.class.getClassLoader().getResourceAsStream("jdbc.properties");
        try {
            pp.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        DRIVER = pp.getProperty("DriverClass");
        URL = pp.getProperty("URL");
        NAME = pp.getProperty("NAME");
        PASSWORD = pp.getProperty("PASSWORD");

    }

    //loadDriver de Oracle
    public static void loadDriver() throws ClassNotFoundException {
        Class.forName(DRIVER);
    }

    public static Connection getConnection() throws Exception {
        loadDriver();
        Connection conn = DriverManager.getConnection(URL, NAME, PASSWORD);
        return conn;
    }

    public static void release(PreparedStatement ppst, Connection conn, ResultSet rs) {

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sql) {
            }
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException sql) {
            }
        }

        if (ppst != null) {
            try {
                ppst.close();
            } catch (SQLException sql) {
            }
        }


    }

    public static void release(PreparedStatement ppst, Connection conn) {

        if (ppst != null) {
            try {
                ppst.close();
            } catch (SQLException sql) {
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException sql) {
            }
        }
    }

}
