package com.example.ruhan_2207088_gpa_calculator;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class SecondViewController {


    private static final Map<String, Double> GRADE_POINTS = Map.of(
            "A+", 4.0, "A", 3.75, "A-", 3.5,"B+", 3.25, "B", 3.00, "B-", 2.75, "C+", 2.5, "C", 2.25, "D", 2.00, "F", 0.00
    );

    private final List<Course> courseList = new ArrayList<>();


    @FXML private TextField totalCreditField;
    @FXML private TextField studentNameField;
    @FXML private TextField studentRollField;
    @FXML private TextField courseNameField;
    @FXML private TextField courseCodeField;
    @FXML private TextField courseCreditField;
    @FXML private TextField teacher1Field;
    @FXML private TextField teacher2Field;
    @FXML private ComboBox<String> gradeComboBox;
    @FXML private Button calculateGPAButton;
    @FXML private Label statusLabel;



    @FXML
    public void initialize() {
        // Populate and sort grades
        List<String> grades = new ArrayList<>(GRADE_POINTS.keySet());
        grades.sort(Comparator.comparingDouble(GRADE_POINTS::get).reversed());
        gradeComboBox.setItems(FXCollections.observableArrayList(grades));

        calculateGPAButton.setDisable(true);
        statusLabel.setText("Courses Added: 0 | Current Credit earned : 0.0 / 0.0");
    }

    @FXML
    private void addCourse(ActionEvent event) {
        try {

            String totalCreditStr = totalCreditField.getText().trim();
            String name = courseNameField.getText().trim();
            String code = courseCodeField.getText().trim();
            String creditStr = courseCreditField.getText().trim();
            String grade = gradeComboBox.getValue();
            String teacher1 = teacher1Field.getText().trim();
            String teacher2 = teacher2Field.getText().trim();


            if (totalCreditStr.isEmpty() || name.isEmpty() || code.isEmpty() || creditStr.isEmpty() || grade == null) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all Course fields");
                return;
            }

            double requiredTotal = Double.parseDouble(totalCreditField.getText().trim());
            double newCredit = Double.parseDouble(creditStr);
            double currentTotal = courseList.stream().mapToDouble(Course::getCredit).sum();


            if (currentTotal + newCredit > requiredTotal) {
                double remaining = requiredTotal - currentTotal;
                showAlert(Alert.AlertType.WARNING, "Credit Limit Reached",
                        String.format("Cannot add this course. You only have %.2f credits remaining.", remaining));
                return;
            }


            Course newCourse = new Course(code, name, newCredit, grade, teacher1, teacher2);
            courseList.add(newCourse);


            if (courseList.size() == 1) {
                totalCreditField.setDisable(true);
            }


            courseNameField.clear();
            courseCodeField.clear();
            courseCreditField.clear();
            teacher1Field.clear();
            teacher2Field.clear();
            gradeComboBox.setValue(null);

            checkCreditTotal();
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    String.format("Course '%s' (%.2f credits) added successfully!", code, newCredit));

        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Course Credit and total course credit must be valid numbers.");
        }
    }


    private void checkCreditTotal() {
        try {
            double requiredTotal = Double.parseDouble(totalCreditField.getText().trim());
            double currentTotal = courseList.stream().mapToDouble(Course::getCredit).sum();
            int courseCount = courseList.size();

            statusLabel.setText(String.format("Courses Added: %d | Current total Credit earned : %.2f / %.2f",
                    courseCount, currentTotal, requiredTotal));

            calculateGPAButton.setDisable(currentTotal != requiredTotal);


        } catch (NumberFormatException e) {
            statusLabel.setText("Courses Added: " + courseList.size() + " | Enter valid total credit!");
            calculateGPAButton.setDisable(true);
        }
    }

    @FXML
    private void calculateGPA(ActionEvent event) {
        String studentName = studentNameField.getText().trim();
        String studentRoll = studentRollField.getText().trim();
        if (studentName.isEmpty() || studentRoll.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter the Student Name and Roll.");
            return;
        }
        double totalPoints = 0;
        double totalCredits = 0;
        for (Course course : courseList) {
            double points = GRADE_POINTS.getOrDefault(course.getGrade(), 0.0);
            totalPoints += points * course.getCredit();
            totalCredits += course.getCredit();
        }
        double finalGPA = (totalCredits > 0) ? totalPoints / totalCredits : 0.0;


        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("result.fxml"));


            VBox root = loader.load();



            ResultController resultController = loader.getController();
            resultController.setResultData(studentName,
                    studentRoll,finalGPA, courseList);

            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("GPA Calculation Result");
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Loading Error", "Could not load result.fxml: " + e.getMessage());
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