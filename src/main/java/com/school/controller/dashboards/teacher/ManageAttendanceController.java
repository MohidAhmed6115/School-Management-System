package com.school.controller.dashboards.teacher;

import com.school.model.Student;
import com.school.model.attendance.StudentAttendance;
import com.util.SchoolDataStore;
import com.util.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.util.SchoolDataStore.getStudentAttendance;
import static com.util.SchoolDataStore.updateAttendance;

public class ManageAttendanceController extends TeacherController {

    @FXML private ChoiceBox<String> sortBy;
    @FXML private TreeTableView<Student> attendanceTreeView;
    @FXML private TreeTableColumn<Student, String> nameColumn;
    @FXML private TreeTableColumn<Student, Integer> sapIdColumn;
    @FXML private TreeTableColumn<Student, String> attendanceColumn;
    @FXML private TreeTableColumn<Student, Double> totalAttendanceColumn;
    @FXML public Button goBackButton;

    private TreeTableColumn.SortType currentDirection = TreeTableColumn.SortType.ASCENDING;
    LocalDate date = LocalDate.now();
    String todayStr = date.toString();

    @FXML
    public void initialize() {
        usernameLabel.setText(SchoolDataStore.currentUser.getName());
        dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));

        nameColumn.setCellValueFactory(param -> {
            Student s = param.getValue().getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    s != null ? s.getName() : ""
            );
        });

        sapIdColumn.setCellValueFactory(param -> {
            Student s = param.getValue().getValue();
            return new javafx.beans.property.SimpleIntegerProperty(
                    s != null ? s.getSapId() : 0
            ).asObject();
        });

        attendanceColumn.setCellValueFactory(param -> {
            Student s = param.getValue().getValue();
            if (s == null) return new javafx.beans.property.SimpleStringProperty("");

            StudentAttendance sa = getStudentAttendance(s.getSapId(), s.getCurrentSemester());
            if (sa == null) return new javafx.beans.property.SimpleStringProperty("No Record");

            String todayStatus = sa.getTodayStatus(todayStr);
            return new javafx.beans.property.SimpleStringProperty(
                    todayStatus != null ? todayStatus : "Not Marked"
            );
        });

        attendanceColumn.setCellFactory(col -> new TreeTableCell<Student, String>() {
            private final ChoiceBox<String> choiceBox = new ChoiceBox<>();

            {
                choiceBox.getItems().addAll("Full Attendance", "Half Attendance", "Absent");
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTreeTableRow().getItem() == null) {
                    setGraphic(null);
                    return;
                }

                // suppress action while setting value programmatically
                choiceBox.setOnAction(null);
                choiceBox.setValue(item != null ? item : "Not Marked");
                applyColor(choiceBox);

                choiceBox.setOnAction(e -> {
                    Student student = getTreeTableRow().getItem();
                    if (student != null && choiceBox.getValue() != null) {
                        updateAttendance(student.getSapId(), student.getCurrentSemester(), date, choiceBox.getValue());
                        applyColor(choiceBox);
                        attendanceTreeView.refresh();
                    }
                });

                setGraphic(choiceBox);
            }
        });

        totalAttendanceColumn.setCellValueFactory(param -> {
            Student s = param.getValue().getValue();
            if (s == null) return new javafx.beans.property.SimpleDoubleProperty(0).asObject();

            StudentAttendance sa = getStudentAttendance(s.getSapId(), s.getCurrentSemester());
            double percentage = (sa != null) ? sa.getAttendancePercentage() : 0.0;
            return new javafx.beans.property.SimpleDoubleProperty(percentage).asObject();
        });

        // this sets the width of each column
        nameColumn.prefWidthProperty().bind(attendanceTreeView.widthProperty().multiply(0.25));
        sapIdColumn.prefWidthProperty().bind(attendanceTreeView.widthProperty().multiply(0.16));
        attendanceColumn.prefWidthProperty().bind(attendanceTreeView.widthProperty().multiply(0.22));
        totalAttendanceColumn.prefWidthProperty().bind(attendanceTreeView.widthProperty().multiply(0.16));

        attendanceTreeView.getColumns().forEach(col -> col.setReorderable(false));

        viewAllStudents();

        sortBy.getItems().addAll("Name", "SAP ID", "Attendance", "Status");
        sortBy.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> applySort()
        );
    }

    private void applyColor(ChoiceBox<String> choiceBox) {
        choiceBox.getStyleClass().removeAll("choice-box-full", "choice-box-half", "choice-box-absent");
        if (choiceBox.getValue() == null) return;
        switch (choiceBox.getValue()) {
            case "Full Attendance" -> choiceBox.getStyleClass().add("choice-box-full");
            case "Half Attendance" -> choiceBox.getStyleClass().add("choice-box-half");
            case "Absent"          -> choiceBox.getStyleClass().add("choice-box-absent");
        }
    }

    public void viewAllStudents() {
        TreeItem<Student> root = new TreeItem<>(null);
        root.setExpanded(true);

        for (Student student : SchoolDataStore.students) {
            if (student == null) continue;
            root.getChildren().add(new TreeItem<>(student));
        }

        attendanceTreeView.setRoot(root);
        attendanceTreeView.setShowRoot(false);
    }

    private void applySort() {
        String selected = sortBy.getValue();
        if (selected == null) return;

        TreeTableColumn<Student, ?> activeColumn = switch (selected) {
            case "Name"   -> nameColumn;
            case "SAP ID" -> sapIdColumn;
            case "Attendance" -> totalAttendanceColumn;
            case "Status" -> attendanceColumn;
            default       -> null;
        };

        if (activeColumn == null) return;

        activeColumn.setSortType(currentDirection);
        attendanceTreeView.getSortOrder().clear();
        attendanceTreeView.getSortOrder().add(activeColumn);
        attendanceTreeView.sort();
    }

    @FXML
    protected void handleGoBack() throws IOException {
        SceneManager.loadScene(goBackButton, "/school/fxml/dashboards/teacher/teacher-dashboard.fxml");
    }
}