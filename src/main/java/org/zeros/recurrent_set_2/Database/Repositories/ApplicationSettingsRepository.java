package org.zeros.recurrent_set_2.Database.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;

import java.util.Optional;

@Repository
public interface ApplicationSettingsRepository extends JpaRepository<ApplicationSettings, Long> {
    Optional<ApplicationSettings> findByName(String name);
    Optional<ApplicationSettings> findByAreDefaultSettings(boolean b);
}
