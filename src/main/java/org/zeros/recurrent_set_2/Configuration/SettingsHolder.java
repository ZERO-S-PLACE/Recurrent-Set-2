package org.zeros.recurrent_set_2.Configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;
import org.zeros.recurrent_set_2.Model.ColorSettings;

@Getter
@Setter
@AllArgsConstructor
public class SettingsHolder {
    private ApplicationSettings applicationSettings;
    private ColorSettings colorSettings;
}
