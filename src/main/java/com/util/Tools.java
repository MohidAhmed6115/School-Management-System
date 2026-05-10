package com.util;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.layout.Region;
import javafx.util.Duration;

import java.io.IOException;

public class Tools {
    public static void animateStripe(Region stripe) {
        Timeline timeline = new Timeline();

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(16), e -> {
            double progress = (System.currentTimeMillis() % 1500) / 1500.0;

            stripe.setStyle(
                    "-fx-background-color: linear-gradient(to right, " +
                            "#16c79a " + (progress * 100 - 10) + "%, " +
                            "#16c79a " + (progress * 100 + 5) + "%, " +
                            "#afffea " + (progress * 100 + 12) + "%, " +
                            "#16c79a " + (progress * 100 + 19) + "%, " +
                            "#16c79a " + (progress * 100 + 29) + "%" +
                            ");"
            );
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
