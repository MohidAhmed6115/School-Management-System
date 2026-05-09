package com.school.controller.dashboards.admin.announcements;

import com.school.model.Student;
import com.school.model.announcements.Announcements;
import com.util.SchoolDataManager;
import com.util.SchoolDataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddAnnouncementController {

    @FXML private StackPane addAnnouncementPopUp;
    @FXML private Button closeButton;
    @FXML private Button cancelButton;
    @FXML private Button confirmButton;
    @FXML private Button addAgainButton;
    @FXML private Label invalidMessage;

    // these are the input fields
    @FXML private TextField messageField;
    @FXML private DatePicker dateField;
    @FXML private Spinner<Integer> importanceField;

    private double xOffset, yOffset;
    private String type;

    @FXML
    private void initialize() {
        addAnnouncementPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        addAnnouncementPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) addAnnouncementPopUp.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        // Apply rounded clip to the root so corners are truly clipped
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(340, 420);
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        addAnnouncementPopUp.setClip(clip);

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 5);
        valueFactory.setValue(5);

        importanceField.setValueFactory(valueFactory);
    }

    public void setType(String type) {
        this.type = type;
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        stage.close();
    }

    public void manageAddAnnouncement() {
        String message = messageField.getText();
        LocalDate localDate = dateField.getValue();
        int importance = importanceField.getValue();

        if (message.isEmpty()) {
            invalidMessage.setText("Please enter the message!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }

        if (localDate == null) {
            invalidMessage.setText("Please enter a deadline!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }

        if (localDate.isBefore(LocalDate.now())) {
            invalidMessage.setText("Deadline has already passed!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy");
        String date = formatter.format(localDate);

        if (type.equals("student")) {
            SchoolDataStore.studentAnnouncements.add(new Announcements(importance, date, message));
            SchoolDataManager.saveStudentAnnouncements(SchoolDataStore.studentAnnouncements);
        }
        else if (type.equals("teacher")) {
            SchoolDataStore.teacherAnnouncements.add(new Announcements(importance, date, message));
            SchoolDataManager.saveTeacherAnnouncements(SchoolDataStore.teacherAnnouncements);
        }

        invalidMessage.setText("Announcement added successfully!");
        invalidMessage.setTextFill(Color.GREEN);

        // shows close button instead of other buttons
        cancelButton.setVisible(false);
        confirmButton.setVisible(false);
        closeButton.setVisible(true);
        addAgainButton.setVisible(true);
    }

    public void handleAddAgainButton() {
        messageField.clear();
        dateField.setValue(null);
        importanceField.getValueFactory().setValue(5);

        closeButton.setVisible(false);
        addAgainButton.setVisible(false);
        cancelButton.setVisible(true);
        confirmButton.setVisible(true);

        invalidMessage.setText("Enter the following information to add announcement");
    }
}
