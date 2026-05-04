package com.school.controller.dashboards.admin.manageStudents;

import com.school.util.DataManager;
import com.school.util.DataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class RemoveStudents {

    @FXML private AnchorPane removeStudentPopUp;
    @FXML private Button closeButton;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button okButton;
    @FXML private Label invalidMessage;
    @FXML private Label textLabel;

    // these are the input fields
    @FXML private TextField nameField;
    @FXML private TextField sapIdField;

    // these are labels that will show student information
    @FXML private VBox studentInfo;
    @FXML private Label studentName;
    @FXML private Label studentSapId;
    @FXML private Label studentDepartment;
    @FXML private Label studentCGPA;

    private double xOffset, yOffset;
    private int studentIndex;

    @FXML
    private void initialize() {

        removeStudentPopUp.getStylesheets().add(
                getClass().getResource("/school/css/popUpWindows/popup-window.css").toExternalForm()
        );

        removeStudentPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        removeStudentPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) removeStudentPopUp.getScene().getWindow();
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

    public void checkStudentInfo() {
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
        for (int i = 0; i < DataStore.students.size(); i++) {
            if (!name.isEmpty() && DataStore.students.get(i).getName().toLowerCase().contains(name)) {
                nameFound = true;
            }
            if (sapId != 0 && DataStore.students.get(i).getSapId() == sapId) {
                sapIdFound = true;
            }

            if (nameFound || sapIdFound) {
                studentIndex = i;
                invalidMessage.setText("Student found!");
                displayStudentInfo();
                okButton.setVisible(false);
                confirmButton.setVisible(true);
                return;
            }
        }
        invalidMessage.setText("Student not found!");
        invalidMessage.setTextFill(Color.RED);
    }

    public void displayStudentInfo() {
        nameField.setVisible(false);
        sapIdField.setVisible(false);
        textLabel.setVisible(false);
        studentInfo.setVisible(true);

        studentName.setText(DataStore.students.get(studentIndex).getName());
        studentSapId.setText(String.valueOf(DataStore.students.get(studentIndex).getSapId()));
        studentDepartment.setText(String.valueOf(DataStore.students.get(studentIndex).getDepartment()));
        studentCGPA.setText(String.valueOf(DataStore.students.get(studentIndex).getCgpa()));
    }

    public void manageRemoveStudent() {
        DataStore.students.remove(studentIndex);
        DataManager.saveStudents(DataStore.students);

        invalidMessage.setText("Student Removed Successfully!");
        invalidMessage.setTextFill(Color.LIGHTGREEN);

        // shows close button instead of other buttons
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        closeButton.setVisible(true);
    }
}
