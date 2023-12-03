package com.sgms.dao;

import com.sgms.pojo.StudentGroup;

import java.sql.*;

public class GroupDao extends BaseDao{
    public ResultSet searchAllGroup() throws SQLException, ClassNotFoundException {
        String sql = "select * from stu_group; ";
        Connection con = this.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }

    public ResultSet searchByGroup(String name) throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM stu_group WHERE (student1name = ? OR student2name = ?)";
        Connection con = this.getCon();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setObject(1, name);
        pst.setObject(2, name);
        ResultSet rs = pst.executeQuery();
        return rs;
    }

    public void insertStuGroup(int groupid, String projectname, String student1name, String student2name, int projectgrade) throws SQLException, ClassNotFoundException {
        String sql = "insert into stu_group(groupid,projectname,student1name,student2name,projectgrade) values(?,?,?,?,?);";
        Connection con = this.getCon();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setObject(1,groupid);
        pst.setObject(2,projectname);
        pst.setObject(3,student1name);
        pst.setObject(4,student2name);
        pst.setObject(5,projectgrade);
        int count = pst.executeUpdate();
        if (count > 0) {
            System.out.println("Ajouter avec succès");
        } else System.out.println("Echec à ajouter");
        pst.close();
        con.close();
    }

    public void deleteGroup(StudentGroup binomes) throws SQLException, ClassNotFoundException {
        if (binomes == null || binomes.getProjectName() == null) {
            System.out.println("Impossible de supprimer une formation nulle.");
            return;
        }
        Connection con = this.getCon();
        String sql = "delete from stu_group where grouplistid=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, binomes.getGroupListId());
        int count = ps.executeUpdate();
        if (count > 0) {
            System.out.println("Supprimé avec succès");
        } else {
            System.out.println("Échec de la suppression");
        }
    }

    public void updateGroup(StudentGroup binomes) throws SQLException, ClassNotFoundException {
        if (binomes == null || binomes.getProjectName() == null) {
            System.out.println("Impossible de mettre à jour une formation nulle.");
            return;
        }
        Connection con = this.getCon();
        String sql = "update stu_group set groupid = ?, projectname = ?, student1name = ?, student2name = ?, projectgrade = ? where grouplistid = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setObject(1, binomes.getGroupId());
        pst.setObject(2, binomes.getProjectName());
        pst.setObject(3, binomes.getStudent1Name());;
        pst.setObject(4, binomes.getStudent2Name());
        pst.setObject(5, binomes.getProjectGrade());
        pst.setObject(6, binomes.getGroupListId());
        int count = pst.executeUpdate();
        if (count > 0) {
            System.out.println("Mis à jour avec succès");
        } else {
            System.out.println("Échec de la mise à jour");
        }
    }
}
