/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package org.key_project.rusty.ast.stmt;

import org.key_project.logic.SyntaxElement;
import org.key_project.rusty.ast.VariableDeclaration;
import org.key_project.rusty.ast.expr.Expr;
import org.key_project.rusty.ast.pat.Pattern;
import org.key_project.rusty.ast.ty.Type;
import org.key_project.rusty.ast.visitor.Visitor;

import org.jspecify.annotations.NonNull;

public class LetStatement implements Statement, VariableDeclaration {
    private final Pattern pat;
    private final Type type;
    private final Expr init;

    public LetStatement(Pattern pat, Type type, Expr init) {
        this.pat = pat;
        this.type = type;
        this.init = init;
    }


    @Override
    public @NonNull SyntaxElement getChild(int n) {
        return switch (n) {
        case 0 -> pat;
        case 1 -> init;
        default -> throw new IndexOutOfBoundsException("LetStatement has two children");
        };
    }

    @Override
    public int getChildCount() {
        return 3;
    }

    public Type getType() {
        return type;
    }

    public Pattern getPattern() {
        return pat;
    }

    @Override
    public String toString() {
        return "let " + pat + ": " + type + " = " + init;
    }

    @Override
    public void visit(Visitor v) {
        throw new RuntimeException("TODO @ DD");
    }
}