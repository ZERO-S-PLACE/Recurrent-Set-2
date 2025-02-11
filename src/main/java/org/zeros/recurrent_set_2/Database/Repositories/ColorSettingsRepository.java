package org.zeros.recurrent_set_2.Database.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;
import org.zeros.recurrent_set_2.Model.ColorSettings;
@Repository
public interface ColorSettingsRepository extends JpaRepository<ColorSettings, Long> {

}
