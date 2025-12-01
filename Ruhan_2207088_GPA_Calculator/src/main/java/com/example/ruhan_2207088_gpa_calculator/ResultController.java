
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
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
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
    private String currentStudentName;
    private String currentStudentRoll;
    private double currentFinalGPA;
    private List<Course> currentCourseList;






    public void setResultData(String name,String roll,double gpa, List<Course> courses) {


        this.currentStudentName = name;
        this.currentStudentRoll = roll;
        this.currentFinalGPA = gpa;
        this.currentCourseList = courses;


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



    @FXML
    private void saveToDatabase(ActionEvent event) {
        if (currentCourseList == null || currentStudentRoll == null || currentCourseList.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Save Error", "No calculated data is available to save.");
            return;
        }

        double totalCredits = currentCourseList.stream().mapToDouble(Course::getCredit).sum();

        try {

            DatabaseManager.saveStudentResult(
                    currentStudentName,
                    currentStudentRoll,
                    currentFinalGPA,
                    totalCredits,
                    currentCourseList
            );
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    String.format("Data for %s (Roll: %s) saved/updated successfully!",
                            currentStudentName, currentStudentRoll));
        } catch (SQLException e) {

            String message = (e.getMessage() != null && e.getMessage().contains("UNIQUE constraint failed"))
                    ? "Record already exists for this roll number. Data has been updated."
                    : "Database error: " + e.getMessage();
            showAlert(Alert.AlertType.ERROR, "Database Save Failed", message);
            e.printStackTrace();
        }
    }



    @FXML
    private void showDataView(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("data-view.fxml"));

            AnchorPane root = loader.load();



            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Student Records Management");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Could not load data-view.fxml. Check the FXML file name and location.");
            e.printStackTrace();
        }
    }



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


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
