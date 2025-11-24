
package com.example.ruhan_2207088_gpa_calculator;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ResultController {
    @FXML private Label studentNameLabel;
    @FXML private Label studentRollLabel;
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> nameColumn;
    @FXML private TableColumn<Course, String> codeColumn;
    @FXML private TableColumn<Course, Double> creditColumn;
    @FXML private TableColumn<Course, String> teacher1Column;
    @FXML private TableColumn<Course, String> teacher2Column;
    @FXML private TableColumn<Course, String> gradeColumn;


    @FXML private Label gpaLabel;

    @FXML
    private void addNewStudent(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("2nd-view.fxml"));
            AnchorPane root = loader.load();


            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GPA Builder");
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading 2nd-view.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void setResultData(String name,String roll,double gpa, List<Course> courses) {

        studentNameLabel.setText("Student Name: " + name);
        studentRollLabel.setText("Student Roll: " + roll);
        gpaLabel.setText(String.format("%.2f", gpa));



        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("code"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("credit"));
        teacher1Column.setCellValueFactory(new PropertyValueFactory<>("teacher1"));
        teacher2Column.setCellValueFactory(new PropertyValueFactory<>("teacher2"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));


        courseTable.setItems(FXCollections.observableArrayList(courses));
    }
}
