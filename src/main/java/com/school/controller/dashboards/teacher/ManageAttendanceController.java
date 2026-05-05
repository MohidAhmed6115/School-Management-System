package com.school.controller.dashboards.teacher;

import com.school.model.Teacher;
import com.school.util.DataStore;
import com.school.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ManageAttendanceController extends TeacherController {
    @FXML
    public void initialize () {
        usernameLabel.setText(DataStore.currentUser.getName());
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
    }

}
