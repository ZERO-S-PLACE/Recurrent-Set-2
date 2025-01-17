package org.zeros.reccurentset2.EquationParser.EquationTreeNode;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.math3.complex.Complex;
import org.zeros.reccurentset2.EquationParser.TwoFactorsCalculation.TwoFactorCalculation;

import java.util.Set;


@AllArgsConstructor
@Getter
@Setter
public class TwoFactorsNode implements EquationTreeNode {

    private  EquationTreeNode leftChild;
    private  EquationTreeNode rightChild;
    private final TwoFactorCalculation calculationCommand;

    @Override
    public Complex getSolution(){
        return calculationCommand.calculate(leftChild.getSolution(), rightChild.getSolution());
    }

    @Override
    public boolean isVariable() {
        return false;
    }

    @Override
    public Set<EquationTreeNode> getChildren() {
        return Set.of(leftChild, rightChild);
    }

    @Override
    public void replaceChild(EquationTreeNode original, EquationTreeNode replacement) {
        if(leftChild == original){
            leftChild = replacement;
        }else if(rightChild == original){
            rightChild = replacement;
        }
    }
}
