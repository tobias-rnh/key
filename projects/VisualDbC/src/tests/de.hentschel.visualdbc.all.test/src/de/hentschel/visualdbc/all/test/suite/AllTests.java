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

package de.hentschel.visualdbc.all.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.key_project.key4eclipse.util.test.suite.AllUtilTests;

import de.hentschel.visualdbc.datasource.key.test.suite.AllKeyDataSourceTests;
import de.hentschel.visualdbc.datasource.test.suite.AllDataSourceTests;
import de.hentschel.visualdbc.datasource.ui.test.suite.AllDataSourceUITests;
import de.hentschel.visualdbc.dbcmodel.diagram.custom.test.suite.AllDiagramCustomTests;
import de.hentschel.visualdbc.example.test.suite.AllExampleTests;
import de.hentschel.visualdbc.generation.test.suite.AllGenerationTests;
import de.hentschel.visualdbc.generation.ui.test.suite.AllGenerationUiTests;
import de.hentschel.visualdbc.interactive.proving.ui.test.suite.AllInteractiveProvingUiTests;

/**
 * <p>
 * Run all contained JUnit 4 test cases.
 * </p>
 * <p>
 * Requires the following JVM settings: -Xms128m -Xmx1024m -XX:MaxPermSize=256m
 * </p>
 * @author Martin Hentschel
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
   AllUtilTests.class,
   AllDataSourceTests.class,
   AllDataSourceUITests.class,
   AllGenerationTests.class,
   AllGenerationUiTests.class,
   AllKeyDataSourceTests.class,
   AllDiagramCustomTests.class,
   AllInteractiveProvingUiTests.class,
   AllExampleTests.class
})
   // TODO: Add unit 3 suite DbcModelAllTests
public class AllTests {
}