/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package de.uka.ilkd.key.java.expression.operator.adt;

import de.uka.ilkd.key.java.Expression;
import de.uka.ilkd.key.java.expression.operator.BinaryOperator;
import de.uka.ilkd.key.java.visitor.Visitor;

import org.key_project.util.ExtList;

public class SeqConcat extends BinaryOperator {

    public SeqConcat(ExtList children) {
        super(children);
    }


    public SeqConcat(Expression seq1, Expression seq2) {
        super(seq1, seq2);
    }


    public int getPrecedence() {
        return 0;
    }


    public int getNotation() {
        return PREFIX;
    }


    public void visit(Visitor v) {
        v.performActionOnSeqConcat(this);
    }

}
