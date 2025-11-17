// Course.java
package com.example.ruhan_2207088_gpa_calculator;

public class Course {
    private String code;
    private String name; // New field
    private double credit;
    private String grade;
    private String teacher1; // New field
    private String teacher2; // New field

    // Updated Constructor
    public Course(String code, String name, double credit, String grade, String teacher1, String teacher2) {
        this.code = code;
        this.name = name;
        this.credit = credit;
        this.grade = grade;
        this.teacher1 = teacher1;
        this.teacher2 = teacher2;
    }

    // --- Getters ---
    // These are REQUIRED for the TableView to work

    public String getCode() { return code; }
    public String getName() { return name; }
    public double getCredit() { return credit; }
    public String getGrade() { return grade; }
    public String getTeacher1() { return teacher1; }
    public String getTeacher2() { return teacher2; }
}
