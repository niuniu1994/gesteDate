package model.bdd;

import model.bdd.utils.JDBUtils;
import model.entitiees.Consultation;
import model.entitiees.Patient;
import model.entitiees.Profession;

import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Dao {
    private BddConnector bddConnector;

    public Dao(BddConnector bddConnector) {
        this.bddConnector = bddConnector;
    }

    public int loginUser(String email, String password) {
        int result = -1;

        try {
            String sql = "SELECT * FROM LOGIN WHERE email = ? AND password = ?";
            this.bddConnector.connect();
            PreparedStatement preparedStatement = this.bddConnector.getConnection().prepareStatement(sql);

            preparedStatement.setString(1, email);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (email.equals("admin")) {
                    result = 0;
                } else {
                    result = resultSet.getInt("FK_PATIENT");
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public List<String> getListeClassification() {
        List<String> result = new ArrayList<>();

        try {
            String sql = "SELECT LIBELLE_CLASSIFICATION FROM CLASSIFICATION";
            this.bddConnector.connect();
            PreparedStatement preparedStatement = this.bddConnector.getConnection().prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSet.getString("LIBELLE_CLASSIFICATION"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public List<String> getListeModeReglements() {
        List<String> result = new ArrayList<>();

        try {
            String sql = "SELECT LIBELLE_MODE FROM MODE_REGLEMENT";
            this.bddConnector.connect();
            PreparedStatement preparedStatement = this.bddConnector.getConnection().prepareStatement(sql);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                result.add(resultSet.getString("LIBELLE_MODE"));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public List<Patient> getListePatients() {
        List<Patient> result = new ArrayList<>();

        String sqlSelect = "select ID_PATIENT,NOM,PRENOM,MOYENCONNAISSANCE,FK_CLASSIFICATION from (SELECT * FROM (PATIENT INNER JOIN HISTORIQUE_PROFESSION HP on PATIENT.ID_PATIENT = HP.FK_PATIENT INNER JOIN PROFESSION P on HP.FK_PROFESSION = P.ID_PROFESSION INNER JOIN CLASSIFICATION C2 on PATIENT.FK_CLASSIFICATION = C2.ID_CLASSIFICATION)) ";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            rs = pst.executeQuery();

            while (rs.next()) {
                int id = rs.getInt(1);
                String nom = rs.getString(2);
                String prenom = rs.getString(3);
                String moyenConnaissance = rs.getString(4);
                String classification = getClassification(rs.getInt(5));
                Patient patient = new Patient(nom, prenom, moyenConnaissance, classification, this.getProfessionList(id));
                patient.setId(id);
                result.add(patient);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return result;
    }

    public String getClassification(int id_classification) {
        String sqlSelect = "select * from CLASSIFICATION where ID_CLASSIFICATION = ?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        String libelle = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setInt(1, id_classification);
            rs = pst.executeQuery();

            if (rs.next()) {
                libelle = rs.getString(2);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return libelle;
    }

    private List<Profession> getProfessionList(int id_patient) {
        String sqlSelect = "select * from avoir where id_profession = ?";

        PreparedStatement pst = null;
        ResultSet rs = null;
        ArrayList<Profession> proList = null;

        // TODO

        return proList;
    }

    public List<Consultation> getListeToutesConsultationsPeriode(Date dateDebut, Date dateFin) {
        List<Consultation> result = new ArrayList<>();

        String sqlSelect = "select ID_CONSULTATION,DATE_CONSULTATION,HEURE_CONSULTATION,RETARD,ANXIETE,MOTS_CLES,POSTURES,COMPORTEMENTS,PRIX, FK_MODE_REGLEMENT,FK_PATIENT from Consultation inner join HISTORIQUE_CONSULTATION ON id_consultation = FK_CONSULTATION inner join Patient on FK_PATIENT = ID_PATIENT WHERE DATE_CONSULTATION BETWEEN ? AND ? ORDER BY HEURE_CONSULTATION ASC";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);

            try {
                pst.setString(1, String.format("%s", new SimpleDateFormat("dd-MM-yyyy").format(dateDebut)));
                pst.setString(2, String.format("%s", new SimpleDateFormat("dd-MM-yyyy").format(dateFin)));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            rs = pst.executeQuery();

            while (rs.next()) {
                Date date = rs.getDate(2);
                Time heure = rs.getTime(3);
                Consultation consultation = new Consultation(date, heure, this.getPatient(rs.getInt(11)));
                consultation.setId(rs.getInt(1));
                consultation.setRetard(rs.getBoolean(4));
                consultation.setAnxiete(rs.getInt(5));
                consultation.setMotCles(rs.getString(6));
                consultation.setPostures(rs.getString(7));
                consultation.setComportements(rs.getString(8));
                consultation.setPrix(String.valueOf(rs.getFloat(9)));
                Integer fk_reglement = rs.getInt(10);
                if (fk_reglement != null) {
                    consultation.setModeReglement(getModeReglement(fk_reglement));
                }
                result.add(consultation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return result;
    }

    public String getModeReglement(int id_mode_reglement) {
        String sqlSelect = "select LIBELLE_MODE from MODE_REGLEMENT where ID_MODE_REGLEMENT = ?";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setInt(1, id_mode_reglement);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getString(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Consultation> getListeConsultationsPatientPeriode(Patient patient, Date dateDebut, Date dateFin) {
        List<Consultation> result = new ArrayList<>();

        String sqlSelect = "select ID_CONSULTATION,DATE_CONSULTATION,HEURE_CONSULTATION,RETARD,ANXIETE,MOTS_CLES,POSTURES,COMPORTEMENTS,PRIX, FK_MODE_REGLEMENT,FK_PATIENT from Consultation inner join HISTORIQUE_CONSULTATION ON id_consultation = FK_CONSULTATION inner join Patient on FK_PATIENT = ID_PATIENT WHERE ID_PATIENT = ? AND DATE_CONSULTATION BETWEEN ? AND ? ORDER BY HEURE_CONSULTATION ASC ";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);

            try {
                pst.setInt(1, patient.getId());
                pst.setString(2, String.format("%s", new SimpleDateFormat("dd-MM-yyyy").format(dateDebut)));
                pst.setString(3, String.format("%s", new SimpleDateFormat("dd-MM-yyyy").format(dateFin)));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            rs = pst.executeQuery();

            while (rs.next()) {
                Date date = rs.getDate(2);
                Time heure = rs.getTime(3);
                Consultation consultation = new Consultation(date, heure, this.getPatient(rs.getInt(11)));
                consultation.setId(rs.getInt(1));
                consultation.setRetard(rs.getBoolean(4));
                consultation.setAnxiete(rs.getInt(5));
                consultation.setMotCles(rs.getString(6));
                consultation.setPostures(rs.getString(7));
                consultation.setComportements(rs.getString(8));
                consultation.setPrix(String.valueOf(rs.getFloat(9)));
                Integer fk_reglement = rs.getInt(10);
                if (fk_reglement != null) {
                    consultation.setModeReglement(getModeReglement(fk_reglement));
                }
                result.add(consultation);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return result;
    }

    public boolean createPatient(Patient patient) {
        boolean success = false;

        String sqlInsertPatient = "INSERT INTO PATIENT(NOM,PRENOM,MOYENCONNAISSANCE,FK_CLASSIFICATION) VALUES (?,?,?,?)";

        PreparedStatement pst = null;
        ResultSet rs = null;
        int patient_id = 0;
        int classification_id = 0;

        try {
            //get key of classification
            classification_id = getIdClassification(patient.getClassification());

            //insert patient and get his id
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlInsertPatient);
            pst.setString(1, patient.getNom().toLowerCase());
            pst.setString(2, patient.getPrenom().toLowerCase());
            pst.setString(3, patient.getMoyenConnaissance());
            pst.setInt(4, classification_id);

            rs = pst.executeQuery();

            if (rs.next()) {
                patient_id = this.getIdPatient(patient);
            }

            //insert profession
            ArrayList<Profession> listPro = (ArrayList<Profession>) patient.getLstProfession();
            for (int i = 0; i < listPro.size(); i++) {
                if (getIdProfession(listPro.get(i)) == -1) {
                    addProfession(listPro.get(i));
                }
            }

            //insert historique_profession
            for (int i = 0; i < listPro.size(); i++) {
                addHistoriqueProfession(patient_id, listPro.get(i));
            }

            success = true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return success;
    }

    public int getIdPatient(Patient patient) {
        String sqlSelect = "SELECT ID_PATIENT FROM PATIENT WHERE NOM = ? AND PRENOM = ?";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setString(1, patient.getNom());
            pst.setString(2, patient.getPrenom());
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return -1;
    }


    public Patient getPatient(int id_patient) {
        String sqlSelect = "select nom,prenom,moyenconnaissance,FK_PATIENT, FK_CLASSIFICATION, ID_PATIENT from (SELECT * FROM PATIENT INNER JOIN HISTORIQUE_PROFESSION HP on PATIENT.ID_PATIENT = HP.FK_PATIENT INNER JOIN PROFESSION P on HP.FK_PROFESSION = P.ID_PROFESSION INNER JOIN CLASSIFICATION C2 on PATIENT.FK_CLASSIFICATION = C2.ID_CLASSIFICATION) WHERE ID_PATIENT = ?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        Patient patient = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setInt(1, id_patient);
            rs = pst.executeQuery();

            if (rs.next()) {
                String nom = rs.getString(1);
                String prenom = rs.getString(2);
                String moyenConnaissance = rs.getString(4);
                String classification = this.getClassification(rs.getInt(5));
                patient = new Patient(nom, prenom, moyenConnaissance, classification, this.getProfessionList(rs.getInt(6)));
                patient.setId(rs.getInt(6));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return patient;
    }

    public Patient getPatient(String nom, String prenom) {
        String sqlSelect = "select nom,prenom,moyenconnaissance,FK_PATIENT, FK_CLASSIFICATION, ID_PATIENT from (SELECT * FROM PATIENT INNER JOIN HISTORIQUE_PROFESSION HP on PATIENT.ID_PATIENT = HP.FK_PATIENT INNER JOIN PROFESSION P on HP.FK_PROFESSION = P.ID_PROFESSION INNER JOIN CLASSIFICATION C2 on PATIENT.FK_CLASSIFICATION = C2.ID_CLASSIFICATION) WHERE NOM = ? AND PRENOM = ?";
        PreparedStatement pst = null;
        ResultSet rs = null;
        Patient patient = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setString(1, nom.toLowerCase());
            pst.setString(2, prenom.toLowerCase());
            rs = pst.executeQuery();

            if (rs.next()) {
                String moyenConnaissance = rs.getString(4);
                String classification = this.getClassification(rs.getInt(5));
                patient = new Patient(nom, prenom, moyenConnaissance, classification, this.getProfessionList(rs.getInt(6)));
                patient.setId(rs.getInt(6));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return patient;
    }

    public int addProfession(Profession profession) {
        String sqlInsertProfession = "INSERT INTO PROFESSION(LIBELLE_PROFESSION) VALUES (?)";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlInsertProfession);
            pst.setString(1, profession.getLibelle());
            pst.executeUpdate();

            rs = pst.getGeneratedKeys();
            if (rs.next()) {
                return this.getIdProfession(profession);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return -1;
    }

    public boolean addHistoriqueProfession(int id_patient, Profession profession) {
        String sqlInsertHistoProf = "INSERT INTO HISTORIQUE_PROFESSION(FK_PATIENT,FK_PROFESSION,DATE_DEBUT,DATE_FIN) VALUES (?,?,?,?)";

        PreparedStatement pst = null;
        ResultSet rs = null;

        int profession_id = getIdProfession(profession);

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlInsertHistoProf);

            pst.setInt(1, id_patient);
            pst.setInt(2, profession_id);
            pst.setDate(3, profession.getDateDebut());
            pst.setDate(4, profession.getDateFin());

            pst.executeUpdate();

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection());
        }


        return false;
    }

    public int getIdClassification(String libelle) {
        String sqlSelect = "SELECT ID_CLASSIFICATION FROM CLASSIFICATION WHERE CLASSIFICATION.LIBELLE_CLASSIFICATION = ?";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setString(1, libelle);
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return -1;
    }

    public int getIdProfession(Profession profession) {
        String sqlSelect = "SELECT ID_PROFESSION FROM PROFESSION WHERE PROFESSION.LIBELLE_PROFESSION = ?";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setString(1, profession.getLibelle());
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }

        return -1;
    }

    public int getIdConsultation(Consultation consultation) {
        String sqlSelect = "SELECT ID_CONSULTATION FROM CONSULTATION WHERE DATE_CONSULTATION = ? AND HEURE_CONSULTATION = ? ";
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setDate(1, consultation.getDate());
            pst.setTime(2, consultation.getHeure());
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }

        return -1;
    }

    public int getIdModeReglement(Consultation consultation) {
        String sqlSelect = "SELECT ID_MODE_REGLEMENT FROM MODE_REGLEMENT WHERE MODE_REGLEMENT.LIBELLE_MODE= ?";

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setString(1, consultation.getModeReglement());
            rs = pst.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return -1;
    }

    public boolean createConsultation(Consultation consultation) {
        boolean success = false;

        String sqlInsertConsult = "insert into Consultation(date_consultation,heure_consultation) values(?,?)";

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlInsertConsult, Statement.RETURN_GENERATED_KEYS);

            pst.setDate(1, consultation.getDate());
            pst.setTime(2, consultation.getHeure());

            pst.executeUpdate();

            rs = pst.getGeneratedKeys();

            if (rs.next()) {
                String sqlInsertConsul_Pat = "insert into HISTORIQUE_CONSULTATION(FK_CONSULTATION,FK_PATIENT) values(?,?)";

                pst = null;

                try {
                    pst = this.bddConnector.getConnection().prepareStatement(sqlInsertConsul_Pat);

                    consultation.setId(this.getIdConsultation(consultation));

                    pst.setInt(1, consultation.getId());
                    pst.setInt(2, consultation.getPatient().getId());

                    rs = pst.executeQuery();

                    if (rs.next()) {
                        success = true;
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return success;
    }

    public boolean deleteConsultation(Consultation consultation) {
        boolean success = false;

        try {
            String sql = "DELETE FROM HISTORIQUE_CONSULTATION WHERE FK_CONSULTATION = ?";
            this.bddConnector.connect();
            PreparedStatement preparedStatement = this.bddConnector.getConnection().prepareStatement(sql);

            preparedStatement.setInt(1, consultation.getId());

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                sql = "DELETE FROM CONSULTATION WHERE ID_CONSULTATION = ?";
                this.bddConnector.connect();
                preparedStatement = this.bddConnector.getConnection().prepareStatement(sql);

                preparedStatement.setInt(1, consultation.getId());

                resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    success = true;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return success;
    }

    public boolean updateConsultation(Consultation consultation) {
        boolean success = false;

        String sqlInsertConsul_Pat = "UPDATE CONSULTATION SET RETARD = ?,ANXIETE = ?,MOTS_CLES = ?,POSTURES = ?,comportements = ?,prix = ?,fk_mode_reglement = ? WHERE ID_CONSULTATION = ?";

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlInsertConsul_Pat);

            pst.setBoolean(1, consultation.isRetard());
            pst.setInt(2, consultation.getAnxiete());
            pst.setString(3, consultation.getMotCles());
            pst.setString(4, consultation.getPostures());
            pst.setString(5, consultation.getComportements());
            pst.setFloat(6, Float.valueOf(consultation.getPrix()));
            if (consultation.getModeReglement() != null) {
                pst.setInt(7, getIdModeReglement(consultation));
            } else {
                pst.setNull(7, Types.INTEGER);
            }
            pst.setInt(8, consultation.getId());

            rs = pst.executeQuery();

            if (rs.next()) {
                success = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBUtils.release(pst, this.bddConnector.getConnection(), rs);
        }

        return success;
    }

    public boolean underThreeRDVDay(Patient patient, Date date) {

        String sqlSelect = "select count(*) from HISTORIQUE_CONSULTATION inner join CONSULTATION C2 on HISTORIQUE_CONSULTATION.FK_CONSULTATION = C2.ID_CONSULTATION where FK_PATIENT = ? AND DATE_CONSULTATION = ?";

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setInt(1, patient.getId());
            pst.setDate(2, date);

            rs = pst.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) < 3) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean underTenHoursDay(Date date) {
        String sqlSelect = "select COUNT(*) from CONSULTATION where DATE_CONSULTATION = ?";

        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            this.bddConnector.connect();
            pst = this.bddConnector.getConnection().prepareStatement(sqlSelect);
            pst.setDate(1, date);
            rs = pst.executeQuery();

            if (rs.next()) {
                if (rs.getInt(1) <= 20)
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
