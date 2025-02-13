package org.zeros.recurrent_set_2.EquationParser.EquationTreeNode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.complex.Complex;

import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
public class VariableNode implements EquationTreeNode {
    private final String variableName;
    private Complex multiplier;
    private Complex exponent;
    private Complex offset;

    @Override
    public Complex getSolution(Map<String, Complex> variables) {
        Complex value = variables.get(variableName);
        if (exponent != null) value = value.pow(exponent);
        if (multiplier != null) value = value.multiply(multiplier);
        if (offset != null) value = value.add(offset);
       return value;
    }

    @Override
    public boolean isVariable() {
        return true;
    }

    @Override
    public Set<EquationTreeNode> getChildren() {
        return Set.of();
    }

    @Override
    public void replaceChild(EquationTreeNode original, EquationTreeNode replacement) {
    }


    public void setMultiplier(Complex multiplier) {
        this.multiplier = (this.multiplier == null)
                ? multiplier
                : this.multiplier.multiply(multiplier);
    }

    public void setOffset(Complex offset) {
        this.offset = (this.offset == null)
                ? offset
                : this.offset.add(offset);
    }
    public void setPower(Complex exponent) {
        this.exponent = (this.exponent == null)
                ? exponent
                : this.exponent.multiply(exponent);
        this.multiplier = (this.multiplier == null)
                ? null
                : this.multiplier.pow(multiplier);
    }
}
