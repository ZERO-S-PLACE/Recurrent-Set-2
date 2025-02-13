package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.WritableImage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.complex.Complex;
import org.jetbrains.annotations.NotNull;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculator;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorCreator;
import org.zeros.recurrent_set_2.ImageGeneration.ChunkComputations.ImageChunk;
import org.zeros.recurrent_set_2.ImageGeneration.ChunkComputations.ParallelImageChunkGenerator;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;
import org.zeros.recurrent_set_2.Model.ViewLocation;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class ImageGeneratorChunks implements ImageGenerator {

    private final DoubleProperty progressProperty = new SimpleDoubleProperty(0);
    private final DoubleProperty generationTimeProperty = new SimpleDoubleProperty(0);
    private final SettingsHolder settingsHolder;
    private final ExpressionCalculatorCreator expressionCalculatorCreator;
    private final ViewLocation viewLocation;
    private final WritableImage image;
    private final int iterations;
    private final ImagePreviewAnimation imagePreviewAnimation;
    private final AtomicInteger progressCounter = new AtomicInteger(0);
    private final ThreadPoolExecutor executorService;
    private Long timeStarted;

    public ImageGeneratorChunks(SettingsHolder settingsHolder,
                                ExpressionCalculatorCreator expressionCalculatorCreator,
                                ViewLocation viewLocation,
                                WritableImage image,
                                int iterations) {
        this.settingsHolder = settingsHolder;
        this.expressionCalculatorCreator = expressionCalculatorCreator;
        this.viewLocation = viewLocation;
        this.image = image;
        this.iterations = iterations;
        executorService = getCustomThreadExecutor();
        imagePreviewAnimation = new ImagePreviewAnimation();
    }


    @Override
    public DoubleProperty progressProperty() {
        return progressProperty;
    }

    @Override
    public DoubleProperty generationTimeProperty() {
        return generationTimeProperty;
    }

    @Override
    public ImagePreviewAnimation getImageGenerationPreview() {
        return imagePreviewAnimation;
    }

    @Override
    public void addImageGenerationPreview(Canvas canvas) {
        imagePreviewAnimation.setCanvas(canvas);
        imagePreviewAnimation.setImage(image);
    }

    @Override
    public void generateImage() {

        logGenerationStarted();
        timeStarted = System.currentTimeMillis();

        RecurrentExpression recurrentExpression = settingsHolder.getRecurrentExpression();

        int columnsCount = 1 + (int) (image.getWidth() / settingsHolder.getApplicationSettings().getMaxChunkBorderSize());
        int rowsCount = 1 + (int) (image.getHeight() / settingsHolder.getApplicationSettings().getMaxChunkBorderSize());
        int columnWidth = (int) (image.getWidth() / columnsCount);
        int rowHeight = (int) (image.getHeight() / rowsCount);

        Point2D imageDimensions = new Point2D(image.getWidth(), image.getHeight());

        double unitsPerPixel = viewLocation.getUnitsPerPixel(imageDimensions);

        Complex topLeftPointCoordinate = viewLocation.getTopLeftPointCoordinate(imageDimensions);

        BoundaryGradientColors boundaryGradientColors = new BoundaryGradientColors(settingsHolder.getColorSettings(), iterations);

        Set<Runnable> tasks = new HashSet<>();

        for (int column = 0; column < columnsCount; column++) {
            for (int row = 0; row < rowsCount; row++) {
                int rowCurrent = row;
                int columnCurrent = column;
                ExpressionCalculator recurrentExpressionCalculator =
                        expressionCalculatorCreator.getExpressionCalculator(
                                recurrentExpression.getRecurrentExpression(),
                                recurrentExpression.getVariableNames());
                ExpressionCalculator firstExpressionCalculator =
                        expressionCalculatorCreator.getExpressionCalculator(
                                recurrentExpression.getFirstExpression(),
                                recurrentExpression.getVariableNames());

                tasks.add(() -> ParallelImageChunkGenerator.builder()
                        .imageChunk(ImageChunk.builder()
                                .columnsEnd((columnCurrent + 1) * columnWidth)
                                .columnsStart(columnCurrent * columnWidth)
                                .rowsEnd((rowCurrent + 1) * rowHeight)
                                .rowsStart(rowCurrent * rowHeight)
                                .build())
                        .boundaryGradientColors(boundaryGradientColors)
                        .writableImage(image)
                        .progressCounter(progressCounter)
                        .executorService(executorService)
                        .smallestChunkBorderSize(settingsHolder.getApplicationSettings().getMinChunkBorderSize())
                        .iterations(iterations)
                        .recurentExpressionCalculator(recurrentExpressionCalculator)
                        .firstExpressionCalculator(firstExpressionCalculator)
                        .unitsPerPixel(unitsPerPixel)
                        .topLeftPoint(topLeftPointCoordinate)
                        .build()
                        .compute());
            }
        }

        if (Thread.currentThread().isInterrupted()) {
            log.atInfo().log("Skipped generation of image before startup");
            return;
        }

        tasks.forEach(executorService::submit);
        imagePreviewAnimation.start();

        awaitGenerationFinished();

        executorService.shutdown();
        imagePreviewAnimation.stop();

    }


    private void awaitGenerationFinished() {
        int imageArea = (int) (image.getWidth() * image.getHeight());
        int logsCounter = 0;
        while (executorService.getActiveCount() > 0) {

            if (Thread.currentThread().isInterrupted()) {
                log.atInfo().log("Skipped generation of image");
                executorService.shutdownNow();
                return;
            }
            if ((System.currentTimeMillis() - timeStarted) *
                    ApplicationSettings.IMAGE_GENERATION_PROPERTIES_REFRESH_FREQUENCY / 1000 > logsCounter) {

                Platform.runLater(() -> {
                    progressProperty.set((double) progressCounter.get() / imageArea);
                    generationTimeProperty.set((double) (System.currentTimeMillis() - timeStarted) / 1000);
                });

                log.atTrace().log("Generating image .. " +
                        String.format("%.2f", progressProperty.get() * 100) + "% " +
                        String.format("%.2f", generationTimeProperty.get()) + "s");
                logsCounter++;
            }
            Thread.onSpinWait();
        }
    }

    @Override
    public void abandonGenerationNow() {
        imagePreviewAnimation.stop();
        executorService.shutdownNow();
    }

    private void logGenerationStarted() {
        log.atInfo().log("Regenerating image...");
        log.atInfo().log("Size: " + image.getWidth() + "x" + image.getHeight());
        log.atInfo().log("Scale: " + String.format("%.2f", viewLocation.getReferenceScale()));
        log.atInfo().log("Iterations: " + iterations);
    }

    private ThreadPoolExecutor getCustomThreadExecutor() {
        return new ThreadPoolExecutor(
                settingsHolder.getApplicationSettings().getNumberOfThreads(),
                Integer.MAX_VALUE,
                60L, TimeUnit.DAYS,
                new LinkedTransferQueue<>()
        );
    }
}
