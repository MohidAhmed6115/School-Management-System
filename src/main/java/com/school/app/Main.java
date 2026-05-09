package com.school.app;

import com.util.SchoolDataManager;
import com.util.SchoolDataStore;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SchoolDataManager.init();
        SchoolDataManager.initResultsDir();
        SchoolDataStore.loadAll();
        SchoolDataManager.saveAll();

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/school/fxml/main-page.fxml")
        );

        Image icon = new Image(Main.class.getResourceAsStream("/school/images/logo.png"));
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