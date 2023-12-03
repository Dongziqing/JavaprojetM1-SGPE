package com.sgms.dao;

import com.sgms.pojo.Formation;

import java.sql.*;

public class FormationDao extends BaseDao{
    public ResultSet searchAllFormation() throws SQLException, ClassNotFoundException {
        String sql = "select * from formation; ";
        Connection con = this.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }

    public void insertFormation(String formid, String formname, String formtype) throws SQLException, ClassNotFoundException {
        String sql = "insert into formation(formid,formname,formtype) values(?,?,?);";
        Connection con = this.getCon();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setObject(1, formid);
        pst.setObject(2, formname);
        pst.setObject(3, formtype);
        int count = pst.executeUpdate();
        if (count > 0) {
            System.out.println("Ajouter avec succès");
        } else System.out.println("Echec à ajouter");
        pst.close();
        con.close();
    }

    public void delFormation(Formation formation) throws SQLException, ClassNotFoundException {
        if (formation == null || formation.getFormId() == null) {
            System.out.println("Impossible de supprimer une formation nulle.");
            return;
        }
        Connection con = this.getCon();
        String sql = "delete from formation where formid=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, formation.getFormId());
        int count = ps.executeUpdate();
        if (count > 0) {
            System.out.println("Supprimé avec succès");
        } else {
            System.out.println("Échec de la suppression");
        }
    }


}
