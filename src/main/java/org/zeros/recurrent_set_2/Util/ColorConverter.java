package org.zeros.recurrent_set_2.Util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import javafx.scene.paint.Color;

@Converter(autoApply = true)
public class ColorConverter implements AttributeConverter<Color, String> {
    @Override
    public String convertToDatabaseColumn(Color color) {
        return (color == null) ? null :
                String.format("#%02X%02X%02X%02X",
                        (int) (color.getOpacity() * 255),
                        (int) (color.getRed() * 255),
                        (int) (color.getGreen() * 255),
                        (int) (color.getBlue() * 255));
    }

    @Override
    public Color convertToEntityAttribute(String dbData) {
        return (dbData == null) ? null : Color.web(dbData);
    }
}