package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorCreator;
import org.zeros.recurrent_set_2.Model.ViewLocation;

@Component
@Slf4j
@RequiredArgsConstructor
public class ImageForExportGenerationController {
    private final SettingsHolder settingsHolder;
    private final ExpressionCalculatorCreator calculatorCreator;
    @Getter
    private final DoubleProperty progressProperty = new SimpleDoubleProperty(1);
    @Getter
    private final DoubleProperty generationTimeProperty = new SimpleDoubleProperty(0);
    @Setter
    private Canvas previewCanvas;
    private ImageGenerator imageGenerator;


    public Image generateNewImageExport(ViewLocation viewLocation) {

        WritableImage image = new WritableImage(
                settingsHolder.getApplicationSettings().getExportWidth(),
                settingsHolder.getApplicationSettings().getExportHeight());

        int iterations = settingsHolder.getApplicationSettings().getIterationsExport();

        imageGenerator = new ImageGeneratorChunks(
                settingsHolder,
                calculatorCreator,
                viewLocation,
                image,
                iterations);

        if (previewCanvas != null) {
            imageGenerator.addImageGenerationPreview(previewCanvas);
            imageGenerator.getImageGenerationPreview().setFillWithImage(true);
        }

        bindGenerationProgressProperties();
        imageGenerator.generateImage();
        unbindGenerationProgressProperties();

        return image;
    }

    private void bindGenerationProgressProperties() {
        Platform.runLater(() -> {
            progressProperty.bind(imageGenerator.progressProperty());
            generationTimeProperty.bind(imageGenerator.generationTimeProperty());
        });
    }

    private void unbindGenerationProgressProperties() {
        Platform.runLater(() -> {
            progressProperty.unbind();
            progressProperty.setValue(1);
            generationTimeProperty.unbind();
            generationTimeProperty.setValue(0);
        });
    }
}

