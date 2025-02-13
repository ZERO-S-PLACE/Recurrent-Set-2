package org.zeros.recurrent_set_2.EquationParser.OneFactorCalculation;

import org.apache.commons.math3.complex.Complex;


public class CtanCalculation implements OneFactorCalculation {
    @Override
    public Complex calculate(Complex z1) {
        return Complex.ONE.divide(z1.tan());
    }
}
