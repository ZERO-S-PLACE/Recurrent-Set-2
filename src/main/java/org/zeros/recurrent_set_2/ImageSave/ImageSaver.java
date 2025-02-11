package org.zeros.recurrent_set_2.ImageSave;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
@NoArgsConstructor
public class ImageSaver {
    public void saveAsPng(Image image,File file ) {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(bufferedImage, "png", file);
            System.out.println("Image saved successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save image.");
        }
    }
}
