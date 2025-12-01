package com.example.ruhan_2207088_gpa_calculator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DataController {

    @FXML private TableView<StudentResult> studentTable;
    @FXML private TableColumn<StudentResult, String> rollColumn;
    @FXML private TableColumn<StudentResult, String> nameColumn;
    @FXML private TableColumn<StudentResult, Double> gpaColumn;
    @FXML private TableColumn<StudentResult, Double> creditColumn;


    private ObservableList<StudentResult> studentList = FXCollections.observableArrayList();


    @FXML
    public void initialize() {

        rollColumn.setCellValueFactory(new PropertyValueFactory<>("roll"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        gpaColumn.setCellValueFactory(new PropertyValueFactory<>("totalGpa"));
        creditColumn.setCellValueFactory(new PropertyValueFactory<>("totalCredit"));


        studentTable.setItems(studentList);


        loadStudentData();
    }

    private void loadStudentData() {
        try {
            List<StudentResult> fetchedResults = DatabaseManager.getAllStudentResults();


            studentList.clear();


            studentList.addAll(fetchedResults);

            if (studentList.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "No Data", "The database contains no student records yet.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to fetch student data: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void deleteRecord(ActionEvent event) {
        StudentResult selectedStudent = studentTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a student record to delete.");
            return;
        }

        Optional<ButtonType> result = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete the record for roll " + selectedStudent.getRoll() + "?",
                ButtonType.YES, ButtonType.NO).showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                DatabaseManager.deleteStudent(selectedStudent.getRoll());
                loadStudentData();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Record deleted successfully.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete record: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

  // edit record
    @FXML
    private void editRecord(ActionEvent event) {
        StudentResult selectedStudent = studentTable.getSelectionModel().getSelectedItem();

        if (selectedStudent == null) {
            showAlert(Alert.AlertType.WARNING, "Selection Required", "Please select a student record to edit.");
            return;
        }


        TextInputDialog dialog = new TextInputDialog(String.valueOf(selectedStudent.getTotalGpa()));
        dialog.setTitle("Edit GPA");
        dialog.setHeaderText("Editing Roll: " + selectedStudent.getRoll());
        dialog.setContentText("Enter New CGPA:");

        Optional<String> result = dialog.showAndWait();


        result.ifPresent(newGpaStr -> {
            try {
                double newGpa = Double.parseDouble(newGpaStr);
                if (newGpa < 0 || newGpa > 4.0) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "CGPA must be between 0.00 and 4.00.");
                    return;
                }

                System.out.println(newGpa);
                System.out.println(selectedStudent.getRoll());
                DatabaseManager.updateStudentGPA(selectedStudent.getRoll(), newGpa, selectedStudent.getTotalCredit());
                loadStudentData(); // Refresh the table
                showAlert(Alert.AlertType.INFORMATION, "Success", "CGPA updated successfully.");

            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid number for CGPA.");
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update record: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }


    @FXML
    private void backToResultView(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("result.fxml"));
            javafx.scene.Parent root = loader.load();

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GPA Calculation Result");
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Could not load result.fxml.");
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