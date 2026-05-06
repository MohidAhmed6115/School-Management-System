package com.school.controller;

import com.school.model.Admin;
import com.school.model.Librarian;
import com.school.model.Student;
import com.school.model.Teacher;
import com.school.util.DataStore;
import com.school.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private HBox loginPopUp;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField userPassword;
    @FXML
    private TextField userPasswordVisible;
    @FXML
    private Button togglePassword;

    private double xOffset, yOffset;
    private boolean passwordVisible = false;
    private Stage mainStage;

    @FXML
    public void initialize() {
        // Move stylesheet to FXML file to avoid duplicates
        // <VBox stylesheets="@/css/login.css" ...>

        userPasswordVisible.setVisible(false);
        userPasswordVisible.setManaged(false);

        loginPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        loginPopUp.setOnMouseDragged(e -> {
            Stage s = getStage();
            if (s != null) {
                s.setX(e.getScreenX() - xOffset);
                s.setY(e.getScreenY() - yOffset);
            }
        });
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    @FXML
    private void handleLogin() {
        String enteredId = usernameField.getText().trim();
//        String enteredPassword = passwordVisible
//                ? userPasswordVisible.getText().trim()
//                : userPassword.getText().trim();
        String enteredPassword = "abc123";

        if (enteredId.isEmpty() || enteredPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        try {
            if (tryLoginAsAdmin(enteredId, enteredPassword))
                return;
            if (tryLoginAsTeacher(enteredId, enteredPassword))
                return;
            if (tryLoginAsStudent(enteredId, enteredPassword))
                return;
            if (tryLoginAsLibrarian(enteredId, enteredPassword))
                return;
            showError("Invalid credentials. Please try again.");

        } catch (Exception e) {
            showError("Something went wrong.");
            e.printStackTrace(); // better than getMessage() for debugging
        }
    }

    private boolean tryLoginAsAdmin(String id, String password) throws Exception {
        for (Admin ad : DataStore.admins) {
            if (matches(ad.getSapId(), ad.getPassword(), id, password)) {
                DataStore.currentUser = ad;
                closePopup();
                SceneManager.loadSceneOnStage(mainStage, "/school/fxml/dashboards/admin/admin-dashboard.fxml");
                return true;
            }
        }
        return false;
    }

    private boolean tryLoginAsTeacher(String id, String password) throws Exception {
        for (Teacher t : DataStore.teachers) {
            if (matches(t.getSapId(), t.getPassword(), id, password)) {
                DataStore.currentUser = t;
                closePopup();
                SceneManager.loadSceneOnStage(mainStage, "/school/fxml/dashboards/teacher/teacher-dashboard.fxml");
                return true;
            }
        }
        return false;
    }

    private boolean tryLoginAsStudent(String id, String password) throws Exception {
        for (Student s : DataStore.students) {
            if (matches(s.getSapId(), s.getPassword(), id, password)) {
                DataStore.currentUser = s;
                closePopup();
                SceneManager.loadSceneOnStage(mainStage, "/school/fxml/dashboards/student/student-dashboard.fxml");
                return true;
            }
        }
        return false;
    }

    private boolean tryLoginAsLibrarian(String id, String password) throws Exception {
        for (Librarian l : DataStore.librarians) {
            if (matches(l.getSapId(), l.getPassword(), id, password)) {
                DataStore.currentUser = l;
                closePopup();
                SceneManager.loadSceneOnStage(mainStage,"/library/fxml/librarian-page.fxml");
                return true;
            }
        }
        return false;
    }

    // Centralized credential check
    private boolean matches(int sapId, String storedPassword, String enteredId, String enteredPassword) {
        return String.valueOf(sapId).equals(enteredId) && storedPassword.equals(enteredPassword);
    }

    @FXML
    private void handleTogglePassword() {
        passwordVisible = !passwordVisible;
        if (passwordVisible) {
            userPasswordVisible.setText(userPassword.getText());
            userPassword.setVisible(false);
            userPassword.setManaged(false);
            userPasswordVisible.setVisible(true);
            userPasswordVisible.setManaged(true);
            togglePassword.setText("🙈");
        } else {
            userPassword.setText(userPasswordVisible.getText());
            userPasswordVisible.setVisible(false);
            userPasswordVisible.setManaged(false);
            userPassword.setVisible(true);
            userPassword.setManaged(true);
            togglePassword.setText("👁");
        }
    }

    @FXML
    private void handleCancel() {
        closePopup();
    }

    private void closePopup() {
        Stage popup = getStage();
        if (popup != null)
            popup.close();
    }

    private void showError(String message) {
        errorLabel.setText(message);
    }

    private Stage getStage() {
        if (loginPopUp.getScene() == null)
            return null;
        return (Stage) loginPopUp.getScene().getWindow();
    }
}