package org.zeros.recurrent_set_2.Database.Services;

import org.zeros.recurrent_set_2.Model.ColorSettings;

import java.util.List;

public interface ColorSettingsService {
    void addNewColorSettings(ColorSettings colorSettings);
    void deleteColorSettings(Long settingsId);
    void updateColorSettings(ColorSettings colorSettings);
    List<ColorSettings> getAllColorSettings();
    void loadAndApplyColorSettings(Long settingsId);
    void setDefaultColorSettings(Long settingsId);
    ColorSettings getDefaultColorSettings();
    void restorePredefinedSettings();

}
