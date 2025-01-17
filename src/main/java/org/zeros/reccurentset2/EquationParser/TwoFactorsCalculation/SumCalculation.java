package org.zeros.reccurentset2.EquationParser.TwoFactorsCalculation;

import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;

@Component
public class SumCalculation implements TwoFactorCalculation {
    @Override
    public Complex calculate(Complex z1, Complex z2) {
        return z1.add(z2);
    }
}
