/*
 * Copyright 2014 gregorygraham.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nz.co.gregs.dbvolution.internal.querygraph;

import java.awt.Color;
import java.awt.Paint;
import nz.co.gregs.dbvolution.example.Marque;
import nz.co.gregs.dbvolution.generic.AbstractTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author gregorygraham
 */
public class QueryGraphVertexFillPaintTransformerTest extends AbstractTest {

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	public QueryGraphVertexFillPaintTransformerTest(Object testIterationName, Object db) {
		super(testIterationName, db);
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of transform method, of class QueryGraphVertexFillPaintTransformer.
	 */
	@Test
	public void testTransform() {
		System.out.println("transform");
		QueryGraphNode i = new QueryGraphNode((new Marque()).getClass());
		QueryGraphVertexFillPaintTransformer instance = new QueryGraphVertexFillPaintTransformer();
		Paint expResult = Color.RED;
		Paint result = instance.transform(i);
		assertEquals(expResult, result);
		expResult = Color.ORANGE;
		i = new QueryGraphNode((new Marque()).getClass(), false);
		result = instance.transform(i);
		assertEquals(expResult, result);
	}

}
