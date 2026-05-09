package com.school.controller.dashboards.admin.manageTeachers;

import com.util.SchoolDataManager;
import com.util.SchoolDataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RemoveTeachers {

    @FXML private StackPane removeTeacherPopUp;
    @FXML private Button closeButton;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button okButton;
    @FXML private Label invalidMessage;
    @FXML private Label textLabel;

    // these are the input fields
    @FXML private TextField nameField;
    @FXML private TextField sapIdField;

    // these are labels that will show teacher information
    @FXML private VBox teacherInfo;
    @FXML private Label teacherName;
    @FXML private Label teacherSapId;
    @FXML private Label teacherSalary;

    private double xOffset, yOffset;
    private int teacherIndex;

    @FXML
    private void initialize() {

        removeTeacherPopUp.getStylesheets().add(
                getClass().getResource("/school/css/dashboards/admin/manageTeachers/remove-teachers.css").toExternalForm()
        );

        removeTeacherPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        removeTeacherPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) removeTeacherPopUp.getScene().getWindow();
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

    public void checkTeacherInfo() {
        String name = nameField.getText().trim().toLowerCase();
        int sapId;

        try {
            sapId = Integer.parseInt(sapIdField.getText());
        } catch (NumberFormatException e) {
            sapId = 0;
        }

        if (name.isEmpty() && sapId == 0) {
            return; // returns if no field is entered
        }

        boolean nameFound = false, sapIdFound = false;
        for (int i = 0; i < SchoolDataStore.teachers.size(); i++) {
            if (!name.isEmpty() && SchoolDataStore.teachers.get(i).getName().toLowerCase().contains(name)) {
                nameFound = true;
            }
            if (sapId != 0 && SchoolDataStore.teachers.get(i).getSapId() == sapId) {
                sapIdFound = true;
            }

            if (nameFound || sapIdFound) {
                teacherIndex = i;
                invalidMessage.setText("Teacher found!");
                displayTeacherInfo();
                okButton.setVisible(false);
                confirmButton.setVisible(true);
                return;
            }
        }
        invalidMessage.setText("Teacher not found!");
        invalidMessage.setTextFill(Color.RED);
    }

    public void displayTeacherInfo() {
        nameField.setVisible(false);
        sapIdField.setVisible(false);
        textLabel.setVisible(false);
        teacherInfo.setVisible(true);

        teacherName.setText(SchoolDataStore.teachers.get(teacherIndex).getName());
        teacherSapId.setText(String.valueOf(SchoolDataStore.teachers.get(teacherIndex).getSapId()));
        teacherSalary.setText(String.valueOf(SchoolDataStore.teachers.get(teacherIndex).getSalary()));
    }

    public void manageRemoveTeacher() {
        SchoolDataStore.teachers.remove(teacherIndex);
        SchoolDataManager.saveTeachers(SchoolDataStore.teachers);

        invalidMessage.setText("Teacher Removed Successfully!");
        invalidMessage.setTextFill(Color.LIGHTGREEN);

        // shows close button instead of other buttons
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        closeButton.setVisible(true);
    }
}
