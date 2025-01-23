package org.zeros.recurrent_set_2.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@Component
public class MainPanelController implements Initializable {
    @FXML
    public AnchorPane topPane;
    @FXML
    public AnchorPane leftPane;
    @FXML
    public ScrollPane mainImageContainer;
    @FXML
    public AnchorPane bottomPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        // Create an empty image of size 400x400
        int width = 1000;
        int height = 800;
        WritableImage writableImage = new WritableImage(width, height);

        // Get the PixelWriter to modify pixels
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        // Draw a gradient
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double red = (double) x / width;
                double green = (double) y / height;
                double blue = 0.5;
                pixelWriter.setColor(x, y, new Color(red, green, blue, 1.0));
            }
        }

        // Create an ImageView to display the image
        ImageView imageView = new ImageView(writableImage);
        mainImageContainer.contentProperty().set(imageView);
    }
}
