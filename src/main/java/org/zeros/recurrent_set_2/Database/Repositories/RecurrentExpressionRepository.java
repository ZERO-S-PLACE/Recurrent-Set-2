package org.zeros.recurrent_set_2.Database.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zeros.recurrent_set_2.Model.ApplicationSettings;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;

import java.util.Optional;

public interface RecurrentExpressionRepository extends JpaRepository<RecurrentExpression, Long> {
    Optional<RecurrentExpression> findByName(String name);
}
