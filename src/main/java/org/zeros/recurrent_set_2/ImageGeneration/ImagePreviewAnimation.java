package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@AllArgsConstructor
@Setter
public class ImagePreviewAnimation extends AnimationTimer {
    private  WritableImage image;
    private  Canvas canvas;
    private double xOffset=0;
    private double yOffset=0;
    private boolean fillWithImage=false;
    private double resizedImageWidth;
    private double resizedImageHeight;

    public void updateOffset(Point2D offset) {
        this.xOffset=offset.getX();
        this.yOffset=offset.getY();
    }

    @Override
    public void start() {
        if(image!=null&&canvas!=null) {
            if(fillWithImage){
                findResizedImageSize();
            }
            super.start();
        }
    }

    private void findResizedImageSize() {
        double xScale=canvas.getWidth()/image.getWidth();
        double yScale=canvas.getHeight()/image.getHeight();
        if(xScale<yScale){
            resizedImageWidth=canvas.getWidth();
            resizedImageHeight=image.getHeight()*xScale;
        }else {
            resizedImageWidth=image.getWidth()*yScale;
            resizedImageHeight=canvas.getHeight();
        }
        xOffset=(canvas.getWidth()-resizedImageWidth)/2;
        yOffset=(canvas.getHeight()-resizedImageHeight)/2;
    }

    @Override
    public void handle(long now) {
        if(fillWithImage) {
                Image resizedImage=resizeImage(image, (int) resizedImageWidth, (int) resizedImageHeight);
                canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                canvas.getGraphicsContext2D().drawImage(resizedImage, xOffset,yOffset);

        }else {
        canvas.getGraphicsContext2D().clearRect(xOffset, yOffset, image.getWidth(), image.getHeight());
        canvas.getGraphicsContext2D().drawImage(image, xOffset, yOffset);
        }
    }

    private Image resizeImage(Image source, int targetWidth, int targetHeight) {
        WritableImage resizedImage = new WritableImage(targetWidth, targetHeight);
        PixelReader reader = source.getPixelReader();
        PixelWriter writer = resizedImage.getPixelWriter();

        double scaleX = source.getWidth() / targetWidth;
        double scaleY = source.getHeight() / targetHeight;

        for (int y = 0; y < targetHeight; y++) {
            for (int x = 0; x < targetWidth; x++) {
                writer.setArgb(x, y, reader.getArgb((int) (x * scaleX), (int) (y * scaleY)));
            }
        }
        return resizedImage;
    }
}
