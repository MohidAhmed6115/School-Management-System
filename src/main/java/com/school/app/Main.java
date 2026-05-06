package com.school.app;

import com.school.util.DataManager;
import com.school.util.DataStore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        DataManager.init();
        DataManager.initResultsDir();
        DataStore.loadAll();
        DataManager.saveAll();

        System.out.println("Hello World! Hello Mohid!");
        System.out.println("Hello World!");
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/school/fxml/main-page.fxml")
        );

        Image icon = new Image(Main.class.getResourceAsStream("/school/images/icon.png"));
        stage.getIcons().add(icon);

        System.out.println("Hello World");

        Scene scene = new Scene(loader.load());
        stage.setTitle("School Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}