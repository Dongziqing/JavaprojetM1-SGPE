package com.sgms.controller;

import com.sgms.dao.BaseDao;
import com.sgms.dao.UserInformationDao;
import com.sgms.pojo.UserInformation;
import com.sgms.utils.MyUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;

public class LoginController {

    @FXML  //Zone de texte pour la saisie du numéro d'étudiant
    protected TextField id;
    @FXML //Zone de texte pour la saisie du mot de passe
    protected PasswordField password;
    @FXML //Message d'erreur concernant la saisie du mot de passe
    protected Label errorlabel;
    private BaseDao baseDao = new BaseDao();
    private UserInformationDao userInformationDao = new UserInformationDao();


    @FXML
    //bouton de connexion
    protected void onLoginClick() throws Exception {
        //Effacer le message d'erreur
        errorlabel.setText("");
        boolean loginStaus = false;
        //Déterminer d'abord si l'entrée est vide
        if (MyUtils.isEmpty(id.getText()) || MyUtils.isEmpty(password.getText())) {
            //Renvoie un message faux si l'une des entrées est vide.
            //Fermer la fenêtre de connexion
            Dialog<ButtonType> warning = new Dialog<>();
            warning.getDialogPane().getButtonTypes().add(new ButtonType("confirmer", ButtonBar.ButtonData.OK_DONE));
            warning.setTitle("Le numéro de compte ou le mot de passe ne peut être vide");
            warning.setContentText("Le numéro de compte ou le mot de passe ne peut être vide");
            warning.show();
            return;
        }
        //Interroger la base de données
        ResultSet rs = userInformationDao.searchAllUser();
        while (rs.next()) {
            if (rs.getString("id") != null && rs.getString("password") != null && rs.getString("id").equals(id.getText()) && rs.getString("password").equals(password.getText())) {
                //Si toutes les entrées sont correctes
                System.out.println("Connexion réussie");
                loginStaus = true;
                login = true;
                UserInformation userInformation = UserInformation.getUser();
                userInformation.setId(rs.getString("id"));
                userInformation.setName(rs.getString("name"));
                userInformation.setJob(rs.getString("job"));
                userInformation.setPassword(rs.getString("password"));
                Main main = new Main();
                main.start(new Stage());
            }
        }
        if (!loginStaus) {
            errorlabel.setText("Le numéro de compte ou le mot de passe est incorrect, veuillez le saisir à nouveau.");
            System.out.println("Échec de la connexion");
        }
    }

    @FXML
    void onRegisterClick() throws Exception {
        Register register = new Register();
        register.start(new Stage());
    }

    public static boolean login = false;
}