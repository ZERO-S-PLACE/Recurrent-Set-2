package org.zeros.recurrent_set_2.Views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Controllers.MainPanelController;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ViewFactory {
    private Scene scene;
    private final MainPanelController mainPanelController;
    private BorderPane mainPanel;



    public void showNewWindow() {
        if (mainPanel == null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/MainWindow.fxml"));
            loader.setController(mainPanelController);
            createStage(loader);
        }
    }

    private void createStage(FXMLLoader loader) {

        try {
            scene = new Scene(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        //stage.getIcons().add(new Image(String.valueOf(ViewFactory.class.getResource("/Icons/ProgramIcon.png"))));
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Recurrent Set 2 by Zeros");
        stage.show();
    }

}
