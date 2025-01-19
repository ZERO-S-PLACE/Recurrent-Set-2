package org.zeros.reccurentset2.EquationParser.TwoFactorsCalculation;

import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;


public class PowerCalculation implements TwoFactorCalculation {
    @Override
    public Complex calculate(Complex z1, Complex z2) {
        if(z1.equals(Complex.ZERO))return z1;
        return z1.pow(z2);
    }
}
