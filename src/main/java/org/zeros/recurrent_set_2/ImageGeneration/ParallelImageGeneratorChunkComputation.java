package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.application.Platform;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.complex.Complex;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculator;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Builder
public class ParallelImageGeneratorChunkComputation {

    private final ImageChunk imageChunk;
    private final Complex topLeftPoint;
    private final double unitsPerPixel;
    private final WritableImage writableImage;
    private final BoundaryGradientColors boundaryGradientColors;
    private final int iterations;
    private final int smallestChunkBorderSize;
    private final ExpressionCalculator recurentExpressionCalculator;
    private final ExpressionCalculator firstExpressionCalculator;



    /*
    ALGORITHM TO OPTIMIZE COMPUTATIONS:
    ASSUMPTION - recurrent sets doesn't have inner, closed areas
    If all points on borders of a chunk belong to the set, whole chunk belongs to the set,
    else, divide repeat this check for sub chunks, repeat this until they are smaller that specified size-
    in this case compute all points normally
    */

    protected void compute() {
        int[] leftBorder = getIterationsSatisfiedAtColumn(imageChunk.columnsStart(), imageChunk.rowsStart(), imageChunk.rowsEnd());
        int[] rightBorder = getIterationsSatisfiedAtColumn(imageChunk.columnsEnd(), imageChunk.rowsStart(), imageChunk.rowsEnd());
        int[] topBorder = getIterationsSatisfiedAtRow(imageChunk.rowsStart(), imageChunk.columnsStart(), imageChunk.columnsEnd());
        int[] bottomBorder = getIterationsSatisfiedAtRow(imageChunk.rowsEnd(), imageChunk.columnsStart(), imageChunk.columnsEnd());

        computeForChunk(imageChunk,
                new ImageChunkBorders(leftBorder, rightBorder, topBorder, bottomBorder));
    }

    private void computeForChunk(ImageChunk imageChunk, ImageChunkBorders imageChunkBorders) {
        if (Thread.currentThread().isInterrupted()) {return;}
        if (Arrays.stream(imageChunkBorders.rightBorder()).allMatch(value -> value == iterations)
                && Arrays.stream(imageChunkBorders.topBorder()).allMatch(value -> value == iterations)
                && Arrays.stream(imageChunkBorders.bottomBorder()).allMatch(value -> value == iterations)
                && Arrays.stream(imageChunkBorders.leftBorder()).allMatch(value -> value == iterations)) {
            colorChunk(imageChunk, iterations);
        } else if (Math.min(imageChunkBorders.rightBorder().length , imageChunkBorders.topBorder().length)
                <= smallestChunkBorderSize) {
            computeAllPoints(imageChunk,imageChunkBorders);
        } else {
            divideAndCheckSubChunks(imageChunk, imageChunkBorders);
        }

    }

    private void divideAndCheckSubChunks(ImageChunk imageChunk, ImageChunkBorders imageChunkBorders) {

        int divisionColumn = (imageChunk.columnsStart() + imageChunk.columnsEnd()) / 2;
        int divisionRow = (imageChunk.rowsStart() + imageChunk.rowsEnd()) / 2;
        int[] verticalBorder = getIterationsSatisfiedAtColumn(divisionColumn, imageChunk.rowsStart(), imageChunk.rowsEnd());
        int[] horizontalBorder = getIterationsSatisfiedAtRow(divisionRow, imageChunk.columnsStart(), imageChunk.columnsEnd());

        int[] leftBorderUpperPart = Arrays.copyOfRange(imageChunkBorders.leftBorder(), 0, divisionRow - imageChunk.rowsStart()+1);
        int[] middleBorderUpperPart = Arrays.copyOfRange(verticalBorder, 0, divisionRow - imageChunk.rowsStart()+1);
        int[] rightBorderUpperPart = Arrays.copyOfRange(imageChunkBorders.rightBorder(), 0, divisionRow - imageChunk.rowsStart()+1);

        int[] leftBorderLowerPart = Arrays.copyOfRange(imageChunkBorders.leftBorder(), divisionRow - imageChunk.rowsStart(), imageChunkBorders.leftBorder().length);
        int[] middleBorderLowerPart = Arrays.copyOfRange(verticalBorder, divisionRow - imageChunk.rowsStart(), imageChunkBorders.leftBorder().length);
        int[] rightBorderLowePart = Arrays.copyOfRange(imageChunkBorders.rightBorder(), divisionRow - imageChunk.rowsStart(), imageChunkBorders.leftBorder().length);

        int[] topBorderLeftPart = Arrays.copyOfRange(imageChunkBorders.topBorder(), 0, divisionColumn - imageChunk.columnsStart()+1);
        int[] middleBorderLeftPart = Arrays.copyOfRange(horizontalBorder, 0, divisionColumn - imageChunk.columnsStart()+1);
        int[] bottomBorderLeftPart = Arrays.copyOfRange(imageChunkBorders.bottomBorder(), 0, divisionColumn - imageChunk.columnsStart()+1);

        int[] topBorderRightPart = Arrays.copyOfRange(imageChunkBorders.topBorder(), divisionColumn - imageChunk.columnsStart(), imageChunkBorders.topBorder().length);
        int[] middleBorderRightPart = Arrays.copyOfRange(horizontalBorder, divisionColumn - imageChunk.columnsStart(), imageChunkBorders.topBorder().length);
        int[] bottomBorderRightPart = Arrays.copyOfRange(imageChunkBorders.bottomBorder(), divisionColumn - imageChunk.columnsStart(), imageChunkBorders.topBorder().length);
        //TOP LEFT CHUNK


            computeForChunk(new ImageChunk(imageChunk.rowsStart(),
                            divisionRow,
                            imageChunk.columnsStart(),
                            divisionColumn),
                    new ImageChunkBorders(leftBorderUpperPart, middleBorderUpperPart,
                            topBorderLeftPart, middleBorderLeftPart));


        //TOP RIGHT CHUNK
       computeForChunk(new ImageChunk(imageChunk.rowsStart(),
                        divisionRow,
                        divisionColumn,
                        imageChunk.columnsEnd()),
                new ImageChunkBorders(middleBorderUpperPart, rightBorderUpperPart,
                        topBorderRightPart, middleBorderRightPart)
        );


        //BOTTOM RIGHT CHUNK
       computeForChunk(new ImageChunk(divisionRow,
                        imageChunk.rowsEnd(),
                        divisionColumn,
                        imageChunk.columnsEnd()),
                new ImageChunkBorders(middleBorderLowerPart, rightBorderLowePart,
                        middleBorderRightPart, bottomBorderRightPart));


        //BOTTOM LEFT CHUNK
       computeForChunk(new ImageChunk(divisionRow,
                        imageChunk.rowsEnd(),
                        imageChunk.columnsStart(),
                        divisionColumn),
                new ImageChunkBorders(leftBorderLowerPart, middleBorderLowerPart,
                        middleBorderLeftPart, bottomBorderLeftPart));

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
    }

    private int[] getIterationsSatisfiedAtRow(int row, int columnsStart, int columnsEnd) {
        int[] borderValues = new int[columnsEnd - columnsStart];
        for (int column = columnsStart; column < columnsEnd; column++) {
            Complex p = getPointFromPixel(row, column);
            borderValues[column - columnsStart] = getSatisfiedOperations(p);
        }
        return borderValues;
    }

    private Complex getPointFromPixel(int row, int column) {
        return topLeftPoint.add(new Complex(column, -row).multiply(unitsPerPixel));
    }

    private int[] getIterationsSatisfiedAtColumn(int column, int rowsStart, int rowsEnd) {
        int[] borderValues = new int[rowsEnd - rowsStart];
        for (int row = rowsStart; row < rowsEnd; row++) {
            Complex p = getPointFromPixel(row, column);
            borderValues[row - rowsStart] = getSatisfiedOperations(p);
        }
        return borderValues;
    }


    private int getSatisfiedOperations(Complex p) {
        Complex z = firstExpressionCalculator.compute(Map.of("p", p));
        for (int i = 0; i < iterations; i++) {
            z = recurentExpressionCalculator.compute(Map.of("p", p, "z", z));
            if (z.abs() > ApplicationSettings.MAXIMAL_EXPRESSION_VALUE) {
                return i;
            }

        }
        return iterations;
    }

    protected void computeAllPoints(ImageChunk imageChunk, ImageChunkBorders imageChunkBorders) {
        int[][] iterationsSatisfied = new int[imageChunk.rowsEnd()-imageChunk.rowsStart()]
                [imageChunk.columnsEnd() - imageChunk.columnsStart()];
        iterationsSatisfied[0]=imageChunkBorders.topBorder();
        iterationsSatisfied[iterationsSatisfied.length-1]=imageChunkBorders.bottomBorder();
        for (int row = imageChunk.rowsStart()+1; row < imageChunk.rowsEnd()-1; row++) {
            if (Thread.currentThread().isInterrupted()) {return;}
            iterationsSatisfied[row-imageChunk.rowsStart()][0] = imageChunkBorders.leftBorder()[row-imageChunk.rowsStart()];
            iterationsSatisfied[row-imageChunk.rowsStart()][imageChunk.columnsEnd() - imageChunk.columnsStart()-1] =
                    imageChunkBorders.rightBorder()[row-imageChunk.rowsStart()];
            for (int column = imageChunk.columnsStart()+1; column < imageChunk.columnsEnd()-1; column++) {
                Complex p = getPointFromPixel(row, column);
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
    }


}
