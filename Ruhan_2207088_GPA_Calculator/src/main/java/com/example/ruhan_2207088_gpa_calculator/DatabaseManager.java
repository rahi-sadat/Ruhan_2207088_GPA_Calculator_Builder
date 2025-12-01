package com.example.ruhan_2207088_gpa_calculator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {


    private static final String DB_FILENAME = "identifier.sqlite";
    private static final String DB_PATH = "database/";

    private static String DB_URL;


    static {
        try {
            DB_URL = "jdbc:sqlite:" + getAbsoluteDbPath();
            System.out.println("Database URL set to: " + DB_URL);
        } catch (Exception e) {

            System.err.println("FATAL: Could not resolve database path.");
            e.printStackTrace();
            DB_URL = "jdbc:sqlite:default.sqlite";
        }
    }


    private static String getAbsoluteDbPath() throws Exception {

        java.net.URL url = DatabaseManager.class.getResource("/" + DB_PATH + DB_FILENAME);

        if (url == null) {
            throw new Exception("Database file not found in resources: /" + DB_PATH + DB_FILENAME);
        }


        return new java.io.File(url.toURI()).getAbsolutePath();
    }



    private static Connection connect() throws SQLException {

        return DriverManager.getConnection(DB_URL);
    }


    public static void createTables() {

        String sqlStudents = "CREATE TABLE IF NOT EXISTS student_results (" +
                "roll TEXT PRIMARY KEY," +
                "name TEXT NOT NULL," +
                "total_gpa REAL NOT NULL," +
                "total_credit REAL NOT NULL" +
                ");";


        String sqlCourses = "CREATE TABLE IF NOT EXISTS student_courses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "student_roll TEXT NOT NULL," +
                "course_name TEXT NOT NULL," +
                "course_code TEXT NOT NULL," +
                "credit REAL NOT NULL," +
                "teacher1 TEXT," +
                "teacher2 TEXT," +
                "grade TEXT NOT NULL," +
                "FOREIGN KEY(student_roll) REFERENCES student_results(roll) ON DELETE CASCADE" +
                ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlStudents);
            stmt.execute(sqlCourses);
            System.out.println("Database tables created or already exist.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    // save and update student result
    public static void saveStudentResult(String name, String roll, double gpa, double totalCredits, List<Course> courses) throws SQLException {

        Connection conn = null;
        try {
            conn = connect();
            conn.setAutoCommit(false);


            String sqlUpsertStudent = "INSERT OR REPLACE INTO student_results (roll, name, total_gpa, total_credit) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlUpsertStudent)) {
                pstmt.setString(1, roll);
                pstmt.setString(2, name);
                pstmt.setDouble(3, gpa);
                pstmt.setDouble(4, totalCredits);
                pstmt.executeUpdate();
            }


            String sqlDeleteCourses = "DELETE FROM student_courses WHERE student_roll = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlDeleteCourses)) {
                pstmt.setString(1, roll);
                pstmt.executeUpdate();
            }


            String sqlInsertCourse = "INSERT INTO student_courses (student_roll, course_name, course_code, credit, teacher1, teacher2, grade) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertCourse)) {
                for (Course course : courses) {
                    pstmt.setString(1, roll);
                    pstmt.setString(2, course.getName());
                    pstmt.setString(3, course.getCode());
                    pstmt.setDouble(4, course.getCredit());
                    pstmt.setString(5, course.getTeacher1());
                    pstmt.setString(6, course.getTeacher2());
                    pstmt.setString(7, course.getGrade());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }


    public static List<StudentResult> getAllStudentResults() throws SQLException {
        String sql = "SELECT roll, name, total_gpa, total_credit FROM student_results";
        List<StudentResult> results = new ArrayList<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                StudentResult result = new StudentResult(
                        rs.getString("name"),
                        rs.getString("roll"),
                        rs.getDouble("total_gpa"),
                        rs.getDouble("total_credit")
                );
                results.add(result);
            }
        }
        return results;
    }


    public static void deleteStudent(String roll) throws SQLException {

        String sql = "DELETE FROM student_results WHERE roll = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roll);
            pstmt.executeUpdate();
        }
    }


    public static void updateStudentGPA(String roll, double newGpa, double existingCredit) throws SQLException {
        String sql = "UPDATE student_results SET total_gpa = ?, total_credit = ? WHERE roll = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, newGpa);
            pstmt.setDouble(2, existingCredit);
            pstmt.setString(3, roll);
            pstmt.executeUpdate();
            System.out.println("Database updated successfully.");
        }
    }
}