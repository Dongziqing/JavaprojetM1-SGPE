package com.sgms.controller;

import com.sgms.pojo.UserInformation;
import com.sgms.utils.MyUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.Objects;

public class Search extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Search.class.getResource(MyUtils.FXML_PATH + "search.fxml")));
        Scene scene = new Scene(root, 711, 518);
        stage.setScene(scene);
        //Sélectionnez le bouton Masquer Supprimer l'accomplissement en fonction de votre identité.
        UserInformation userInformation = UserInformation.getUser();
        String job = userInformation.getJob();
        Button del = (Button) root.lookup("#del");
        if (job.equals("étudiant")) {
            del.setVisible(false);
        }
        stage.setResizable(false);
        stage.show();


    }
}
