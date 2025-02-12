package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorCreator;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;
import org.zeros.recurrent_set_2.Model.ViewLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class ImageGenerationController {

    private final SimpleObjectProperty<Canvas> imageCanvasProperty = new SimpleObjectProperty<>();
    private final Map<Image, Point2D> imagePartsLocationsMap = new HashMap<>();
    private final ObservableMap<Image, ImageGeneratorChunks> imageGeneratorsMap = FXCollections.observableHashMap();
    @Getter
    private final DoubleProperty progressProperty = new SimpleDoubleProperty(1);
    @Getter
    private final DoubleProperty generationTimeProperty = new SimpleDoubleProperty(0);
    private final SettingsHolder settingsHolder;
    private final ExpressionCalculatorCreator calculatorCreator;
    @Getter
    private RecurrentExpression recurrentExpression;
    @Getter
    private ViewLocation viewLocation;
    private Point2D imageDimensions;
    private Point2D referencePointOnCanvas;
    private ExecutorService executorService;


    public ImageGenerationController(SettingsHolder settingsHolder, ExpressionCalculatorCreator calculatorCreator) {
        this.settingsHolder = settingsHolder;
        this.calculatorCreator = calculatorCreator;

    }

    public void setMoveReference(Point2D referencePointOnCanvas) {
        this.referencePointOnCanvas = referencePointOnCanvas;
    }

    public void moveViewAndRegenerateBlankPart(Point2D newPosition) {
        if (referencePointOnCanvas != null) {
            applyOffsetToExistingImage(newPosition);
            generateMissingParts();
        }
    }

    private void applyOffsetToExistingImage(Point2D newPosition) {

        Point2D offset = newPosition.subtract(referencePointOnCanvas);
        referencePointOnCanvas = null;
        imagePartsLocationsMap.forEach((image, location) ->
                imagePartsLocationsMap.replace(image, location.add(offset)));
        imageGeneratorsMap.forEach((image, imageGenerator) ->
                imageGenerator.getImageGenerationPreview().updateOffset(
                        imagePartsLocationsMap.get(image).add(offset)));
        viewLocation.applyOffset(imageDimensions, offset);
        rewriteAllImageParts();
        imageGeneratorsMap.forEach((image, imageGenerator) ->
                imageGenerator.getImageGenerationPreview().start());
    }

    public void moveViewTemporary(Point2D newPosition) {
        if (referencePointOnCanvas != null) {
            imageGeneratorsMap.forEach((image, imageGenerator) ->
                    imageGenerator.getImageGenerationPreview().stop());
            rewriteAllImageParts(newPosition.subtract(referencePointOnCanvas));
        }
    }

    private void rewriteAllImageParts() {
        rewriteAllImageParts(new Point2D(0, 0));
    }

    private void rewriteAllImageParts(Point2D offsetOnCanvas) {
        imageCanvasProperty.get().getGraphicsContext2D().clearRect(0, 0, imageDimensions.getX(), imageDimensions.getY());
        imagePartsLocationsMap.forEach((image, location) ->
                writeImageOnCanvas(image, location.add(offsetOnCanvas)));
    }

    public void resizeImage(double scaleFactor, Point2D referencePointOnCanvas) {
        Complex pointOnSet = viewLocation.getPointOnSetCoordinate(imageDimensions, referencePointOnCanvas);
        viewLocation.setCenterPoint(pointOnSet.add(viewLocation.getCenterPoint().subtract(pointOnSet).multiply(1 / scaleFactor)));
        viewLocation.setHorizontalSpan(viewLocation.getHorizontalSpan() / scaleFactor);
        viewLocation.setReferenceScale(viewLocation.getReferenceScale() * scaleFactor);
        regenerateImage();
    }

    private void generateMissingParts() {
        regenerateImage();
    }

    public void regenerateImage() {
        resetVariables();
        executorService.submit(() -> generateNewImageWithPreview(imageDimensions, new Point2D(0, 0)));
        executorService.shutdown();

        //FOR TEST ONLY
        /*try {
            executor.awaitTermination(12,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

    }

    private void resetVariables() {
        Canvas canvas = new Canvas(imageDimensions.getX(), imageDimensions.getY());
        imageCanvasProperty.set(canvas);
        imagePartsLocationsMap.clear();
        removeGenerationProgressBindings();
        imageGeneratorsMap.forEach((image, imageGenerator) -> imageGenerator.abandonGenerationNow());
        imageGeneratorsMap.clear();
        if (executorService != null) {
            executorService.shutdownNow();
            try {
                executorService.awaitTermination(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        executorService = Executors.newSingleThreadExecutor();
    }

    private void generateNewImageWithPreview(Point2D imageDimensions, Point2D locationOnCanvas) {
        WritableImage image = new WritableImage((int) imageDimensions.getX(), (int) imageDimensions.getY());
        imagePartsLocationsMap.put(image, locationOnCanvas);
        for (int i = 0; i < 2; i++) {

            int iterations = i == 0 ? getIterationsPreview() : getIterationsInView();
            ImageGeneratorChunks imageGenerator = new ImageGeneratorChunks(settingsHolder, calculatorCreator, recurrentExpression, viewLocation, image, iterations);
            Platform.runLater(() -> imageGeneratorsMap.put(image, imageGenerator));
            createGenerationProgressBindings();
            imageGenerator.addImageGenerationPreview(imageCanvasProperty.get());
            imageGenerator.generateImage();
            if (i == 0) {
                log.atInfo().log("Image preview generated");
            } else {
                log.atInfo().log("Image generated");
            }
            removeGenerationProgressBindings();
            Platform.runLater(() -> imageGeneratorsMap.remove(image));
            rewriteAllImageParts();
        }
    }

    private void removeGenerationProgressBindings() {
        Platform.runLater(() -> {
            progressProperty.unbind();
            progressProperty.set(1);
            generationTimeProperty.unbind();
        });
    }


    /*
    INCRESING ITERATIONS COUNT IN BIGGER SCALES, TO INCRESE QUALITY OF IMAGE
    */

    private void createGenerationProgressBindings() {
        Platform.runLater(() -> {
            progressProperty.bind(averageProgressBinding);
            generationTimeProperty.bind(timeOfGenerationBinding);
        });
    }

    private int getIterationsInView() {


        if (viewLocation.getReferenceScale() > 1) {
            int iterations = (int) (settingsHolder.getApplicationSettings().getIterationsMin() * Math.sqrt(Math.sqrt(viewLocation.getReferenceScale())));
            if (iterations < settingsHolder.getApplicationSettings().getIterationsMax()) {
                return iterations;
            }
            return settingsHolder.getApplicationSettings().getIterationsMax();
        }
        return settingsHolder.getApplicationSettings().getIterationsMin();
    }

    private int getIterationsPreview() {

        if (viewLocation.getReferenceScale() > 1) {
            int iterations = (int) (settingsHolder.getApplicationSettings().getIterationsPreview() * Math.sqrt(Math.sqrt(viewLocation.getReferenceScale())));
            if (iterations < settingsHolder.getApplicationSettings().getIterationsMin()) {
                return iterations;
            }
            return settingsHolder.getApplicationSettings().getIterationsMin();
        }
        return settingsHolder.getApplicationSettings().getIterationsPreview();
    }

    private void writeImageOnCanvas(Image image, Point2D locationOnCanvas) {
        imageCanvasProperty.get().getGraphicsContext2D().clearRect(locationOnCanvas.getX(), locationOnCanvas.getY(), image.getWidth(), image.getHeight());
        imageCanvasProperty.get().getGraphicsContext2D().drawImage(image, locationOnCanvas.getX(), locationOnCanvas.getY());
    }

    public void setImageSize(int width, int height) {
        this.imageDimensions = new Point2D(width, height);
    }

    public void setExpression(RecurrentExpression recurrentExpression) {
        this.recurrentExpression = recurrentExpression;
        this.viewLocation = recurrentExpression.getDefaultViewLocation();
    }

    public void changeLocation(ViewLocation location) {
        this.viewLocation = location;
    }

    public ObjectProperty<Canvas> getImageCanvasProperty() {
        return imageCanvasProperty;
    }    private final DoubleBinding averageProgressBinding = new DoubleBinding() {
        {
            super.bind(imageGeneratorsMap);
            imageGeneratorsMap.addListener((MapChangeListener<Image, ImageGenerator>) change -> {
                if (change.wasAdded()) {
                    super.bind(change.getValueAdded().progressProperty());
                    averageProgressBinding.invalidate();
                }
                if (change.wasRemoved()) {
                    super.unbind(change.getValueRemoved().progressProperty());
                }
            });
        }

        @Override
        protected double computeValue() {

            if (imageGeneratorsMap.isEmpty()) return 1;
            return imageGeneratorsMap.values().stream().mapToDouble(
                            imageGenerator ->
                                    imageGenerator.progressProperty().get()).average()
                    .orElse(1);

        }
    };
    private final DoubleBinding timeOfGenerationBinding = new DoubleBinding() {
        {

            super.bind(imageGeneratorsMap);
            imageGeneratorsMap.addListener((MapChangeListener<Image, ImageGenerator>) change -> {
                if (change.wasAdded()) {
                    super.bind(change.getValueAdded().generationTimeProperty());
                }
                if (change.wasRemoved()) {
                    super.unbind(change.getValueRemoved().generationTimeProperty());
                }
            });
        }

        @Override
        protected double computeValue() {
            if (imageGeneratorsMap.isEmpty()) return 0;
            return imageGeneratorsMap.values().stream().mapToDouble(
                            imageGenerator ->
                                    imageGenerator.generationTimeProperty().get()).max()
                    .orElse(0);

        }
    };

}











