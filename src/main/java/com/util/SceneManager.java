package com.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
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

    public static <T> T openPopup(Node currentNode, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(currentNode.getScene().getWindow());

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            dialog.setScene(scene);

            Stage mainStage = (Stage) currentNode.getScene().getWindow();
            Parent backgroundRoot = mainStage.getScene().getRoot();
            GaussianBlur blur = new GaussianBlur(0);
            backgroundRoot.setEffect(blur);

            dialog.setOnShowing(e -> blur.setRadius(4));
            dialog.setOnHiding(e -> backgroundRoot.setEffect(null));

            T controller = loader.getController();
            dialog.showAndWait();
            return controller;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // in case you need to pass anything to controller
    public static <T> void openPopup(Node currentNode, String fxmlPath, java.util.function.Consumer<T> beforeShow) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Parent root = loader.load();

            Stage dialog = new Stage();
            dialog.initStyle(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(currentNode.getScene().getWindow());

            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);
            dialog.setScene(scene);

            Stage mainStage = (Stage) currentNode.getScene().getWindow();
            Parent backgroundRoot = mainStage.getScene().getRoot();
            GaussianBlur blur = new GaussianBlur(0);
            backgroundRoot.setEffect(blur);

            dialog.setOnShowing(e -> blur.setRadius(4));
            dialog.setOnHiding(e -> backgroundRoot.setEffect(null));

            if (beforeShow != null) beforeShow.accept(loader.getController());

            dialog.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}