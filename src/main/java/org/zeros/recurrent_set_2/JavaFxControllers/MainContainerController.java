package org.zeros.recurrent_set_2.JavaFxControllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ResourceBundle;

@RequiredArgsConstructor
@Component
public class MainContainerController implements Initializable {

    @FXML
    public StackPane layersContainer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    //layersContainer.prefWidthProperty().bind(layersContainer.sceneProperty().get().widthProperty());
    //layersContainer.prefHeightProperty().bind(layersContainer.sceneProperty().get().heightProperty());
    }

    public void setBackground(Pane pane) {
        layersContainer.getChildren().add(pane);
        pane.toBack();

    }


}
