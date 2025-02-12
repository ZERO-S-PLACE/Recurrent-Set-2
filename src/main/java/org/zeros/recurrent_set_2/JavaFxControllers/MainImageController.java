package org.zeros.recurrent_set_2.JavaFxControllers;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.ImageGeneration.ImageGenerationController;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

import java.net.URL;
import java.util.ResourceBundle;

@Component
@RequiredArgsConstructor
public class MainImageController implements Initializable {



    @FXML
    public BorderPane mainImageContainer;
    private final ImageGenerationController imageGenerationController;
    private final SettingsHolder settingsHolder;
    private final SaveFileDialogController saveFileDialogController;

    private final EventHandler<MouseEvent> imageSlideListener = this::moveImage;



    private void moveImage(MouseEvent mouseEvent) {

        imageGenerationController.moveViewTemporary(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addResizeHandling();
        addDragListeners();
        addScrollListeners();
        ProgressBar progressBar = new ProgressBar();
        progressBar.prefWidthProperty().bind(Bindings.createDoubleBinding(()->mainImageContainer.widthProperty().get()-100,mainImageContainer.widthProperty()));
        progressBar.progressProperty().addListener(((observable, oldValue, newValue) ->
        {
            if(oldValue.doubleValue()>=1&& newValue.doubleValue()<1){
                progressBar.setVisible(true);
            }
            if(oldValue.doubleValue()<1&& newValue.doubleValue()>=1){
                progressBar.setVisible(false);
            }

        }));
        progressBar.setMinHeight(20);
        progressBar.progressProperty().bind(imageGenerationController.getProgressProperty());
        Label timeLabel = new Label();
        timeLabel.textProperty().bind(Bindings.createStringBinding(
                () -> String.format("Time: %.2f",imageGenerationController.getGenerationTimeProperty().get()),
                imageGenerationController.getGenerationTimeProperty()
        ));
        mainImageContainer.setBottom(new HBox(progressBar, timeLabel));
        mainImageContainer.setBackground(new Background(new BackgroundFill(settingsHolder.getColorSettings().getBackgroundColor(), null, null)));
    }

    private void addScrollListeners() {
        mainImageContainer.setOnScroll(event -> {
            Point2D reference=new Point2D(event.getX(), event.getY());
            Platform.runLater(() ->{
            if(event.getDeltaY()>0){

                imageGenerationController.resizeImage(settingsHolder.getApplicationSettings().getDefaultRescaleOnScroll()*Math.log10(event.getDeltaY()),reference);
            }else if(event.getDeltaY()<0) {
                imageGenerationController.resizeImage(1/settingsHolder.getApplicationSettings().getDefaultRescaleOnScroll()/Math.log10(-event.getDeltaY()),reference);
            }
        });
        });
    }

    private void addResizeHandling() {
        Platform.runLater(()-> {
                    mainImageContainer.prefWidthProperty().bind(mainImageContainer.getScene().widthProperty());
                    mainImageContainer.prefHeightProperty().bind(mainImageContainer.getScene().heightProperty());

        imageGenerationController.getImageCanvasProperty().addListener((observable, oldValue, newValue) -> {
            mainImageContainer.getChildren().remove(oldValue);
            mainImageContainer.setCenter(newValue);
        });});

        mainImageContainer.widthProperty().addListener((observable, oldValue, newValue) -> {
            onWindowResize();});
        mainImageContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            onWindowResize();});
    }

    private void addDragListeners() {
        mainImageContainer.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.isControlDown()){
                saveFileDialogController.saveFileDialog(imageGenerationController.getViewLocation(),imageGenerationController.getRecurrentExpression());
            }
        });


        mainImageContainer.setOnDragDetected(event -> {
            imageGenerationController.setMoveReference(new Point2D(event.getX(), event.getY()));
            mainImageContainer.addEventHandler(MouseEvent.MOUSE_DRAGGED, imageSlideListener);
            mainImageContainer.startFullDrag();
            event.consume();
        });

        mainImageContainer.setOnMouseDragReleased(event -> {
            mainImageContainer.removeEventHandler(MouseEvent.MOUSE_DRAGGED, imageSlideListener);
            imageGenerationController.moveViewAndRegenerateBlankPart(new Point2D(event.getX(), event.getY()));
            event.consume();
        });
    }

    private void onWindowResize() {
        Platform.runLater(()-> {
            imageGenerationController.setImageSize((int) mainImageContainer.getWidth(), (int) mainImageContainer.getHeight());
            imageGenerationController.regenerateImage();
        });
    }


    public void generateImage(RecurrentExpression recurrentExpression) {
        imageGenerationController.setImageSize((int) mainImageContainer.getWidth(), (int) mainImageContainer.getHeight());
        imageGenerationController.setExpression(recurrentExpression);
        imageGenerationController.regenerateImage();
    }
}

