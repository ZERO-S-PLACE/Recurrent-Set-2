package org.zeros.recurrent_set_2.EquationParser.EquationTreeNode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.math3.complex.Complex;
import org.zeros.recurrent_set_2.EquationParser.OneFactorCalculation.OneFactorCalculation;

import java.util.Map;
import java.util.Set;


@AllArgsConstructor
@Getter
@Setter
public class OneFactorNode implements EquationTreeNode {

    private  EquationTreeNode child;
    private final OneFactorCalculation calculationCommand;

    @Override
    public Complex getSolution(Map<String, Complex> variables){
        return calculationCommand.calculate(child.getSolution(variables));
    }

    @Override
    public boolean isVariable() {
        return false;
    }

    @Override
    public Set<EquationTreeNode> getChildren() {
        return Set.of(child);
    }

    @Override
    public void replaceChild(EquationTreeNode original, EquationTreeNode replacement) {
        if (original == child) {
            this.child = replacement;
        }
    }
}
