package com.school.controller.dashboards.student;

import com.school.model.Student;
import com.school.model.result.Course;
import com.school.model.result.SemesterResult;
import com.util.DataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

import static com.util.DataStore.getStudentResult;

public class ResultCardController extends StudentController {

    @FXML private Label cgpaLabel;
    @FXML private Label studentNameLabel;
    @FXML private Label departmentLabel;
    @FXML private Accordion semesterAccordion;

    private int currentSemester;

    @FXML
    protected void initialize() {
        usernameLabel.setText(DataStore.currentUser.getName());
        studentNameLabel.setText(DataStore.currentUser.getName());
        if (DataStore.currentUser instanceof Student s) {
            departmentLabel.setText(s.getDepartment());
            cgpaLabel.setText(String.valueOf(s.getCgpa()));
            currentSemester = s.getCurrentSemester();
        }

        loadSemesters();
    }

    private void loadSemesters() {
        semesterAccordion.getPanes().clear();

        double[] semesterGpas = new double[currentSemester];
        String[][][] semesterCourses = new String[currentSemester][][];

        if (DataStore.currentUser instanceof Student student) {
            for (int i = 0; i < currentSemester; i++) {
                SemesterResult result = getStudentResult(student.getSapId(), i + 1);
                List<Course> courses = result.getCourses();

                semesterCourses[i] = new String[courses.size()][2];

                for (int j = 0; j < courses.size(); j++) {
                    semesterCourses[i][j][0] = courses.get(j).getCourseName();
                    semesterCourses[i][j][1] = courses.get(j).getGrade();
                }
                semesterGpas[i] = result.getGpa();
            }
        }

        for (int i = 0; i < semesterGpas.length; i++) {
            TitledPane pane = buildSemesterPane(i + 1, semesterGpas[i], semesterCourses[i]);
            semesterAccordion.getPanes().add(pane);
        }
    }

    private TitledPane buildSemesterPane(int semesterNum, double gpa, String[][] subjects) {

        // ── Header label shown on the collapsed row ──
        Label title = new Label(
                "  Semester " + semesterNum + "          GPA: " + String.format("%.2f", gpa)
        );
        title.setStyle("-fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold;");

        // ── Content shown when expanded ──
        VBox content = new VBox(0);
        content.getStyleClass().add("semester-content");

        // Table header row
        HBox header = new HBox();
        header.getStyleClass().add("subject-header-row");
        Label subjectHeader = new Label("Subject");
        subjectHeader.getStyleClass().add("subject-header-cell");
        subjectHeader.setPrefWidth(300);
        Label gradeHeader = new Label("Grade");
        gradeHeader.getStyleClass().add("subject-header-cell");
        gradeHeader.setPrefWidth(100);
        header.getChildren().addAll(subjectHeader, gradeHeader);
        content.getChildren().add(header);

        // Subject rows
        for (int i = 0; i < subjects.length; i++) {
            HBox row = new HBox();
            row.getStyleClass().add(i % 2 == 0 ? "subject-row" : "subject-row-alt");

            Label subjectLabel = new Label(subjects[i][0]);
            subjectLabel.getStyleClass().add("subject-cell");
            subjectLabel.setPrefWidth(300);

            Label gradeLabel = new Label(subjects[i][1]);
            gradeLabel.getStyleClass().add("grade-cell");
            gradeLabel.setPrefWidth(100);
            styleGrade(gradeLabel, subjects[i][1]);

            row.getChildren().addAll(subjectLabel, gradeLabel);
            content.getChildren().add(row);
        }

        TitledPane pane = new TitledPane();
        pane.setGraphic(title);
        pane.setText("");
        pane.setContent(content);
        pane.getStyleClass().add("semester-pane");
        pane.setAnimated(true);

        return pane;
    }

    // Colors the grade label based on the grade value
    private void styleGrade(Label gradeLabel, String grade) {
        gradeLabel.getStyleClass().add("grade-cell");
        switch (grade) {
            case "A", "A+" -> gradeLabel.getStyleClass().add("grade-a");
            case "A-" -> gradeLabel.getStyleClass().add("grade-a-minus");
            case "B+" -> gradeLabel.getStyleClass().add("grade-b-plus");
            case "B" -> gradeLabel.getStyleClass().add("grade-b");
            default -> gradeLabel.getStyleClass().add("grade-other");
        }
    }

    @FXML
    private void handleBack() throws IOException {
        SceneManager.loadScene(logoutButton, "/school/fxml/dashboards/student/student-dashboard.fxml");
    }
}