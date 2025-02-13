package org.zeros.recurrent_set_2.ImageCreation;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.JavaFxControllers.ImageGenerationControllers.ImageGenerationController;

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
            controller.setImageSize(4000,2000);
            controller.regenerateImage();
            long end = System.currentTimeMillis();
            chunkSizeTimeValuesList.add(new Point2D(i,end-start));
            System.out.println("checked chunk.."+i);
        }
        for (Point2D value : chunkSizeTimeValuesList) {
            System.out.println("Chunk size: " + value.getX() + " time: " + value.getY());
        }



    }

    @Test
    public void createImageChunkSizeMaxOptimization() {

        ArrayList<Point2D> chunkSizeTimeValuesList = new ArrayList<>();
        for(int i=40;i<2000;i=i+10){
            holder.getApplicationSettings().setMaxChunkBorderSize(i);
            long start = System.currentTimeMillis();
            controller.setImageSize(4000,2000);
            controller.regenerateImage();
            long end = System.currentTimeMillis();
            chunkSizeTimeValuesList.add(new Point2D(i,end-start));
            System.out.println("checked chunk.."+i);
        }
        for (Point2D value : chunkSizeTimeValuesList) {
            System.out.println("Chunk size: " + value.getX() + " time: " + value.getY());
        }



    }
}
