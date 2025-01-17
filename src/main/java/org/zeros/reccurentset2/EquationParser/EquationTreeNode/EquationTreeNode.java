package org.zeros.reccurentset2.EquationParser.EquationTreeNode;

import org.apache.commons.math3.complex.Complex;

import java.util.Set;


public interface EquationTreeNode {

    public Complex getSolution();
    public boolean isVariable();
    public Set<EquationTreeNode> getChildren();
    public void replaceChild(EquationTreeNode original,EquationTreeNode replacement);
}
