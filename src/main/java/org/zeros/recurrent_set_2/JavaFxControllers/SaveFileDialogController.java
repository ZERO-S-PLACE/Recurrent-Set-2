package org.zeros.recurrent_set_2.JavaFxControllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.zeros.recurrent_set_2.JavaFxControllers.ImageGenerationControllers.ImageForExportGenerationController;
import org.zeros.recurrent_set_2.Util.ImageSaver;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;
import org.zeros.recurrent_set_2.Model.ViewLocation;
import org.zeros.recurrent_set_2.Views.ViewFactory;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Controller
@Slf4j
public class SaveFileDialogController {

    private final ImageSaver imageSaver;
    private final ImageForExportGenerationController imageGenerationController;
    private final ViewFactory viewFactory;
    private FileChooser fileChooser;
    private Stage stage;
    private Scene scene;
    private ProgressBar progressBar;
    private Canvas canvas;

    public SaveFileDialogController(@Lazy ImageSaver imageSaver, @Lazy ImageForExportGenerationController imageGenerationController, @Lazy ViewFactory viewFactory) {
        this.imageSaver = imageSaver;
        this.imageGenerationController = imageGenerationController;
        this.viewFactory = viewFactory;
        //Platform.runLater(() -> {
       // configureFileChooser();
       // initializeStage();
       // });
    }


    public void initializeStage() {

        stage = new Stage();
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(imageGenerationController.getProgressProperty());
        canvas = new Canvas();
        canvas.heightProperty().bind(Bindings.createDoubleBinding(() -> stage.heightProperty().get() - 100, stage.heightProperty()));
        canvas.widthProperty().bind(stage.widthProperty());
        scene = new Scene(new VBox(canvas, progressBar));

        stage.getIcons().add(new Image(String.valueOf(ViewFactory.class.getResource("/program_icon2.png"))));
        stage.setTitle("Image Export");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.setWidth(800);
        stage.setHeight(600);
    }

    public void saveFileDialog(ViewLocation viewLocation, RecurrentExpression recurrentExpression) {
        viewFactory.getMainScene().getRoot().setDisable(true);

        fileChooser.setInitialFileName(recurrentExpression.getName()
                +String.format(" %.2f ", viewLocation.getCenterPoint().getReal())
                +String.format(" %.2f ", viewLocation.getCenterPoint().getImaginary())
                +"i.jpg");


        File selectedFile = fileChooser.showSaveDialog(stage);
        if (selectedFile == null) {
            viewFactory.getMainScene().getRoot().setDisable(false);
            return;
        }
        stage.show();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            imageGenerationController.setPreviewCanvas(canvas);
            Image image = imageGenerationController.generateNewImageExport(viewLocation);
            if(Thread.currentThread().isInterrupted()){
                Platform.runLater(() -> {
                    stage.close();
                    viewFactory.getMainScene().getRoot().setDisable(false);
                });
                return;}
            if(selectedFile.getAbsolutePath().endsWith(".png")) {
                imageSaver.saveAsPng(image, selectedFile);
            }
            else if(selectedFile.getAbsolutePath().endsWith(".jpg")) {
                imageSaver.saveAsJpg(image, selectedFile);
            }
            Platform.runLater(() -> {
                stage.hide();
                viewFactory.getMainScene().getRoot().setDisable(false);
            });
        });
       executorService.shutdown();

        scene.getWindow().setOnCloseRequest(event -> {
            executorService.shutdownNow();
            try {
                executorService.awaitTermination(10, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.atInfo().log("Saving image canceled");
            stage.hide();
            viewFactory.getMainScene().getRoot().setDisable(false);
        });

    }

    private void configureFileChooser() {
        fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");

        fileChooser.setInitialDirectory(new File(System.getProperty("user.home") + "/Desktop"));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.jpg"),
                new FileChooser.ExtensionFilter("Image Files", "*.png")
        );
    }

}
