package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.animation.AnimationTimer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculator;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorCreator;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;
import org.zeros.recurrent_set_2.Model.ViewLocation;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageGenerationController {

    private final ArrayList<ExpressionCalculator> recurrentExpressionCalculators = new ArrayList<>();
    private final ArrayList<ExpressionCalculator> firstExpressionCalculators = new ArrayList<>();
    private final Map<WritableImage, Point2D> imagePartsLocationsMap = new HashMap<>();
    private final SimpleObjectProperty<Canvas> imageCanvasProperty = new SimpleObjectProperty<>();
    private final BoundaryGradientColors boundaryGradientColors;
    private final SettingsHolder settingsHolder;
    private final ExpressionCalculatorCreator calculatorCreator;
    private final Map<ImagePreviewAnimation, Point2D> animationTimers = new HashMap<>();
    private final Set<ExecutorService> generationThreadExecutors = new HashSet<>();
    private RecurrentExpression recurrentExpression;
    private ViewLocation viewLocation;
    private int imageWidth;
    private int imageHeight;
    private Point2D referencePointOnCanvas;
    private AtomicInteger progressCounter = new AtomicInteger(0);
    private Long timeStarted =0L;



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
        animationTimers.forEach((animation, location) ->
                animation.updateOffset(location.add(offset)));
        viewLocation.setCenterPoint(viewLocation.getCenterPoint().add(
                new Complex(-offset.getX() * getUnitsPerPixel(), offset.getY() * getUnitsPerPixel())));
        rewriteAllImageParts();
        animationTimers.keySet().forEach((ImagePreviewAnimation::start));
    }

    public void moveViewTemporary(Point2D newPosition) {
        if (referencePointOnCanvas != null) {
            animationTimers.keySet().forEach((ImagePreviewAnimation::stop));
            rewriteAllImageParts(newPosition.subtract(referencePointOnCanvas));
        }
    }

    public void abandonMove() {
        this.referencePointOnCanvas = null;
        rewriteAllImageParts();
        animationTimers.keySet().forEach((ImagePreviewAnimation::start));
    }

    private void rewriteAllImageParts() {
        rewriteAllImageParts(new Point2D(0, 0));
    }

    private void rewriteAllImageParts(Point2D offsetOnCanvas) {
        imageCanvasProperty.get().getGraphicsContext2D().clearRect(0, 0, imageWidth, imageHeight);
        imagePartsLocationsMap.forEach((image, location) ->
                writeImageOnCanvas(image, location.add(offsetOnCanvas)));
    }

    public void resizeImage(double scaleFactor, Point2D referencePointOnCanvas) {
        Complex pointOnSet = getPointOnSetFromCanvas(referencePointOnCanvas);
        viewLocation.setCenterPoint(pointOnSet.add(viewLocation.getCenterPoint().subtract(pointOnSet).multiply(1 / scaleFactor)));
        viewLocation.setHorizontalSpan(viewLocation.getHorizontalSpan() / scaleFactor);
        viewLocation.setReferenceScale(viewLocation.getReferenceScale() * scaleFactor);
        regenerateImage();

    }

    private Complex getPointOnSetFromCanvas(Point2D pointOnCanvas) {
        Complex topLeftPoint = viewLocation.getCenterPoint().add(new Complex(
                getUnitsPerPixel() * ((double) -imageWidth / 2),
                getUnitsPerPixel() * ((double) imageHeight / 2)));

        return topLeftPoint.add(new Complex(pointOnCanvas.getX() * getUnitsPerPixel(),
                -pointOnCanvas.getY() * getUnitsPerPixel()));
    }

    private void generateMissingParts() {
        regenerateImage();
    }

    public void regenerateImage() {
        logGenerationStarted();
        resetVariables();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        generationThreadExecutors.add(executor);
        executor.submit(() -> generateNewImage(imageWidth, imageHeight, new Point2D(0, 0)));
        executor.shutdown();

        //FOR TEST ONLY
        /*try {
            executor.awaitTermination(12,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/

    }

    private void logGenerationStarted() {
        log.atInfo().log("Regenerating image...");
        log.atInfo().log("Size: " + imageWidth + "x" + imageHeight);
        log.atInfo().log("Scale: " + viewLocation.getReferenceScale());
        log.atInfo().log("Iterations: " + getIterationsInView());
    }

    private void resetVariables() {
        Canvas canvas = new Canvas(imageWidth, imageHeight);
        imageCanvasProperty.set(canvas);
        generationThreadExecutors.forEach(ExecutorService::shutdownNow);
        generationThreadExecutors.clear();
        animationTimers.keySet().forEach(AnimationTimer::stop);
        animationTimers.clear();
        imagePartsLocationsMap.clear();

    }

    private void initializeCalculatorsPools(RecurrentExpression expression, int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            recurrentExpressionCalculators.add(calculatorCreator.getExpressionCalculator(expression.getRecurrentExpression(), expression.getVariableNames()));
            firstExpressionCalculators.add(calculatorCreator.getExpressionCalculator(expression.getFirstExpression(), expression.getVariableNames()));
        }
    }

    private void generateNewImage(int width, int height, Point2D locationOnCanvas) {

        progressCounter = new AtomicInteger(0);
        timeStarted=System.currentTimeMillis();
        WritableImage image = new WritableImage(imageWidth, imageHeight);
        imagePartsLocationsMap.put(image, locationOnCanvas);
        int iterations = getIterationsInView();
        boundaryGradientColors.regenerateColorMaps(iterations);

        int columnsCount = width / settingsHolder.getApplicationSettings().getMaxChunkBorderSize() + 1;
        int rowsCount = height / settingsHolder.getApplicationSettings().getMaxChunkBorderSize() + 1;

        initializeCalculatorsPools(recurrentExpression, rowsCount * columnsCount);
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(
                settingsHolder.getApplicationSettings().getNumberOfThreads(),  // Core pool size
                Integer.MAX_VALUE, // Max pool size
                60L, TimeUnit.DAYS,
                new LinkedBlockingQueue<>()
        );
        generationThreadExecutors.add(executorService);
        Set<Runnable> tasks = new HashSet<>();
        for (int column = 0; column < columnsCount; column++) {

            for (int row = 0; row < rowsCount; row++) {
                int rowCurrent = row;
                int columnCurrent = column;

                tasks.add(() -> {

                    int columnWidth = width / columnsCount;
                    int rowHeight = height / rowsCount;
                    double unitsPerPixel = getUnitsPerPixel();
                    ParallelImageGeneratorChunkComputation.builder()
                            .boundaryGradientColors(boundaryGradientColors)
                            .imageChunk(ImageChunk.builder()
                                    .columnsEnd((columnCurrent + 1) * columnWidth)
                                    .columnsStart(columnCurrent * columnWidth)
                                    .rowsEnd((rowCurrent + 1) * rowHeight)
                                    .rowsStart(rowCurrent * rowHeight)
                                    .build())
                            .writableImage(image)
                            .progressCounter(progressCounter)
                            .executorService(executorService)
                            .smallestChunkBorderSize(settingsHolder.getApplicationSettings().getMinChunkBorderSize())
                            .iterations(iterations)
                            .recurentExpressionCalculator(recurrentExpressionCalculators.get(columnCurrent * rowsCount + rowCurrent))
                            .firstExpressionCalculator(firstExpressionCalculators.get(columnCurrent * rowsCount + rowCurrent))
                            .unitsPerPixel(unitsPerPixel)
                            .topLeftPoint(viewLocation.getCenterPoint().add(new Complex(
                                    unitsPerPixel * ((double) -width / 2),
                                    unitsPerPixel * ((double) height / 2))))
                            .build()
                            .compute();

                });

            }
        }
        if (Thread.currentThread().isInterrupted()) {
            log.atInfo().log("Skipped generation of image");
            return;
        }
        tasks.forEach(executorService::submit);
        ImagePreviewAnimation timer = new ImagePreviewAnimation(image, imageCanvasProperty.get(), locationOnCanvas.getX(), locationOnCanvas.getY());
        animationTimers.put(timer, imagePartsLocationsMap.get(image));
        timer.start();
        if (awaitGenerationFinished(executorService)) return;
        clearTempVariables(executorService, timer);

        log.atInfo().log("Finished generating new image");

    }

    private void clearTempVariables(ThreadPoolExecutor executorService, ImagePreviewAnimation timer) {
        executorService.shutdown();
        timer.stop();
        animationTimers.remove(timer);
        rewriteAllImageParts();
        generationThreadExecutors.remove(executorService);
        timeStarted=0L;
    }

    private boolean awaitGenerationFinished(ThreadPoolExecutor executorService) {
        int logsCounter = 0;
        while (executorService.getActiveCount() > 0) {
            if (getGenerationTime()>logsCounter) {
               logsCounter++;
                log.atInfo().log("Processing image.. " + String.format("%.2f", getProgress()) + "%");
                log.atInfo().log("Processing image.. " + String.format("%.2f", getGenerationTime()) + "s");
            }

            if (Thread.currentThread().isInterrupted()) {
                log.atInfo().log("Skipped generation of image");
                return true;
            }
            Thread.onSpinWait();
        }
        return false;
    }

    private int getIterationsInView() {

        /* INCRESING ITERATIONS COUNT IN BIGGER SCALES, TO INCRESE QUALITY OF IMAGE*/
        if (viewLocation.getReferenceScale() > 1) {
            int iterations = (int) (settingsHolder.getApplicationSettings().getIterationsMin() * Math.sqrt(Math.sqrt(viewLocation.getReferenceScale())));
            if (iterations < settingsHolder.getApplicationSettings().getIterationsMax()) {
                return iterations;
            }
            return settingsHolder.getApplicationSettings().getIterationsMax();
        }
        return settingsHolder.getApplicationSettings().getIterationsMin();
    }

    private void writeImageOnCanvas(Image image, Point2D locationOnCanvas) {
        imageCanvasProperty.get().getGraphicsContext2D().clearRect(locationOnCanvas.getX(), locationOnCanvas.getY(), image.getWidth(), image.getHeight());
        imageCanvasProperty.get().getGraphicsContext2D().drawImage(image, locationOnCanvas.getX(), locationOnCanvas.getY());
    }

    public double getProgress() {
        return (double) progressCounter.get() / (imageWidth * imageHeight) * 100;
    }
    public double getGenerationTime(){
        return (double) ( System.currentTimeMillis()-timeStarted ) /1000;
    }


    private double getUnitsPerPixel() {
        return viewLocation.getHorizontalSpan() / imageCanvasProperty.get().getWidth();
    }

    public void setImageSize(int width, int height) {
        this.imageHeight = height;
        this.imageWidth = width;
        imageCanvasProperty.set(new Canvas(imageWidth, imageHeight));
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
    }

}





