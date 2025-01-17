package org.zeros.reccurentset2.EquationParser.EquationTreeNode;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.complex.Complex;
import org.zeros.reccurentset2.EquationParser.OneFactorCalculation.OneFactorCalculation;

import java.util.Set;


@AllArgsConstructor
@Getter
@Setter
public class OneFactorNode implements EquationTreeNode {

    private  EquationTreeNode child;
    private final OneFactorCalculation calculationCommand;

    @Override
    public Complex getSolution(){
        return calculationCommand.calculate(child.getSolution());
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
