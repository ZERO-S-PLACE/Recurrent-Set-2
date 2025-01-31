package org.zeros.recurrent_set_2.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.ImageGeneration.ImageGenerationController;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MainPanelController implements Initializable {

    @FXML
    public BorderPane mainImageContainer;

    public ImageView mainImage;

    private final ImageGenerationController imageGenerationController;
    private final SettingsHolder settingsHolder;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        mainImageContainer.setOnMouseClicked(event -> generateImage(RecurrentExpression.X_SHAPE));
        mainImageContainer.setBackground(new Background(new BackgroundFill(settingsHolder.getColorSettings().getBackgroundColor(), null, null)));
    }


    public void generateImage(RecurrentExpression recurrentExpression) {
        if (mainImage == null) {
            WritableImage writableImage=imageGenerationController.getNewImage(recurrentExpression,
                    (int) mainImageContainer.getWidth(),
                    (int) mainImageContainer.getHeight());
            mainImage = new ImageView(writableImage);
            mainImageContainer.setCenter(mainImage);
            mainImage.setPreserveRatio(true);
            BorderPane.setAlignment(mainImage, Pos.CENTER);
        } else {
            mainImage.setImage(imageGenerationController.getNewImage(recurrentExpression,
                    (int) mainImageContainer.getWidth(),
                    (int) mainImageContainer.getHeight()));
        }
    }
}

