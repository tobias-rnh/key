/*******************************************************************************
 * Copyright (c) 2011 Martin Hentschel.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Martin Hentschel - initial API and implementation
 *******************************************************************************/

package de.hentschel.visualdbc.statistic.ui.control;

import de.hentschel.visualdbc.dbcmodel.DbcModel;

/**
 * Defines the methods that are required to show content 
 * in a {@link StatisticComposite}.
 * @author Martin Hentschel
 */
public interface IStatisticProvider {
   /**
    * Returns the {@link DbcModel}.
    * @return The {@link DbcModel}.
    */
   public DbcModel getModel();
}