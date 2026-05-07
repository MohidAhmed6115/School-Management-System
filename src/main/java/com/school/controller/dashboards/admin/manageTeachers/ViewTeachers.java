package com.school.controller.dashboards.admin.manageTeachers;

import com.school.model.Teacher;
import com.util.SchoolDataStore;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ViewTeachers {

    @FXML private AnchorPane viewTeachersPopUp;
    @FXML private TextField searchBar;
    @FXML private ChoiceBox<String> sortBy;
    @FXML private TreeTableView<Teacher> teacherTreeView;
    @FXML private TreeTableColumn<Teacher, String> nameColumn;
    @FXML private TreeTableColumn<Teacher, Integer> sapIdColumn;
    @FXML private TreeTableColumn<Teacher, Double> salaryColumn;
    @FXML private Button sortDirectionBtn;

    private TreeTableColumn.SortType currentDirection = TreeTableColumn.SortType.ASCENDING;
    private double xOffset, yOffset;

    @FXML
    private void initialize() {

        // allows the dragging of the pop-up
        viewTeachersPopUp.getStylesheets().add(
                getClass().getResource("/school/css/popUpWindows/popup-window.css").toExternalForm()
        );

        viewTeachersPopUp.setOnMousePressed(e -> {
            xOffset = e.getSceneX();
            yOffset = e.getSceneY();
        });

        viewTeachersPopUp.setOnMouseDragged(e -> {
            Stage stage = (Stage) viewTeachersPopUp.getScene().getWindow();
            stage.setX(e.getScreenX() - xOffset);
            stage.setY(e.getScreenY() - yOffset);
        });

        // columns FIRST (safer)
        nameColumn.setCellValueFactory(param -> {
            Teacher t = param.getValue().getValue();
            return new javafx.beans.property.SimpleStringProperty(
                    t != null ? t.getName() : ""
            );
        });

        sapIdColumn.setCellValueFactory(param -> {
            Teacher t = param.getValue().getValue();
            return new javafx.beans.property.SimpleIntegerProperty(
                    t != null ? t.getSapId() : 0
            ).asObject();
        });

        salaryColumn.setCellValueFactory(param -> {
            Teacher t = param.getValue().getValue();
            return new javafx.beans.property.SimpleDoubleProperty(
                    t != null ? t.getSalary() : 0.0
            ).asObject();
        });

        // prevents the user to be able to re-order the columns
        teacherTreeView.getColumns().forEach(col -> col.setReorderable(false));

        // rows
        viewAllTeachers();

        searchBar.textProperty().addListener((obs, oldVal, newVal) -> {
            checkTeacherInfo();
        });

        // Put these choices in the ChoiceBox options
        sortBy.getItems().addAll("Name", "SAP ID", "Salary");

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

    public void checkTeacherInfo() {
        String enteredSearchText = searchBar.getText().trim().toLowerCase();

        if (enteredSearchText.isEmpty()) {
            viewAllTeachers(); // if search is cleared, show all
            return;
        }

        TreeItem<Teacher> root = new TreeItem<>(null);
        root.setExpanded(true);

        for (Teacher teacher : SchoolDataStore.teachers) {
            if (teacher == null) continue;

            boolean matchesName = teacher.getName().toLowerCase().contains(enteredSearchText);
            boolean matchesSapId = String.valueOf(teacher.getSapId()).contains(enteredSearchText);

            if (matchesName || matchesSapId) {
                root.getChildren().add(new TreeItem<>(teacher));
            }
        }

        teacherTreeView.setRoot(root);
        teacherTreeView.setShowRoot(false);
    }

    public void viewAllTeachers() {

        TreeItem<Teacher> root = new TreeItem<>(null);
        root.setExpanded(true);

        for (Teacher teacher : SchoolDataStore.teachers) {

            if (teacher == null) continue;

            root.getChildren().add(new TreeItem<>(teacher));
        }

        teacherTreeView.setRoot(root);
        teacherTreeView.setShowRoot(false);
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

        TreeTableColumn<Teacher, ?> activeColumn = switch (selected) {
            case "Name"   -> nameColumn;
            case "SAP ID" -> sapIdColumn;
            case "Salary" -> salaryColumn;
            default       -> null;
        };

        if (activeColumn == null) return;

        activeColumn.setSortType(currentDirection);
        teacherTreeView.getSortOrder().clear();
        teacherTreeView.getSortOrder().add(activeColumn);
        teacherTreeView.sort();
    }

}