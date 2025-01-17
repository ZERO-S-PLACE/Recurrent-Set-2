package org.zeros.reccurentset2.EquationParser.EquationTreeNode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.complex.Complex;

import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
public class VariableNode implements EquationTreeNode {
    private Complex value;
    private Complex multiplier;
    private Complex exponent;
    private Complex offset;

    @Override
    public Complex getSolution() {
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

    public void setValue(Complex value) {
        if (exponent != null) value = value.pow(exponent);
        if (multiplier != null) value = value.multiply(multiplier);
        if (offset != null) value = value.add(offset);
        this.value = value;
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


}
