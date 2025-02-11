package org.zeros.recurrent_set_2.Database.Services;

import org.zeros.recurrent_set_2.Model.RecurrentExpression;
import org.zeros.recurrent_set_2.Model.ViewLocation;

import java.util.List;

public class RecurrentExpressionServiceImpl implements RecurrentExpressionService {
    @Override
    public void addNewExpression(RecurrentExpression recurrentExpression) {

    }

    @Override
    public void deleteExpression(Long expressionId) {

    }

    @Override
    public void updateExpression(RecurrentExpression expression) {

    }

    @Override
    public List<RecurrentExpression> getAllExpressions() {
        return List.of();
    }

    @Override
    public void loadAndApplyExpression(Long expressionId) {

    }

    @Override
    public RecurrentExpression getExpressionById(Long expressionId) {
        return null;
    }

    @Override
    public void restorePredefinedExpressions() {

    }

    @Override
    public void setDefaultViewLocation(Long expressionId, Long viewId) {

    }

    @Override
    public void saveNewViewLocation(Long expressionId, ViewLocation viewLocation) {

    }

    @Override
    public void deleteViewLocation(Long expressionId, Long viewId) {

    }
}
