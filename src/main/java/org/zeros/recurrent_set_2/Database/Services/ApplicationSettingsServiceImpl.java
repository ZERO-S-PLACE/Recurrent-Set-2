package org.zeros.recurrent_set_2.Database.Services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.Database.Repositories.ApplicationSettingsRepository;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationSettingsServiceImpl implements ApplicationSettingsService {
    private final SettingsHolder settingsHolder;
    private final ApplicationSettingsRepository applicationSettingsRepository;

    @Override
    @Transactional
    public void addNewSettings(@NonNull ApplicationSettings applicationSettings) {

        if (applicationSettings.getId() != null) {
            throw new IllegalArgumentException("Settings already saved");
        }
        if (applicationSettingsRepository.findByName(applicationSettings.getName()).isPresent()) {
            throw new IllegalArgumentException("Settings with name " + applicationSettings.getName() + " already exists");
        }
        applicationSettingsRepository.save(applicationSettings);
    }

    @Override
    @Transactional
    public void deleteSettings(@NonNull Long settingsId) {
        if (!Objects.equals(settingsHolder.getApplicationSettings().getId(), settingsId)) {
            applicationSettingsRepository.deleteById(settingsId);
        }
        throw new IllegalArgumentException("Cannot delete currently selected application settings");
    }

    @Override
    @Transactional
    public void updateSettings(@NonNull ApplicationSettings applicationSettings) {
        if (applicationSettings.getId() == null) {
            throw new IllegalArgumentException("Settings doesn't exist");
        }
        if (applicationSettingsRepository.findByName(applicationSettings.getName()).isPresent() &&
                !applicationSettingsRepository.findByName(applicationSettings.getName()).get().getId().equals(applicationSettings.getId())) {
            throw new IllegalArgumentException("Settings with name " + applicationSettings.getName() + " already exists");
        }
        applicationSettingsRepository.save(applicationSettings);

    }

    @Override
    @Transactional
    public List<ApplicationSettings> getAllSettings() {
        return applicationSettingsRepository.findAll();
    }

    @Override
    @Transactional
    public void loadAndApplySettings(@NonNull Long settingsId) {
        if (settingsId == null) {
            throw new IllegalArgumentException("No settings id provided");
        }
        ApplicationSettings applicationSettings = applicationSettingsRepository.findById(settingsId)
                .orElseThrow(() -> new IllegalArgumentException("Settings with id " + settingsId + " doesn't exist"));
        settingsHolder.setApplicationSettings(applicationSettings);
    }

    @Override
    @Transactional
    public ApplicationSettings getDefaultSettings() {
        return applicationSettingsRepository.findByAreDefaultSettings(true)
                .orElse(ApplicationSettings.DEFAULT);
    }

    @Override
    @Transactional
    public void setDefaultSettings(@NonNull Long settingsId) {
        ApplicationSettings applicationSettings = applicationSettingsRepository.findById(settingsId)
                .orElseThrow(() -> new IllegalArgumentException("Settings with id " + settingsId + " doesn't exist"));
        for (ApplicationSettings settings : getAllSettings()) {
            settings.setAreDefaultSettings(false);
        }
        applicationSettings.setAreDefaultSettings(true);
    }

    @Override
    @Transactional
    public void restorePredefinedSettings() {
        Optional<ApplicationSettings> defaultSettingsSaved = applicationSettingsRepository.findByName(ApplicationSettings.DEFAULT.getName());
        defaultSettingsSaved.ifPresent(applicationSettingsRepository::delete);
        ApplicationSettings defaultSettingsRestored = applicationSettingsRepository.save(ApplicationSettings.DEFAULT);
        settingsHolder.setApplicationSettings(defaultSettingsRestored);
        setDefaultSettings(defaultSettingsRestored.getId());
    }
}