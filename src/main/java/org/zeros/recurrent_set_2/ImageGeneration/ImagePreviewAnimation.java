package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.zeros.recurrent_set_2.Util.ImageResizer;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
public class ImagePreviewAnimation extends AnimationTimer {
    private WritableImage image;
    private Canvas canvas;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean fillWithImage = false;
    private double resizedImageWidth;
    private double resizedImageHeight;

    @Override
    public void start() {
        if (image != null && canvas != null) {
            if (fillWithImage) {
                findResizedImageSize();
            }
            super.start();
        }
    }

    @Override
    public void handle(long now) {
        if (fillWithImage) {
            Image resizedImage = ImageResizer.resizeImage(image, image.getWidth() / resizedImageWidth);
            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            canvas.getGraphicsContext2D().drawImage(resizedImage, xOffset, yOffset);

        } else {
            canvas.getGraphicsContext2D().clearRect(xOffset, yOffset, image.getWidth(), image.getHeight());
            canvas.getGraphicsContext2D().drawImage(image, xOffset, yOffset);
        }
    }

    private void findResizedImageSize() {
        double xScale = canvas.getWidth() / image.getWidth();
        double yScale = canvas.getHeight() / image.getHeight();
        if (xScale < yScale) {
            resizedImageWidth = canvas.getWidth();
            resizedImageHeight = image.getHeight() * xScale;
        } else {
            resizedImageWidth = image.getWidth() * yScale;
            resizedImageHeight = canvas.getHeight();
        }
        xOffset = (canvas.getWidth() - resizedImageWidth) / 2;
        yOffset = (canvas.getHeight() - resizedImageHeight) / 2;
    }

    public void updateOffset(Point2D offset) {
        this.xOffset = offset.getX();
        this.yOffset = offset.getY();
    }

}
