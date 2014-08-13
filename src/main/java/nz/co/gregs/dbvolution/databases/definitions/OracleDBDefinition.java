/*
 * Copyright 2014 gregory.graham.
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
package nz.co.gregs.dbvolution.databases.definitions;

import java.text.SimpleDateFormat;
import java.util.Date;
import nz.co.gregs.dbvolution.DBRow;
import nz.co.gregs.dbvolution.datatypes.DBBoolean;
import nz.co.gregs.dbvolution.datatypes.DBDate;
import nz.co.gregs.dbvolution.datatypes.DBString;
import nz.co.gregs.dbvolution.datatypes.QueryableDatatype;
import nz.co.gregs.dbvolution.query.QueryOptions;
import nz.co.gregs.dbvolution.query.RowDefinition;

/**
 * Defines the features of all Oracle databases that differ from the standard
 * database.
 *
 * <p>
 * This DBDefinition is sub-classed by {@link Oracle11DBDefinition} and
 * {@link  Oracle12DBDefinition} to provide the full set of features required to
 * use an Oracle database.
 *
 * @author Gregory Graham
 */
public class OracleDBDefinition extends DBDefinition {

	String dateFormatStr = "yyyy-M-d HH:mm:ss Z";
	String oracleDateFormatStr = "YYYY-MM-DD HH24:MI:SS TZHTZM";
	SimpleDateFormat javaToStringFormatter = new SimpleDateFormat(dateFormatStr);

	@Override
	public String getDateFormattedForQuery(Date date) {
		if (date == null) {
			return getNull();
		}
//        yyyy-MM-dd hh:mm:ss[.nnnnnnnnn]
		return " TO_TIMESTAMP_TZ('" + javaToStringFormatter.format(date) + "','" + oracleDateFormatStr + "') ";
		//return "'"+strToDateFormat.format(date)+"'";
	}

	@Override
	public String formatTableName(DBRow table) {
		final String sqlObjectName = table.getTableName();
		return formatNameForOracle(sqlObjectName);
	}

	@Override
	public String getPrimaryKeySequenceName(String table, String column) {
		return formatNameForOracle(super.getPrimaryKeySequenceName(table, column));
	}

	@Override
	public String getPrimaryKeyTriggerName(String table, String column) {
		return formatNameForOracle(super.getPrimaryKeyTriggerName(table, column));
	}

	private static String formatNameForOracle(final String sqlObjectName) {
		if (sqlObjectName.length() < 30) {
			return sqlObjectName.replaceAll("^[_-]", "O").replaceAll("-", "_");
		} else {
			return ("O" + sqlObjectName.hashCode()).replaceAll("^[_-]", "O").replaceAll("-", "_");
		}
	}

	@Override
	public String getTableAlias(RowDefinition tabRow) {
		return "\"" + super.getTableAlias(tabRow) + "\"";
	}

	@Override
	public String formatForColumnAlias(final String actualName) {
		String formattedName = actualName.replaceAll("\\.", "__");
		return ("DB" + formattedName.hashCode()).replaceAll("-", "_") + "";
	}

	@Override
	public String beginTableAlias() {
		return " ";
	}

	@Override
	public String getSQLTypeOfDBDatatype(QueryableDatatype qdt) {
		if (qdt instanceof DBBoolean) {
			return " NUMBER(1)";
		} else if (qdt instanceof DBString) {
			return " VARCHAR2(1000) ";
		} else if (qdt instanceof DBDate) {
			return " TIMESTAMP ";
//        } else if (qdt instanceof DBLargeObject) {
//            return " LONGBLOB ";
		} else {
			return qdt.getSQLDatatype();
		}
	}

//    @Override
//    public boolean prefersIndexBasedGroupByClause() {
//        return true;
//    }
	@Override
	public Object endSQLStatement() {
		return "";
	}

	@Override
	public String endInsertLine() {
		return "";
	}

	@Override
	public String endDeleteLine() {
		return "";
	}

	@Override
	public Object getLimitRowsSubClauseAfterWhereClause(QueryOptions options) {
		return "";
	}

	@Override
	public String getCurrentUserFunctionName() {
		return "USER";
	}

	@Override
	public String doPositionInStringTransform(String originalString, String stringToFind) {
		return "INSTR(" + originalString + "," + stringToFind + ")";
	}

	@Override
	public String getIfNullFunctionName() {
		return "ISNULL"; //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public String getStringLengthFunctionName() {
		return "LENGTH";
	}

	@Override
	public String doSubstringTransform(String originalString, String start, String length) {
		return " SUBSTR("
				+ originalString
				+ ", "
				+ start
				+ (length.trim().isEmpty() ? "" : ", " + length)
				+ ") ";
	}

	@Override
	public boolean supportsRadiansFunction() {
		return false;
	}

	@Override
	public boolean supportsDegreesFunction() {
		return false;
	}

}
