package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.image.WritableImage;
import lombok.Builder;
import org.apache.commons.math3.complex.Complex;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
/*
@Builder
public class ParallelImageGenerator extends RecursiveAction {

    private final int rowsStart;
    private final int rowsEnd;
    private final int columnsStart;
    private final int columnsEnd;
    private final Complex topLeftPoint;
    private final double unitsPerPixel;
    private final WritableImage writableImage;
    private final BoundaryGradientColors boundaryGradientColors;
    private final SettingsHolder settingsHolder;
    private final ExpressionCalculator recurentExpressionCalculator;
    private final ExpressionCalculator firstExpressionCalculator;

    @Override
    protected void compute() {
        System.out.println("new iteration..rows: " + rowsStart + " " + rowsEnd + " columns:" + columnsStart + " " + columnsEnd);
        Map<Point2D,Complex> pointsToRecompute = new HashMap<>();
        for (int row = rowsStart; row < rowsEnd; row++) {
            int[] iterationsSatisfied = new int[columnsEnd - columnsStart];
            for (int column = columnsStart; column < columnsEnd; column++) {
                Complex p = getPointFromPixel(column, row);
               int iterationsSatisfiedAtCell= getSatisfiedOperations(p);
                iterationsSatisfied[column - columnsStart] =iterationsSatisfiedAtCell;
                if(iterationsSatisfiedAtCell==settingsHolder.getApplicationSettings().getIterations()){
                    pointsToRecompute.put(new Point2D(column,row),p);
                }
            }
            colorPoint(row, iterationsSatisfied);
        }
        recomputePoints(pointsToRecompute);
        System.out.println("..end");

    }

    private Complex getPointFromPixel(int column, int row) {
        return topLeftPoint.add(new Complex(column, row).multiply(unitsPerPixel));
    }

    private void recomputePoints(Map<Point2D, Complex> pointsToRecompute) {
        pointsToRecompute.forEach((location,value) -> {
            Complex z = firstExpressionCalculator.compute(Map.of("p", value));
            for (int i = 0; i < settingsHolder.getApplicationSettings().getIterations(); i++) {
                z = recurentExpressionCalculator.compute(Map.of("p", value, "z", z));
                if (z.abs() > 2) {
                    int finalI = i;
                    Platform.runLater(()->{
                       writableImage.getPixelWriter().setColor((int) location.getX(), (int) location.getY()
                               , boundaryGradientColors.getGradientColor(finalI)
                       );
                   });
                break;
                }
            }
        });
    }

    private int getSatisfiedOperations(Complex p) {
        Complex z = firstExpressionCalculator.compute(Map.of("p", p));
        for (int i = 0; i < settingsHolder.getApplicationSettings().getIterationsPreView(); i++) {
            z = recurentExpressionCalculator.compute(Map.of("p", p, "z", z));
            if (z.abs() > 2) {
                return i;
            }
        }
        return settingsHolder.getApplicationSettings().getIterations();
    }

    private void colorPoint(int row, int[] iterationsSatisfied) {
        Platform.runLater(() -> {
            for (int column = columnsStart; column < columnsEnd; column++) {
                {
                    writableImage.getPixelWriter().setColor(column, row, boundaryGradientColors.getGradientColor(
                            iterationsSatisfied[column - columnsStart]
                    ));
                }
            }
        });
    }
}
*/