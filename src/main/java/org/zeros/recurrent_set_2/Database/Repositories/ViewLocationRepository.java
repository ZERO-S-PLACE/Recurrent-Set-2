package org.zeros.recurrent_set_2.Database.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;
import org.zeros.recurrent_set_2.Model.ViewLocation;

public interface ViewLocationRepository extends JpaRepository<ViewLocation, Long> {
}
