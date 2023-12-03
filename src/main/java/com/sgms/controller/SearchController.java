package com.sgms.controller;

import com.sgms.dao.BaseDao;
import com.sgms.dao.GroupDao;
import com.sgms.dao.ProjectDao;
import com.sgms.dao.StudentGradeDao;
import com.sgms.pojo.StudentGrade;
import com.sgms.pojo.UserInformation;
import com.sgms.utils.MyUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Optional;

public class SearchController {
    @FXML //stu_grade
    private TableView<StudentGrade> gradeTable = new TableView<>();
    @FXML
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
    TextField content;
    private StudentGrade delStu;


    private final ObservableList<StudentGrade> cellData = FXCollections.observableArrayList();
    //connexion à la base de données
    private BaseDao baseDao = new BaseDao();

    private UserInformation userInformation = UserInformation.getUser();

    private StudentGradeDao studentGradeDao = new StudentGradeDao();

    private GroupDao groupDao = new GroupDao();

    private ProjectDao projectDao = new ProjectDao();



    @FXML
    private void onSearchClick() throws SQLException, ClassNotFoundException {
        gradeTable.getItems().clear();
        gradeTable.refresh();
        cellData.clear();
        String keyword = content.getText();
        if (MyUtils.isEmpty(keyword)) {
            Dialog<ButtonType> warning = new Dialog<>();
            warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
            warning.setTitle("Les mots-clés sont vides");
            warning.setContentText("Les mots-clés ne peuvent pas être vides");
            warning.show();
        } else {
            if (userInformation.getJob().equals("enseignant")) {
                ResultSet rs = studentGradeDao.keywordSearchAll(keyword);
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
                    String fid = rs.getString("formid");
                    String java = rs.getString("java");
                    String sar = rs.getString("sar");
                    String marketing = rs.getString("marketing");
                    String ml = rs.getString("ml");
                    String name = rs.getString("name");
                    Integer id = rs.getInt("id");
                    StudentGrade studentGrade = new StudentGrade(date1, fid, javaFS, SarFS, MarketingFS, MlFS, name, id);
                    //Ajouter à la liste
                    cellData.add(studentGrade);
//                System.out.println(studentGrade.toString());
                }
                //S'il est vide, une fenêtre pop-up demande
                if (cellData.isEmpty()) {
                    //Fenêtre popup si le résultat est nul
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Échec de la recherche");
                    warning.setContentText("Le contenu de la recherche par mot-clé est vide");
                    warning.show();
                } else {
                    gradeTable.setEditable(true);
                    date.setCellValueFactory(new PropertyValueFactory<>("date"));
                    java.setCellValueFactory(new PropertyValueFactory<>("java"));
                    sar.setCellValueFactory(new PropertyValueFactory<>("sar"));
                    marketing.setCellValueFactory(new PropertyValueFactory<>("marketing"));
                    ml.setCellValueFactory(new PropertyValueFactory<>("ml"));
                    nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

                    //Ajouter des données à un tableau
                    gradeTable.setItems(cellData);
                    gradeTable.setVisible(true);
                }

                //Définir l'événement du bouton
                //Cliquer pour obtenir le contenu d'une cellule
                //Facilite la suppression ultérieure
                gradeTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        TableView<StudentGrade> sw = gradeTable;
                        delStu = sw.getSelectionModel().getSelectedItem();
//                        System.out.println(student);
                    }
                });

                //Définir la cellule éditable
                //Double cliquer pour modifier puis appeler la méthode dao pour mettre à jour les données de la base
                //La cellule doit être définie, sinon elle ne peut pas être modifiée.
                java.setCellFactory(TextFieldTableCell.forTableColumn());
                sar.setCellFactory(TextFieldTableCell.forTableColumn());
                marketing.setCellFactory(TextFieldTableCell.forTableColumn());
                ml.setCellFactory(TextFieldTableCell.forTableColumn());
                Callback<TableColumn<StudentGrade, String>, TableCell<StudentGrade, String>> cellFactory
                        = (TableColumn<StudentGrade, String> p) -> {
                    return new TableCell<>();
                };


                java.setOnEditCommit(
                        (TableColumn.CellEditEvent<StudentGrade, String> t) -> {
                            ((StudentGrade) t.getTableView().getItems().get(
                                    t.getTablePosition().getRow())
                            ).setJava(t.getNewValue());
                            System.out.println("Modifié avec succès");
                            try {
                                studentGradeDao.updateAllGrade(cellData);
                                this.onSearchClick();
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
                                this.onSearchClick();
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
                                this.onSearchClick();
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
                                this.onSearchClick();
                            } catch (SQLException | ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                        });

            } else {
                ResultSet rs = studentGradeDao.keywordSearch(keyword);
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
                    String fid = rs.getString("formid");
                    String java = rs.getString("java");
                    String sar = rs.getString("sar");
                    String marketing = rs.getString("marketing");
                    String ml = rs.getString("ml");
                    String name = rs.getString("name");
                    Integer id = rs.getInt("id");
                    StudentGrade studentGrade = new StudentGrade(date1, fid, javaFS, SarFS, MarketingFS, MlFS, name, id);
                    //Ajouter à la liste
                    cellData.add(studentGrade);
//                System.out.println(studentGrade.toString());
                }
                if (cellData.isEmpty()) {
                    Dialog<ButtonType> warning = new Dialog<>();
                    warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
                    warning.setTitle("Échec de la recherche");
                    warning.setContentText("Le contenu de la recherche par mot-clé est vide");
                    warning.show();
                    return;
                }
                gradeTable.setEditable(true);
                date.setCellValueFactory(new PropertyValueFactory<>("date"));
                java.setCellValueFactory(new PropertyValueFactory<>("java"));
                sar.setCellValueFactory(new PropertyValueFactory<>("sar"));
                marketing.setCellValueFactory(new PropertyValueFactory<>("marketing"));
                ml.setCellValueFactory(new PropertyValueFactory<>("ml"));
                nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
                gradeTable.setItems(cellData);
                gradeTable.setVisible(true);
            }
        }
    }

    @FXML
    void onDelClick() throws SQLException, ClassNotFoundException {

        if (gradeTable.getSelectionModel().getSelectedItem() == null) {
            Dialog<ButtonType> warning = new Dialog<>();
            warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
            warning.setTitle("Échec de la suppression");
            warning.setContentText("Le contenu de la suppression ne peut pas être vide");
            warning.show();
            return;
        }
        Alert alert = new Alert(Alert.AlertType.WARNING, "La suppression est-elle confirmée ?",
                new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE),
                new ButtonType("Confirmer", ButtonBar.ButtonData.YES));
        alert.setTitle("Confirmer la suppression");
        alert.setHeaderText("Veuillez confirmer si vous souhaitez supprimer les résultats de l'étudiant");
        Optional<ButtonType> buttonType = alert.showAndWait();
        if (buttonType.get().getButtonData().equals(ButtonBar.ButtonData.CANCEL_CLOSE)) {
            //Échec de la suppression
            return;
        } else {
            studentGradeDao.delstugrade(delStu);
            Dialog<ButtonType> warning = new Dialog<>();
            warning.getDialogPane().getButtonTypes().add(new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE));
            warning.setTitle("Supprimé avec succès");
            warning.setContentText("Supprimé avec succès");
            warning.show();
            onSearchClick();
        }
    }

}
