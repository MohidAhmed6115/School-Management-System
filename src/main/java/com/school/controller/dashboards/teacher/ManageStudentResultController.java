package com.school.controller.dashboards.teacher;

import com.school.model.Student;
import com.school.model.result.Course;
import com.school.model.result.SemesterResult;
import com.util.DataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.List;

public class ManageStudentResultController {

    @FXML private AnchorPane manageStudentResultPopUp;
    @FXML private Button closeButton;
    @FXML private Button cancelButton;
    @FXML private Button uploadResultButton;
    @FXML private Button uploadAnotherButton;
    @FXML private Label invalidMessage;

    // these are the input fields
    @FXML private TextField marksField;
    @FXML private ChoiceBox<String> studentNameField;
    @FXML private ChoiceBox<String> courseNameField;

    private double xOffset, yOffset;
    Student selected;
    int currentSemester;

    @FXML
    private void initialize() {
        manageStudentResultPopUp.getStylesheets().add(
                getClass().getResource("/school/css/popUpWindows/popup-window.css").toExternalForm()
        );

        manageStudentResultPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        manageStudentResultPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) manageStudentResultPopUp.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        for (Student student : DataStore.students) {
            studentNameField.getItems().add(student.getName());
        }

        studentNameField.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setCourseNames();
            currentSemester = selected.getCurrentSemester();
        });
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }

    // this selects the course names of the student selected in the choice box
    private void setCourseNames() {
        courseNameField.getItems().clear();

        String selectedName = studentNameField.getValue();
        if (selectedName == null) return;

        // Find the student by name to get their sapId
        selected = DataStore.students.stream()
                .filter(s -> s.getName().equals(selectedName))
                .findFirst()
                .orElse(null);

        if (selected == null) return;

        int sapId = selected.getSapId();

        // Now get their semester results
        SemesterResult result = DataStore.getStudentResult(sapId, 2);
        if (result == null) {
            System.out.println("No results found for this student");
            return;
        }

        List<Course> courses = result.getCourses();

        for (Course course : courses) {
            courseNameField.getItems().add(course.getCourseName());
        }
    }

    public void manageUploadResult() {
        String studentName = studentNameField.getValue();
        String courseName = courseNameField.getValue();
        int marks = -1;
        marks = Integer.parseInt(marksField.getText());

        if (studentName == null) {
            invalidMessage.setText("Please select a Student!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }
        if (courseName == null) {
            invalidMessage.setText("Please select a Course!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }
        if (marks == -1) {
            invalidMessage.setText("Please enter marks for the selected Course!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }
        if (marks < 0 || marks > 100) {
            invalidMessage.setText("Invalid Marks! Please enter a number between 0-100");
            invalidMessage.setTextFill(Color.RED);
            return;
        }

        DataStore.updateGrade(selected.getSapId(), currentSemester, courseName, marks);

        invalidMessage.setText("Result Uploaded Successfully!");
        invalidMessage.setTextFill(Color.LIGHTGREEN);

        // shows close button instead of other buttons
        cancelButton.setVisible(false);
        uploadResultButton.setVisible(false);
        closeButton.setVisible(true);
        uploadAnotherButton.setVisible(true);
    }

    public void handleUploadAgainButton() {
        studentNameField.setValue(null);
        courseNameField.setValue(null);
        marksField.clear();

        closeButton.setVisible(false);
        uploadAnotherButton.setVisible(false);
        cancelButton.setVisible(true);
        uploadResultButton.setVisible(true);

        invalidMessage.setText("Enter the following information to upload a student result");

    }
}
