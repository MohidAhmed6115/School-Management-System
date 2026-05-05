package com.school.controller.dashboards.teacher;

import com.school.model.Student;
import com.school.model.Teacher;
import com.school.model.User;
import com.school.util.DataStore;
import com.school.util.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.school.util.DataStore.getStudentAttendance;
import static com.school.util.DataStore.updateAttendance;

public class ManageAttendanceController extends TeacherController {

    @FXML private ChoiceBox<String> sortBy;
    @FXML private TreeTableView<Student> attendanceTreeView;
    @FXML private TreeTableColumn<Student, String> nameColumn;
    @FXML private TreeTableColumn<Student, Integer> sapIdColumn;
    @FXML private TreeTableColumn<Student, String> attendanceColumn;
    @FXML private TreeTableColumn<Student, Double> totalAttendanceColumn;
    @FXML private Button sortDirectionBtn;

    private TreeTableColumn.SortType currentDirection = TreeTableColumn.SortType.ASCENDING;
    LocalDate date = LocalDate.now();

    @FXML
    public void initialize () {
        usernameLabel.setText(DataStore.currentUser.getName());
        dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM d yyyy")));

        // columns FIRST (safer)
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

        attendanceColumn.setCellFactory(col -> new TreeTableCell<Student, String>() {
            private final ChoiceBox<String> choiceBox = new ChoiceBox<>();

            {
                // runs once when cell is created
                choiceBox.getItems().addAll("Full Attendance", "Half Attendance", "Absent");

                choiceBox.setOnAction(e -> {
                    Student student = getTreeTableRow().getItem();
                    if (student != null) {
                        updateAttendance(student.getSapId(), student.getCurrentSemester(), date, choiceBox.getValue());
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    choiceBox.setValue(item);
                    setGraphic(choiceBox);
                }
            }
        });

        totalAttendanceColumn.setCellValueFactory(param -> {
            Student s = param.getValue().getValue();
            return new javafx.beans.property.SimpleDoubleProperty(
                    s != null ? getStudentAttendance(s.getSapId(), s.getCurrentSemester()).getAttendancePercentage() : 0
            ).asObject();
        });

        // prevents the user to be able to re-order the columns
        attendanceTreeView.getColumns().forEach(col -> col.setReorderable(false));

        // rows
        viewAllStudents();

        // Put these choices in the ChoiceBox options
        sortBy.getItems().addAll("Name", "SAP ID", "Department", "CGPA");

        // Listen for selection changes
        sortBy.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> applySort()
        );
    }

    public void viewAllStudents() {

        TreeItem<Student> root = new TreeItem<>(null);
        root.setExpanded(true);

        for (Student student : DataStore.students) {

            if (student == null) continue;

            root.getChildren().add(new TreeItem<>(student));
        }

        attendanceTreeView.setRoot(root);
        attendanceTreeView.setShowRoot(false);
    }

    @FXML
    private void handleSortDirection() {

        // Flip the direction
        if (currentDirection == TreeTableColumn.SortType.ASCENDING) {
            currentDirection = TreeTableColumn.SortType.DESCENDING;
            sortDirectionBtn.setText("↓ DESC");
        } else {
            currentDirection = TreeTableColumn.SortType.ASCENDING;
            sortDirectionBtn.setText("↑ ASC");
        }

        // Apply direction to whichever column is currently active
        applySort();
    }

    private void applySort() {
        String selected = sortBy.getValue();
        if (selected == null) return;

        TreeTableColumn<Student, ?> activeColumn = switch (selected) {
            case "Name"   -> nameColumn;
            case "SAP ID" -> sapIdColumn;
            default       -> null;
        };

        if (activeColumn == null) return;

        activeColumn.setSortType(currentDirection);
        attendanceTreeView.getSortOrder().clear();
        attendanceTreeView.getSortOrder().add(activeColumn);
        attendanceTreeView.sort();
    }


}
