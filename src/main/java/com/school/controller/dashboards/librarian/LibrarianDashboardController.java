package com.school.controller.dashboards.librarian;

import com.school.model.Librarian;
import com.school.util.DataStore;
import com.school.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LibrarianDashboardController extends LibrarianController {
    @FXML
    public VBox manageTeacherButton;
    @FXML
    public VBox manageStudentButton;
    @FXML
    public VBox libraryButton;
    @FXML
    private Label totalStudentsLabel;
    @FXML
    private Label totalTeachersLabel;

    @FXML
    protected void initialize() {
        usernameLabel.setText(DataStore.currentUser.getName());
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));

    }

    @FXML
    private void handleLibrary() throws IOException {
        if (DataStore.currentUser instanceof Librarian) {
            SceneManager.loadScene(logoutButton, "/library/fxml/librarian-page.fxml");
        } else {
            SceneManager.loadScene(logoutButton, "/library/fxml/librarian-page.fxml");
        }
    }
}
