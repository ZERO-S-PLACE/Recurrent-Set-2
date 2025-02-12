package org.zeros.recurrent_set_2.JavaFxControllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.zeros.recurrent_set_2.ImageGeneration.ImageForExportGenerationController;
import org.zeros.recurrent_set_2.ImageSave.ImageSaver;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;
import org.zeros.recurrent_set_2.Model.ViewLocation;
import org.zeros.recurrent_set_2.Views.ViewFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SaveFileDialogController implements Initializable {

    private final ImageSaver imageSaver;
    private final ImageForExportGenerationController imageGenerationController;
    private final ViewFactory viewFactory;

    public SaveFileDialogController(@Lazy ImageSaver imageSaver, @Lazy ImageForExportGenerationController imageGenerationController, @Lazy ViewFactory viewFactory) {
        this.imageSaver = imageSaver;
        this.imageGenerationController = imageGenerationController;
        this.viewFactory = viewFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void saveFileDialog(ViewLocation viewLocation, RecurrentExpression recurrentExpression) {
        viewFactory.getMainScene().getRoot().setDisable(true);

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");

        // Set filters (optional)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png"),
        new FileChooser.ExtensionFilter("Image Files", "*.jpg")
        );

        // Open file selection dialog

        Stage stage = new Stage();

        File selectedFile = fileChooser.showSaveDialog(stage);
        stage.show();
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(imageGenerationController.getProgressProperty());

        Canvas canvas = new Canvas();
        canvas.heightProperty().bind(Bindings.createDoubleBinding(() -> stage.heightProperty().get() - 100, stage.heightProperty()));
        canvas.widthProperty().bind(stage.widthProperty());
        Scene scene = new Scene(new VBox(canvas, progressBar));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);

        new Thread(() -> {
            imageGenerationController.setPreviewCanvas(canvas);
            Image image = imageGenerationController.generateNewImageExport(viewLocation, recurrentExpression);
            if(selectedFile.getAbsolutePath().endsWith(".png")) {
                imageSaver.saveAsPng(image, selectedFile);
            }
            else if(selectedFile.getAbsolutePath().endsWith(".jpg")) {
                imageSaver.saveAsJpg(image, selectedFile);
            }
            Platform.runLater(() -> {
                stage.close();
                viewFactory.getMainScene().getRoot().setDisable(false);
            });
        }).start();

    }
}
