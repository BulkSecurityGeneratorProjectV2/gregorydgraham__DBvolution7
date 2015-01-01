/*
 * Copyright 2015 gregorygraham.
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
package nz.co.gregs.dbvolution.exceptions;

import nz.co.gregs.dbvolution.DBRow;
import nz.co.gregs.dbvolution.columns.ColumnProvider;

/**
 *
 * @author gregorygraham
 */
public class UnableToCreateAscendingExpressionForRecursiveQuery extends DBRuntimeException {

	private static final long serialVersionUID = 1L;

	public UnableToCreateAscendingExpressionForRecursiveQuery(ColumnProvider keyToFollow, DBRow originatingRow) {
		super("Unable To Create Ascending Expression For Recursive Query: some combination of the datatypes in "+keyToFollow.getColumn().getPropertyWrapper().javaName()+" and "+originatingRow.getClass().getSimpleName()+" prevents ascending queries working, please check them.");
	}
	
}
