package org.zeros.recurrent_set_2.EquationParser.EquationTreeNode;

import org.apache.commons.math3.complex.Complex;

import java.util.Map;
import java.util.Set;


public interface EquationTreeNode {

    public Complex getSolution(Map<String, Complex> variables);
    public boolean isVariable();
    public Set<EquationTreeNode> getChildren();
    public void replaceChild(EquationTreeNode original,EquationTreeNode replacement);
}
