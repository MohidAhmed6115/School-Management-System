package com.school.controller.dashboards.admin.manageTeachers;

import com.school.model.Teacher;
import com.util.SchoolDataManager;
import com.util.SchoolDataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class AddTeachers {

    @FXML private StackPane addTeacherPopUp;
    @FXML private Button closeButton;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Label invalidMessage;

    // these are the input fields
    @FXML private TextField nameField;
    @FXML private TextField salaryField;

    private double xOffset, yOffset;

    @FXML
    private void initialize() {

        addTeacherPopUp.getStylesheets().add(
                getClass().getResource("/school/css/dashboards/admin/manageTeachers/add-teachers.css").toExternalForm()
        );

        addTeacherPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        addTeacherPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) addTeacherPopUp.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }

    public void manageAddTeacher() {
        String name = nameField.getText();
        double salary;

        try {
            salary = Double.parseDouble(salaryField.getText());
        } catch (NumberFormatException e) {
            invalidMessage.setText("Invalid Input");
            invalidMessage.setTextFill(Color.RED);
            return;
        }

        SchoolDataStore.teachers.add(new Teacher(name, salary));
        SchoolDataManager.saveTeachers(SchoolDataStore.teachers);

        invalidMessage.setText("Teacher added Successfully!");
        invalidMessage.setTextFill(Color.GREEN);

        // shows close button instead of other buttons
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        closeButton.setVisible(true);
    }
}
