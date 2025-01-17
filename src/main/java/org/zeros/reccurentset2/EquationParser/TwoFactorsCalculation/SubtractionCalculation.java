package org.zeros.reccurentset2.EquationParser.TwoFactorsCalculation;

import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;

@Component
public class SubtractionCalculation implements TwoFactorCalculation {
    @Override
    public Complex calculate(Complex z1, Complex z2) {
        return z1.subtract(z2);
    }
}
