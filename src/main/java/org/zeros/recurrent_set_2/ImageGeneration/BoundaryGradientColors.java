package org.zeros.recurrent_set_2.ImageGeneration;

import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;

import java.util.HashMap;
import java.util.Map;

@Component
public class BoundaryGradientColors {

    private final SettingsHolder settings;

    private  Map<Integer, Color> gradientColors ;
    private  Map<Integer, Integer> gradientColorsArgb;

    public BoundaryGradientColors(SettingsHolder settingsHolder) {
        this.settings = settingsHolder;
        regenerateColorMaps();
    }

    private void regenerateColorMaps() {
        gradientColors= new HashMap<>();
        gradientColorsArgb = new HashMap<>();
        createGradientFromSettings();
        mapGradientToArgb();
    }

    private void mapGradientToArgb() {
        gradientColors.forEach((key, value) ->
                gradientColorsArgb.put(key,mapColorToArgb(value)));
    }


    private void createGradientFromSettings() {
        gradientColors.put(0, Color.TRANSPARENT);
        gradientColors.put(settings.getApplicationSettings().getIterations(), settings.getColorSettings().getIncludedElementsColor());
        Color startColor = settings.getColorSettings().getBoundaryGradientStartColor();
        Color endColor = settings.getColorSettings().getBoundaryGradientEndColor();
        Boolean fadeOut = settings.getColorSettings().getFadeOut();
        for (int i = 1; i < settings.getApplicationSettings().getIterations(); i++) {
            double coefficient = Math.log(i) / Math.log(settings.getApplicationSettings().getIterations());
            Color gradientColor = Color.color(
                    startColor.getRed() * (1 - coefficient) + endColor.getRed() * coefficient,
                    startColor.getGreen() * (1 - coefficient) + endColor.getGreen() * coefficient,
                    startColor.getBlue() * (1 - coefficient) + endColor.getBlue() * coefficient,
                    fadeOut ? startColor.getOpacity() * coefficient : 1
            );
            gradientColors.put(i, gradientColor);
        }
    }

    private int mapColorToArgb(Color color) {
        int alpha = (int) (color.getOpacity() * 255) << 24;
        int red = (int) (color.getRed() * 255) << 16;
        int green = (int) (color.getGreen() * 255) << 8;
        int blue = (int) (color.getBlue() * 255);
        return alpha | red | green | blue;
    }


    public Color getGradientColor(int iterationsPassed) {
        return gradientColors.get(iterationsPassed);
    }

    public int getGradientColorArgb(int iterationsPassed) {
        return gradientColorsArgb.get(iterationsPassed);
    }
}
