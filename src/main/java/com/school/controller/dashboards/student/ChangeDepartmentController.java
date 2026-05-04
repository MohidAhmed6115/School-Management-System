package com.school.controller.dashboards.student;

import com.school.model.Student;
import com.school.util.DataManager;
import com.school.util.DataStore;
import com.school.util.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ChangeDepartmentController extends StudentController {

    // ── Department cards ──────────────────────────────────────────
    @FXML private VBox cardSE;
    @FXML private VBox cardAI;
    @FXML private VBox cardCS;
    @FXML private VBox cardCY;
    @FXML private VBox cardIT;

    // ── Status / feedback area ────────────────────────────────────
    @FXML private VBox statusBox;          // the whole feedback panel
    @FXML private HBox loadingRow;         // spinner + "Waiting…" text
    @FXML private Label loadingLabel;
    @FXML private Label statusLabel;       // shown after approval

    // Dot labels used for the animated ellipsis
    @FXML private Label dot1;
    @FXML private Label dot2;
    @FXML private Label dot3;

    // go back button
    @FXML private Button goBackButton;

    // Which department was clicked
    private String pendingDepartment;

    // Dots animation timeline (loop)
    private Timeline dotsTimeline;

    @FXML
    protected void initialize() {
        usernameLabel.setText(DataStore.currentUser.getName());
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));
        statusBox.setVisible(false);
        statusBox.setManaged(false);

        // Wire each card
        cardSE.setOnMouseClicked(e -> handleDepartmentSelected("Software Engineering"));
        cardAI.setOnMouseClicked(e -> handleDepartmentSelected("Artificial Intelligence"));
        cardCS.setOnMouseClicked(e -> handleDepartmentSelected("Computer Science"));
        cardCY.setOnMouseClicked(e -> handleDepartmentSelected("Cyber Security"));
        cardIT.setOnMouseClicked(e -> handleDepartmentSelected("Information Technology"));
    }

    // ── Department selection handler ──────────────────────────────

    private void handleDepartmentSelected(String department) {
        // Skip if same department
        if (DataStore.currentUser instanceof Student s) {
            if (department.equals(s.getDepartment())) {
                return;
            }
        }

        pendingDepartment = department;

        // Disable cards while processing
        setCardsDisabled(true);

        // Show status box
        statusBox.setVisible(true);
        statusBox.setManaged(true);
        loadingRow.setVisible(true);
        loadingRow.setManaged(true);
        statusLabel.setVisible(false);
        statusLabel.setManaged(false);
        loadingLabel.setText("Waiting for admin approval");

        // Start animated dots
        startDotsAnimation();

        // After 5 seconds → approve
        Timeline approvalTimer = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> onApproved())
        );
        approvalTimer.play();
    }

    private void onApproved() {
        stopDotsAnimation();

        // Hide loading row, show success label
        loadingRow.setVisible(false);
        loadingRow.setManaged(false);

        statusLabel.setText("✓  Request approved by admin!");
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);

        if (DataStore.currentUser instanceof Student s) {s.setDepartment(pendingDepartment);}
        DataManager.saveStudents(DataStore.students);

        // Re-enable cards after a brief pause
        Timeline reEnableTimer = new Timeline(
                new KeyFrame(Duration.seconds(2), e -> {
                    setCardsDisabled(false);
                    // Fade status box away
                    fadeOutStatusBox();
                })
        );
        reEnableTimer.play();
    }

    // ── Animated ellipsis ("…") ───────────────────────────────────

    private void startDotsAnimation() {
        dot1.setOpacity(0);
        dot2.setOpacity(0);
        dot3.setOpacity(0);

        dotsTimeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(dot1.opacityProperty(), 0),
                        new KeyValue(dot2.opacityProperty(), 0),
                        new KeyValue(dot3.opacityProperty(), 0)),
                new KeyFrame(Duration.millis(400),
                        new KeyValue(dot1.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(800),
                        new KeyValue(dot2.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(1200),
                        new KeyValue(dot3.opacityProperty(), 1)),
                new KeyFrame(Duration.millis(1600),
                        new KeyValue(dot1.opacityProperty(), 0),
                        new KeyValue(dot2.opacityProperty(), 0),
                        new KeyValue(dot3.opacityProperty(), 0))
        );
        dotsTimeline.setCycleCount(Timeline.INDEFINITE);
        dotsTimeline.play();
    }

    private void stopDotsAnimation() {
        if (dotsTimeline != null) {
            dotsTimeline.stop();
        }
    }

    // ── Fade out the status panel ─────────────────────────────────

    private void fadeOutStatusBox() {
        Timeline fade = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(statusBox.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(1), new KeyValue(statusBox.opacityProperty(), 0.0))
        );
        fade.setOnFinished(e -> {
            statusBox.setVisible(false);
            statusBox.setManaged(false);
            statusBox.setOpacity(1.0);   // reset for next use
        });
        fade.play();
    }

    // ── Helper ────────────────────────────────────────────────────

    private void setCardsDisabled(boolean disabled) {
        for (VBox card : new VBox[]{cardSE, cardAI, cardCS, cardCY, cardIT}) {
            card.setDisable(disabled);
            card.setOpacity(disabled ? 0.5 : 1.0);
        }
    }

    @FXML
    private void handleGoBack() throws IOException {
        SceneManager.loadScene(goBackButton, "/school/fxml/dashboards/student/student-dashboard.fxml");
    }
}