package com.library.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.library.controller.book.BookIssue;
import com.library.controller.book.BookIssueController;
import com.library.controller.librarian.LibrarianFunctions;
import com.library.controller.librarian.SearchFunctions;
import com.library.controller.librarian.SearchFunctions.BookRecord;
import com.school.model.Admin;
import com.school.model.Student;
import com.util.DataStore;

import com.util.SceneManager;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class mainPageController implements Initializable {

    Stage main;

    // Creating table
    @FXML
    private TableView<SearchFunctions.BookRecord> table;
    // Columns
    TableColumn<SearchFunctions.BookRecord, String> colTitle = new TableColumn<>("Title");
    TableColumn<SearchFunctions.BookRecord, String> colAuthor = new TableColumn<>("Author");
    TableColumn<SearchFunctions.BookRecord, String> colCategory = new TableColumn<>("Category");
    TableColumn<SearchFunctions.BookRecord, String> colYear = new TableColumn<>("Year");
    TableColumn<SearchFunctions.BookRecord, String> colTotal = new TableColumn<>("Total Copies");
    TableColumn<SearchFunctions.BookRecord, String> colAvailable = new TableColumn<>("Available");

    @FXML
    Label lblDateTime;
    // Choice menu for Choosing to search from
    @FXML
    ChoiceBox<String> searchChoice;
    // Label for displaying stas under search button
    @FXML
    private Label lblTotal;
    // Text field for searching
    @FXML
    TextField searchBar;
    @FXML
    Button homeButton, themeToggle;

    private boolean isLightMode = false;

    // Array of options in the dropdown menu
    private String[] options = {"Title", "Author", "Lib Catalogue", "Year", "Category"};

    // Setting table each column width
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Load library books into memory so search works
        com.library.util.DataStore.loadAll();

        // Home button it directs user back to it's dashboard
        homeButton.setOnAction(e -> {
            try {
//                FXMLLoader loader;
//                Parent root;
                if (DataStore.currentUser instanceof Admin) {
                    SceneManager.loadScene(homeButton, "/school/fxml/dashboards/admin/admin-dashboard.fxml");
//                    loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/admin/admin-dashboard.fxml"));
//                    root = loader.load();
                } else if (DataStore.currentUser instanceof Student) {
                    SceneManager.loadScene(homeButton, "/school/fxml/dashboards/student/student-dashboard.fxml");
//                    loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/student/student-dashboard.fxml"));
//                    root = loader.load();
                } else {
                    SceneManager.loadScene(homeButton, "/school/fxml/dashboards/teacher/teacher-dashboard.fxml");
//                    loader = new FXMLLoader(getClass().getResource("/school/fxml/dashboards/teacher/teacher-dashboard.fxml"));
//                    root = loader.load();
                }
//                Stage stage = (Stage) homeButton.getScene().getWindow();
//                stage.setScene(new Scene(root));
            } catch (IOException e1) {
                System.out.println("Error: " + e1.getMessage());
            }

        });

        // Date and Time displaying on Nav Bar
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE,  dd MMM yyyy  |  hh:mm:ss a");
            lblDateTime.setText(now.format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

        // Theme toggle button
        themeToggle.setOnAction(e -> {
            isLightMode = !isLightMode;
            javafx.scene.Scene scene = themeToggle.getScene();
            scene.getStylesheets().clear();
            if (isLightMode) {
                scene.getStylesheets().add(getClass().getResource("/library/css/main-page-light.css").toExternalForm());
                themeToggle.setText("☀️");
            } else {
                scene.getStylesheets().add(getClass().getResource("/library/css/main-page.css").toExternalForm());
                themeToggle.setText("🌙");
            }
        });

        // Filling the Choice Box
        searchChoice.getItems().addAll(options);
        searchChoice.setValue("Title");

        // Connecting Table to Getters of BookRecord which is inside SearchFunctions
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalBooks"));
        colAvailable.setCellValueFactory(new PropertyValueFactory<>("availableBooks"));

        // custom factory settings and calling capitalizeCell() which is custom factory
        // settings
        colTitle.setCellFactory(col -> capitalizeCell());
        colAuthor.setCellFactory(col -> capitalizeCell());
        colCategory.setCellFactory(col -> capitalizeCell());

        // adding all every column to table
        table.getColumns().addAll(colTitle, colAuthor, colCategory, colYear, colTotal, colAvailable);

        // When user hit enter this code will run and e is the ActionEvent taking place
        // that will trigger this block of code known as lambda function
        // searchButton.setOnAction(e -> {
        //     // If search is empty
        //     if (searchChoice.getValue() == null || searchBar.getText().isEmpty()) {
        //         lblTotal.setText("Please enter a search term.");
        //         return;
        //     } else {
        //         SearchFunctions sf = new SearchFunctions();
        //         ArrayList<SearchFunctions.BookRecord> results = sf.search(searchBar.getText(),
        //                 searchChoice.getValue());
        //         // Updating the table
        //         table.setItems(FXCollections.observableArrayList(results));
        //         // Updating the stats
        //         int total = results.stream().mapToInt(b -> b.getTotalBooks()).sum();
        //         int available = results.stream().mapToInt(b -> b.getAvailableBooks()).sum();
        //         int borrowed = total - available;
        //         lblTotal.setText("Found: " + results.size() + " titles    "
        //                 + "Total copies: " + total + "    "
        //                 + "Available: " + available + "    "
        //                 + "Borrowed: " + borrowed);
        //     }
        // });
        // RealTime search
        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                table.getItems().clear();
                lblTotal.setText("");
                return;
            }
            refreshTable(newValue);
        });

        // Setting table width
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        colTitle.setPrefWidth(200);
        colAuthor.setPrefWidth(150);
        colCategory.setPrefWidth(100);
        colYear.setPrefWidth(80);
        colTotal.setPrefWidth(100);
        colAvailable.setPrefWidth(100);


        // table lambda method for selecting the row
        table.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                SearchFunctions.BookRecord selected = table.getSelectionModel().getSelectedItem();
                if (selected == null) {
                    return;
                } else if (selected.getAvailableBooks() == 0) {
                    lblTotal.setText("No copies available for this book");
                    return;
                }

                // Pop up appearance
                try {
                    String libCatalogue = BookIssue.changingBooksFile(selected.getTitle());
                    Parent root;
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/fxml/issue-book.fxml"));
                    root = loader.load();
                    Scene bookPopUp = new Scene(root);
                    BookIssueController popupController = loader.getController();

                    Stage popupStage = new Stage(); // new separate stage
                    popupStage.initModality(Modality.APPLICATION_MODAL); // blocks main window
                    popupStage.setTitle("Book Issue");
                    popupStage.setScene(bookPopUp);

                    // For displaying data to Issue Book Data controller
                    popupController.setBookData(selected.getTitle(), libCatalogue);

                    // For not title heading like minimze,maximize,close
                    // popupStage.initStyle(StageStyle.UNDECORATED);
                    popupStage.showAndWait();
                    if (popupController.confirmation()) {
                        String currentDay = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MMM/yyyy"));
                        String deadLine = LocalDate.now().plusDays(14).format(DateTimeFormatter.ofPattern("dd/MMM/yyyy"));
                        BookIssue.issueBook(selected.getTitle(), currentDay, libCatalogue, deadLine);
                        LibrarianFunctions.calcFine(LocalDate.parse(currentDay, DateTimeFormatter.ofPattern("dd/MMM/yyyy")), LocalDate.parse(deadLine, DateTimeFormatter.ofPattern("dd/MMM/yyyy")));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());

                }
            }

        });
    }

    // First letter UpperCase
    private TableCell<BookRecord, String> capitalizeCell() {
        return new TableCell<>() {

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    String[] words = item.split(" ");
                    StringBuilder result = new StringBuilder();
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            result.append(Character.toUpperCase(word.charAt(0)))
                                    .append(word.substring(1).toLowerCase())
                                    .append(" ");
                        }
                    }
                    setText(result.toString().trim());
                }
            }
        };
    }

    private void refreshTable(String querry) {
        if (searchBar.getText().isEmpty()) {
            return;
        }
        SearchFunctions sf = new SearchFunctions();
        ArrayList<SearchFunctions.BookRecord> results = sf.search(
                querry, searchChoice.getValue()
        );
        table.setItems(FXCollections.observableArrayList(results));

        int total = results.stream().mapToInt(b -> b.getTotalBooks()).sum();
        int available = results.stream().mapToInt(b -> b.getAvailableBooks()).sum();
        int borrowed = total - available;
        lblTotal.setText("Found: " + results.size() + " titles    "
                + "Total copies: " + total + "    "
                + "Available: " + available + "    "
                + "Borrowed: " + borrowed);
    }
}
