package org.zeros.reccurentset2.EquationParser;

import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.complex.Complex;
import org.zeros.reccurentset2.EquationParser.EquationTreeNode.EquationTreeNode;
import org.zeros.reccurentset2.EquationParser.EquationTreeNode.VariableNode;


import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
public class ExpressionCalculator {

    private final EquationTreeNode equationTree;

    private final Map<String, Set<VariableNode>> variables;
    private int count=0;

    public Complex compute(Map<String, Complex> variablesValues) {
        if(count==0) {
            insertVariables(variablesValues);
            count++;
        }
        return equationTree.getSolution();
    }

    private void insertVariables(Map<String, Complex> variablesValues) {
        variables.forEach((name, variables) ->
                variables.forEach(variableNode ->
                        variableNode.setValue(variablesValues.get(name))));
    }

}
