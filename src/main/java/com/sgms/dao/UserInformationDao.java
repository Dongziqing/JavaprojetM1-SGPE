package com.sgms.dao;

import com.sgms.pojo.UserInformation;

import java.sql.*;

public class UserInformationDao extends BaseDao{
    //Interroger tous les utilisateurs (informations de connexion)
    public ResultSet searchAllUser() throws SQLException, ClassNotFoundException {
        String sql = "select * from user_information;";
        Connection con = this.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }

    public boolean addNewUser(String id, String name, String password, String job) throws SQLException, ClassNotFoundException {

        if (job.equals("étudiant")) {
            String sql = "insert into user_information(id,name,password,job) values(?,?,?,?);";
            Connection con = this.getCon();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, id);
            ps.setObject(2, name);
            ps.setObject(3, password);
            ps.setObject(4, "étudiant");
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("Enregistrement réussi");
                ps.close();
                con.close();
                return true;
            } else {
                System.out.println("Echec de l'enregistrement");
                return false;
            }
        } else if (job.equals("enseignant")) {
            String sql = "insert into user_information(id,name,password,job) values(?,?,?,?);";
            Connection con = this.getCon();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setObject(1, id);
            ps.setObject(2, name);
            ps.setObject(3, password);
            ps.setObject(4, "enseignant");
            int count = ps.executeUpdate();
            if (count > 0) {
                System.out.println("Enregistrement réussi");
                ps.close();
                con.close();
                return true;
            } else {
                System.out.println("Echec de l'enregistrement");
                return false;
            }
        }
        return false;
    }

    public void modifyUser() throws SQLException, ClassNotFoundException {
        UserInformation userInformation = UserInformation.getUser();
        String sql = "update user_information set name=?,password=? where id=?;";
        Connection con = this.getCon();
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setObject(1, userInformation.getName());
        ps.setObject(2, userInformation.getNewPassword());
        ps.setObject(3, userInformation.getId());
        int count = ps.executeUpdate();
        if (count > 0) {
            System.out.println("Modifié avec succès");
            ps.close();
            con.close();
        } else {
            System.out.println("Échec de la modification");
        }
    }


}
