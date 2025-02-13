package org.zeros.recurrent_set_2.Database.Services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zeros.recurrent_set_2.Configuration.SettingsHolder;
import org.zeros.recurrent_set_2.Database.Repositories.RecurrentExpressionRepository;
import org.zeros.recurrent_set_2.Database.Repositories.ViewLocationRepository;
import org.zeros.recurrent_set_2.EquationParser.ExpressionCalculatorCreator;
import org.zeros.recurrent_set_2.Model.RecurrentExpression;
import org.zeros.recurrent_set_2.Model.ViewLocation;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RecurrentExpressionServiceImpl implements RecurrentExpressionService {

    private final RecurrentExpressionRepository recurrentExpressionRepository;
    private final ViewLocationRepository viewLocationRepository;
    private final ExpressionCalculatorCreator expressionCalculatorCreator;
    private final SettingsHolder settingsHolder;

    @Override
    @Transactional
    public void addNewExpression(@NonNull RecurrentExpression recurrentExpression) {

        if(recurrentExpression.getId() != null) {
            throw new IllegalArgumentException("Expression already saved");
        }
        if(recurrentExpressionRepository.findByName(recurrentExpression.getName()).isPresent()) {
            throw new IllegalArgumentException("Expression with name " + recurrentExpression.getName() + " already exists");
        }
        validateEquations(recurrentExpression);
        recurrentExpressionRepository.save(recurrentExpression);

    }

    @Override
    @Transactional
    public void deleteExpression(@NonNull Long expressionId) {
        if(Objects.equals(settingsHolder.getRecurrentExpression().getId(), expressionId)) {
            throw new IllegalArgumentException("Expression in use");
        }
        recurrentExpressionRepository.deleteById(expressionId);
    }

    @Override
    @Transactional
    public void updateExpression(@NonNull RecurrentExpression recurrentExpression) {
        if(recurrentExpression.getId() == null) {
            throw new IllegalArgumentException("Expression doesn't exist");
        }
        if(recurrentExpressionRepository.findByName(recurrentExpression.getName()).isPresent()&&!recurrentExpressionRepository.findByName(recurrentExpression.getName()).get().getId().equals(recurrentExpression.getId())) {
            throw new IllegalArgumentException("Expression with name " + recurrentExpression.getName() + " already exists");
        }
        validateEquations(recurrentExpression);
        recurrentExpressionRepository.save(recurrentExpression);
    }

    private void validateEquations(@NotNull RecurrentExpression recurrentExpression) {
        try{
            expressionCalculatorCreator.getExpressionCalculator(recurrentExpression.getFirstExpression(), recurrentExpression.getVariableNames());
        }catch(Exception e){
            throw new IllegalArgumentException("First expression is invalid");
        }
        try{
            expressionCalculatorCreator.getExpressionCalculator(recurrentExpression.getRecurrentExpression(), recurrentExpression.getVariableNames());
        }catch(Exception e){
            throw new IllegalArgumentException("Recurrent expression is invalid");
        }
    }

    @Override
    @Transactional
    public List<RecurrentExpression> getAllExpressions() {
        return recurrentExpressionRepository.findAll();
    }

    @Override
    @Transactional
    public void loadAndApplyExpression(@NonNull Long expressionId) {
        RecurrentExpression recurrentExpression = recurrentExpressionRepository.findById(expressionId)
                .orElseThrow(()->new IllegalArgumentException("Expression doesn't exist"));
        settingsHolder.setRecurrentExpression(recurrentExpression);

    }

    @Override
    @Transactional
    public RecurrentExpression getExpressionById(@NonNull Long expressionId) {
        return recurrentExpressionRepository.findById(expressionId)
                .orElseThrow(()->new IllegalArgumentException("Expression doesn't exist"));
    }

    @Override
    @Transactional
    public void restorePredefinedExpressions() {
        Set<RecurrentExpression> predefinedExpressions =Set.of(RecurrentExpression.MANDELBROT,
                RecurrentExpression.JULIA_SET,RecurrentExpression.X_SHAPE,RecurrentExpression.X1_SHAPE,RecurrentExpression.X2_SHAPE,RecurrentExpression.X3_SHAPE);
        for(RecurrentExpression expression : predefinedExpressions) {
            if(recurrentExpressionRepository.findByName(expression.getName()).isPresent()) {
                recurrentExpressionRepository.delete(recurrentExpressionRepository.findByName(expression.getName()).get());
            }
            ViewLocation defaultLocation=viewLocationRepository.save(expression.getDefaultViewLocation());
            expression.setDefaultViewLocation(defaultLocation);
            expression.getSavedViewLocations().add(defaultLocation);
            recurrentExpressionRepository.save(expression);
        }
       loadAndApplyExpression(recurrentExpressionRepository.findByName(RecurrentExpression.MANDELBROT.getName())
               .orElseThrow(()->new RuntimeException("Application error")).getId());

    }

    @Override
    @Transactional
    public void setDefaultViewLocation(@NonNull Long expressionId, @NonNull Long viewId) {
        RecurrentExpression recurrentExpression=getExpressionById(expressionId);
        ViewLocation viewLocation=recurrentExpression.getSavedViewLocations().stream()
                .filter(view ->view.getId().equals(viewId) ).findFirst()
                .orElseThrow(()->new IllegalArgumentException("View location doesn't exist"));
        recurrentExpression.setDefaultViewLocation(viewLocation);
    }

    @Override
    @Transactional
    public void saveNewViewLocation(@NonNull Long expressionId, @NonNull ViewLocation viewLocation) {
        RecurrentExpression recurrentExpression=getExpressionById(expressionId);
        ViewLocation saved=viewLocationRepository.save(viewLocation);
        recurrentExpression.getSavedViewLocations().add(saved);
    }

    @Override
    @Transactional
    public void deleteViewLocation(@NonNull Long expressionId, @NonNull Long viewId) {
        RecurrentExpression recurrentExpression=getExpressionById(expressionId);
        if(recurrentExpression.getDefaultViewLocation().getId().equals(viewId)) {
            throw new IllegalArgumentException("Cannot delete default view location");
        }
        ViewLocation viewLocation=viewLocationRepository.findById(viewId)
                .orElseThrow(()->new IllegalArgumentException("View location doesn't exist"));
        recurrentExpression.getSavedViewLocations().remove(viewLocation);
        viewLocationRepository.delete(viewLocation);
    }
}
