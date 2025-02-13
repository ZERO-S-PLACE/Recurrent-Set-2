package org.zeros.recurrent_set_2.Database.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.Database.Repositories.ColorSettingsRepository;
import org.zeros.recurrent_set_2.Model.ColorSettings;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ColorSettingsServiceImpl implements ColorSettingsService {

    private final SettingsHolder settingsHolder;
    private final ColorSettingsRepository colorSettingsRepository;

    @Override
    @Transactional
    public void addNewColorSettings(ColorSettings colorSettings) {
        if (colorSettings == null) {
            throw new IllegalArgumentException("No application settings provided");
        }
        if (colorSettings.getId() != null) {
            throw new IllegalArgumentException("Settings already saved");
        }
        if (colorSettingsRepository.findByName(colorSettings.getName()).isPresent()) {
            throw new IllegalArgumentException("Settings with name " + colorSettings.getName() + " already exists");
        }
        colorSettingsRepository.save(colorSettings);
    }

    @Override
    @Transactional
    public void deleteColorSettings(Long settingsId) {
        if (!Objects.equals(settingsHolder.getColorSettings().getId(), settingsId)) {
            colorSettingsRepository.deleteById(settingsId);
        }
        throw new IllegalArgumentException("Cannot delete currently selected application settings");
    }

    @Override
    @Transactional
    public void updateColorSettings(ColorSettings colorSettings) {
        if (colorSettings == null) {
            throw new IllegalArgumentException("No application settings provided");
        }
        if (colorSettings.getId() == null) {
            throw new IllegalArgumentException("Settings doesn't exist");
        }
        if (colorSettingsRepository.findByName(colorSettings.getName()).isPresent()
                &&!colorSettingsRepository.findByName(colorSettings.getName()).get().getId().equals(colorSettings.getId())) {
            throw new IllegalArgumentException("Settings with name " + colorSettings.getName() + " already exists");
        }
        colorSettingsRepository.save(colorSettings);

    }

    @Override
    @Transactional
    public List<ColorSettings> getAllColorSettings() {
        return colorSettingsRepository.findAll();
    }

    @Override
    @Transactional
    public void loadAndApplyColorSettings(Long settingsId) {
        if (settingsId == null) {
            throw new IllegalArgumentException("No settings id provided");
        }
        ColorSettings colorSettings = colorSettingsRepository.findById(settingsId)
                .orElseThrow(() -> new IllegalArgumentException("Settings with id " + settingsId + " doesn't exist"));
        settingsHolder.setColorSettings(colorSettings);
    }

    @Override
    @Transactional
    public ColorSettings getDefaultColorSettings() {
        return colorSettingsRepository.findByAreDefaultSettings(true)
                .orElse(ColorSettings.DEFAULT);
    }

    @Override
    @Transactional
    public void setDefaultColorSettings(Long settingsId) {
        if (settingsId == null) {
            throw new IllegalArgumentException("No settings id provided");
        }
        ColorSettings colorSettings = colorSettingsRepository.findById(settingsId)
                .orElseThrow(() -> new IllegalArgumentException("Settings with id " + settingsId + " doesn't exist"));
        for (ColorSettings settings : getAllColorSettings()) {
            settings.setAreDefaultSettings(false);
        }
        colorSettings.setAreDefaultSettings(true);
    }

    @Override
    @Transactional
    public void restorePredefinedSettings() {
        Optional<ColorSettings> defaultSettingsSaved = colorSettingsRepository.findByName(ColorSettings.DEFAULT.getName());
        defaultSettingsSaved.ifPresent(colorSettingsRepository::delete);
        ColorSettings defaultSettingsRestored = colorSettingsRepository.save(ColorSettings.DEFAULT);
        settingsHolder.setColorSettings(defaultSettingsRestored);
        setDefaultColorSettings(defaultSettingsRestored.getId());
    }
}
