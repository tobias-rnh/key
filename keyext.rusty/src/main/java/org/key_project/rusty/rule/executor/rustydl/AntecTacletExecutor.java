/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package org.key_project.rusty.rule.executor.rustydl;

import org.key_project.rusty.Services;
import org.key_project.rusty.logic.PosInOccurrence;
import org.key_project.rusty.logic.Sequent;
import org.key_project.rusty.logic.SequentChangeInfo;
import org.key_project.rusty.proof.Goal;
import org.key_project.rusty.rule.AntecTaclet;
import org.key_project.rusty.rule.MatchConditions;
import org.key_project.rusty.rule.RuleApp;
import org.key_project.rusty.rule.tacletbuilder.AntecSuccTacletGoalTemplate;
import org.key_project.rusty.rule.tacletbuilder.TacletGoalTemplate;

/**
 * Executes a Taclet which matches on a formula in the antecedent
 *
 * @author Richard Bubel
 * @param <TacletKind> the kind of taclet this executor is responsible for
 */
public class AntecTacletExecutor<TacletKind extends AntecTaclet>
        extends FindTacletExecutor<TacletKind> {

    public AntecTacletExecutor(TacletKind taclet) {
        super(taclet);
    }

    @Override
    protected void applyAdd(Sequent add, SequentChangeInfo currentSequent,
            PosInOccurrence whereToAdd, PosInOccurrence posOfFind, MatchConditions matchCond,
            Goal goal, RuleApp ruleApp, Services services) {
        addToAntec(add.antecedent(), currentSequent, whereToAdd,
            posOfFind, matchCond, goal, ruleApp, services);
        addToSucc(add.succedent(), currentSequent, null,
            posOfFind, matchCond, goal, ruleApp, services);
    }

    @Override
    protected void applyReplacewith(TacletGoalTemplate gt, SequentChangeInfo currentSequent,
            PosInOccurrence posOfFind, MatchConditions matchCond, Goal goal, RuleApp ruleApp,
            Services services) {
        if (gt instanceof AntecSuccTacletGoalTemplate astgt) {
            final Sequent replWith = astgt.replaceWith();
            replaceAtPos(replWith.antecedent(), currentSequent, posOfFind,
                matchCond,
                goal, ruleApp, services);
            if (!replWith.succedent().isEmpty()) {
                addToSucc(replWith.succedent(),
                    currentSequent, null, posOfFind, matchCond, goal, ruleApp, services);
            }
        } else {
            // Then there was no replacewith...
        }
    }
}