package org.zeros.recurrent_set_2.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.JavaFxControllers.MainContainerController;
import org.zeros.recurrent_set_2.JavaFxControllers.MainImageController;

import java.io.IOException;

@Component
@Getter
@RequiredArgsConstructor
public class ViewFactory {
    private Scene mainScene;
    private Stage mainStage;
    private final MainImageController mainPanelController;
    private final MainContainerController mainContainerController;
    private BorderPane mainImagePanel;


    public void showNewWindow() {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainContainer.fxml"));
        loader.setController(mainContainerController);
        createStage(loader);

        mainContainerController.setBackground(getImagePanel());
    }

    public BorderPane getImagePanel() {
        if (mainImagePanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainImage.fxml"));
            loader.setController(mainPanelController);
            try {
                mainImagePanel=loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return mainImagePanel;
    }

    private void createStage(FXMLLoader loader) {

        try {
            mainScene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mainStage = new Stage();
        mainStage.getIcons().add(new Image(String.valueOf(ViewFactory.class.getResource("/program_icon2.png"))));
        mainStage.setScene(mainScene);
        mainStage.setMaximized(true);
        mainStage.setTitle("Recurrent Set 2 by Zeros");
        mainStage.show();
    }

}
