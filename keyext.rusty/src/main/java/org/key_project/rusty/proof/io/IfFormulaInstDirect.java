/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package org.key_project.rusty.proof.io;

import org.key_project.rusty.Services;
import org.key_project.rusty.logic.SequentFormula;
import org.key_project.rusty.rule.IfFormulaInstantiation;

/**
 * Instantiation of an if-formula that has to be proven by an explicit if-goal
 */
public class IfFormulaInstDirect implements IfFormulaInstantiation {
    /**
     * Simply the formula
     */
    private final SequentFormula sf;

    public IfFormulaInstDirect(SequentFormula sf) {
        this.sf = sf;
    }

    /**
     * @return the cf this is pointing to
     */
    @Override
    public SequentFormula getConstrainedFormula() {
        return sf;
    }

    public String toString() {
        return toString(null);
    }

    public boolean equals(Object p_obj) {
        if (!(p_obj instanceof IfFormulaInstDirect o)) {
            return false;
        }
        return sf.equals(o.sf);
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + sf.hashCode();
        return result;
    }

    @Override
    public String toString(Services services) {
        return ProofSaver.printAnything(sf.formula(), services);
    }
}