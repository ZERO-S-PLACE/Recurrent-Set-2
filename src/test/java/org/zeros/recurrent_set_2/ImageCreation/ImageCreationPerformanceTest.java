package org.zeros.recurrent_set_2.ImageCreation;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.ImageGeneration.ImageGenerationController;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

import java.util.ArrayList;

@SpringBootTest
public class ImageCreationPerformanceTest {
    @Autowired
    ImageGenerationController controller;
    @Autowired
    SettingsHolder holder;

    @Test
    public void createImageChunkSizeMinOptimization() {
        ArrayList<Point2D> chunkSizeTimeValuesList = new ArrayList<>();
        for(int i=5;i<150;i++){
            holder.getApplicationSettings().setMinChunkBorderSize(i);
            long start = System.currentTimeMillis();
            controller.getNewImage(RecurrentExpression.X_SHAPE, 2000, 400);
            long end = System.currentTimeMillis();
            chunkSizeTimeValuesList.add(new Point2D(i,end-start));
            System.out.println("checked chunk.."+i);
        }
       for (int i = 0; i < chunkSizeTimeValuesList.size(); i++) {
           Point2D value=chunkSizeTimeValuesList.get(i);
           System.out.println("Chunk size: " +  value.getX()+ " time: " + value.getY());
       }



    }

    @Test
    public void createImageChunkSizeMaxOptimization() {

        ArrayList<Point2D> chunkSizeTimeValuesList = new ArrayList<>();
        for(int i=40;i<2000;i=i+10){
            holder.getApplicationSettings().setMaxChunkBorderSize(i);
            long start = System.currentTimeMillis();
            controller.getNewImage(RecurrentExpression.MANDELBROT, 2000, 2000);
            long end = System.currentTimeMillis();
            chunkSizeTimeValuesList.add(new Point2D(i,end-start));
            System.out.println("checked chunk.."+i);
        }
        for (int i = 0; i < chunkSizeTimeValuesList.size(); i++) {
            Point2D value=chunkSizeTimeValuesList.get(i);
            System.out.println("Chunk size: " +  value.getX()+ " time: " + value.getY());
        }



    }
}
