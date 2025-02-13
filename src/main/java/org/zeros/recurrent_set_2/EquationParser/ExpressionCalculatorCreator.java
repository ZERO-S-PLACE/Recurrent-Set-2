package org.zeros.recurrent_set_2.EquationParser;

import lombok.NonNull;

import java.util.Set;

public interface ExpressionCalculatorCreator {
     ExpressionCalculator getExpressionCalculator(String expression,  Set<String> variableNames);
}
