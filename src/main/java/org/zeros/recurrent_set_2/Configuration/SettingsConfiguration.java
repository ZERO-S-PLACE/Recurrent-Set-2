package org.zeros.recurrent_set_2.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;
import org.zeros.recurrent_set_2.Model.ColorSettings;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

@Configuration(proxyBeanMethods=false)
public class SettingsConfiguration {


    @Bean
    public SettingsHolder settingsHolder() {
        SettingsHolder settingsHolder =new SettingsHolder(
                ApplicationSettings.DEFAULT,
                ColorSettings.DEFAULT,
                RecurrentExpression.DEVIL_SHAPE);
        return settingsHolder;
    }
}
