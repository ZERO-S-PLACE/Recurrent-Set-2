package org.zeros.recurrent_set_2;

import javafx.application.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Controllers.MainImageController;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

@Component
@RequiredArgsConstructor
public class StartConfig implements CommandLineRunner {

    private final MainImageController mainImageController;

    @Override
    public void run(String... args) {
       new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(()->mainImageController.generateImage(RecurrentExpression.X1_SHAPE));
        }).start();
    }

}
