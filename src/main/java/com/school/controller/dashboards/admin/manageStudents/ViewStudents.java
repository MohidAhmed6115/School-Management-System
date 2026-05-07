package com.school.controller.dashboards.admin.manageStudents;

import com.school.model.Student;
import com.util.DataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewStudents {

    @FXML private AnchorPane viewStudentsPopUp;
    @FXML private TextField searchBar;
    @FXML private ChoiceBox<String> sortBy;
    @FXML private TreeTableView<Student> studentTreeView;
    @FXML private TreeTableColumn<Student, String> nameColumn;
    @FXML private TreeTableColumn<Student, Integer> sapIdColumn;
    @FXML private TreeTableColumn<Student, String> departmentColumn;
    @FXML private TreeTableColumn<Student, Double> cgpaColumn;
    @FXML private Button sortDirectionBtn;

    private TreeTableColumn.SortType currentDirection = TreeTableColumn.SortType.ASCENDING;
    private double xOffset, yOffset;

    @FXML
    private void initialize() {

        // allows the dragging of the pop-up
        viewStudentsPopUp.getStylesheets().add(
                getClass().getResource("/school/css/popUpWindows/popup-window.css").toExternalForm()
        );

        viewStudentsPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        viewStudentsPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) viewStudentsPopUp.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

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

        departmentColumn.setCellValueFactory(param -> {
            Student s = param.getValue().getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    s != null ? s.getDepartment() : ""
            );
        });

        cgpaColumn.setCellValueFactory(param -> {
            Student s = param.getValue().getValue();
            return new javafx.beans.property.SimpleDoubleProperty(
                    s != null ? s.getCgpa() : 0.0
            ).asObject();
        });

        // prevents the user to be able to re-order the columns
        studentTreeView.getColumns().forEach(col -> col.setReorderable(false));

        // rows
        viewAllStudents();

        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            checkStudentInfo();
        });

        // Put these choices in the ChoiceBox options
        sortBy.getItems().addAll("Name", "SAP ID", "Department", "CGPA");

        // Listen for selection changes
        sortBy.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldVal, newVal) -> applySort()
        );
    }

    @FXML
    private void handleClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
    }

    public void checkStudentInfo() {
        String enteredSearchText = searchBar.getText().trim().toLowerCase();

        if (enteredSearchText.isEmpty()) {
            viewAllStudents(); // if search is cleared, show all
            return;
        }

        TreeItem<Student> root = new TreeItem<>(null);
        root.setExpanded(true);

        for (Student student : DataStore.students) {
            if (student == null) continue;

            boolean matchesName = student.getName().toLowerCase().contains(enteredSearchText);
            boolean matchesSapId = String.valueOf(student.getSapId()).contains(enteredSearchText);

            if (matchesName || matchesSapId) {
                root.getChildren().add(new TreeItem<>(student));
            }
        }

        studentTreeView.setRoot(root);
        studentTreeView.setShowRoot(false);
    }

    public void viewAllStudents() {

        TreeItem<Student> root = new TreeItem<>(null);
        root.setExpanded(true);

        for (Student student : DataStore.students) {

            if (student == null) continue;

            root.getChildren().add(new TreeItem<>(student));
        }

        studentTreeView.setRoot(root);
        studentTreeView.setShowRoot(false);
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

    // Call this from BOTH the ChoiceBox listener AND the toggle button
    private void applySort() {
        String selected = sortBy.getValue();
        if (selected == null) return;

        TreeTableColumn<Student, ?> activeColumn = switch (selected) {
            case "Name"   -> nameColumn;
            case "SAP ID" -> sapIdColumn;
            case "Department" -> departmentColumn;
            case "CGPA" -> cgpaColumn;
            default       -> null;
        };

        if (activeColumn == null) return;

        activeColumn.setSortType(currentDirection);
        studentTreeView.getSortOrder().clear();
        studentTreeView.getSortOrder().add(activeColumn);
        studentTreeView.sort();
    }

}