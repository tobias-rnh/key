/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package org.key_project.rusty.rule;

import org.key_project.logic.Term;
import org.key_project.logic.Visitor;
import org.key_project.logic.op.QuantifiableVariable;
import org.key_project.rusty.logic.op.sv.SchemaVariable;

/**
 * This class is used to collect all appearing SchemaVariables that are bound in a Taclet.
 * Duplicates are not removed becaues the use of persistent datastructure and up to now we just have
 * a SetAsList-implementaion causing to have O(sqr(n)) if it would be used. The class is used by
 * RuleApps to compute all non-instantiated variables.
 */
public class TacletVariableSVCollector extends TacletSchemaVariableCollector {
    /**
     * visits term t in post order ({@link Term#execPostOrder(Visitor)}) and
     * collects all bound schema variables
     *
     * @param visited the Term to be visited (<code>t</code> must not be <code>null</code>
     */
    public void visit(Term visited) {
        for (int j = 0; j < visited.arity(); j++) {
            for (int i = 0; i < visited.varsBoundHere(j).size(); i++) {
                QuantifiableVariable boundVar = visited.varsBoundHere(j).get(i);
                if (boundVar instanceof SchemaVariable) {
                    varList = varList.prepend((SchemaVariable) boundVar);
                }
            }
        }
    }
}