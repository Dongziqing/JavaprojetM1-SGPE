package com.sgms.dao;

import com.sgms.pojo.StudentGrade;
import com.sgms.pojo.UserInformation;
import javafx.collections.ObservableList;

import java.sql.*;

public class StudentGradeDao extends BaseDao{

    private UserInformation userInformation = UserInformation.getUser();


    //Vérifier les notes de tous les élèves
    public ResultSet searchAllGrade() throws SQLException, ClassNotFoundException {
        String sql = "select * from stu_grade;";
        Connection con = this.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;

    }

    //Interroger toutes les notes d'un seul utilisateur étudiant
    public ResultSet searchGrade() throws SQLException, ClassNotFoundException {
        UserInformation userInformation = UserInformation.getUser();
        String sql = "SELECT* FROM stu_grade WHERE name= ";
        String name = "'" + userInformation.getName() + "'";
        sql += name;
        Connection con = this.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }

    //Édition pour etudiants Recherche par mot-clé
    public ResultSet keywordSearch(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT* FROM stu_grade WHERE name= ";
        String name = "'" + userInformation.getName() + "'";
        sql += name;
        String temp = "'" + keyword + "'";
        sql += "and date = ";
        sql += temp;
        Connection con = this.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;
    }

    //Insérer un message sur les note d'étudiant
    public void insertGrade(Object formid, Date date, String java, String sar, String marketng, String ml, String name) throws SQLException, ClassNotFoundException {
        String sql = "insert into stu_grade(formid,date,java,sar,marketing,ml,name) values(?,?,?,?,?,?,?);";
        Connection con = this.getCon();
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setObject(1, formid);
        pst.setObject(2, date);
        pst.setObject(3, java);
        pst.setObject(4, sar);
        pst.setObject(5, marketng);
        pst.setObject(6, ml);
        pst.setObject(7, name);
        int count = pst.executeUpdate();
        if (count > 0) {
            System.out.println("Ajouter avec succès");
        } else System.out.println("Echec à ajouter");
//        pst.addBatch();
//        pst.executeBatch();
        //Déblocage des ressources
        pst.close();
        con.close();
    }

    //Édition pour enseignants Recherche par mot-clé
    public ResultSet keywordSearchAll(String keyword) throws SQLException, ClassNotFoundException {
        String sql = "SELECT* FROM stu_grade WHERE ";
//        String name="'"+userInformation.getName()+"'";
        String temp = "'%" + keyword;
        temp += "%'";
        temp.replace("%", "\\%");
//        System.out.println(temp);
        sql += temp;
        sql += "or date like ";
        sql += temp;

        //Recherche par nom
        sql += " or name like ";
        String temp2 = "'";
        temp2 += keyword;
        temp2 += "'";
        sql += temp2;
        Connection con = this.getCon();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        return rs;

    }

    public void updateAllGrade(ObservableList<StudentGrade> cellData) throws SQLException, ClassNotFoundException {
        Connection con = this.getCon();
        String sql = "update stu_grade set date=?,java=?,sar=?,marketing=?,ml=?, where id=?;";
        PreparedStatement ps = con.prepareStatement(sql);
        for (Object o : cellData) {
            StudentGrade stu = (StudentGrade) o;
//            System.out.println(stu);
            ps.setObject(1, stu.getDate());
            ps.setObject(2, stu.getJava());
            ps.setObject(3, stu.getSar());
            ps.setObject(4, stu.getMarketing());
            ps.setObject(5, stu.getMl());
            ps.setObject(6, stu.getId());
            ps.executeUpdate();
        }
    }

    //Suppression des enregistrements des notes d'étudiants spécifiés
    public void delstugrade(StudentGrade stu) throws SQLException, ClassNotFoundException {
        Connection con = this.getCon();
        String sql = "delete from stu_grade where id=?";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, stu.getId());
        int count = ps.executeUpdate();
        if (count > 0) {
            System.out.println("Supprimé avec succès");
        } else {
            System.out.println("Échec de la suppression");
        }
    }


}
