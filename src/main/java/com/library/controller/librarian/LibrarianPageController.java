package com.library.controller.librarian;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.library.controller.book.BookIssue;
import com.library.controller.book.BookIssue.IssuedBookData;
import com.school.model.Admin;
import com.school.model.Librarian;
import com.util.DataStore;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LibrarianPageController implements Initializable {
    @FXML
    Button addBookButton, removeBookButton, returnBookButton, logoutButton;

    @FXML
    private TableView<IssuedBookData> table;

    TableColumn<IssuedBookData, String> colDate = new TableColumn<>("Date");
    TableColumn<IssuedBookData, String> colSAP = new TableColumn<>("Sap");
    TableColumn<IssuedBookData, String> colCatalogue = new TableColumn<>("Lib Catalogue");
    TableColumn<IssuedBookData, String> colTitle = new TableColumn<>("Title");
    TableColumn<IssuedBookData, String> colDeadLine = new TableColumn<>("Dead Line");

    @FXML
    public void logout(ActionEvent e) {
        try {
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Parent root = null;
            if (DataStore.currentUser instanceof Librarian) {

                root = FXMLLoader.load(getClass().getResource("/school/fxml/main-page.fxml"));
            } else if (DataStore.currentUser instanceof Admin) {

                root = FXMLLoader.load(getClass().getResource("/school/fxml/dashboards/admin/admin-dashboard.fxml"));
            }
            Scene scene = new Scene(root);
            // String css = this.getClass().getResource("/library/css/main-page.css").toExternalForm();
            // scene.getStylesheets().add(css);

            stage.setScene(scene);
        } catch (IOException except) {

            System.out.println(e);
        }

    }

    // Abstract method
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        com.library.util.DataStore.loadAll();

        addBookButton.setOnAction(event -> {
            try {

                Parent root = FXMLLoader.load(getClass().getResource("/library/fxml/add-book.fxml"));
                Scene addBookPopUp = new Scene(root);

                Stage popupStage = new Stage(); // new separate stage
                popupStage.initModality(Modality.APPLICATION_MODAL); // blocks main window
                popupStage.setTitle("Book Issue");
                popupStage.setScene(addBookPopUp);
                popupStage.showAndWait();
            } catch (IOException e) {
                System.out.println(e);
            }

        });

        removeBookButton.setOnAction(event -> {
            try {

                Parent root = FXMLLoader.load(getClass().getResource("/library/fxml/remove-book.fxml"));
                Scene addBookPopUp = new Scene(root);

                Stage popupStage = new Stage(); // new separate stage
                popupStage.initModality(Modality.APPLICATION_MODAL); // blocks main window
                popupStage.setTitle("Book Issue");
                popupStage.setScene(addBookPopUp);
                String css = this.getClass().getResource("/library/css/remove-book.css").toExternalForm();
                addBookPopUp.getStylesheets().add(css);

                popupStage.showAndWait();
            } catch (IOException e) {
                System.out.println(e);
            }
        });
        returnBookButton.setOnAction(event -> {


            try {

                Parent root = FXMLLoader.load(getClass().getResource("/library/fxml/return-book.fxml"));
                Scene addBookPopUp = new Scene(root);

                Stage popupStage = new Stage(); // new separate stage
                popupStage.initModality(Modality.APPLICATION_MODAL); // blocks main window
                popupStage.setTitle("Book Issue");
                popupStage.setScene(addBookPopUp);
                popupStage.showAndWait();
                table.setItems(FXCollections.observableArrayList(BookIssue.issuedList));
            } catch (IOException e) {
                System.out.println(e);
            }
        });

        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colCatalogue.setCellValueFactory(new PropertyValueFactory<>("libCatalogue"));
        colSAP.setCellValueFactory(new PropertyValueFactory<>("sap"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        colDeadLine.setCellValueFactory(new PropertyValueFactory<>("deadLine"));

        colTitle.setCellFactory(col -> capitalizeCell());
        colCatalogue.setCellFactory(col -> capitalizeCell());
        colDate.setCellFactory(col -> capitalizeCell());

        table.getColumns().addAll(colDate, colDeadLine, colSAP, colCatalogue, colTitle);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        colTitle.setPrefWidth(200);
        colDate.setPrefWidth(100);
        colCatalogue.setPrefWidth(180);
        colSAP.setPrefWidth(80);

        System.out.println("list size: " + BookIssue.issuedList.size());
        table.setItems(FXCollections.observableArrayList(BookIssue.issuedList));
    }

    // First Letter UpperCase
    private TableCell<IssuedBookData, String> capitalizeCell() {
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
}