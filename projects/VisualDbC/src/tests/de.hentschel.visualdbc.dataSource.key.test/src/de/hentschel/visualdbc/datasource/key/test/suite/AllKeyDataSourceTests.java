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

package de.hentschel.visualdbc.datasource.key.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import de.hentschel.visualdbc.datasource.key.test.testCase.AttributeTest;
import de.hentschel.visualdbc.datasource.key.test.testCase.GeneralizationTest;
import de.hentschel.visualdbc.datasource.key.test.testCase.InnerTypeTest;
import de.hentschel.visualdbc.datasource.key.test.testCase.InvariantTest;
import de.hentschel.visualdbc.datasource.key.test.testCase.LogUtilTest;
import de.hentschel.visualdbc.datasource.key.test.testCase.TypeTest;
import de.hentschel.visualdbc.datasource.key.test.testCase.KeyConnectionTest;
import de.hentschel.visualdbc.datasource.key.test.testCase.MethodTest;
import de.hentschel.visualdbc.datasource.key.test.testCase.PackageTest;

/**
 * Run all contained JUnit 4 test cases.
 * @author Martin Hentschel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
   AttributeTest.class,
   GeneralizationTest.class,
   InnerTypeTest.class,
   KeyConnectionTest.class,
   LogUtilTest.class,
   MethodTest.class,
   PackageTest.class,
   InvariantTest.class,
   TypeTest.class
})
public class AllKeyDataSourceTests {
}