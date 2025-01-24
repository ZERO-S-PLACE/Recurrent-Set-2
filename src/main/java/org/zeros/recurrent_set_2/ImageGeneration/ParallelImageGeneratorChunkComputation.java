package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.application.Platform;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.complex.Complex;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculator;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.RecursiveAction;

@Slf4j
@Builder
public class ParallelImageGeneratorChunkComputation extends RecursiveAction {

    private final ImageChunk imageChunk;
    private final Complex topLeftPoint;
    private final double pixelToUnit;
    private final WritableImage writableImage;
    private final BoundaryGradientColors boundaryGradientColors;
    private final int iterations;
    private final SettingsHolder settingsHolder;
    private final ExpressionCalculator recurentExpressionCalculator;
    private final ExpressionCalculator firstExpressionCalculator;

    /*
    ALGORITHM TO OPTIMIZE COMPUTATIONS:
    ASSUMPTION - recurrent sets doesn't have inner, closed areas
    If all points on borders of a chunk belong to the set, whole chunk belongs to the set,
    else, divide repeat this check for sub chunks, repeat this until they are smaller that specified size-
    in this case compute all points normally
    */

    @Override
    protected void compute() {
        log.info("Computing image...{}", imageChunk.toString());
        int[] leftBorder = getIterationsSatisfiedAtColumn(imageChunk.columnsStart(), imageChunk.rowsStart(), imageChunk.rowsEnd());
        int[] rightBorder = getIterationsSatisfiedAtColumn(imageChunk.columnsEnd(), imageChunk.rowsStart(), imageChunk.rowsEnd());
        int[] topBorder = getIterationsSatisfiedAtRow(imageChunk.rowsStart(), imageChunk.columnsStart(), imageChunk.columnsEnd());
        int[] bottomBorder = getIterationsSatisfiedAtRow(imageChunk.rowsEnd(), imageChunk.columnsStart(), imageChunk.columnsEnd());

        computeForChunk(imageChunk,
                new ImageChunkBorders(leftBorder, rightBorder, topBorder, bottomBorder));
    }

    private void computeForChunk(ImageChunk imageChunk, ImageChunkBorders imageChunkBorders) {
      /*  if(Arrays.stream(imageChunkBorders.rightBorder()).allMatch( value -> value==0)
        && Arrays.stream(imageChunkBorders.topBorder()).allMatch( value -> value==0)
            && Arrays.stream(imageChunkBorders.bottomBorder()).allMatch( value -> value==0)
                && Arrays.stream(imageChunkBorders.leftBorder()).allMatch( value -> value==0)){
            colorChunk(imageChunk,0);
        }*/
        // else
        if (Arrays.stream(imageChunkBorders.rightBorder()).allMatch(value -> value == iterations)
                && Arrays.stream(imageChunkBorders.topBorder()).allMatch(value -> value == iterations)
                && Arrays.stream(imageChunkBorders.bottomBorder()).allMatch(value -> value == iterations)
                && Arrays.stream(imageChunkBorders.leftBorder()).allMatch(value -> value == iterations)) {
            colorChunk(imageChunk, iterations);
        } else if ((imageChunkBorders.rightBorder().length + imageChunkBorders.topBorder().length) / 2 < settingsHolder.getApplicationSettings().getSmallestChunkBorderSize()) {
            computeAllPoints(imageChunk);
        } else {
            divideAndCheckSubChunks(imageChunk, imageChunkBorders);
        }
    }

    private void divideAndCheckSubChunks(ImageChunk imageChunk, ImageChunkBorders imageChunkBorders) {
        int divisionColumn = (imageChunk.columnsStart() + imageChunk.columnsEnd()) / 2;
        int divisionRow = (imageChunk.rowsStart() + imageChunk.rowsEnd()) / 2;
        int[] verticalBorder = getIterationsSatisfiedAtColumn(divisionColumn, imageChunk.rowsStart(), imageChunk.rowsEnd());
        int[] horizontalBorder = getIterationsSatisfiedAtRow(divisionRow, imageChunk.columnsStart(), imageChunk.columnsEnd());

        int[] leftUpperBorder = Arrays.copyOfRange(imageChunkBorders.leftBorder(), 0, divisionRow - imageChunk.rowsStart());
        int[] middleUpperBorder = Arrays.copyOfRange(verticalBorder, 0, divisionRow - imageChunk.rowsStart());
        int[] rightUpperBorder = Arrays.copyOfRange(imageChunkBorders.rightBorder(), 0, divisionRow - imageChunk.rowsStart());

        int[] leftBottomBorder = Arrays.copyOfRange(imageChunkBorders.leftBorder(), divisionRow - imageChunk.rowsStart(), imageChunkBorders.leftBorder().length);
        int[] middleBottomBorder = Arrays.copyOfRange(verticalBorder, divisionRow - imageChunk.rowsStart(), imageChunkBorders.leftBorder().length);
        int[] rightBottomBorder = Arrays.copyOfRange(imageChunkBorders.rightBorder(), divisionRow - imageChunk.rowsStart(), imageChunkBorders.leftBorder().length);

        int[] topLeftBorder = Arrays.copyOfRange(imageChunkBorders.topBorder(), 0, divisionColumn - imageChunk.columnsStart());
        int[] middleLeftBorder = Arrays.copyOfRange(horizontalBorder, 0, divisionColumn - imageChunk.columnsStart());
        int[] bottomLeftBorder = Arrays.copyOfRange(imageChunkBorders.rightBorder(), 0, divisionColumn - imageChunk.columnsStart());

        int[] topRightBorder = Arrays.copyOfRange(imageChunkBorders.topBorder(), divisionColumn - imageChunk.columnsStart(), imageChunkBorders.topBorder().length);
        int[] middleRightBorder = Arrays.copyOfRange(horizontalBorder, divisionColumn - imageChunk.columnsStart(), imageChunkBorders.topBorder().length);
        int[] bottomRightBorder = Arrays.copyOfRange(imageChunkBorders.rightBorder(), divisionColumn - imageChunk.columnsStart(), imageChunkBorders.topBorder().length);
        //TOP LEFT CHUNK
        computeForChunk(new ImageChunk(imageChunk.rowsStart(),
                        divisionRow,
                        imageChunk.columnsStart(),
                        divisionColumn),
                new ImageChunkBorders(leftUpperBorder, middleUpperBorder, topLeftBorder, middleLeftBorder));

        //TOP RIGHT CHUNK
        computeForChunk(new ImageChunk(imageChunk.rowsStart(),
                        divisionRow,
                        divisionColumn,
                        imageChunk.columnsEnd()),
                new ImageChunkBorders(middleUpperBorder, rightUpperBorder, topRightBorder, middleRightBorder));

        //BOTTOM RIGHT CHUNK
        computeForChunk(new ImageChunk(divisionRow,
                        imageChunk.rowsEnd(),
                        divisionColumn,
                        imageChunk.columnsEnd()),
                new ImageChunkBorders(middleBottomBorder, rightBottomBorder, middleRightBorder, bottomRightBorder));

        //BOTTOM LEFT CHUNK
        computeForChunk(new ImageChunk(divisionRow,
                        imageChunk.rowsEnd(),
                        imageChunk.columnsStart(),
                        divisionColumn),
                new ImageChunkBorders(leftBottomBorder, middleBottomBorder, middleLeftBorder, bottomLeftBorder));
    }

    private void colorChunk(ImageChunk imageChunk, int iterations) {

        int width = imageChunk.columnsEnd() - imageChunk.columnsStart();
        int height = imageChunk.rowsEnd() - imageChunk.rowsStart();
        int[] pixelBuffer = new int[width * height];
        Arrays.fill(pixelBuffer, boundaryGradientColors.getGradientColorArgb(iterations));

        Platform.runLater(() ->
                writableImage.getPixelWriter().setPixels(
                        imageChunk.columnsStart(), imageChunk.rowsStart(),
                        width, height,
                        PixelFormat.getIntArgbInstance(),
                        pixelBuffer,
                        0,
                        width
                ));
        Platform.requestNextPulse();
    }

    private int[] getIterationsSatisfiedAtRow(int row, int columnsStart, int columnsEnd) {
        int[] borderValues = new int[columnsEnd - columnsStart];
        for (int column = columnsStart; column < columnsEnd; column++) {
            Complex p = topLeftPoint.add(new Complex(column, row).multiply(pixelToUnit));
            borderValues[column - columnsStart] = getSatisfiedOperations(p);
        }
        return borderValues;
    }

    private int[] getIterationsSatisfiedAtColumn(int column, int rowsStart, int rowsEnd) {
        int[] borderValues = new int[rowsEnd - rowsStart];
        for (int row = rowsStart; row < rowsEnd; row++) {
            Complex p = topLeftPoint.add(new Complex(column, row).multiply(pixelToUnit));
            borderValues[row - rowsStart] = getSatisfiedOperations(p);
        }
        return borderValues;
    }


    private int getSatisfiedOperations(Complex p) {
        Complex z = firstExpressionCalculator.compute(Map.of("p", p));
        for (int i = 0; i < iterations; i++) {
            z = recurentExpressionCalculator.compute(Map.of("p", p, "z", z));
            if (z.abs() > 2) {
                return i;
            }

        }
        return iterations;
    }

    protected void computeAllPoints(ImageChunk imageChunk) {
        int[][] iterationsSatisfied = new int[imageChunk.rowsEnd()-imageChunk.rowsStart()]
                [imageChunk.columnsEnd() - imageChunk.columnsStart()];
        for (int row = imageChunk.rowsStart(); row < imageChunk.rowsEnd(); row++) {
            for (int column = imageChunk.columnsStart(); column < imageChunk.columnsEnd(); column++) {
                Complex p = topLeftPoint.add(new Complex(column, row).multiply(pixelToUnit));
                int iterationsSatisfiedAtCell = getSatisfiedOperations(p);
                iterationsSatisfied[row-imageChunk.rowsStart()][column - imageChunk.columnsStart()] = iterationsSatisfiedAtCell;
            }

        }
        colorChunk(imageChunk, iterationsSatisfied);
    }

    private void colorChunk( ImageChunk imageChunk, int[][] iterationsSatisfied) {
        int chunkWidth = imageChunk.columnsEnd() - imageChunk.columnsStart();
        int chunkHeight = imageChunk.rowsEnd() - imageChunk.rowsStart();
        int[] pixelBuffer = new int[chunkWidth * chunkHeight];


        for (int row = 0; row < chunkHeight; row++) {
            for (int column = 0; column < chunkWidth; column++) {
                pixelBuffer[row * chunkWidth + column] = boundaryGradientColors.getGradientColorArgb(
                        iterationsSatisfied[row][column]);
            }
        }


        Platform.runLater(() -> writableImage.getPixelWriter().setPixels(
                imageChunk.columnsStart(),
                imageChunk.rowsStart(),
                chunkWidth,
                chunkHeight,
                PixelFormat.getIntArgbInstance(),
                pixelBuffer,
                0,
                chunkWidth
        ));
        Platform.requestNextPulse();
    }


}
