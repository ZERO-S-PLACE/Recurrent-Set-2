package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
public class ImagePreviewAnimation extends AnimationTimer {
    private final Image image;
    private final Canvas canvas;
    private double xOffset=0;
    private double yOffset=0;

    public void updateOffset(Point2D offset) {
        this.xOffset=offset.getX();
        this.yOffset=offset.getY();
    }

    @Override
    public void handle(long now) {
        canvas.getGraphicsContext2D().clearRect(xOffset, yOffset, image.getWidth(), image.getHeight());
        canvas.getGraphicsContext2D().drawImage(image, xOffset, yOffset);
    }
}
