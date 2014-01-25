/*
 * Copyright 2013 gregory.graham.
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
package nz.co.gregs.dbvolution.operators;

import nz.co.gregs.dbvolution.datatypes.QueryableDatatype;
import nz.co.gregs.dbvolution.datatypes.QueryableDatatypeSyncer.DBSafeInternalQDTAdaptor;
import nz.co.gregs.dbvolution.DBDatabase;
import nz.co.gregs.dbvolution.expressions.DBExpression;

/**
 *
 * @author gregory.graham
 */
public class DBLessThanOperator extends DBOperator {

    public static final long serialVersionUID = 1L;
//    protected final QueryableDatatype firstValue;

    /**
     *
     * @param lessThanThis
     */
    public DBLessThanOperator(DBExpression lessThanThis) {
        super();
        if (lessThanThis != null) {
            this.firstValue = lessThanThis.copy();
        } else {
            this.firstValue = null;
        }
    }

    public DBLessThanOperator() {
        super();
        this.firstValue = null;
    }

    public String getInverse() {
        return " >= ";
    }

    public String getOperator() {
        return " < ";
    }

    @Override
    public String generateWhereLine(DBDatabase db, String columnName) {
//        firstValue.setDatabase(database);
        return db.getDefinition().beginAndLine() + columnName + (invertOperator ? getInverse() : getOperator()) + firstValue.toSQLString(db) + " ";
    }

    @Override
    public String generateRelationship(DBDatabase database, String columnName, String otherColumnName) {
        return columnName + (invertOperator ? getInverse() : getOperator()) + otherColumnName;
    }

    @Override
    public DBOperator getInverseOperator() {
        return new DBGreaterThanOrEqualsOperator(firstValue);
    }
    
    @Override
    public DBLessThanOperator copyAndAdapt(DBSafeInternalQDTAdaptor typeAdaptor) {
    	DBLessThanOperator op = new DBLessThanOperator(typeAdaptor.convert(firstValue));
    	op.invertOperator = this.invertOperator;
    	op.includeNulls = this.includeNulls;
    	return op;
    }
}
