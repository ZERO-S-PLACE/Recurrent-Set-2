package org.zeros.recurrent_set_2.EquationParser.EquationTreeSimplifier;

import org.zeros.recurrent_set_2.EquationParser.EquationTreeNode.EquationTreeNode;

public interface EquationTreeSimplifier {
    EquationTreeNode simplify(EquationTreeNode root);
}
