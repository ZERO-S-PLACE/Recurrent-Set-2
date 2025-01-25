package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.WritableImage;
import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculator;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorCreator;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;
import org.zeros.recurrent_set_2.Model.ViewLocation;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ImageGenerationController {

    private final ArrayList<ExpressionCalculator> recurrentExpressionCalculators = new ArrayList<>();
    private final ArrayList<ExpressionCalculator> firstExpressionCalculators = new ArrayList<>();
    private final SimpleObjectProperty<WritableImage> imageProperty;
    private final BoundaryGradientColors boundaryGradientColors;
    private final SettingsHolder settingsHolder;
    private final ExpressionCalculatorCreator calculatorCreator;
    private RecurrentExpression recurrentExpression;

    public ImageGenerationController(BoundaryGradientColors boundaryGradientColors, SettingsHolder settingsHolder, ExpressionCalculatorCreator calculatorCreator) {
        this.boundaryGradientColors = boundaryGradientColors;
        this.settingsHolder = settingsHolder;
        this.calculatorCreator = calculatorCreator;
        this.imageProperty = new SimpleObjectProperty<>();
    }

    public WritableImage getNewImage(RecurrentExpression expression, int width, int height) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        WritableImage image = new WritableImage(width, height);
        imageProperty.set(image);
        this.recurrentExpression = expression;
        executor.submit(() -> generateNewImage(width, height, expression.getDefaultViewLocation()));
        executor.shutdown();

        //FOR TEST ONLY
        /*try {
            executor.awaitTermination(12,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }*/
        return imageProperty.get();
    }

    private void initializeCalculatorsPools(RecurrentExpression expression, int poolSize) {
        for (int i = 0; i < poolSize; i++) {
            recurrentExpressionCalculators.add(calculatorCreator.getExpressionCalculator(expression.getRecurrentExpression(), expression.getVariableNames()));
            firstExpressionCalculators.add(calculatorCreator.getExpressionCalculator(expression.getFirstExpression(), expression.getVariableNames()));
        }
    }

    private void generateNewImage(int width, int height, ViewLocation location) {
        int columnsCount = (int) ((width * Math.sqrt(settingsHolder.getApplicationSettings().getNumberOfThreads())) / height);
        int rowsCount = settingsHolder.getApplicationSettings().getNumberOfThreads() / columnsCount;
        initializeCalculatorsPools(recurrentExpression, rowsCount * columnsCount);
        ExecutorService executorService = Executors.newFixedThreadPool(columnsCount * rowsCount);
        AtomicInteger counter = new AtomicInteger(0);

        for (int column = 0; column < columnsCount; column++) {

            for (int row = 0; row < rowsCount; row++) {
                int rowCurrent = row;
                int columnCurrent = column;
                counter.incrementAndGet();
                executorService.submit(() -> {
                    int columnWidth = width / columnsCount;
                    int rowHeight = height / rowsCount;
                    double unitsPerPixel = getUnitsPerPixel(width, location);
                    ParallelImageGeneratorChunkComputation.builder()
                            .boundaryGradientColors(boundaryGradientColors)
                            .imageChunk(ImageChunk.builder()
                                    .columnsEnd((columnCurrent + 1) * columnWidth)
                                    .columnsStart(columnCurrent * columnWidth)
                                    .rowsEnd((rowCurrent + 1) * rowHeight)
                                    .rowsStart(rowCurrent * rowHeight)
                                    .build())
                            .writableImage(imageProperty.get())
                            .executorService(executorService)
                            .counter(counter)
                            .settingsHolder(settingsHolder)
                            .iterations(settingsHolder.getApplicationSettings().getIterations())
                            .recurentExpressionCalculator(recurrentExpressionCalculators.get(columnCurrent * rowsCount + rowCurrent))
                            .firstExpressionCalculator(firstExpressionCalculators.get(columnCurrent * rowsCount + rowCurrent))
                            .unitsPerPixel(unitsPerPixel)
                            .topLeftPoint(location.getCenterPoint().add(new Complex(
                                    unitsPerPixel * ((double) -width / 2),
                                    unitsPerPixel * ((double) height / 2))))
                            .build()
                            .compute();
                });
            }
        }
        int counterLast = 0;
while (counter.get() >0) {
    if(counterLast>counter.get()) {
        System.out.println(counterLast);
    }
    counterLast=counter.get();
    try {
        Thread.sleep(100);
    } catch (InterruptedException e) {
        throw new RuntimeException(e);
    }
}
        executorService.shutdown();

        try {
            executorService.awaitTermination(12,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Finished generating new image");

    }


    private static double getUnitsPerPixel(int width, ViewLocation location) {
        return location.getHorizontalSpan() / width;
    }


}





