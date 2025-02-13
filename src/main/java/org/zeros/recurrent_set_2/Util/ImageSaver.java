package org.zeros.recurrent_set_2.Util;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ImageSaver {

    private final SettingsHolder settingsHolder;

    public void saveAsPng(Image image,File file ) {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            ImageIO.write(bufferedImage, "png", file);
            System.out.println("Image saved successfully: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save image.");
        }
    }

    public void saveAsJpg(Image image,File file ) {
        try {
            BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
            BufferedImage outputImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2d = outputImage.createGraphics();

            g2d.setColor(new Color(
                    (int) (settingsHolder.getColorSettings().getBackgroundColor().getRed()*255),
                    (int) (settingsHolder.getColorSettings().getBackgroundColor().getGreen()*255),
                    (int) settingsHolder.getColorSettings().getBackgroundColor().getBlue()*255));
            g2d.fillRect(0, 0, outputImage.getWidth(), outputImage.getHeight());
            g2d.drawImage(bufferedImage, 0, 0,null);
            if(ImageIO.write(outputImage, "jpg", file)) {
                System.out.println("Image saved successfully: " + file.getAbsolutePath());
            }else {
                System.err.println("Failed to save image.");
            }
        } catch (IOException e) {
            System.err.println("Failed to save image.");
        }
    }
}
