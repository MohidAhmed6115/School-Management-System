package com.school.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

public class MainPageController {
    @FXML
    private Button signInButton;
    @FXML
    private WebView logoView;

    @FXML
    private void initialize() {
        String svg = """
                    <html>
                    <body style="margin:0;padding:0;background:#1a1a2e;">
                    <svg width="36" height="36" viewBox="0 0 36 36" xmlns="http://www.w3.org/2000/svg">
                      <circle cx="18" cy="18" r="17" fill="#16c79a" opacity="0.15"/>
                      <circle cx="18" cy="18" r="12" fill="none" stroke="#16c79a" stroke-width="1.5"/>
                      <text x="18" y="23" font-family="Georgia,serif" font-size="14"
                            font-weight="bold" fill="#16c79a" text-anchor="middle">R</text>
                    </svg>
                    </body></html>
                """;
        logoView.getEngine().loadContent(svg);
        logoView.setStyle("-fx-background-color: transparent;");
    }

    @FXML
    private void handleSignIn() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/school/fxml/login.fxml"));
            Parent root = loader.load();

            LoginController loginController = loader.getController();
            Stage mainStage = (Stage) signInButton.getScene().getWindow();
            loginController.setMainStage(mainStage);

            Stage popup = new Stage();
            popup.initStyle(StageStyle.TRANSPARENT);
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.setResizable(false);

            // IMPORTANT: wrap root in a StackPane with padding so shadow isn't clipped
            javafx.scene.layout.StackPane wrapper = new javafx.scene.layout.StackPane(root);
            wrapper.setPadding(new javafx.geometry.Insets(12)); // room for shadow
            wrapper.setStyle("-fx-background-color: transparent;");

            Scene scene = new Scene(wrapper);
            scene.setFill(Color.TRANSPARENT);
            popup.setScene(scene);

            // Apply rounded clip + drop shadow AFTER layout so dimensions are known
            root.applyCss();
            root.layout();

            double arcSize = 20;

            // Clip so children don't bleed outside rounded corners
            javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle();
            clip.setArcWidth(arcSize * 2);
            clip.setArcHeight(arcSize * 2);
            root.layoutBoundsProperty().addListener((obs, oldVal, newVal) -> {
                clip.setWidth(newVal.getWidth());
                clip.setHeight(newVal.getHeight());
            });
            // Set initial size too
            clip.setWidth(root.prefWidth(-1));
            clip.setHeight(root.prefHeight(-1));
            root.setClip(clip);

            // Drop shadow on the wrapper (not the clipped node — clip hides overflow)
            wrapper.setEffect(new javafx.scene.effect.DropShadow(
                    20,   // radius
                    0,
                    4, // offsetX, offsetY
                    javafx.scene.paint.Color.rgb(0, 0, 0, 0.45)
            ));

            // Blur background of main stage
            Parent backgroundRoot = mainStage.getScene().getRoot();
            GaussianBlur blur = new GaussianBlur(0);
            backgroundRoot.setEffect(blur);

            popup.setOnShowing(e -> blur.setRadius(4));
            popup.setOnHiding(e -> backgroundRoot.setEffect(null));

            popup.show();
        } catch (Exception e) {
            System.out.println("Could not open login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}