/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package org.key_project.rusty.ast.pat;

import org.key_project.rusty.ast.RustyProgramElement;
import org.key_project.rusty.ast.visitor.Visitor;

public interface Pattern extends RustyProgramElement {
    @Override
    default void visit(Visitor v) {
        throw new RuntimeException("TODO @ DD");
    }
}