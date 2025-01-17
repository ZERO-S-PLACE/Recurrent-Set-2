package org.zeros.reccurentset2.EquationParser.OneFactorCalculation;

import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;

@Component
public class CosCalculation implements OneFactorCalculation {
    @Override
    public Complex calculate(Complex z1) {
    return z1.cos();
    }
}
