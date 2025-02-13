package org.zeros.recurrent_set_2.Database.Repositories;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;
import org.zeros.recurrent_set_2.Model.ColorSettings;
import scala.None;

import java.util.Optional;

@Repository
public interface ColorSettingsRepository extends JpaRepository<ColorSettings, Long> {

    Optional<ColorSettings> findByName(@NonNull @NotBlank String name);

    Optional<ColorSettings> findByAreDefaultSettings(boolean areDefaultSettings);
}
