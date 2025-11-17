
package com.example.ruhan_2207088_gpa_calculator;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;

public class ResultController {
    @FXML private TableView<Course> courseTable;
    @FXML private TableColumn<Course, String> nameColumn;
    @FXML private TableColumn<Course, String> codeColumn;
    @FXML private TableColumn<Course, Double> creditColumn;
    @FXML private TableColumn<Course, String> teacher1Column;
    @FXML private TableColumn<Course, String> teacher2Column;
    @FXML private TableColumn<Course, String> gradeColumn;


    @FXML private Label gpaLabel;


    public void setResultData(double gpa, List<Course> courses) {


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
