package org.zeros.recurrent_set_2.EquationParser.EquationTreeSimplifier;

import lombok.NonNull;
import org.apache.commons.math3.complex.Complex;
import org.springframework.stereotype.Component;
import org.zeros.recurrent_set_2.EquationParser.EquationTreeNode.ConstantNode;
import org.zeros.recurrent_set_2.EquationParser.EquationTreeNode.EquationTreeNode;
import org.zeros.recurrent_set_2.EquationParser.EquationTreeNode.TwoFactorsNode;
import org.zeros.recurrent_set_2.EquationParser.EquationTreeNode.VariableNode;
import org.zeros.recurrent_set_2.EquationParser.TwoFactorsCalculation.*;

import java.util.HashMap;
import java.util.function.Predicate;

@Component
public class EquationTreeSimplifierMaximal implements EquationTreeSimplifier {
    @Override
    public EquationTreeNode simplify(@NonNull EquationTreeNode root) {
        return simplifyEquationTreeNode(root);
    }

    private EquationTreeNode simplifyEquationTreeNode(EquationTreeNode node) {
        if (node.getChildren().isEmpty()) {
            return node;
        }
        for (EquationTreeNode child : node.getChildren()) {
            node.replaceChild(child, simplifyEquationTreeNode(child));
        }
        if (node.getChildren().stream().allMatch(child -> child.getChildren().isEmpty())) {
            if (node.getChildren().stream().noneMatch(EquationTreeNode::isVariable)) {
                return new ConstantNode(node.getSolution(new HashMap<>()));
            }
            if (node.getChildren().stream().allMatch(EquationTreeNode::isVariable)) {
                return node;
            }
            return includeInVariable(node);

        }
        return node;
    }

    private EquationTreeNode includeInVariable(EquationTreeNode node) {

        if (node instanceof TwoFactorsNode) {
            VariableNode variableNode = (VariableNode) node.getChildren().stream().filter(EquationTreeNode::isVariable)
                    .findFirst().orElseThrow();
            EquationTreeNode constantNode = node.getChildren().stream().filter(Predicate.not(EquationTreeNode::isVariable))
                    .findFirst().orElseThrow();
            switch (((TwoFactorsNode) node).getCalculationCommand()) {
                case SumCalculation ignored -> {
                    return includeOffset(variableNode, constantNode.getSolution(new HashMap<>()));
                }
                case SubtractionCalculation ignored -> {
                    return includeOffset(variableNode, constantNode.getSolution(new HashMap<>()).multiply(-1));
                }
                case DivisionCalculation ignored -> {
                    return includeMultiplier(variableNode, Complex.ONE.divide(constantNode.getSolution(new HashMap<>())));
                }
                case MultiplicationCalculation ignored -> {
                    return includeMultiplier(variableNode, constantNode.getSolution(new HashMap<>()));
                }
                case PowerCalculation ignored -> {
                    if(variableNode.getOffset()==null){
                        return includePower(variableNode,constantNode.getSolution(new HashMap<>()));
                    }
                    return node;
                }
                default -> {
                    return node;
                }
            }
        }
        return node;
    }

    private EquationTreeNode includeOffset(VariableNode variableNode, Complex offset) {


        variableNode.setOffset(offset);
        return variableNode;
    }
    private EquationTreeNode includeMultiplier(VariableNode variableNode, Complex multiplier) {
        variableNode.setMultiplier(multiplier);
        return variableNode;
    }
    private EquationTreeNode includePower(VariableNode variableNode, Complex power) {
        variableNode.setPower(power);
        return variableNode;
    }




}
