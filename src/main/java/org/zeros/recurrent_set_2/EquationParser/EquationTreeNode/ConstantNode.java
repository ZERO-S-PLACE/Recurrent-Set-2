package org.zeros.recurrent_set_2.EquationParser.EquationTreeNode;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.complex.Complex;

import java.util.Map;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class ConstantNode implements EquationTreeNode {

    private final Complex value;

    @Override
    public Complex getSolution(Map<String, Complex> variables) {
        return value;
    }

    @Override
    public boolean isVariable() {
        return false;
    }

    @Override
    public Set<EquationTreeNode> getChildren() {
        return Set.of();
    }

    @Override
    public void replaceChild(EquationTreeNode original, EquationTreeNode replacement) {}

}
