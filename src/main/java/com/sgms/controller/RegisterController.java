package com.sgms.controller;

import com.sgms.dao.BaseDao;
import com.sgms.dao.UserInformationDao;
import com.sgms.utils.MyUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RegisterController {
    private BaseDao baseDao = new BaseDao();

    private UserInformationDao userInformationDao = new UserInformationDao();

    @FXML
    private ComboBox job = new ComboBox<>();
    @FXML
    private TextField id;
    @FXML
    private TextField name;
    @FXML
    private PasswordField password;

    @FXML
    void onRegisterClick() throws SQLException, ClassNotFoundException {
        //Obtenir la liste des identités
        //ObservableList items = job.getItems();
        String jobStr = (String) job.getSelectionModel().selectedItemProperty().getValue();
        //System.out.println(job.getSelectionModel().selectedItemProperty().getValue());

        //Déterminez d'abord si vous êtes disponible.
        if (MyUtils.isEmpty(id.getText()) || MyUtils.isEmpty(name.getText()) || MyUtils.isEmpty(password.getText())
                || MyUtils.isEmpty(jobStr)) {
            //System.out.println("isnull");
            Dialog<ButtonType> warning = new Dialog<>();
            warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
            warning.setTitle("Echec de l'enregistrement");
            warning.setContentText("Veuillez compléter les informations");
            warning.show();
            return;
        }
        String idStr = id.getText();
        String nameStr = name.getText();
        String passwordStr = password.getText();
        //Déterminer si le numéro d'étudiant est enregistré en premier
        ResultSet rs = userInformationDao.searchAllUser();
        while (rs.next()) {
            if (idStr.equals(rs.getString("id"))) {
                //S'il y a des ids en double
                Dialog<ButtonType> warning = new Dialog<>();
                warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                warning.setTitle("Echec de l'enregistrement");
                warning.setContentText("Ce id a été enregistré");
                warning.show();
                return;
            }
        }
        //Créer un nouvel utilisateur en appelant une méthode dans le dao
        boolean status = userInformationDao.addNewUser(idStr, MyUtils.capitalizeFirstLetter(nameStr), passwordStr, jobStr);
        if (status) {
            Dialog<ButtonType> warning = new Dialog<>();
            warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
            warning.setTitle("Enregistrement réussi");
            warning.setContentText("Enregistrement réussi");
            warning.show();
            //Effacer toutes les zones de texte pour éviter les enregistrements en double
            id.clear();
            name.clear();
            password.clear();
        }


    }
}
