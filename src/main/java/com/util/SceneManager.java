package com.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class SceneManager {

    // Used when you already have a node on the target stage
    public static <T> T loadScene(Node currentNode, String fxmlPath) throws IOException {
        Stage currentStage = (Stage) currentNode.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(
                SceneManager.class.getResource(fxmlPath)
        );

        double currentWidth = currentStage.getScene().getWidth();
        double currentHeight = currentStage.getScene().getHeight();

        Scene scene = new Scene(loader.load(), currentWidth, currentHeight);
        currentStage.setScene(scene);
        return loader.getController();
    }

    // Used when you have the stage directly (e.g. from login popup)
    public static <T> T loadSceneOnStage(Stage stage, String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(
                SceneManager.class.getResource(fxmlPath)
        );

        double currentWidth = stage.getScene().getWidth();
        double currentHeight = stage.getScene().getHeight();

        Scene scene = new Scene(loader.load(), currentWidth, currentHeight);
        stage.setScene(scene);
        return loader.getController();
    }

    public static <T> T openPopup(String fxmlPath) throws Exception {
        FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
        Parent root = loader.load();

        Stage popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setResizable(false);
        popup.setScene(new Scene(root));
        popup.show();

        return loader.getController();
    }
}