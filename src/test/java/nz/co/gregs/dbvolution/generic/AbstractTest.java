/*
 * Copyright 2013 Gregory Graham.
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
package nz.co.gregs.dbvolution.generic;

import nz.co.gregs.dbvolution.DBDatabase;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import net.sourceforge.tedhi.FlexibleDateFormat;
import net.sourceforge.tedhi.FlexibleDateRangeFormat;
import nz.co.gregs.dbvolution.DBTable;
import nz.co.gregs.dbvolution.databases.*;
import nz.co.gregs.dbvolution.example.*;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 *
 * @author Gregory Graham
 */
@RunWith(Parameterized.class)
public abstract class AbstractTest {

	public DBDatabase database;
	Marque myMarqueRow = new Marque();
	CarCompany myCarCompanyRow = new CarCompany();
	public DBTable<Marque> marquesTable;
	DBTable<CarCompany> carCompanies;
	public List<Marque> marqueRows = new ArrayList<Marque>();
	public List<CarCompany> carTableRows = new ArrayList<CarCompany>();
	public static final FlexibleDateFormat TEDHI_FORMAT = FlexibleDateFormat.getPatternInstance("dd/M/yyyy h:m:s", Locale.UK);
	public static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss", Locale.UK);
	public static final FlexibleDateRangeFormat TEDHI_RANGE_FORMAT = FlexibleDateRangeFormat.getPatternInstance("M yyyy", Locale.UK);
	public static String firstDateStr = "23/March/2013 12:34:56";
	public static String secondDateStr = "2/April/2011 1:02:03";
	public static Date march23rd2013 = (new GregorianCalendar(2013, 2, 23, 12, 34, 56)).getTime();
	public static Date april2nd2011 = (new GregorianCalendar(2011, 3, 2, 1, 2, 3)).getTime();

	@Parameters(name = "{0}")
	public static List<Object[]> data() throws IOException, SQLException, ClassNotFoundException {

		List<Object[]> databases = new ArrayList<Object[]>();

		String url = System.getProperty("dbv.url");
		String host = System.getProperty("dbv.host");
		String port = System.getProperty("dbv.port");
		String instance = System.getProperty("dbv.instance");
		String database = System.getProperty("dbv.database");
		String username = System.getProperty("dbv.username");
		String password = System.getProperty("dbv.password");
		String schema = System.getProperty("dbv.schema");		
		
		if (System.getProperty("testSQLite") != null) {
			final SQLiteDB sqliteDB = new SQLiteTestDB(url, username, password);
			databases.add(new Object[]{"SQLiteDB", sqliteDB});
		}
//		if (System.getProperty("testMySQLMXJDB") != null) {
//			databases.add(new Object[]{"SQLMXJDB", MySQLMXJDBInitialisation.getMySQLDBInstance()});
//		}
		if (System.getProperty("testMySQL") != null) {
			databases.add(new Object[]{"MySQLDB", new MySQLTestDatabase(url, username, password)});
		}
//		if (System.getProperty("testMySQLRDS") != null) {
//			databases.add(new Object[]{"MySQLDB-RDS", new MySQLRDSTestDatabase()});
//		}
		if (System.getProperty("testMySQL56") != null) {
			databases.add(new Object[]{"MySQLDB-5.6", new MySQL56TestDatabase(url, username, password)});
		}
		if (System.getProperty("testH2DB") != null) {
			databases.add(new Object[]{"H2DB", new H2TestDatabase(url, username, password)});
		}
		if (System.getProperty("testH2FileDB") != null) {
			databases.add(new Object[]{"H2FileDB",  H2TestDatabase.H2TestDatabaseFromFilename(database, username, password)});
		}
		if (System.getProperty("testH2DataSourceDB") != null) {
			JdbcDataSource h2DataSource = new JdbcDataSource();
			h2DataSource.setUser(username);
			h2DataSource.setPassword(password);
			h2DataSource.setURL(url);
//			h2DataSource.setUser("");
//			h2DataSource.setPassword("");
//			h2DataSource.setURL("jdbc:h2:./dataSourceTest.h2db");
			H2DB databaseFromDataSource = H2TestDatabase.getDatabaseFromDataSource(h2DataSource);
			databases.add(new Object[]{"H2DataSourceDB", databaseFromDataSource});
		}
		if (System.getProperty("testPostgresSQL") != null) {
//			if (host != null) {
//				databases.add(new Object[]{"PostgresSQL", TestPostgreSQL.getTestDatabase()});
//			} else {
				databases.add(new Object[]{"PostgresSQL", TestPostgreSQL.getTestDatabase(url, host, port, instance, username, password, schema)});
//			}
		}
//		if (System.getProperty("testPostgresSQLRDS") != null) {
//			databases.add(new Object[]{"PostgresSQL-RDS", TestPostgreSQL.getRDSTestDatabase()});
//		}
		if (System.getProperty("testNuo") != null) {
			databases.add(new Object[]{"NuoDB", new NuoDB("localhost", 48004L, "dbv", "dbv", "dbv", "dbv")});
		}
//		if (System.getProperty("testOracleAWS") != null) {
//			databases.add(new Object[]{"Oracle11DB", new OracleAWS11TestDB()});
//		}
		if (System.getProperty("testOracleXE") != null) {
			databases.add(new Object[]{"Oracle11DB", new Oracle11XETestDB(host, port, instance, username, password)});
		}
//		if (System.getProperty("testOracle12") != null) {
//			databases.add(new Object[]{"Oracle12DB", new Oracle12DB("dbvtest-oracle12.cygjg2wvuyam.ap-southeast-2.rds.amazonaws.com", 1521, "ORCL", "dbv", "Testingdbv")});
//		}
		if (System.getProperty("testMSSQLServer") != null) {
			databases.add(new Object[]{"MSSQLServer", new MSSQLServerTestDB(host, instance, database, port, username, password)});
		}
		if (System.getProperty("testJTDSSQLServer") != null) {
			databases.add(new Object[]{"JTDSSQLServer", new JTDSSQLServerTestDB(host, instance, database, port, username, password)});
		}
//		if (System.getProperty("testJavaDBMemory") != null) {
//			databases.add(new Object[]{"JavaDBMemory", new JavaDBMemoryDB("localhost", 1527, "dbv", "dbv", "dbv")});
//		}
//		if (System.getProperty("testJavaDB") != null) {
//			databases.add(new Object[]{"JavaDB", new JavaDB("localhost", 1527, "dbv", "dbv", "dbv")});
//		}
		if (System.getProperty("testH2MemoryDB") != null) {
			// Do basic testing
			final H2MemoryDB h2MemoryDB = new H2MemoryTestDB(instance, username, password);
			databases.add(new Object[]{"H2MemoryDB", h2MemoryDB});
		}
		if(databases.isEmpty() ){
			databases.add(new Object[]{"H2BlankDB",  H2MemoryTestDB.blankDB()});
		}

		return databases;
	}

	public AbstractTest(Object testIterationName, Object db) {
		if (db instanceof DBDatabase) {
			this.database = (DBDatabase) db;
			database.setPrintSQLBeforeExecuting(true);
		}
	}

	public String testableSQL(String str) {
		if (str != null) {
			String trimStr = str.trim().replaceAll("[ \\r\\n]+", " ").toLowerCase();
			if ((database instanceof OracleDB) || (database instanceof JavaDB)) {
				return trimStr
						.replaceAll("\"", "")
						.replaceAll(" oo", " ")
						.replaceAll("\\b_+", "")
						.replaceAll(" +[aA][sS] +", " ")
						.replaceAll(" *; *$", "");
			} else if (database instanceof PostgresDB) {
				return trimStr.replaceAll("::[a-zA-Z]*", "");
			} else if ((database instanceof NuoDB)) {
				return trimStr.replaceAll("\\(\\(([^)]*)\\)=true\\)", "$1");
			} else if (database instanceof MSSQLServerDB) {
				return trimStr.replaceAll("[\\[\\]]", "");
			} else if (database instanceof JTDSSQLServerDB) {
				return trimStr.replaceAll("[\\[\\]]", "");
			} else {
				return trimStr;
			}
		} else {
			return str;
		}
	}

	public String testableSQLWithoutColumnAliases(String str) {
		if (str != null) {
			String trimStr = str
					.trim()
					.replaceAll(" DB[_0-9]+", "")
					.replaceAll("[ \\r\\n]+", " ")
					.toLowerCase();
			if ((database instanceof OracleDB)
					|| (database instanceof JavaDB)) {
				return trimStr
						.replaceAll("\"", "")
						.replaceAll("\\boo", "__")
						.replaceAll("\\b_+", "")
						.replaceAll(" *; *$", "")
						.replaceAll(" as ", " ");
			} else if ((database instanceof NuoDB)) {
				return trimStr.replaceAll("\\(\\(([^)]*)\\)=true\\)", "$1");
			} else if ((database instanceof MSSQLServerDB) || database instanceof JTDSSQLServerDB) {
				return trimStr
						.replaceAll("\\[", "")
						.replaceAll("]", "")
						.replaceAll(" *;", "");
			} else {
				return trimStr;
			}
		} else {
			return str;
		}
	}

	@Before
	@SuppressWarnings("empty-statement")
	public void setUp() throws Exception {
		setup(database);
	}

	public void setup(DBDatabase database) throws Exception {
		database.setPrintSQLBeforeExecuting(false);
		database.preventDroppingOfTables(false);
		database.dropTableNoExceptions(new Marque());
		database.createTable(myMarqueRow);

		database.preventDroppingOfTables(false);
		database.dropTableNoExceptions(myCarCompanyRow);
		database.createTable(myCarCompanyRow);

		marquesTable = DBTable.getInstance(database, myMarqueRow);
		carCompanies = DBTable.getInstance(database, myCarCompanyRow);
		carCompanies.insert(new CarCompany("TOYOTA", 1));
		carTableRows.add(new CarCompany("Ford", 2));
		carTableRows.add(new CarCompany("GENERAL MOTORS", 3));
		carTableRows.add(new CarCompany("OTHER", 4));
		carCompanies.insert(carTableRows);

		Date firstDate = DATETIME_FORMAT.parse(firstDateStr);
		Date secondDate = DATETIME_FORMAT.parse(secondDateStr);

		marqueRows.add(new Marque(4893059, "True", 1246974, null, 3, "UV", "PEUGEOT", null, "Y", null, 4, true));
		marqueRows.add(new Marque(4893090, "False", 1246974, "", 1, "UV", "FORD", "", "Y", firstDate, 2, false));
		marqueRows.add(new Marque(4893101, "False", 1246974, "", 2, "UV", "HOLDEN", "", "Y", firstDate, 3, null));
		marqueRows.add(new Marque(4893112, "False", 1246974, "", 2, "UV", "MITSUBISHI", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(4893150, "False", 1246974, "", 3, "UV", "SUZUKI", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(4893263, "False", 1246974, "", 2, "UV", "HONDA", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(4893353, "False", 1246974, "", 4, "UV", "NISSAN", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(4893557, "False", 1246974, "", 2, "UV", "SUBARU", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(4894018, "False", 1246974, "", 2, "UV", "MAZDA", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(4895203, "False", 1246974, "", 2, "UV", "ROVER", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(4896300, "False", 1246974, null, 2, "UV", "HYUNDAI", null, "Y", firstDate, 1, null));
		marqueRows.add(new Marque(4899527, "False", 1246974, "", 1, "UV", "JEEP", "", "Y", firstDate, 3, null));
		marqueRows.add(new Marque(7659280, "False", 1246972, "Y", 3, "", "DAIHATSU", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(7681544, "False", 1246974, "", 2, "UV", "LANDROVER", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(7730022, "False", 1246974, "", 2, "UV", "VOLVO", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(8376505, "False", 1246974, "", null, "", "ISUZU", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(8587147, "False", 1246974, "", null, "", "DAEWOO", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(9971178, "False", 1246974, "", 1, "", "CHRYSLER", "", "Y", firstDate, 4, null));
		marqueRows.add(new Marque(13224369, "False", 1246974, "", 0, "", "VW", "", "Y", secondDate, 4, null));
		marqueRows.add(new Marque(6664478, "False", 1246974, "", 0, "", "BMW", "", "Y", secondDate, 4, null));
		marqueRows.add(new Marque(1, "False", 1246974, "", 0, "", "TOYOTA", "", "Y", firstDate, 1, true));
		marqueRows.add(new Marque(2, "False", 1246974, "", 0, "", "HUMMER", "", "Y", secondDate, 3, null));

//		database.setPrintSQLBeforeExecuting(true);
		marquesTable.insert(marqueRows);
		database.setPrintSQLBeforeExecuting(false);

		database.preventDroppingOfTables(false);
		database.dropTableNoExceptions(new CompanyLogo());
		database.createTable(new CompanyLogo());

		database.preventDroppingOfTables(false);
		database.dropTableNoExceptions(new LinkCarCompanyAndLogo());
		database.createTable(new LinkCarCompanyAndLogo());

		database.setPrintSQLBeforeExecuting(true);
	}

	@After
	public void tearDown() throws Exception {
		tearDown(database);
	}

	public void tearDown(DBDatabase database) throws Exception {
	}

	private static class H2TestDatabase extends H2DB {

		private static H2DB getDatabaseFromDataSource(JdbcDataSource h2DataSource) {
			return new H2DB(h2DataSource);
		}

		public static H2DB H2TestDatabaseFromFilename(String instance, String username, String password) {
			return new H2DB(instance,username, password, false);
		}

		public H2TestDatabase(String url, String username, String password) {
			super(url,username, password);
		}
	}

//	private static class MySQLRDSTestDatabase extends MySQLDB {
//
//		public MySQLRDSTestDatabase() {
//			//super("jdbc:mysql://150.242.43.218:3306/test?createDatabaseIfNotExist=true", "dbv", "Testingdbv");
//			super("jdbc:mysql://dbvtest-mysql.cygjg2wvuyam.ap-southeast-2.rds.amazonaws.com:3306/test?createDatabaseIfNotExist=true", "dbv", "Testingdbv");
//		}
//	}

	private static class MySQL56TestDatabase extends MySQLDB {

		public MySQL56TestDatabase(String url, String username, String password) {
			super(url, username, password);
//			super("jdbc:mysql://52.64.179.175:3306/dbv?createDatabaseIfNotExist=true", "dbv", "dbv");
		}
	}

	private static class MySQLTestDatabase extends MySQLDB {

		public MySQLTestDatabase(String url, String username, String password) {
			super(url,username, password);
//			super("jdbc:mysql://localhost:3306/test?createDatabaseIfNotExist=true", "dbv", "dbv");
		}
	}

	private static class TestPostgreSQL extends PostgresDB {

		protected static PostgresDB getTestDatabase(String url, String host, String port, String database, String username, String password, String schema) {
			//return new PostgresDB("150.242.43.218", 5432, "dbvtest", "dbv", "Testingdbv");
			return new PostgresDB(host, new Integer(port), database, username, password);
		}

//		protected static PostgresDB getRDSTestDatabase() {
			//return new PostgresDB("150.242.43.218", 5432, "dbvtest", "dbv", "Testingdbv");
//			return new PostgresDB("dbvtest-postgresql.cygjg2wvuyam.ap-southeast-2.rds.amazonaws.com", 5432, "dbvtest", "dbv", "Testingdbv");
//		}

//		protected static PostgresDB getLocalTestDatabase() {
//			return new PostgresDB("dbvtest", "dbv", "dbv", "");
//		}
	}

	private static class SQLiteTestDB extends SQLiteDB {

		public SQLiteTestDB(String url, String username, String password) throws SQLException {
			//super("jdbc:sqlite:dbvolutionTest.sqlite", "dbv", "dbv");
			super(url, username, password);
		}
	}

//	private static class OracleAWS11TestDB extends OracleAWS11DB {
//
//		public OracleAWS11TestDB() {
//			super("dbvtest-oracle-se1.cygjg2wvuyam.ap-southeast-2.rds.amazonaws.com", 1521, "ORCL", "dbv", "Testingdbv");
//		}
//	}

	private static class Oracle11XETestDB extends Oracle11XEDB {

		public Oracle11XETestDB(String host, String port, String instance, String username, String password) {
			super(host, Integer.getInteger(port), instance, username, password);
			//super("54.206.70.155", 1521, "XE", "DBV", "Testingdbv2");
			//super("ec2-54-206-23-5.ap-southeast-2.compute.amazonaws.com", 1521, "XE", "DBV", "Testingdbv");
		}
	}

	private static class MSSQLServerTestDB extends MSSQLServerDB {

		public MSSQLServerTestDB(String host, String instance, String database, String port, String username, String password) {
			super(host, instance, database, Integer.getInteger(port), username, password);
//			super("dbvtest-mssql.cygjg2wvuyam.ap-southeast-2.rds.amazonaws.com", "dbvtest", "dbvtest", 1433, "dbv", "Testingdbv");
		}
	}

	private static class JTDSSQLServerTestDB extends JTDSSQLServerDB {

		public JTDSSQLServerTestDB(String host, String instance, String database, String port, String username, String password) {
			super(host, instance, database, Integer.getInteger(port), username, password);
//			super("dbvtest-mssql.cygjg2wvuyam.ap-southeast-2.rds.amazonaws.com", "dbvtest", "dbvtest", 1433, "dbv", "Testingdbv");
		}
	}

	private static class H2MemoryTestDB extends H2MemoryDB {

		public static H2MemoryTestDB blankDB() {
			return new H2MemoryTestDB();
		}
		
		public H2MemoryTestDB() {
			this("memoryTest.h2db", "", "");
		}
		
		public H2MemoryTestDB(String instance, String username, String password) {
			super(instance,username, password, false);
//			super("memoryTest.h2db", "", "", false);
		}
	}
}
