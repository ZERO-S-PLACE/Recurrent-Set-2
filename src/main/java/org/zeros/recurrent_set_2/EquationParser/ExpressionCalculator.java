package org.zeros.recurrent_set_2.EquationParser;

import org.apache.commons.math3.complex.Complex;

import java.util.Map;

public interface ExpressionCalculator {
     Complex compute(Map<String, Complex> variablesValues);
}
