package org.qfab.tools;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.dbunit.DatabaseTestCase;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.qfab.domains.DataLocation;
import org.qfab.domains.DataSource;
import org.qfab.domains.Institution;
import org.qfab.domains.Person;
import org.qfab.domains.Site;
import org.qfab.managers.DataLocationManager;
import org.qfab.managers.DataSourceManager;
import org.qfab.managers.InstitutionManager;
import org.qfab.managers.PersonManager;
import org.qfab.managers.SiteManager;
import org.qfab.utils.DBUtils;
import org.qfab.domains.SubjectCode;
import org.qfab.utils.RifcsIO;

public class TestSample extends DatabaseTestCase

{
	
	private FlatXmlDataSet loadedDataSet;
	public static final String TABLE_PERSON ="person";
	private IDatabaseTester databaseTester;
	
	
	   public TestSample(String name)
	    {
	        super( name );
	        
	    }
	   
	   protected void setUp() throws Exception
	    {
		   System.out.println("We are in setup");
		   super.setUp();
		   getSetUpOperation();

	    }
	   
	   protected void tearDown() throws Exception{
		   		getTearDownOperation();
	           }
	   
	   protected DatabaseOperation getSetUpOperation() throws Exception{
	           return DatabaseOperation.CLEAN_INSERT;
	   }
	   
	   protected DatabaseOperation getTearDownOperation() throws Exception{
		   System.out.println("And now we delete");
	           return DatabaseOperation.DELETE_ALL;
	   }
	   
	   
	   protected IDatabaseConnection getConnection() throws Exception{
		   System.out.println("We are in getConnection");
			Class driverClass = Class.forName("com.p6spy.engine.spy.P6SpyDriver");
			Connection jdbcConnection = DriverManager.getConnection("jdbc:mysql://servername/dbname", "username", "password");
			IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
			return connection;
		}

	// Load the data which will be inserted for the test
	   protected IDataSet getDataSet() throws Exception{ 
		   loadedDataSet = new FlatXmlDataSetBuilder().build(new FileInputStream("testdata/full.xml"));
	   return loadedDataSet;
	         }
	   
	   public void testCheckPersonDataLoaded() throws Exception{
	   assertNotNull(loadedDataSet);
	   int rowCount = loadedDataSet.getTable(TABLE_PERSON).getRowCount();
	   assertEquals(1, rowCount);
	   }
	   
	   
	   public void testCheckPersonLoading() throws Exception{
		   Person person = PersonManager.getPersonByEmail("email@host");
		   assertEquals(new Long(3), person.getId());
		   assertEquals("hG9YzRu8L7b0mBPa4wt8MFE88n4=", person.getPassword());
		   assertFalse(person.getIsQfabAdmin());
		   assertEquals(1, person.getInstitutions().size());
		   
	   }
	   
	   public void testCheckPersonModifying() throws Exception{
		   Person person = PersonManager.getPersonByEmail("email@host");
		   assertEquals(new Long(3), person.getId());
		   person.setFirstname("John");
		   person.setLastname("Doe");
		   person.setPassword("Password");
		   person.setIsQfabAdmin(true);
		   DBUtils.saveToDB(person);
		   Person newPerson = PersonManager.getPersonByEmail("email@host");
		   assertEquals(new Long(3), newPerson.getId());
		   assertEquals("John", newPerson.getFirstname());
		   assertEquals("Doe", newPerson.getLastname());
		   assertEquals("Password", newPerson.getPassword());
		   assertTrue(newPerson.getIsQfabAdmin());
	   }
	   
	   public void testAddInstitutiontoPerson() throws Exception{	   
		   Institution harvard = new Institution();
		   harvard.setName("Harvard");
		   harvard.setValue("NlaHarvard");
		   Person person = PersonManager.getPersonByEmail("email@host");
		   Set<Person> persons = new HashSet<Person>();
		   persons.add(person);
		   harvard.setPersons(persons);
		   DBUtils.saveToDB(harvard);
		   person = PersonManager.getPersonByEmail("email@host");
		   assertEquals(2, person.getInstitutions().size());
	   }
	   
	   	   
}
