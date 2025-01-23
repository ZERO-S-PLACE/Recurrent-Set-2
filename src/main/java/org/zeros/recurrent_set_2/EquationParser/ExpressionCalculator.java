package org.zeros.recurrent_set_2.EquationParser;

import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.complex.Complex;
import org.zeros.recurrent_set_2.EquationParser.EquationTreeNode.EquationTreeNode;


import java.util.Map;

@RequiredArgsConstructor
public class ExpressionCalculator {

    private final EquationTreeNode equationTree;

    public Complex compute(Map<String, Complex> variablesValues) {
        return equationTree.getSolution(variablesValues);
    }



}
