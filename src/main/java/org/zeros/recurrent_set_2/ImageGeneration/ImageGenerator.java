package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.Canvas;
import org.zeros.recurrent_set_2.JavaFxControllers.ImageGenerationControllers.ImagePreviewAnimation;

public interface ImageGenerator {

    public DoubleProperty progressProperty();

    public DoubleProperty generationTimeProperty();

    public ImagePreviewAnimation getImageGenerationPreview();
    public void addImageGenerationPreview(Canvas canvas);

    public void generateImage();

    public void abandonGenerationNow();

}
