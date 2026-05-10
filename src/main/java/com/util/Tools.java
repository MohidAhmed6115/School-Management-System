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
            stripe.setStyle(
                    "-fx-background-color: linear-gradient(to right, " +
                            "#16c79a " + (p - 5)  + "%, " +
                            "#16c79a " + (p + 8)  + "%, " +
                            "#caffef " + (p + 16) + "%, " +
                            "#16c79a " + (p + 24) + "%, " +
                            "#16c79a " + (p + 35) + "%" +
                            ");"
            );
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}