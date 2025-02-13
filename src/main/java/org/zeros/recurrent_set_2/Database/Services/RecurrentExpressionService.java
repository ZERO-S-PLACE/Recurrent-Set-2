package org.zeros.recurrent_set_2.Database.Services;


import org.zeros.recurrent_set_2.Model.RecurrentExpression;
import org.zeros.recurrent_set_2.Model.ViewLocation;

import java.util.List;

public interface RecurrentExpressionService {
    void addNewExpression(RecurrentExpression recurrentExpression);

    void deleteExpression(Long expressionId);

    void updateExpression(RecurrentExpression expression);

    List<RecurrentExpression> getAllExpressions();

    void loadAndApplyExpression(Long expressionId);

    RecurrentExpression getExpressionById(Long expressionId);

    void restorePredefinedExpressions();

    void setDefaultViewLocation(Long expressionId, Long viewId);

    void saveNewViewLocation(Long expressionId, ViewLocation viewLocation);

    void deleteViewLocation(Long expressionId, Long viewId);
}
