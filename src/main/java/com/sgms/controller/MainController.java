package com.sgms.controller;

import com.sgms.dao.*;
import com.sgms.pojo.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.sgms.utils.MyUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Optional;



public class MainController {
    //Obtenir des informations sur l'utilisateur
    UserInformation userInformation = UserInformation.getUser();
    @FXML
    Text name;
    @FXML
    Text id;
    @FXML //Fiche de notation du projet
    private TableView<StudentGrade> gradeTable = new TableView<>();
    @FXML
    private TableView<Formation> formTable = new TableView<>();
    @FXML
    private TableView<StudentGroup> groupTable = new TableView<>();
    @FXML
    private  TableView<Project> projectTable = new TableView<>();
    @FXML //Valeurs des attributs pour chaque colonne
    private TableColumn<StudentGrade, Date> date = new TableColumn<>();

    @FXML
    TableColumn<StudentGrade, String> java = new TableColumn<>();
    @FXML
    TableColumn<StudentGrade, String> sar = new TableColumn<>();
    @FXML
    TableColumn<StudentGrade, String> marketing = new TableColumn<>();
    @FXML
    TableColumn<StudentGrade, String> ml = new TableColumn<>();
    @FXML
    TableColumn<StudentGrade, String> nameCol = new TableColumn<>();
    @FXML
    TableColumn<StudentGrade, Integer> numCol = new TableColumn<>();
    @FXML
    TableColumn<StudentGrade, String> fid = new TableColumn<>();


    //Pour le stockage
    private final ObservableList<StudentGrade> cellData = FXCollections.observableArrayList();

    //connexion à la base de données
    private BaseDao baseDao = new BaseDao();

    private UserInformationDao userInformationDao = new UserInformationDao();

    private GroupDao groupDao = new GroupDao();

    private FormationDao formationDao = new FormationDao();

    private StudentGradeDao studentGradeDao = new StudentGradeDao();

    private ProjectDao projectDao = new ProjectDao();

    //Étudiants à supprimer
    public StudentGrade delStu;

    public Formation delForm;
    public StudentGroup studentGroup;

    public Project delProject;

    @FXML
    public void onShowClick() throws SQLException, ClassNotFoundException {
        cellData.clear();
        //Affichage de la mise à jour des informations de l'étiquette
        name.setText(userInformation.getName());
        id.setText(userInformation.getId());
        //Effacez les données avant chaque clic pour éviter les affichages en double.
        gradeTable.refresh();
        gradeTable.getItems().clear();
        //Déterminer l'identité
        //Afficher les notes de chacun s'il s'agit d'un enseignant
        if (userInformation.getJob().equals("enseignant")) {
            ResultSet rs = studentGradeDao.searchAllGrade();
            while (rs.next()) {
                ResultSet resultSetGroup = groupDao.searchByGroup(rs.getString("name"));
                HashMap<String, String> groupMap = MyUtils.genHashMap(resultSetGroup, "projectname", "projectgrade");
                ResultSet resultSet = projectDao.getProjectInfo();
                HashMap<String, String> projectMap = MyUtils.genHashMap(resultSet,"subjectname","duedate");

                int dateJava = MyUtils.calculateDaysBetween(rs.getDate("date"), Date.valueOf(projectMap.get("Java")));
                String javaFS = String.valueOf(MyUtils.finalScore(rs.getString("java"),groupMap.get("Java"), dateJava));

                int dateSar = MyUtils.calculateDaysBetween(rs.getDate("date"), Date.valueOf(projectMap.get("Sar")));
                String SarFS = String.valueOf(MyUtils.finalScore(rs.getString("sar"),groupMap.get("Sar"), dateSar));

                int dateMarketing = MyUtils.calculateDaysBetween(rs.getDate("date"), Date.valueOf(projectMap.get("Marketing")));
                String MarketingFS = String.valueOf(MyUtils.finalScore(rs.getString("marketing"),groupMap.get("Marketing"), dateMarketing));

                int dateMl = MyUtils.calculateDaysBetween(rs.getDate("date"), Date.valueOf(projectMap.get("Ml")));
                String MlFS = String.valueOf(MyUtils.finalScore(rs.getString("ml"),groupMap.get("Ml"), dateMl));

                Integer id = rs.getInt("id");
                Date date1 = rs.getDate("date");
                String formid = rs.getString("formid");
                String name = rs.getString("name");
                StudentGrade studentGrade = new StudentGrade(date1, formid, javaFS, SarFS, MarketingFS, MlFS, name, id);
                //Ajouter à la liste
                cellData.add(studentGrade);
            }

            gradeTable.setEditable(true);
            //Attribution de valeurs aux colonnes
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            fid.setCellValueFactory(new PropertyValueFactory<>("fid"));
            java.setCellValueFactory(new PropertyValueFactory<>("java"));
            sar.setCellValueFactory(new PropertyValueFactory<>("sar"));
            marketing.setCellValueFactory(new PropertyValueFactory<>("marketing"));
            ml.setCellValueFactory(new PropertyValueFactory<>("ml"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            numCol.setCellValueFactory(new PropertyValueFactory<>("id"));
            gradeTable.setItems(cellData);
            gradeTable.setVisible(true);

            gradeTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    TableView<StudentGrade> sw = gradeTable;
                    StudentGrade studentGrade = sw.getSelectionModel().getSelectedItem();
                    delStu = studentGrade;
//                    System.out.println(delstu);
                }
            });

            //Configurer la cellule pour qu'elle soit éditable
            //Double-cliquer pour modifier, puis appeler la méthode dao pour mettre à jour les données de la base.
            java.setCellFactory(TextFieldTableCell.forTableColumn());
            sar.setCellFactory(TextFieldTableCell.forTableColumn());
            marketing.setCellFactory(TextFieldTableCell.forTableColumn());
            ml.setCellFactory(TextFieldTableCell.forTableColumn());
            Callback<TableColumn<StudentGrade, String>, TableCell<StudentGrade, String>> cellFactory
                    = (TableColumn<StudentGrade, String> p) -> {
                return new TableCell<>();
            };
            //Le réglage de chaque cellule peut être modifié

            java.setOnEditCommit(
                    (TableColumn.CellEditEvent<StudentGrade, String> t) -> {
                        ((StudentGrade) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setJava(t.getNewValue());
                        System.out.println("Modifié avec succès");
                        try {
                            studentGradeDao.updateAllGrade(cellData);
                            this.onShowClick();
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });

            sar.setOnEditCommit(
                    (TableColumn.CellEditEvent<StudentGrade, String> t) -> {
                        ((StudentGrade) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setSar(t.getNewValue());
                        System.out.println("Modifié avec succès");
                        try {
                            studentGradeDao.updateAllGrade(cellData);
                            this.onShowClick();
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });

            marketing.setOnEditCommit(
                    (TableColumn.CellEditEvent<StudentGrade, String> t) -> {
                        ((StudentGrade) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setMarketing(t.getNewValue());
                        System.out.println("Modifié avec succès");
                        try {
                            studentGradeDao.updateAllGrade(cellData);
                            this.onShowClick();
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });

            ml.setOnEditCommit(
                    (TableColumn.CellEditEvent<StudentGrade, String> t) -> {
                        ((StudentGrade) t.getTableView().getItems().get(
                                t.getTablePosition().getRow())
                        ).setMl(t.getNewValue());
                        System.out.println("Modifié avec succès");
                        try {
                            studentGradeDao.updateAllGrade(cellData);
                            this.onShowClick();
                        } catch (SQLException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    });

        }//if end
        else {
            ResultSet rs = studentGradeDao.searchGrade();
            while (rs.next()) {
                ResultSet resultSetGroup = groupDao.searchByGroup(rs.getString("name"));
                HashMap<String, String> groupMap = MyUtils.genHashMap(resultSetGroup, "projectname", "projectgrade");
                ResultSet resultSet = projectDao.getProjectInfo();
                HashMap<String, String> projectMap = MyUtils.genHashMap(resultSet,"subjectname","duedate");

                int dateJava = MyUtils.calculateDaysBetween(rs.getDate("date"), Date.valueOf(projectMap.get("Java")));
                String javaFS = String.valueOf(MyUtils.finalScore(rs.getString("java"),groupMap.get("Java"), dateJava));

                int dateSar = MyUtils.calculateDaysBetween(rs.getDate("date"), Date.valueOf(projectMap.get("Sar")));
                String SarFS = String.valueOf(MyUtils.finalScore(rs.getString("sar"),groupMap.get("Sar"), dateSar));

                int dateMarketing = MyUtils.calculateDaysBetween(rs.getDate("date"), Date.valueOf(projectMap.get("Marketing")));
                String MarketingFS = String.valueOf(MyUtils.finalScore(rs.getString("marketing"),groupMap.get("Marketing"), dateMarketing));

                int dateMl = MyUtils.calculateDaysBetween(rs.getDate("date"), Date.valueOf(projectMap.get("Ml")));
                String MlFS = String.valueOf(MyUtils.finalScore(rs.getString("ml"),groupMap.get("Ml"), dateMl));
                Date date1 = rs.getDate("date");
                String formId = rs.getString("formid");
                String name = rs.getString("name");
                Integer id = rs.getInt("id");
                StudentGrade studentGrade = new StudentGrade(date1, formId, javaFS, SarFS, MarketingFS, MlFS, name, id);
                cellData.add(studentGrade);
            }
            gradeTable.setEditable(true);

            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            fid.setCellValueFactory(new PropertyValueFactory<>("fid"));
            java.setCellValueFactory(new PropertyValueFactory<>("java"));
            sar.setCellValueFactory(new PropertyValueFactory<>("sar"));
            marketing.setCellValueFactory(new PropertyValueFactory<>("marketing"));
            ml.setCellValueFactory(new PropertyValueFactory<>("ml"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
            numCol.setCellValueFactory(new PropertyValueFactory<>("id"));

            gradeTable.setItems(cellData);
            gradeTable.setVisible(true);
        }


    }

    @FXML
        //Ajout de notes
    void onaddclick() {
        //Créer une nouvelle fenêtre pour ajouter des notes
        Stage add = new Stage();
        Pane pane = new Pane();
        pane.setPrefHeight(400);
        pane.setPrefWidth(600);
        //balise de titre
        Label label = new Label("Notes ajoutés");
        label.setFont(new Font("arial", 26));
        label.setLayoutX(248);
        label.setLayoutY(22);

        Label label1 = new Label("Date:");
        label1.setFont(new Font("arial", 24));
        label1.setLayoutX(57);
        label1.setLayoutY(67);

        Label fid = new Label("formid");
        fid.setFont(new Font("arial", 24));
        fid.setLayoutX(57);
        fid.setLayoutY(120);


        Label java = new Label("Java :");
        java.setFont(new Font("arial", 24));
        java.setLayoutX(57);
        java.setLayoutY(180);

        Label sar = new Label("SAR:");
        sar.setFont(new Font("arial", 24));
        sar.setLayoutX(57);
        sar.setLayoutY(230);

        Label mar = new Label("Marketing :");
        mar.setFont(new Font("arial", 24));
        mar.setLayoutX(57);
        mar.setLayoutY(280);

        Label ml = new Label("ML:");
        ml.setFont(new Font("arial", 24));
        ml.setLayoutX(57);
        ml.setLayoutY(330);

        Label name = new Label("Nom:");
        name.setFont(new Font("arial", 24));
        name.setLayoutX(333);
        name.setLayoutY(67);

        TextField t1 = new TextField();
        t1.setPrefHeight(30);
        t1.setLayoutX(150);
        t1.setLayoutY(68);
        t1.setPromptText("par exemple:2022-1-1");
        ComboBox c1 = new ComboBox<>();
        c1.setPrefHeight(30);
        c1.setLayoutX(200);
        c1.setLayoutY(128);
        c1.getItems().addAll("1", "2", "3", "4", "5", "6", "7", "8", "9");
        TextField t2 = new TextField();
        t2.setPrefHeight(30);
        t2.setLayoutX(200);
        t2.setLayoutY(180);
        TextField t3 = new TextField();
        t3.setPrefHeight(30);
        t3.setLayoutX(200);
        t3.setLayoutY(230);
        TextField t4 = new TextField();
        t4.setPrefHeight(30);
        t4.setLayoutX(200);
        t4.setLayoutY(280);
        TextField t5 = new TextField();
        t5.setPrefHeight(30);
        t5.setLayoutX(200);
        t5.setLayoutY(330);
        TextField tname = new TextField();
        tname.setPrefHeight(30);
        tname.setLayoutX(400);
        tname.setLayoutY(67);
        Button button = new Button("Confirmer l'ajout");
        button.setLayoutX(478);
        button.setLayoutY(360);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Effectuer une opération d'insertion dans la base de données
                try {
                    //获取Project信息 提取对应学科的提交时间 并且存入hashMap
//                    ResultSet resultSet = baseDao.getProjectInfo();
//                    HashMap<String, java.util.Date> projectMap = MyUtils.genProjectMap(resultSet);
//                    int dateJava = MyUtils.calculateDaysBetween(Date.valueOf(t1.getText()), projectMap.get("Java"));
//                    String javaFS = String.valueOf(MyUtils.finalScore(t2.getText(), dateJava));
//                    int dateSar = MyUtils.calculateDaysBetween(Date.valueOf(t1.getText()), projectMap.get("Sar"));
//                    String SarFS = String.valueOf(MyUtils.finalScore(t3.getText(), dateSar));
//                    int dateMarketing = MyUtils.calculateDaysBetween(Date.valueOf(t1.getText()), projectMap.get("Marketing"));
//                    String MarketingFS = String.valueOf(MyUtils.finalScore(t4.getText(), dateMarketing));
//                    int dateML = MyUtils.calculateDaysBetween(Date.valueOf(t1.getText()), projectMap.get("ML"));
//                    String MLFS = String.valueOf(MyUtils.finalScore(t5.getText(), dateML));

                    studentGradeDao.insertGrade(c1.getValue(), Date.valueOf(t1.getText()), t2.getText(), t3.getText(), t4.getText(), t5.getText(),
                            MyUtils.capitalizeFirstLetter(tname.getText()));
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Ajouter avec succès");
                    warning.setContentText("Ajouter avec succès");
                    warning.show();
                    //Appeler showclick pour mettre à jour le contenu de la tableview après l'avoir ajouté avec succès.
                    onShowClick();
                    //Fermer la fenêtre d'ajout pour éviter les ajouts en double
                    add.close();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        pane.getChildren().addAll(label, label1, fid, java, sar, mar, ml, c1, t1, t2, t3, t4, t5, button, name, tname);

        Scene scene = new Scene(pane);
        add.setScene(scene);
        add.setResizable(false);
        add.setTitle("Ajout de notes");
        add.show();
    }

    @FXML
        //Modification des informations personnelles
    void onModifyInfClick() {
        //Créer une nouvelle fenêtre
        Stage stage = new Stage();
        Pane pane = new Pane();
        pane.setPrefWidth(800);
        pane.setPrefHeight(650);
        Label title = new Label("Modification des informations personnelles");
        Font font = new Font("arial", 20);
        title.setFont(font);
        title.setTextFill(Color.color(1.0, 0.1, 0.1));
        title.setLayoutX(202);
        title.setLayoutY(14);
        Label id = new Label("id:");
        id.setFont(font);
        id.setLayoutX(21);
        id.setLayoutY(100);
        Label name = new Label("Username:");
        name.setFont(font);
        name.setLayoutX(21);
        name.setLayoutY(200);
        Label password = new Label("Password original:");
        password.setFont(font);
        password.setLayoutX(21);
        password.setLayoutY(300);


        Label warnings = new Label("Veuillez ne pas remplir le contenu sans le modifier!!!");
        warnings.setFont(font);
        warnings.setTextFill(Color.color(1, 0, 0));
        warnings.setLayoutX(21);
        warnings.setLayoutY(400);
        warnings.setFont(Font.font(25));

        Label sid = new Label(userInformation.getId());
        sid.setLayoutX(150);
        sid.setLayoutY(100);
        sid.setFont(Font.font(20));
        Label sname = new Label(userInformation.getName());
        sname.setLayoutX(150);
        sname.setLayoutY(200);
        sname.setFont(Font.font(20));

        PasswordField oldpassword = new PasswordField();
        oldpassword.setLayoutX(200);
        oldpassword.setLayoutY(300);
        oldpassword.setPrefWidth(144);
        oldpassword.setPrefHeight(30);

        //Événement de liaison pour l'ajout d'un bouton
        Button button = new Button("Confirmation des changements");
        button.setLayoutX(400);
        button.setLayoutY(500);
        button.setFont(font);

        Label label1 = new Label("New id:");
        Label label2 = new Label("Nom:");
        Label label3 = new Label("New password:");
        label1.setFont(font);
        label2.setFont(font);
        label3.setFont(font);
        label1.setLayoutX(350);
        label2.setLayoutX(350);
        label3.setLayoutX(21);
        label1.setLayoutY(100);
        label2.setLayoutY(200);
        label3.setLayoutY(350);
        TextField newId = new TextField("L'id n'est pas favorable à une modification pour l'instant.");
        newId.setEditable(false);
        TextField newName = new TextField();
        newName.setPromptText("Veuillez saisir votre nom");
        PasswordField newPassword = new PasswordField();
        newId.setLayoutX(500);
        newName.setLayoutX(500);
        newPassword.setLayoutX(200);
        newId.setLayoutY(100);
        newName.setLayoutY(200);
        newPassword.setLayoutY(350);
        newId.setPrefHeight(40);
        newName.setPrefHeight(30);
        newPassword.setPrefHeight(30);

        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //Modifier les informations personnelles
                //Vérifier d'abord ce qui doit être modifié L'id, en tant que valeur unique, ne peut pas être modifié pour l'instant
                if (!MyUtils.isEmpty(newName.getText())) {
                    userInformation.setName(newName.getText());
                }
                if (!MyUtils.isEmpty(newPassword.getText())) {
                    userInformation.setNewPassword(newPassword.getText());
                }
                 if(MyUtils.isEmpty(newName.getText())||MyUtils.isEmpty(newPassword.getText())){
                    System.out.println("Ce qui n'a pas été modifié");
                    return;
                }//Si le nouveau mot de passe est nul, le nouveau mot de passe sera maintenu par défaut.

                //Vérifier que l'ancien mot de passe est correct
                //S'il est correct, exécuter
                if (oldpassword.getText().equals(userInformation.getPassword())) {
                    try {
                        userInformationDao.modifyUser();
                        Dialog<ButtonType> warning = new Dialog<>();
                        warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                        warning.setTitle("Modifié avec succès");
                        warning.setContentText("Modification réussie des informations personnelles");
                        warning.show();
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("mot de passe incorrect");
                    warning.setContentText("Mauvais mot de passe, veuillez le saisir à nouveau");
                    warning.show();
                }

            }
        });


        pane.getChildren().addAll(title, warnings, id, name, password, button, sid, sname, oldpassword);
        pane.getChildren().addAll(label1, label2, label3, newId, newName, newPassword);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.setTitle("Modification des informations personnelles");
        stage.setResizable(false);
        stage.show();

    }

    @FXML
        //Trouver des notes
    void onSearchClick() throws Exception {
        Search search = new Search();
        search.start(new Stage());
    }

    @FXML
        //Supprimer les notes des étudiants
    void onDelClick() throws SQLException, ClassNotFoundException {
        //System.out.println(delstu);
        //Déterminer d'abord si la sélection est vide
        if (gradeTable.getSelectionModel().getSelectedItem() == null) {
            Dialog<ButtonType> warning = new Dialog<>();
            warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
            warning.setTitle("Échec de la suppression");
            warning.setContentText("Le contenu supprimé est vide");
            warning.show();
            return;
        }
        //Fenêtre pop-up pour confirmer la suppression
        Alert alert = new Alert(Alert.AlertType.WARNING, "La suppression est-elle confirmée?",
                new ButtonType("Annuler ", ButtonBar.ButtonData.CANCEL_CLOSE),
                new ButtonType("Confirmer", ButtonBar.ButtonData.YES));
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Veuillez confirmer si vous souhaitez supprimer les résultats de l'étudiant");
        Optional<ButtonType> buttonType = alert.showAndWait();
        //Obtenir la valeur buttonData du bouton correspondant au type de bouton.
        if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
//            System.out.println("Échec de la suppression");
            return;
        } else {
//            System.out.println("Supprimé avec succès");
            studentGradeDao.delstugrade(delStu);
            Dialog<ButtonType> warning = new Dialog<>();
            warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
            warning.setTitle("Supprimé avec succès");
            warning.setContentText("Supprimé avec succès");
            warning.show();
            //Liste des mises à jour
            onShowClick();
        }
    }

    public void onShowFormClick() throws SQLException, ClassNotFoundException {
        ObservableList<Formation> cellData1 = FXCollections.observableArrayList();
        Stage formStage = new Stage();
        Pane formpane = new Pane();
        formTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                TableView<Formation> fm = formTable;
                delForm = fm.getSelectionModel().getSelectedItem();
            }
        });
        //formpane.setPrefWidth(800);formpane.setPrefHeight(600);
        TableView<Formation> formTable = new TableView<>();
        Label placeholderLabel = new Label("Il n'y a aucun contenu dans le tableau");
        formTable.setPlaceholder(placeholderLabel);
        TableColumn<Formation, String> formid = new TableColumn<>("Form ID");
        TableColumn<Formation, String> formname = new TableColumn<>("Form Name");
        TableColumn<Formation, String> formtype = new TableColumn<>("Form Type");
        formTable.getColumns().addAll(formid, formname, formtype);

        Label label1 = new Label("formid:");
        label1.setFont(new Font("Arial", 24));
        label1.setLayoutX(270);
        label1.setLayoutY(100);
        TextField t1 = new TextField();
        t1.setPrefHeight(30);
        t1.setLayoutX(400);
        t1.setLayoutY(100);

        Label label2 = new Label(" formname:");
        label2.setFont(new Font("Arial", 24));
        label2.setLayoutX(270);
        label2.setLayoutY(150);
        TextField t2 = new TextField();
        t2.setPrefHeight(30);
        t2.setLayoutX(400);
        t2.setLayoutY(150);

        Label label3 = new Label("formtype:");
        label3.setFont(new Font("Arial", 24));
        label3.setLayoutX(270);
        label3.setLayoutY(200);
        TextField t3 = new TextField();
        t3.setPrefHeight(30);
        t3.setLayoutX(400);
        t3.setLayoutY(200);

        Button add1Button = new Button("Ajouter");
        add1Button.setLayoutX(300);
        add1Button.setLayoutY(300);
        Button del1Button = new Button("Supprimer");
        del1Button.setLayoutX(420);
        del1Button.setLayoutY(300);

        add1Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    formationDao.insertFormation(t1.getText(), MyUtils.capitalizeFirstLetter(MyUtils.capitalizeFirstLetter(t2.getText())), t3.getText());
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Ajouter avec succès");
                    warning.setContentText("Ajouter avec succès");
                    warning.show();
                    onShowFormClick();
                    formStage.close();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        formpane.getChildren().addAll(label1, label2, label3, t1, t2, t3);

        del1Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (formTable.getSelectionModel().getSelectedItem() == null) {
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Échec de la suppression");
                    warning.setContentText("Le contenu supprimé est vide");
                    warning.show();
                    return;
                }
                Alert alert = new Alert(Alert.AlertType.WARNING, "La suppression est-elle confirmée?",
                        new ButtonType("Annuler ", ButtonBar.ButtonData.CANCEL_CLOSE),
                        new ButtonType("Confirmer", ButtonBar.ButtonData.YES));
                alert.setTitle("Confirmer la suppression");
                alert.setHeaderText("Veuillez confirmer si vous souhaitez supprimer les résultats de l'étudiant");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
//            System.out.println("Échec de la suppression");
                    return;
                } else {
                    try {
                        delForm = formTable.getSelectionModel().getSelectedItem();
                        formationDao.delFormation(delForm);
                    } catch (SQLException | ClassNotFoundException e) {
                        Alert alertDelForm = new Alert(Alert.AlertType.ERROR);
                        alertDelForm.setTitle("Erreur");
                        alertDelForm.setHeaderText("Erreur lors de l'ajout du formulaire");
                        alertDelForm.setContentText("Une erreur s'est produite : " + e.getMessage());
                        alertDelForm.showAndWait();
                        throw new RuntimeException(e);
                    }
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Supprimé avec succès");
                    warning.setContentText("Supprimé avec succès");
                    warning.show();
                    try {
                        onShowFormClick();
                        formStage.close();
                    } catch (SQLException | ClassNotFoundException e) {

                        throw new RuntimeException(e);
                    }

                }
            }
        });


        //cellData1.clear();
        ResultSet rs = formationDao.searchAllFormation();
        while (rs.next()) {
            String formId = rs.getString("formId");
            String formName = rs.getString("formName");
            String formType = rs.getString("formType");
            Formation formation = new Formation(formId, formName, formType);
            cellData1.add(formation);

        }
        formid.setCellValueFactory(new PropertyValueFactory<>("formId"));
        formname.setCellValueFactory(new PropertyValueFactory<>("formName"));
        formtype.setCellValueFactory(new PropertyValueFactory<>("formType"));
        formTable.setItems(cellData1);


        formpane.getChildren().addAll(formTable, add1Button, del1Button);

        Scene formScene = new Scene(formpane, 600, 400);
        formStage.setScene(formScene);
        formStage.setResizable(false);
        formStage.setTitle("Formations List");
        formStage.show();


    }

    public void onShowGroupClick() throws SQLException, ClassNotFoundException{
        ObservableList<StudentGroup> cellData2 = FXCollections.observableArrayList();
        Stage binomeStage = new Stage();
        Pane binomepane = new Pane();
        groupTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TableView<StudentGroup> bi = groupTable;
                StudentGroup studentGroup = bi.getSelectionModel().getSelectedItem();
                studentGroup = studentGroup;
            }
        });

        TableView<StudentGroup> groupTableView = new TableView<>();
        Label placeholderLabel = new Label("Il n'y a aucun contenu dans le tableau");
        groupTableView.setPlaceholder(placeholderLabel);
        TableColumn<StudentGroup,Integer> grouplistid = new TableColumn<>("id");
        TableColumn<StudentGroup,Integer> groupid = new TableColumn<>("groupid");
        TableColumn<StudentGroup, String> projectname = new TableColumn<>("projectName");
        TableColumn<StudentGroup,String> student1name = new TableColumn<>("stu1");
        TableColumn<StudentGroup,String> student2name = new TableColumn<>("stu2");
        TableColumn<StudentGroup,Integer>projectgrade = new TableColumn<>("projectgrade");
        groupTableView.getColumns().addAll(grouplistid,groupid,projectname,student1name,student2name,projectgrade);

        Label label1 = new Label("ProjectName:");
        label1.setFont(new Font("Arial",22));
        label1.setLayoutX(500); label1.setLayoutY(120);
        TextField t1 = new TextField();
        t1.setPrefHeight(30);
        t1.setPrefWidth(80);
        t1.setLayoutX(650);
        t1.setLayoutY(120);

        Label label2 = new Label("Stu1:");
        label2.setFont(new Font("Arial", 22));
        label2.setLayoutX(520);
        label2.setLayoutY(170);
        TextField t2 = new TextField();
        t2.setPrefHeight(30);
        t2.setPrefWidth(80);
        t2.setLayoutX(650);
        t2.setLayoutY(170);

        Label label3 = new Label("Stu2:");
        label3.setFont(new Font("Arial", 22));
        label3.setLayoutX(520);
        label3.setLayoutY(220);
        TextField t3 = new TextField();
        t3.setPrefHeight(30);
        t3.setPrefWidth(80);
        t3.setLayoutX(650);
        t3.setLayoutY(220);

        Label label5 = new Label("Grade:");
        label5.setFont(new Font("Arial",22));
        label5.setLayoutX(520); label5.setLayoutY(270);
        TextField t5 = new TextField();
        t5.setPrefHeight(30);
        t5.setPrefWidth(80);
        t5.setLayoutX(650);
        t5.setLayoutY(270);

        Label label4 = new Label("Gid:");
        label4.setFont((new Font("Arial",22)));
        label4.setLayoutX(520); label4.setLayoutY(70);
        TextField t4 = new TextField();
        t4.setPrefHeight(30);
        t4.setPrefWidth(80);
        t4.setLayoutX(650);
        t4.setLayoutY(70);

        Button add2Button = new Button("Ajouter");
        add2Button.setLayoutX(520);
        add2Button.setLayoutY(350);
        Button updateButton = new Button("mise à jour");
        updateButton.setLayoutX(600);
        updateButton.setLayoutY(350);
        Button del2Button = new Button("Supprimer");
        del2Button.setLayoutX(700);
        del2Button.setLayoutY(350);

        add2Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    int grade = 0;
                    if (!t5.getText().isEmpty() ) {
                        grade = Integer.parseInt(t5.getText());
                    }
                    groupDao.insertStuGroup(Integer.parseInt(t4.getText()),
                            MyUtils.capitalizeFirstLetter(t1.getText()),
                            MyUtils.capitalizeFirstLetter(t2.getText()),
                            MyUtils.capitalizeFirstLetter(t3.getText()), grade);
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Ajouter avec succès");
                    warning.setContentText("Ajouter avec succès");
                    warning.show();
                    onShowGroupClick();
                    binomeStage.close();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        binomepane.getChildren().addAll(label1, label2, label3, label4,label5,t1, t2, t3,t4,t5);

        //TODO
        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (groupTableView.getSelectionModel().getSelectedItem() == null) {
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Échec de la suppression");
                    warning.setContentText("Le contenu supprimé est vide");
                    warning.show();
                    return;
                }
                Alert alert = new Alert(Alert.AlertType.WARNING, "La suppression est-elle confirmée?",
                        new ButtonType("Annuler ", ButtonBar.ButtonData.CANCEL_CLOSE),
                        new ButtonType("Confirmer", ButtonBar.ButtonData.YES));
                alert.setTitle("Confirmer la suppression");
                alert.setHeaderText("Veuillez confirmer si vous souhaitez supprimer les résultats de l'étudiant");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
                } else {
                    try {
                        int grade = 0;
                        studentGroup = groupTableView.getSelectionModel().getSelectedItem();

                        if(!t1.getText().isEmpty()){
                            studentGroup.setProjectName(MyUtils.capitalizeFirstLetter(t1.getText()));
                        }
                        if(!t2.getText().isEmpty()){
                            studentGroup.setStudent1Name(MyUtils.capitalizeFirstLetter(t2.getText()));
                        }
                        if(!t3.getText().isEmpty()){
                            studentGroup.setStudent2Name(MyUtils.capitalizeFirstLetter(t3.getText()));
                        }
                        if(!t4.getText().isEmpty()){
                            studentGroup.setGroupId(Integer.parseInt(t4.getText()));
                        }
                        if (!t5.getText().isEmpty() ) {
                            grade = Integer.parseInt(t5.getText());
                            studentGroup.setProjectGrade(grade);
                        }
                        groupDao.updateGroup(studentGroup);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Supprimé avec succès");
                    warning.setContentText("Supprimé avec succès");
                    warning.show();
                    try {
                        onShowGroupClick();
                        binomeStage.close();
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

        del2Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (groupTableView.getSelectionModel().getSelectedItem() == null) {
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Échec de la suppression");
                    warning.setContentText("Le contenu supprimé est vide");
                    warning.show();
                    return;
                }
                Alert alert = new Alert(Alert.AlertType.WARNING, "La suppression est-elle confirmée?",
                        new ButtonType("Annuler ", ButtonBar.ButtonData.CANCEL_CLOSE),
                        new ButtonType("Confirmer", ButtonBar.ButtonData.YES));
                alert.setTitle("Confirmer la suppression");
                alert.setHeaderText("Veuillez confirmer si vous souhaitez supprimer les résultats de l'étudiant");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
//            System.out.println("Échec de la suppression");
                    return;
                } else {
                    try {
                        TableView<StudentGroup> bi = groupTableView;
                        StudentGroup studentGroup = bi.getSelectionModel().getSelectedItem();
                        studentGroup = studentGroup;
                        groupDao.deleteGroup(studentGroup);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Supprimé avec succès");
                    warning.setContentText("Supprimé avec succès");
                    warning.show();
                    try {
                        onShowGroupClick();
                        binomeStage.close();
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });


        //cellData1.clear();
        try (ResultSet rs = groupDao.searchAllGroup()) {
            while (rs.next()) {
                int groupListId = rs.getInt("groupListId");
                int groupId = rs.getInt("groupId");
                String projectName = rs.getString("projectName");
                String student1Name = rs.getString("student1Name");
                String student2Name = rs.getString("student2Name");
                int projectGrade = rs.getInt("projectGrade");
                StudentGroup studentGroup = new StudentGroup(groupListId, groupId, projectName, student1Name, student2Name, projectGrade);
                cellData2.add(studentGroup);

            }
        }
        grouplistid.setCellValueFactory(new PropertyValueFactory<>("groupListId"));
        groupid.setCellValueFactory(new PropertyValueFactory<>("groupId"));
        projectname.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        student1name.setCellValueFactory(new PropertyValueFactory<>("student1Name"));
        student2name.setCellValueFactory(new PropertyValueFactory<>("student2Name"));
        projectgrade.setCellValueFactory(new PropertyValueFactory<>("projectGrade"));
        groupTableView.setItems(cellData2);


        binomepane.getChildren().addAll(groupTableView, add2Button, updateButton, del2Button);

        Scene binomeScene = new Scene(binomepane, 800, 400);
        binomeStage.setScene(binomeScene);
        binomeStage.setResizable(false);
        binomeStage.setTitle("Groups List");
        binomeStage.show();
    }

    public void onShowProjectClick() throws SQLException,ClassNotFoundException{
        ObservableList<Project> cellData3 = FXCollections.observableArrayList();
        Stage projetStage = new Stage();
        Pane projetpane = new Pane();
        projectTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TableView<Project> pr = projectTable;
                delProject = pr.getSelectionModel().getSelectedItem();
            }
        });
        TableView<Project> projectTable = new TableView<>();
        Label placeholderLabel = new Label("Il n'y a aucun contenu dans le tableau");
        projectTable.setPlaceholder(placeholderLabel);
        TableColumn<Project,Integer> projectId = new TableColumn<>("Proid");
        TableColumn<Project,String> subjectName= new TableColumn<>("SName");
        TableColumn<Project,String> topic = new TableColumn<>("Topic");
        TableColumn<Project,Date> dueDate = new TableColumn<>("Duedate");
        projectTable.getColumns().addAll(projectId,subjectName,topic,dueDate);

        Label label1 = new Label("SName:");
        label1.setFont(new Font("Arial", 24));
        label1.setLayoutX(350);
        label1.setLayoutY(100);
        TextField t1 = new TextField();
        t1.setPrefHeight(30);
        t1.setPrefWidth(120);
        t1.setLayoutX(450);
        t1.setLayoutY(100);

        Label label2 = new Label("Topic:");
        label2.setFont(new Font("Arial", 24));
        label2.setLayoutX(350);
        label2.setLayoutY(150);
        TextField t2 = new TextField();
        t2.setPrefHeight(30);
        t2.setPrefWidth(120);
        t2.setLayoutX(450);
        t2.setLayoutY(150);

        Label label3 = new Label("Duedate:");
        label3.setFont(new Font("Arial", 24));
        label3.setLayoutX(350);
        label3.setLayoutY(200);
        TextField t3 = new TextField();
        t3.setPrefHeight(30);
        t3.setPrefWidth(120);
        t3.setLayoutX(450);
        t3.setLayoutY(200);

        Button add3Button = new Button("Ajouter");
        add3Button.setLayoutX(350);
        add3Button.setLayoutY(300);
        Button del3Button = new Button("Supprimer");
        del3Button.setLayoutX(490);
        del3Button.setLayoutY(300);

        add3Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    projectDao.insertProject(MyUtils.capitalizeFirstLetter(t1.getText()), t2.getText(), Date.valueOf(t3.getText()));
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Ajouter avec succès");
                    warning.setContentText("Ajouter avec succès");
                    warning.show();
                    onShowProjectClick();
                    projetStage.close();
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        projetpane.getChildren().addAll(label1,label2,label3,t1,t2,t3);

        del3Button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (projectTable.getSelectionModel().getSelectedItem() == null) {
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Échec de la suppression");
                    warning.setContentText("Le contenu supprimé est vide");
                    warning.show();
                    return;
                }
                Alert alert = new Alert(Alert.AlertType.WARNING, "La suppression est-elle confirmée?",
                        new ButtonType("Annuler ", ButtonBar.ButtonData.CANCEL_CLOSE),
                        new ButtonType("Confirmer", ButtonBar.ButtonData.YES));
                alert.setTitle("Confirmer la suppression");
                alert.setHeaderText("Veuillez confirmer si vous souhaitez supprimer les résultats de l'étudiant");
                Optional<ButtonType> buttonType = alert.showAndWait();
                if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
//            System.out.println("Échec de la suppression");
                    return;
                } else {
                    try {
                        Project project = projectTable.getSelectionModel().getSelectedItem();
                        delProject = project;
                        projectDao.delProject(delProject);
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Supprimé avec succès");
                    warning.setContentText("Supprimé avec succès");
                    warning.show();
                    try {
                        onShowProjectClick();
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

        ResultSet rs = projectDao.searchAllProject();
        while (rs.next()) {
            int projectIdOne = rs.getInt("projectId");
            String subjectNameOne = rs.getString("subjectName");
            String topicOne = rs.getString("topic");
            Date dueDateOne = rs.getDate("dueDate");
            Project project = new Project(projectIdOne,subjectNameOne,topicOne,dueDateOne);
            cellData3.add(project);
        }
        projectId.setCellValueFactory(new PropertyValueFactory<>("projectId"));
        subjectName.setCellValueFactory(new PropertyValueFactory<>("subjectName"));
        topic.setCellValueFactory(new PropertyValueFactory<>("topic"));
        dueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        projectTable.setItems(cellData3);

        projetpane.getChildren().addAll(projectTable,add3Button,del3Button);

        Scene projetScene = new Scene(projetpane,600,400);
        projetStage.setScene(projetScene);
        projetStage.setResizable(false);
        projetStage.setTitle("Projets List");
        projetStage.show();
    }
}



