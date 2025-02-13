package org.zeros.recurrent_set_2.Util;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class ImageResizer {

    public static Image resizeImage(Image source, double scale) {
        int targetWidth = (int) (source.getWidth() /scale);
        int targetHeight = (int) (source.getHeight() /scale);
        WritableImage resizedImage = new WritableImage(targetWidth, targetHeight);
        PixelReader reader = source.getPixelReader();
        PixelWriter writer = resizedImage.getPixelWriter();

        for (int y = 0; y < targetHeight; y++) {
            for (int x = 0; x < targetWidth; x++) {
                writer.setArgb(x, y, reader.getArgb((int) (x * scale), (int) (y * scale)));
            }
        }
        return resizedImage;
    }
}
