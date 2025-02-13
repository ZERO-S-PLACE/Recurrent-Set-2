package org.zeros.recurrent_set_2;

import javafx.application.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.Database.Services.ApplicationSettingsService;
import org.zeros.recurrent_set_2.Database.Services.ColorSettingsService;
import org.zeros.recurrent_set_2.Database.Services.RecurrentExpressionService;
import org.zeros.recurrent_set_2.JavaFxControllers.MainImageController;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

@Component
@RequiredArgsConstructor
public class StartConfig implements CommandLineRunner {

    private final MainImageController mainImageController;
    private final ApplicationSettingsService applicationSettingsService;
    private final RecurrentExpressionService recurrentExpressionService;
    private final ColorSettingsService colorSettingsService;
    private final SettingsHolder settingsHolder;

    @Override
    public void run(String... args) {
        restoreDefaultValuesIfNotPresent();
        loadDefaultSettings();
        createDefaultImage();
    }

    private void loadDefaultSettings() {
        settingsHolder.setColorSettings(colorSettingsService.getDefaultColorSettings());
        settingsHolder.setApplicationSettings(applicationSettingsService.getDefaultSettings());
        settingsHolder.setRecurrentExpression(recurrentExpressionService.getAllExpressions().stream()
                .filter(recurrentExpression -> recurrentExpression.getName().contains("X1 Shape")).findFirst()
                .orElse(recurrentExpressionService.getAllExpressions().stream().findFirst().orElse(RecurrentExpression.X_SHAPE)));
    }

    private void restoreDefaultValuesIfNotPresent() {
        if (applicationSettingsService.getAllSettings().isEmpty()) {
            applicationSettingsService.restorePredefinedSettings();
        }
        if (colorSettingsService.getAllColorSettings().isEmpty()) {
            colorSettingsService.restorePredefinedSettings();
        }
        if (recurrentExpressionService.getAllExpressions().isEmpty()) {
            recurrentExpressionService.restorePredefinedExpressions();
        }
    }

    private void createDefaultImage() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(mainImageController::generateImage);
        }).start();
    }

}
