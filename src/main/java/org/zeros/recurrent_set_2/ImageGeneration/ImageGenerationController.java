package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculator;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorCreator;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

@Component
public class ImageGenerationController {

    private final ArrayList<ExpressionCalculator> recurrentExpressionCalculators=new ArrayList<>();
    private final ArrayList<ExpressionCalculator> firstExpressionCalculators=new ArrayList<>();
    private final SimpleObjectProperty<WritableImage> imageProperty;
    private final BoundaryGradientColors boundaryGradientColors;
    private final SettingsHolder settingsHolder;
    private final ExpressionCalculatorCreator calculatorCreator;
    private  RecurrentExpression recurrentExpression;

    public ImageGenerationController(BoundaryGradientColors boundaryGradientColors, SettingsHolder settingsHolder, ExpressionCalculatorCreator calculatorCreator) {
        this.boundaryGradientColors = boundaryGradientColors;
        this.settingsHolder = settingsHolder;
        this.calculatorCreator = calculatorCreator;
        this.imageProperty = new SimpleObjectProperty<>();
    }

    public WritableImage getNewImage(RecurrentExpression expression,int width, int height) {
        WritableImage image = new WritableImage(width, height);
        imageProperty.set(image);
        new Thread(() -> {
            this.recurrentExpression = expression;
            initializeCalculatorsPools(expression);
            generateNewImage(width, height);

        }).start();
        return imageProperty.get();
    }

    private void initializeCalculatorsPools(RecurrentExpression expression) {
        for (int i = 0; i < settingsHolder.getApplicationSettings().getNumberOfThreads(); i++) {
            recurrentExpressionCalculators.add(calculatorCreator.getExpressionCalculator(expression.recurrentExpression,expression.getVariableNames()));
            firstExpressionCalculators.add(calculatorCreator.getExpressionCalculator(expression.firstExpression,expression.getVariableNames()));
        }
    }

    private void generateNewImage( int width, int height){

        ForkJoinPool pool = new ForkJoinPool();
        int collumnsCount = (int) Math.sqrt(settingsHolder.getApplicationSettings().getNumberOfThreads());
        int rowsCount=settingsHolder.getApplicationSettings().getNumberOfThreads()/collumnsCount;
        for (int column = 0; column <collumnsCount; column++) {
            for (int row = 0; row < rowsCount; row++) {
                pool.execute(ParallelImageGenerator.builder()
                        .boundaryGradientColors(boundaryGradientColors)
                        .columnsEnd((column + 1) * width / (collumnsCount + 1))
                        .columnsStart(column * width / (collumnsCount + 1))
                        .rowsEnd((row + 1) * height / (row + 1))
                        .rowsStart(row * height / (row + 1))
                        .writableImage(imageProperty.get())
                        .settingsHolder(settingsHolder)
                        .recurentExpressionCalculator(recurrentExpressionCalculators.get(column*rowsCount+row))
                        .firstExpressionCalculator(firstExpressionCalculators.get(column*rowsCount+row))
                        .pixelToUnit((recurrentExpression.getInitialRangeMax() -
                                recurrentExpression.getInitialRangeMin()) / width)
                        .topLeftPoint(new Complex(recurrentExpression.getInitialRangeMin(),

                               -1.5))
                        .build());

            }
        }
        pool.shutdown();
        try {
            pool.awaitTermination(10, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }


}
