package org.zeros.recurrent_set_2.EquationParser.TwoFactorsCalculation;

import org.apache.commons.math3.complex.Complex;


public class MultiplicationCalculation implements TwoFactorCalculation {
    @Override
    public Complex calculate(Complex z1, Complex z2) {
        return z1.multiply(z2);
    }
}
