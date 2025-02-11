package org.zeros.recurrent_set_2.JavaFxControllers;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.zeros.recurrent_set_2.ImageGeneration.ImageGenerationController;
import org.zeros.recurrent_set_2.ImageSave.ImageSaver;
import org.zeros.recurrent_set_2.Views.ViewFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class SaveFileDialogController implements Initializable {

    private final ImageSaver imageSaver;
    private final ImageGenerationController imageGenerationController;
    private final ViewFactory viewFactory;

    public SaveFileDialogController(@Lazy ImageSaver imageSaver,@Lazy ImageGenerationController imageGenerationController,@Lazy ViewFactory viewFactory) {
        this.imageSaver = imageSaver;
        this.imageGenerationController = imageGenerationController;
        this.viewFactory = viewFactory;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void saveFileDialog() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a File");

        // Set filters (optional)
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png")
        );

        // Open file selection dialog
        viewFactory.getMainScene().getRoot().setDisable(true);
        Stage stage = new Stage();
        stage.show();
        File selectedFile = fileChooser.showOpenDialog(stage);
        ProgressBar progressBar = new ProgressBar();
        progressBar.progressProperty().bind(imageGenerationController.getProgressProperty());
        Scene scene = new Scene(progressBar);
        stage.setScene(scene);

        new Thread(() -> {
            Image image = imageGenerationController.generateImageExport();
            imageSaver.saveAsPng(image, selectedFile);
            Platform.runLater(() -> {
                stage.close();
                viewFactory.getMainScene().getRoot().setDisable(false);
            });
        }).start();

    }
}
