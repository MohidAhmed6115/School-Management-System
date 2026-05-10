package com.util;

import javafx.animation.*;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class Tools {
    //  STRIPE
    public static void animateStripe(Region stripe) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            double progress = (System.currentTimeMillis() % 2500) / 2500.0;
            double eased = (1 - Math.cos(progress * Math.PI)) / 2;
            double p = eased * 120 - 10;
            stripe.setStyle(String.format(
                    "-fx-background-color: linear-gradient(to right, " +
                            "#16c79a %.2f%%, " +
                            "#16c79a %.2f%%, " +
                            "#caffef %.2f%%, " +
                            "#16c79a %.2f%%, " +
                            "#16c79a %.2f%%);",
                    p - 5, p + 8, p + 16, p + 24, p + 35
            ));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}