/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package org.key_project.rusty.rule.tacletbuilder;

import org.key_project.logic.op.QuantifiableVariable;
import org.key_project.rusty.logic.BoundVarsVisitor;
import org.key_project.rusty.logic.Sequent;
import org.key_project.rusty.logic.op.sv.SchemaVariable;
import org.key_project.rusty.rule.Taclet;
import org.key_project.util.collection.DefaultImmutableSet;
import org.key_project.util.collection.ImmutableList;
import org.key_project.util.collection.ImmutableSLList;
import org.key_project.util.collection.ImmutableSet;

/**
 * this class contains the goals of the schematic theory specific rules (Taclet). There are new
 * sequents that have to be added, new rules and rule variables. The replacewith-goal is implemented
 * in subclasses
 */
public class TacletGoalTemplate {
    /** stores sequent that is one of the new goals */
    private Sequent addedSeq = Sequent.EMPTY_SEQUENT;

    /** stores list of Taclet which are introduced */
    private ImmutableList<Taclet> addedRules = ImmutableSLList.nil();

    /** program variables added by this taclet to the namespace */
    private ImmutableSet<SchemaVariable> addedProgVars = DefaultImmutableSet.nil();

    private String name = null;

    /**
     * creates new Goaldescription
     *
     * @param addedSeq new Sequent to be added
     * @param addedRules IList<Taclet> contains the new allowed rules at this branch
     * @param addedProgVars a SetOf<SchemaVariable> which will be instantiated with an application
     *        time unused (new) program variables that are introduced by an application of this
     *        template
     */
    public TacletGoalTemplate(Sequent addedSeq, ImmutableList<Taclet> addedRules,
            ImmutableSet<SchemaVariable> addedProgVars) {
        // TacletBuilder.checkContainsFreeVarSV(addedSeq, null, "add sequent");

        this.addedRules = addedRules;
        this.addedSeq = addedSeq;
        this.addedProgVars = addedProgVars;
    }

    /**
     * creates new Goaldescription same effect as <code>new TacletGoalTemplate(addedSeq,
     *                                             addedRules,
     *                                             SetAsListOf.<SchemaVariable>nil())
     *                                             </code>
     *
     * @param addedSeq new Sequent to be added
     * @param addedRules IList<Taclet> contains the new allowed rules at this branch
     */
    public TacletGoalTemplate(Sequent addedSeq, ImmutableList<Taclet> addedRules) {
        this(addedSeq, addedRules, DefaultImmutableSet.nil());
    }

    /**
     * a Taclet may add a new Sequent as Goal. Use this method to get this Sequent
     *
     * @return Sequent to be added as Goal or Sequent.EMPTY_SEQUENT if no such Sequent exists
     */
    public Sequent sequent() {
        return addedSeq;
    }

    /**
     * the goal of a Taclet may introduce new rules. Call this method to get them
     *
     * @return IList<Taclet> contains new introduced rules
     */
    public ImmutableList<Taclet> rules() {
        return addedRules;
    }

    public ImmutableSet<SchemaVariable> addedProgVars() {
        return addedProgVars;
    }

    public Object replaceWithExpressionAsObject() {
        return null;
    }

    /**
     * retrieves and returns all variables that are bound in the goal template
     *
     * @return all variables that occur bound in this goal template
     */
    public ImmutableSet<QuantifiableVariable> getBoundVariables() {
        ImmutableSet<QuantifiableVariable> result = DefaultImmutableSet.nil();

        for (Taclet taclet : rules()) {
            result = result.union(taclet.getBoundVariables());
        }

        final BoundVarsVisitor bvv = new BoundVarsVisitor();
        bvv.visit(sequent());

        return result.union(bvv.getBoundVariables());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        String result = "";
        if (!sequent().isEmpty()) {
            result += "\\add " + sequent() + " ";
        }
        if (!rules().isEmpty()) {
            result += "\\addrules " + rules() + " ";
        }
        if (!addedProgVars().isEmpty()) {
            result += "\\addprogvars " + addedProgVars() + " ";
        }
        return result;
    }
}