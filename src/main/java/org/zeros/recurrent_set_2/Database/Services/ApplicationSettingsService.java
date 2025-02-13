package org.zeros.recurrent_set_2.Database.Services;

import org.zeros.recurrent_set_2.Model.ApplicationSettings;

import java.util.List;

public interface ApplicationSettingsService {
    void addNewSettings(ApplicationSettings applicationSettings);

    void deleteSettings(Long settingsId);

    void updateSettings(ApplicationSettings applicationSettings);

    List<ApplicationSettings> getAllSettings();

    void loadAndApplySettings(Long settingsId);

    ApplicationSettings getDefaultSettings();

    void setDefaultSettings(Long settingsId);

    void restorePredefinedSettings();
}
