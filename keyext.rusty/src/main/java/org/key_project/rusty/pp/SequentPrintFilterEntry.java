/* This file is part of KeY - https://key-project.org
 * KeY is licensed under the GNU General Public License Version 2
 * SPDX-License-Identifier: GPL-2.0-only */
package org.key_project.rusty.pp;

import org.key_project.rusty.logic.SequentFormula;

/**
 * One element of a sequent as delivered by SequentPrintFilter
 */
public interface SequentPrintFilterEntry {
    /**
     * Formula to display
     */
    SequentFormula getFilteredFormula();

    /**
     * Original formula from sequent
     */
    SequentFormula getOriginalFormula();
}