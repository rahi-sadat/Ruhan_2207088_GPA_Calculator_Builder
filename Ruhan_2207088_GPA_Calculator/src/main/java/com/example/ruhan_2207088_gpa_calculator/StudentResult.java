package com.example.ruhan_2207088_gpa_calculator;

public class StudentResult {
    private String name;
    private String roll;
    private double totalGpa;
    private double totalCredit;

    public StudentResult(String name, String roll, double totalGpa, double totalCredit) {
        this.name = name;
        this.roll = roll;
        this.totalGpa = totalGpa;
        this.totalCredit = totalCredit;
    }

    // --- Getters (Required by JavaFX TableView PropertyValueFactory) ---
    public String getName() {
        return name;
    }
    public String getRoll() {
        return roll;
    }
    public double getTotalGpa() {
        return totalGpa;
    }
    public double getTotalCredit() {
        return totalCredit;
    }

    // --- Setters (Optional, but good for data manipulation) ---
    public void setName(String name) {
        this.name = name;
    }
    public void setRoll(String roll) {
        this.roll = roll;
    }
    public void setTotalGpa(double totalGpa) {
        this.totalGpa = totalGpa;
    }
    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }
}
