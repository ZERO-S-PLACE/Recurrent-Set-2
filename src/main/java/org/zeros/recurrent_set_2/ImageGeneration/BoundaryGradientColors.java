package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;

import java.util.HashMap;
import java.util.Map;
@Component
public class BoundaryGradientColors {

    private final Map<Integer, Color> gradientColors;

    public BoundaryGradientColors(SettingsHolder settings) {
        gradientColors = createGradientFromSettings(settings);
    }

    private Map<Integer, Color> createGradientFromSettings(SettingsHolder settings) {
        Map<Integer, Color> gradientColors = new HashMap<>();
        gradientColors.put(0, Color.TRANSPARENT);
        gradientColors.put(settings.getApplicationSettings().getIterations(), settings.getColorSettings().getIncludedElementsColor());
        Color startColor =settings.getColorSettings().getBoundaryGradientStartColor();
        Color endColor =settings.getColorSettings().getBoundaryGradientEndColor();
        Boolean fadeOut=settings.getColorSettings().getFadeOut();
        for (int i = 1; i < settings.getApplicationSettings().getIterations(); i++) {
            double coefficient=Math.log(i)/Math.log(settings.getApplicationSettings().getIterations());
            Color gradientColor = Color.color(
                    startColor.getRed()*(1-coefficient)+endColor.getRed()*coefficient,
                    startColor.getGreen()*(1-coefficient)+endColor.getGreen()*coefficient,
                    startColor.getBlue()*(1-coefficient)+endColor.getBlue()*coefficient,
                    fadeOut ? startColor.getOpacity()*coefficient:1
            );
            gradientColors.put(i, gradientColor);
        }
        return gradientColors;
    }



    public Color getGradientColor(int iterationsPassed) {
        return gradientColors.get(iterationsPassed);
    }
}
