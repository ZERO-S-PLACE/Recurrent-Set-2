package org.zeros.recurrent_set_2.Configuration;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.Setter;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;
import org.zeros.recurrent_set_2.Model.ColorSettings;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

@Getter
@Setter
public class SettingsHolder {

    private ApplicationSettings applicationSettings;
    private ColorSettings colorSettings;
    private final ObjectProperty<RecurrentExpression> recurrentExpressionProperty;

    public SettingsHolder(ApplicationSettings applicationSettings, ColorSettings colorSettings, RecurrentExpression recurrentExpression) {
        this.applicationSettings = applicationSettings;
        this.colorSettings = colorSettings;
        this.recurrentExpressionProperty = new SimpleObjectProperty<>(recurrentExpression);
    }

    public RecurrentExpression getRecurrentExpression() {
        return recurrentExpressionProperty.get();
    }

    public void setRecurrentExpression(RecurrentExpression recurrentExpression) {
        recurrentExpressionProperty.set(recurrentExpression);
    }
}
