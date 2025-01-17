package org.zeros.reccurentset2.EquationParser;

import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;
import org.zeros.reccurentset2.EquationParser.OneFactorCalculation.*;
import org.zeros.reccurentset2.EquationParser.TwoFactorsCalculation.*;


import java.util.Map;


@Component
public class CharactersValues {

    public final Map<String, TwoFactorCalculation> OPERATORS_CALCULATORS;
    public final Map<String, OneFactorCalculation> FUNCTIONS_CALCULATORS;
    public final Map<String, Complex> CONSTANT_VALUES;


    public CharactersValues(SumCalculation sumCalculation,
                            DivisionCalculation divisionCalculation,
                            MultiplicationCalculation multiplicationCalculation,
                            SubtractionCalculation subtractionCalculation,
                            PowerCalculation powerCalculation,
                            CosCalculation cosCalculation,
                            SinCalculation sinCalculation,
                            TanCalculation tanCalculation,
                            CtanCalculation ctanCalculation) {

        OPERATORS_CALCULATORS=Map.of("+", sumCalculation,
                "-", subtractionCalculation,
                "*", multiplicationCalculation,
                "/", divisionCalculation,
                "^", powerCalculation);

        FUNCTIONS_CALCULATORS=Map.of("sin", sinCalculation,
                "cos", cosCalculation,
                "tg", tanCalculation,
                "ctg", ctanCalculation);
        CONSTANT_VALUES= Map.of("e", Complex.valueOf(Math.E, 0),
                        "i", Complex.valueOf(0, 1),
                        "PI", Complex.valueOf(Math.PI, 0));
    }
}
