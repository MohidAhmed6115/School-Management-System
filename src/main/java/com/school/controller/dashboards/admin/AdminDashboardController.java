package com.school.controller.dashboards.admin;

import com.school.util.DataStore;
import com.school.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminDashboardController extends AdminController {
    @FXML public VBox manageTeacherButton;
    @FXML public VBox manageStudentButton;
    @FXML public VBox libraryButton;
    @FXML private Label totalStudentsLabel;
    @FXML private Label totalTeachersLabel;

    @FXML
    protected void initialize() {
        usernameLabel.setText(DataStore.currentUser.getName());
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));

        totalStudentsLabel.setText(String.valueOf(DataStore.students.size()));
        totalTeachersLabel.setText(String.valueOf(DataStore.teachers.size()));
    }

    @FXML
    private void handleManageTeachers() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/admin/manageTeachers/manage-teachers.fxml");
    }

    @FXML
    private void handleManageStudents() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/admin/manageStudents/manage-students.fxml");
    }

    @FXML
    private void handleLibrary() throws IOException {
        SceneManager.loadScene(logoutButton, "/library/fxml/librarian-page.fxml");
    }
}
