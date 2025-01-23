package org.zeros.recurrent_set_2.EquationParser;

import org.apache.commons.math3.complex.Complex;
import org.zeros.recurrent_set_2.EquationParser.OneFactorCalculation.*;
import org.zeros.recurrent_set_2.EquationParser.TwoFactorsCalculation.*;


import java.util.Map;

public class CharactersValues {

    public static final Map<String, Complex> CONSTANT_VALUES= Map.of("e", Complex.valueOf(Math.E, 0),
            "i", Complex.valueOf(0, 1),
            "PI", Complex.valueOf(Math.PI, 0));;




     public static OneFactorCalculation   FUNCTIONS_CALCULATORSget(String operator) {
         return switch (operator) {
             case "sin" -> new SinCalculation();
             case "cos" -> new CosCalculation();
             case "tg" -> new TanCalculation();
             case "ctg" -> new CtanCalculation();
             default -> throw new IllegalStateException("Unexpected value: " + operator);
         };
     }

    public static TwoFactorCalculation OPERATORS_CALCULATORSget(String operator) {
        return switch (operator) {
            case "+" -> new SumCalculation();
            case "/" -> new DivisionCalculation();
            case "*" -> new MultiplicationCalculation();
            case "-" -> new SubtractionCalculation();
            case "^" -> new PowerCalculation();
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }
}
