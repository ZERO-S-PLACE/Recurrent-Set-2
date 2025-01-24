package org.zeros.recurrent_set_2.Controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.ImageGeneration.BoundaryGradientColors;
import org.zeros.recurrent_set_2.ImageGeneration.ImageGenerationController;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MainPanelController implements Initializable {
    @FXML
    public AnchorPane topPane;
    @FXML
    public AnchorPane leftPane;
    @FXML
    public ScrollPane mainImageContainer;
    @FXML
    public AnchorPane bottomPane;

    private final ImageGenerationController imageGenerationController;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
            ImageView imageView = new ImageView(imageGenerationController.getNewImage(RecurrentExpression.MANDELBROT, 2000, 1400));
            mainImageContainer.contentProperty().set(imageView);
    }
}
