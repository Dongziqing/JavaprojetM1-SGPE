package com.sgms.app;

import com.sgms.controller.LoginController;
import com.sgms.controller.Register;
import com.sgms.utils.MyUtils;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

//Portail de connexion au programme
public class LoginApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader = new FXMLLoader(LoginApplication.class.getResource("login.fxml"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(Register.class.getResource(MyUtils.FXML_PATH + "login.fxml")));
        Scene scene = new Scene(root, 617, 432);
        stage.setScene(scene);
        stage.setTitle("Système de gestion de projets des étudiants");
        //Fixer la taille immuable
        stage.setResizable(false);
        stage.show();
        //Paramétrage de la fermeture de la fenêtre de connexion après l'ouverture de la session
        Button login = (Button) root.lookup("#login");
        login.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                System.out.println(LoginController.login);
                if (LoginController.login) {
                    //Fermer la fenêtre de connexion actuelle
                    stage.close();
                }
            }
        });

    }


    public static void main(String[] args) {
        launch();
    }
}