package org.qfab.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.qfab.domains.DataLocation;
import org.qfab.domains.DataOwner;
import org.qfab.domains.DataSource;
import org.qfab.domains.Person;
import org.qfab.domains.Site;
import org.qfab.domains.SubjectCode;
import org.qfab.utils.DBUtils;
import org.qfab.utils.EmailManager;
import org.qfab.utils.RifcsIO;

public class DataSourceManager {
	
	
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
	
	public static List<DataOwner> getAdminsForDataSource(DataSource ds) {
		System.out.println("DataSourceManager.getAdminsForDataSource called @ " + new java.util.Date());
		  StringBuffer hql = new StringBuffer();
		  hql.append("SELECT distinct(person) FROM DataOwner");
		  hql.append(" WHERE data_id = ");
		  hql.append(ds.getId());
		  hql.append(" and is_owner=true");
		  List<DataOwner> list = DBUtils.find(hql.toString());
		  return list;
		 }
	
	public static List<Site> getPublishedSites(DataSource ds){
		  StringBuffer hql = new StringBuffer();
		  hql.append("SELECT distinct(site) FROM DataLocation");
		  hql.append(String.format(" WHERE data_id = %d", ds.getId()));
		  hql.append(String.format(" and is_dl_published=true"));	 
		  List<Site> list = DBUtils.find(hql.toString());
		  return list;
	}
	
	public static List<Site> getSupportedSites(DataSource ds) {  
		  StringBuffer hql = new StringBuffer();
		  hql.append(String.format("SELECT distinct(site) FROM DataLocation"));
		  hql.append(String.format(" WHERE data_id = %d", ds.getId()));	  
		  List<Site> list = DBUtils.find(hql.toString());
		  return list;
		  // TODO Auto-generated method stubturn list;
		 }
	
	public static List<DataSource> getRemainingDataSources(String currentUser) {
		System.out.println("DataSourceManager.getRemainingDataSources called @ " + new java.util.Date());
		Person person = PersonManager.getPersonByEmail(currentUser);
		if (person == null) { 
			return null;
		}
		String hql = String.format("SELECT * FROM data WHERE id NOT IN (SELECT data_id FROM data_owner WHERE person_id = %d)",
				person.getId());
		List<DataSource> list = DBUtils.findBySQLquery(hql, DataSource.class);
		return list;
	}

	public static Boolean addNewDataOwner(Integer id ,String userEmail, HashSet<String> newusers){
		
		DataSource ds = DataSourceManager.getDataSourceById(id);
		for (String newuser : newusers) {
			Person person = PersonManager.getPersonByEmail(newuser);
			if(person==null){
				person=PersonManager.registerNewUser("", "", newuser, "", null, false);		
			}
			DataOwner dataO = DataOwnerManager.getDataOwnerByPersonAndData(person.getId().intValue(),id);
			if(dataO==null){
				dataO = new DataOwner();
				dataO.setPerson(person);
				dataO.setDataSource(ds);
				dataO.setIsOwner(true);
			}
			dataO.setIsOwner(true);
			EmailManager.sendDataOwnerEmail(newuser,userEmail,ds.getName());
			DBUtils.saveToDB(dataO);
			
			
		}
		return true;
	}

	public static String saveOrUpdate(Integer id, String dsFullName, String dsAbbrev, String dsURL, String dsRights, String dsDescription,String email, List<Map<String, String>> dsSubjects, List<Integer> allcheckedsites,List<Integer> alluncheckedsites){
		Boolean needAdmin = new Boolean(false);
		DataSource ds =null;
		Set<DataLocation> dls = new HashSet<DataLocation>();
		List<DataLocation> dataList = new ArrayList<DataLocation>();
		if(id == null|| id == 0){
			ds=getDataSourceByURL(dsURL);
			if(ds!=null){
				Set<DataOwner> setadmins = ds.getAdminData();
				List<DataOwner> listadmins = new ArrayList<DataOwner>();
				listadmins.addAll(setadmins);
				List<String> emails = new ArrayList<String>();
				for (DataOwner admin : listadmins) {
					emails.add(admin.getPerson().getEmail());
				}
				if(!emails.contains(email)){
					return "The Data Source already exists.Please Contact the admin or select the Data Source in the dropdown list at the top of the form";
				}else{
					return "You already are an owner of this Data Source";
				}
			}	
			ds = new DataSource();
			ds.setIsPublished(false);
			needAdmin = true;
		}else{
			ds=getDataSourceById(id);
			dataList.addAll(dls);
			List<DataOwner> dataAdmins=getAdminsForDataSource(ds);
			
			if(dataAdmins==null || dataAdmins.size()==0){
				needAdmin = true;
			}
		}
		ds.setName(dsFullName);
		ds.setAbbrev(dsAbbrev);
		ds.setRights(dsRights);
		ds.setUrl(dsURL);
		ds.setDescription(dsDescription);
		
		
		Set<SubjectCode> subjectCodes = new HashSet<SubjectCode>();
		for (int i = 0; i < dsSubjects.size(); i++) {
			Map<String, String> formSubject = dsSubjects.get(i);
			// FIXME: Should return List<SubjectCode> from SQL instead of a loop
			SubjectCode subj = SubjectCodeManager.getSubjectByCodeAndType(Integer.parseInt(formSubject.get("code")), formSubject.get("type"));
			
			if (subj == null) {
				System.out.println("this is null");
				subj = new SubjectCode();
				subj.setCode(formSubject.get("code"));
				subj.setType(formSubject.get("type"));
				subjectCodes.add(subj);
			} else {
				subjectCodes.add(subj);
			}
		}
		ds.setSubjects(subjectCodes);
		//DBUtils.saveToDB(ds);
		
		/*This is a tricky method down there
		We have to create a List of DataLocation insteaq of using the Set
		This is because of the date deleted parameter
		Instead of recreating the DataLocation with an empty date deleted we have to modify the preexisting one to avoid conflict in Hibernate
		Only with the list we can get an object by its index*/	
		for (Integer siteId : allcheckedsites) {
			DataLocation dl = DataLocationManager.getLocationBySiteandDataSource(siteId, id);
			if(dl==null){
				dl=new DataLocation();
				dl.setDataSource(ds);
				dl.setSite(SiteManager.getSiteById(siteId));
				dl.setDateCreated(new java.util.Date());
				dl.setIsDlPublished(false);
				dataList.add(dl);
			}else{
				List<Long> dataIds = new ArrayList<Long>();
				for (DataLocation dataLocation : dls) {
					dataIds.add(dataLocation.getId());
				}
				if(!dataIds.contains(dl.getId())){		
					dataList.add(dl);
				}else{
					Integer match = dataIds.indexOf(dl.getId());
					dataList.get(match).setDateDeleted(null);
				}
			}		
		}
		dls=new HashSet<DataLocation>();
		dls.addAll(dataList);
		ds.setDataLocations(dls);
		DBUtils.saveToDB(ds);
		
		for (Integer siteId : alluncheckedsites) {
			DataLocation dl = DataLocationManager.getLocationBySiteandDataSource(siteId, id);
			if(dl!=null){
				dl.setDateDeleted(new java.util.Date());
				DBUtils.saveToDB(dl);
			}			
		}
		
		//We need an admin for this new datasource
		if(needAdmin){
			DataOwner dataowner = new DataOwner();
			dataowner.setDataSource(ds);
			dataowner.setPerson(PersonManager.getPersonByEmail(email));
			dataowner.setIsOwner(true);
			DBUtils.saveToDB(dataowner);
		}
		
		if(ds.getIsPublished()){
			RifcsIO rifcs = new RifcsIO();
			try {
				rifcs.writeRifcsforDataSource(ds);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
		return "Done";
		
	}

	private static DataSource getDataSourceByURL(String dsURL) {
		System.out.println("DataSourceManager.getDataSourceByURL called @ " + new java.util.Date());
		String hql = "FROM DataSource WHERE url = '"+dsURL+"'";
		List<DataSource> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public static Boolean publish(DataSource ds){
		RifcsIO rifcs = new RifcsIO();
		try {
			rifcs.writeRifcsforDataSource(ds);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
