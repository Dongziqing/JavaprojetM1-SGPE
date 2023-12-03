package com.sgms.dao;

import com.sgms.pojo.Project;

import java.sql.*;

public class ProjectDao extends BaseDao{

    public ResultSet searchAllProject() throws SQLException,ClassNotFoundException{
        String sql = "select * from project;";
        Connection con = this.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }

    public void insertProject(String subjectname, String topic, Date duedate) throws SQLException,ClassNotFoundException{
        String sql = "insert into project(subjectname,topic,duedate) values(?,?,?);";
        Connection con = this.getCon();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setObject(1,subjectname);
        pst.setObject(2, topic);
        pst.setObject(3, duedate);
        int count = pst.executeUpdate();
        if (count > 0) {
            System.out.println("Ajouter avec succès");
        } else System.out.println("Echec à ajouter");
        pst.close();
        con.close();
    }

    //Mise à jour des données pour tous les utilisateurs après modification
    public void delProject(Project project) throws SQLException,ClassNotFoundException{
        Connection con = this.getCon();
        String sql = "delete from project where projectid=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, project.getProjectId());
        int count = ps.executeUpdate();
        if (count > 0) {
            System.out.println("Supprimé avec succès");
        } else {
            System.out.println("Échec de la suppression");
        }
    }

    public ResultSet getProjectInfo() throws SQLException, ClassNotFoundException {
        String sql = "select * from project";
        Connection con = this.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }

}
