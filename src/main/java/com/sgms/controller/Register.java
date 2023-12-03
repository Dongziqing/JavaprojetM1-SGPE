package com.sgms.controller;

import com.sgms.utils.MyUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.util.Objects;

public class Register extends Application {
    @Override
    public void start(Stage stage) throws Exception {
//        FXMLLoader  = new FXMLLoader(LoginApplication.class.getResource("register.fxml"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(Register.class.getResource(MyUtils.FXML_PATH + "register.fxml")));
        ComboBox job = (ComboBox) root.lookup("#job");
        Label label = (Label) root.lookup("id");
        job.getItems().addAll("étudiant", "enseignant");
        Scene scene = new Scene(root, 615, 514);
        stage.setTitle("Enregistrement de l'utilisateur");
        stage.setScene(scene);
        //Fixer la taille immuable
        stage.setResizable(false);
        stage.show();
    }
}
