/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package org.key_project.rusty.rule;

import org.key_project.rusty.logic.PosInOccurrence;
import org.key_project.rusty.proof.Goal;
import org.key_project.util.collection.ImmutableList;

import org.jspecify.annotations.Nullable;

public interface RuleApp extends org.key_project.ncore.rules.RuleApp<Goal> {
    /**
     * returns the rule of this rule application
     */
    Rule rule();

    /**
     * returns the PositionInOccurrence (representing a SequentFormula and a position in the
     * corresponding formula) of this rule application
     */
    PosInOccurrence posInOccurrence();

    /**
     * applies the specified rule at the specified position if all schema variables have been
     * instantiated
     *
     * @param goal the Goal where to apply the rule
     * @return list of new created goals
     */
    @Nullable
    ImmutableList<Goal> execute(Goal goal);

    /**
     * returns true if all variables are instantiated
     *
     * @return true if all variables are instantiated
     */
    boolean complete();

    /**
     * @return user-friendly name for this rule-application
     */
    default String displayName() {
        return rule().displayName();
    }
}