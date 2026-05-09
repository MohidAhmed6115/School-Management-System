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
import java.util.ArrayList;

public class RemoveAnnouncementController {

    @FXML private StackPane removeAnnouncementPopUp;
    @FXML private Button closeButton;
    @FXML private Button confirmButton;
    @FXML private Label invalidMessage;

    @FXML private ListView<Announcements> announcementListView;

    private double xOffset, yOffset;
    private String type;

    @FXML
    private void initialize() {
        removeAnnouncementPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        removeAnnouncementPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) removeAnnouncementPopUp.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        // Apply rounded clip to the root so corners are truly clipped
        javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(340, 420);
        clip.setArcWidth(32);
        clip.setArcHeight(32);
        removeAnnouncementPopUp.setClip(clip);
    }

    public void setType(String type) {
        this.type = type;
        setAnnouncementListView();
    }

    private void setAnnouncementListView() {
        if (type.equals("student")) {
            announcementListView.getItems().addAll(SchoolDataStore.studentAnnouncements);
        }
        else if (type.equals("teacher")) {
            announcementListView.getItems().addAll(SchoolDataStore.teacherAnnouncements);
        }
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void manageRemoveAnnouncement() {
        Announcements selected = announcementListView.getSelectionModel().getSelectedItem();

        if (selected == null) {
            invalidMessage.setText("Please select an announcement to delete!");
            invalidMessage.setTextFill(Color.RED);
            return;
        }
        else {
            if (type.equals("student")) {
                SchoolDataStore.studentAnnouncements.remove(selected);
            SchoolDataManager.saveStudentAnnouncements(SchoolDataStore.studentAnnouncements);
            }
            else if (type.equals("teacher")) {
                SchoolDataStore.teacherAnnouncements.remove(selected);
            SchoolDataManager.saveTeacherAnnouncements(SchoolDataStore.teacherAnnouncements);
            }
            announcementListView.getItems().remove(selected);
        }

        invalidMessage.setText("Announcement removed successfully!");
        invalidMessage.setTextFill(Color.GREEN);
    }
}
