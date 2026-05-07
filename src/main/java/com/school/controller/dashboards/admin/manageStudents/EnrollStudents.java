package com.school.controller.dashboards.admin.manageStudents;

import com.school.model.Student;
import com.util.SchoolDataManager;
import com.util.SchoolDataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class EnrollStudents {

    @FXML private AnchorPane enrollStudentPopUp;
    @FXML private Button closeButton;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button enrollAgainButton;
    @FXML private Label invalidMessage;

    // these are the input fields
    @FXML private TextField nameField;
    @FXML private ChoiceBox<String> departmentField;

    private double xOffset, yOffset;

    @FXML
    private void initialize() {

        enrollStudentPopUp.getStylesheets().add(
                getClass().getResource("/school/css/popUpWindows/popup-window.css").toExternalForm()
        );

        enrollStudentPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        enrollStudentPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) enrollStudentPopUp.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        departmentField.getItems().addAll("Software Engineering", "Artificial Intelligence", "Computer Science", "Cyber Security", "Information Technology");
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }

    public void manageEnrollStudent() {
        String name = nameField.getText();
        String department = departmentField.getValue();

        if (name.isEmpty()) {
            invalidMessage.setText("Please enter the Student Name!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }
        if (department == null) {
            invalidMessage.setText("Please select a Department!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }

        SchoolDataStore.students.add(new Student(name, department));
        SchoolDataManager.saveStudents(SchoolDataStore.students);

        invalidMessage.setText("Student enrolled Successfully!");
        invalidMessage.setTextFill(Color.LIGHTGREEN);

        // shows close button instead of other buttons
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        closeButton.setVisible(true);
        enrollAgainButton.setVisible(true);
    }

    public void handleEnrollAgainButton() {
        nameField.clear();
        departmentField.setValue(null);

        closeButton.setVisible(false);
        enrollAgainButton.setVisible(false);
        cancelButton.setVisible(true);
        confirmButton.setVisible(true);

        invalidMessage.setText("Enter the following information to enroll the Student");

    }
}
