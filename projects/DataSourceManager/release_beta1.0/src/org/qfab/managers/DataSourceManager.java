package org.qfab.managers;

import java.util.List;

import org.qfab.domains.DataLocation;
import org.qfab.domains.DataSource;
import org.qfab.domains.Site;
import org.qfab.utils.DBUtils;
import org.qfab.domains.Person;
public class DataSourceManager {
	
	public static List<DataSource> getAll() {		
		String hql = "From DataSource";
		List<DataSource> list = DBUtils.find(hql);
		
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	
	public static List<DataSource> getRemainingDataSources(String currentUser) {
		Person person = PersonManager.getPersonByEmail(currentUser);
		if (person == null) { 
			return null;
		}
		String hql = String.format("SELECT * FROM data WHERE id NOT IN (SELECT data_id FROM data_location WHERE person_id = %d)",
				person.getId());
		List<DataSource> list = DBUtils.findBySQLquery(hql, DataSource.class);
		return list;
	}
	
	public static DataSource getDataSourceByURL(String url) {
		String hql = String.format("FROM DataSource WHERE url = '%s'", url);
		List<DataSource> list = DBUtils.find(hql);
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public static List<DataLocation> getCoOwners(DataSource dataSource) {
		StringBuffer hql = new StringBuffer(); 
		hql.append("FROM DataLocation ");
		hql.append(String.format(" WHERE data_id = %d", dataSource.getId()));
		hql.append(" ORDER BY person_id, site_id");
		List<DataLocation> list = DBUtils.find(hql.toString());		
		return list;
	}
	
	
	public static List<Site> getSupportedSites(DataSource ds) {  
		  StringBuffer hql = new StringBuffer();
		  hql.append("SELECT distinct(site) FROM DataLocation");
		  hql.append(" WHERE data_id = ");
		  hql.append(ds.getId());
		  List<Site> list = DBUtils.find(hql.toString());
		  return list;
		 }
	
	public static List<Person> getAdminsForDataSourceinSite(DataSource ds,Site site) {
		System.out.println("DataSourceManager.getAdminsForDataSourceinSite called @ " + new java.util.Date());
		
		  StringBuffer hql = new StringBuffer();
		  hql.append("SELECT distinct(person) FROM DataLocation");
		  hql.append(" WHERE data_id = ");
		  hql.append(ds.getId());
		  hql.append(" and site_id = ");
		  hql.append(site.getId());
		  hql.append(" and is_owner=true");
		  System.out.println(hql.toString());
		  List<Person> list = DBUtils.find(hql.toString());
		  System.out.println("We have "+ list.size()+" Admins");
		  return list;
		 }
	
	public static List<Person> getAdminsForDataSource(DataSource ds) {
		System.out.println("DataSourceManager.getAdminsForDataSource called @ " + new java.util.Date());
		
		  StringBuffer hql = new StringBuffer();
		  hql.append("SELECT distinct(person) FROM DataLocation");
		  hql.append(" WHERE data_id = ");
		  hql.append(ds.getId());
		  hql.append(" and is_owner=true");
		  System.out.println(hql.toString());
		  List<Person> list = DBUtils.find(hql.toString());
		  System.out.println("We have "+ list.size()+" Admins");
		  return list;
		 }
	
	public static DataSource getDataSourceById(int datasourceId) {
		System.out.println("DataSourceManager.getDataSourceById called @ " + new java.util.Date());
		System.out.println("Data Source ID: " + datasourceId);
		
		String hql = "FROM DataSource WHERE id = " + datasourceId;
		List<DataSource> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	
	
	

}
