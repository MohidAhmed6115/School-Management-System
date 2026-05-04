package com.school.controller.dashboards.student;

import com.school.model.Student;
import com.school.util.DataStore;
import com.school.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class StudentDashboardController extends StudentController {

    @FXML public VBox checkResultButton;
    @FXML public VBox changeDepartmentButton;
    @FXML public VBox feeButton;
    @FXML public VBox libraryButton;
    @FXML private Label studentCgpa;
    @FXML private Label welcomeMessage;

    @FXML
    public void initialize () {
        usernameLabel.setText(DataStore.currentUser.getName());
        welcomeMessage.setText("Good morning, " + DataStore.currentUser.getName());
        if (DataStore.currentUser instanceof Student s) {
            studentCgpa.setText(String.valueOf(s.getCgpa()));
        }
    }

    @FXML
    private void handleCheckResult() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/student/result-card.fxml");
    }

    @FXML
    private void handleChangeDepartment() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/student/change-department.fxml");
    }

    @FXML
    private void handleFeeSystem() {

    }

    @FXML
    private void handleLibrary() throws IOException {
        SceneManager.loadScene(logoutButton, "/library/fxml/main-page.fxml");
    }
}
