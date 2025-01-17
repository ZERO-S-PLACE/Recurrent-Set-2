package org.zeros.reccurentset2.EquationParser.EquationTreeSimplifier;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.zeros.reccurentset2.EquationParser.EquationTreeNode.ConstantNode;
import org.zeros.reccurentset2.EquationParser.EquationTreeNode.EquationTreeNode;
import org.zeros.reccurentset2.EquationParser.EquationTreeNode.TwoFactorsNode;
import org.zeros.reccurentset2.EquationParser.TwoFactorsCalculation.SubtractionCalculation;
import org.zeros.reccurentset2.EquationParser.TwoFactorsCalculation.SumCalculation;

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
                return new ConstantNode(node.getSolution());
            }
            if (node.getChildren().stream().allMatch(EquationTreeNode::isVariable)) {
                return node;
            }
            return includeInVariable(node);

        }
        return node;
    }

    private EquationTreeNode includeInVariable(EquationTreeNode node) {
        //todo
        if(node instanceof TwoFactorsNode) {
            switch (((TwoFactorsNode)node).getCalculationCommand()) {
                case SumCalculation sumCalculation->{
                    return node;

                }
                case SubtractionCalculation subtractionCalculation->{

                return node;
                }
                default -> {
                    return node;
                }
            }
        }
        return node;
    }
}
