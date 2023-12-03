package com.sgms.controller;

import com.sgms.app.LoginApplication;
import com.sgms.pojo.UserInformation;
import com.sgms.utils.MyUtils;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {


    @Override
    public void start(Stage stage) throws Exception {
//        FXMLLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Parent root = FXMLLoader.load(Objects.requireNonNull(Register.class.getResource(MyUtils.FXML_PATH + "Main.fxml")));
        Scene scene = new Scene(root, 898, 621);
        //Obtenir des informations sur l'utilisateur
        UserInformation userInformation = UserInformation.getUser();
        //Obtenir une étiquette de texte
        Text name = (Text) root.lookup("#name");
        Text id = (Text) root.lookup("#id");
        //Définition des valeurs marquées
        name.setText(userInformation.getName());
        id.setText(userInformation.getId());


        //Configuration du côté étudiant
        //Les boutons Ajouter une note et Supprimer une note ne sont pas visibles
        //Pas de modification des notes
        //Obtenez ces 3 boutons
        Button addbutton = (Button) root.lookup("#add");
        Button delbutton = (Button) root.lookup("#del");
        Button showformbutton = (Button) root.lookup("#showform");
        Button showgroupbutton =(Button) root.lookup("#showgroup");
        Button showprojectbutton =(Button) root.lookup("#showproject");
        Button findbutton = (Button) root.lookup("#find");
        //Distinguer le type d'affichage du client en jugeant l'attribut du travail
        //Empêcher de sauter le portail de connexion pour entrer directement dans le système
        if (userInformation.getJob().equals("étudiant")) {
            stage.setTitle("SGPE(côté étudiant)");
            addbutton.setVisible(false);
            delbutton.setVisible(false);
            showformbutton.setVisible(false);
            showgroupbutton.setVisible(false);
            showprojectbutton.setVisible(false);
            //Déplacer le bouton Rechercher des résultats au centre

        } else if (userInformation.getJob().equals("enseignant")) {
            stage.setTitle("SGPE(côté enseignant)");
        }
        //Lier un événement au bouton de sortie
        //Retour à l'écran de connexion
        Button tuichu = (Button) root.lookup("#exit");
        tuichu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
                LoginApplication loginApplication = new LoginApplication();
                try {
                    loginApplication.start(new Stage());
                    System.out.println("Sortie réussie");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        stage.setScene(scene);
        //Fixer la taille immuable
        stage.setResizable(false);
        stage.show();


    }
}
