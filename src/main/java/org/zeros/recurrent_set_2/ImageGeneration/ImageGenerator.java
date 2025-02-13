package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

public interface ImageGenerator {

    public DoubleProperty progressProperty();

    public DoubleProperty generationTimeProperty();

    public ImagePreviewAnimation getImageGenerationPreview();
    public void addImageGenerationPreview(Canvas canvas);

    public Image generateImage();

    public void abandonGenerationNow();

}
