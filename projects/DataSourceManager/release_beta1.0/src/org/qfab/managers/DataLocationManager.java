package org.qfab.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.qfab.domains.DataLocation;
import org.qfab.domains.DataSource;
import org.qfab.domains.Person;
import org.qfab.domains.Site;
import org.qfab.domains.SubjectCode;
import org.qfab.utils.DBUtils;
import org.qfab.utils.EmailManager;
import org.qfab.utils.RifcsIO;

public class DataLocationManager {

	public static DataLocation checkIfLocationExists(DataSource datasource,
			Person person) {
		String hql = "from DataLocation dl where dl.dataSource.id = "
				+ datasource.getId() + " and dl.person.id = " + person.getId();
		List<DataLocation> list = DBUtils.find(hql);

		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public static List<DataLocation> getLocationBySiteandDataSource(Site site,
			DataSource dataSource) {
		System.out.println("DataSource Id = " + dataSource.getId());
		System.out.println("Site Id = " + site.getId());
		String hql = "from DataLocation dl where dl.dataSource.id = "
				+ dataSource.getId() + " and dl.site.id = " + site.getId();
		List<DataLocation> list = DBUtils.find(hql);

		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}	
	
	public static DataLocation getLocationById(int dataLocationId) {
		System.out.println("DataLocationManager.getDataLocation called @ " + new java.util.Date());
		System.out.println("Data Location ID: " + dataLocationId);
		String hql = "FROM DataLocation WHERE id = " + dataLocationId;
		List<DataLocation> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public static List<DataLocation> getOtherSupportedSites(int dataLocationId, DataSource dataSource) {
		System.out.println("DataLocationManager.getOtherSupportedSites called @ " + new java.util.Date());
		System.out.println("Data Source: " + dataSource.getName() + " (Id=" + dataSource.getId() + ")");
		
		String hql = "FROM DataLocation WHERE data_id = " + dataSource.getId() + " and id <> " + dataLocationId;
		List<DataLocation> list = DBUtils.find(hql);		
		return list;
	}
	
	public static List<Person> getCoOwners(DataLocation dataLoc) {
		StringBuffer hql = new StringBuffer(); 
		hql.append("SELECT person FROM DataLocation ");
		hql.append(String.format(" WHERE data_id = %d", dataLoc.getDataSource().getId()));
		hql.append(String.format(" AND site_id = %d", dataLoc.getSite().getId()));
		hql.append(String.format(" AND person_id <> %d", dataLoc.getPerson().getId()));
		hql.append(" AND is_owner is TRUE");
		List<Person> list = DBUtils.find(hql.toString());		
		return list;
	}

	public static List<Long> returnCoOwnerIDs(DataLocation dataLoc) {
		StringBuffer hql = new StringBuffer(); 
		hql.append("SELECT person.id FROM DataLocation ");
		hql.append(String.format(" WHERE data_id = %d", dataLoc.getDataSource().getId()));
		hql.append(String.format(" AND site_id = ", dataLoc.getSite().getId()));
		hql.append(String.format(" AND person_id <> %d", dataLoc.getPerson().getId()));
		hql.append(" AND is_owner is TRUE");
		List<Long> list = DBUtils.find(hql.toString());
		return list;
	}
	
	public static Boolean saveOrUpdate(Integer dataLocationId, String currentUser,
			String dsFullName, String dsAbbrev, String dsURL, String dsRights, 
			String dsDescription, List<Map<String, String>> dsSubjects,
			List<Map<String,String>> siteOwners) {
		System.out.printf("DataLocationManager.saveOrUpdate called @ %s\n", 
			new java.util.Date());
		
		if (dataLocationId != null) {
			DataLocation dataLoc = getLocationById(dataLocationId);
			if (dataLoc != null) {
				DataSource ds = dataLoc.getDataSource();
				ds.setName(dsFullName);
				ds.setAbbrev(dsAbbrev);
				ds.setUrl(dsURL);
				ds.setRights(dsRights);
				ds.setDescription(dsDescription);
				
				Set<SubjectCode> subjectCodes = new HashSet<SubjectCode>();
				for (int i = 0; i < dsSubjects.size(); i++) {
					Map<String, String> formSubject = dsSubjects.get(i);
					// FIXME: Should return List<SubjectCode> from SQL instead of a loop
					SubjectCode subj = SubjectCodeManager.getSubjectByCodeAndType(
							Integer.parseInt(formSubject.get("code")), formSubject.get("type"));
					
					if (subj == null) {
						subj = new SubjectCode();
						subj.setCode(formSubject.get("code"));
						subj.setType(formSubject.get("type"));
						subjectCodes.add(subj);
					} else {
						subjectCodes.add(subj);
					}
				}
				ds.setSubjects(subjectCodes);
				dataLoc.setDataSource(ds);
				addSiteOwner(currentUser,dataLocationId, ds, siteOwners, false);
				DBUtils.saveToDB(dataLoc);
				return true;
			} 
			return false;
		} else {
			System.out.println("Creating new Data Location");			
			DataSource ds = DataSourceManager.getDataSourceByURL(dsURL); 
			if (ds == null) {
				ds = new DataSource(dsFullName, dsAbbrev, dsURL, dsRights, dsDescription);
				ds.setIsPublished(false);
				
				Set<SubjectCode> subjectCodes = new HashSet<SubjectCode>();
				for (int i = 0; i < dsSubjects.size(); i++) {
					Map<String, String> formSubject = dsSubjects.get(i);
					//Map<String, String> formSubject = dsSubjects.get(i);
					SubjectCode subj = SubjectCodeManager.getSubjectByCodeAndType(
					Integer.parseInt(formSubject.get("code")), formSubject.get("type"));
					if (subj == null) {
						subj = new SubjectCode();
						subj.setCode(formSubject.get("code"));
						subj.setType(formSubject.get("type"));
						DBUtils.saveToDB(subj);
					}
					subjectCodes.add(subj);
				}
				ds.setSubjects(subjectCodes);
				DBUtils.saveToDB(ds);				
				return addSiteOwner(currentUser,dataLocationId, ds, siteOwners, false);
			} else {
				// data source already exists, user is_part of data location				
				return addSiteOwner(currentUser,dataLocationId, ds, siteOwners, true);
			}
		}
	}
	
	private static boolean addSiteOwner(String currentUser, int currentDataLocID, 
			DataSource ds, List<Map<String,String>> siteOwners, boolean isPartOf) {
		DataLocation currentDataLoc = getLocationById(currentDataLocID);
		
		for(Map<String,String> siteOwner : siteOwners) {
			DataLocation dataLoc = null;
			Person person = null;
			
			int dataLocID = Integer.parseInt(siteOwner.get("dataLocID"));
			try {
				int personID = Integer.parseInt(siteOwner.get("personID"));
				person = PersonManager.getPersonById(personID);
			} catch (NumberFormatException nfe) {
				String email = siteOwner.get("personID");
				person = PersonManager.getPersonByEmail(email); 
				if (person == null) {
					person = PersonManager.registerNewUser(null, null, email, null, null, true);
					
					//FIXME: This loop will end up spamming the co-owner for each
					//data source site.
					String recordTitle = String.format("%s", ds.getName());
					EmailManager.sendCoOwnerEmail(person.getEmail(), "New User", recordTitle);
				}
			} catch (Exception e) {
				// something else went wrong
				return false;
			}
			int siteID = Integer.parseInt(siteOwner.get("siteID"));
			Site site = SiteManager.getSiteById(siteID);
			boolean isOwner = Boolean.parseBoolean(siteOwner.get("isOwner"));
			if (dataLocID > -1 && !isOwner) {
				DBUtils.delete(getLocationById(dataLocID));
			} else if (dataLocID == -1 && isOwner) {
				if (person != null && site != null) {
					dataLoc = new DataLocation();
					dataLoc.setDataSource(ds);
					dataLoc.setPerson(person);
					dataLoc.setSite(site);
					if (isPartOf && !currentUser.equals(person.getEmail()) ) {						
						dataLoc.setIsOwner(false);
						dataLoc.setIsAvailable(false);
					} else {
						dataLoc.setIsOwner(isOwner);
						dataLoc.setIsAvailable(currentDataLoc.getIsAvailable());
					}					
					DBUtils.saveToDB(dataLoc);
				}
			}
			
		}
		return true;
	}
	
	public static Boolean stopOwning(Integer dataLocationId) {
		System.out.println("DataLocationManager.stopOwning called @ " + new java.util.Date());
		System.out.println("Data Location ID: " + dataLocationId);
		DataLocation dataLoc = getLocationById(dataLocationId);
		if (dataLoc != null) {
			int dataSourceID = dataLoc.getDataSource().getId().intValue();
			int siteID = dataLoc.getSite().getId().intValue();
			DataSource datasource = new DataSource();	
			//DONE: Delete data_location for all co-owners at the same site			
			String hql = String.format("DELETE FROM DataLocation WHERE data_id = %d and site_id = %d", 
					dataSourceID, siteID);
			DBUtils.delete(hql);
			
			//DONE: Find new owner for data_source
			//DONE: send email to new owner
			//DONE: select owner that has 'is_available = true'
			//We first look for potential owner having their site already published on RDA
			hql = String.format("FROM DataLocation WHERE data_id = %d and is_available = true ORDER BY date_created ASC", 
					dataSourceID);
			List<DataLocation> newdatalocations = DBUtils.find(hql);
			if (newdatalocations != null && newdatalocations.size() > 0) {
				DataLocation datalocation = newdatalocations.get(0);
				List<DataLocation> datalocations = DataLocationManager.getLocationBySiteandDataSource(datalocation.getSite(), datalocation.getDataSource());
				for (DataLocation dataLocationbySite : datalocations) {
					datalocation.setIsOwner(true);
					DBUtils.saveToDB(datalocation);
					Person newOwner= datalocation.getPerson();
					EmailManager.sendNewDataOwnerEmail(newOwner.getEmail(), newOwner.getFirstname(), datalocation.getDataSource().getName());
				}
			}else{
			hql = String.format("FROM DataLocation WHERE data_id = %d ORDER BY date_created ASC", 
					dataSourceID);
			newdatalocations = DBUtils.find(hql);
			if (newdatalocations != null && newdatalocations.size() > 0) {
				DataLocation datalocation = newdatalocations.get(0);
				datalocation.setIsOwner(true);
				DBUtils.saveToDB(datalocation);
				Person newOwner= datalocation.getPerson();
				EmailManager.sendNewDataOwnerEmail(newOwner.getEmail(), newOwner.getFirstname(), datalocation.getDataSource().getName());
			}
		}
			RifcsIO rifcs = new RifcsIO();
			datasource = DataSourceManager.getDataSourceById(dataSourceID);
			try {
				rifcs.writeRifcsforDataSource(datasource);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//DONE: Update RIF-CS, remove 'supported-by' site party if applicable
		}
		return true;
	}
	
	public static Boolean stopHosting(Integer dataLocationId) {
	System.out.println("DataLocationManager.stopHosting called @ " + new java.util.Date());
	System.out.println("Data Location ID: " + dataLocationId);
		DataLocation dataLoc = getLocationById(dataLocationId);
		if (dataLoc != null) {
			int dataSourceID = dataLoc.getDataSource().getId().intValue();
			int siteID = dataLoc.getSite().getId().intValue();
			DataSource datasource = new DataSource();
			//DONE: Delete data_location for all datalocation for this site at the same site			
			String hql = String.format("DELETE FROM DataLocation WHERE data_id = %d and site_id = %d", 
					dataSourceID, siteID);
			DBUtils.delete(hql);
			RifcsIO rifcs = new RifcsIO();
			datasource = DataSourceManager.getDataSourceById(dataSourceID);
			try {
				rifcs.writeRifcsforDataSource(datasource);
			} catch (Exception e) {
				e.printStackTrace();
			}
			//DONE: Update RIF-CS, remove 'supported-by' site party if applicable
		}
	return true;
	}
	
	public List<Site> getSupportedSites(DataLocation dl) {		
		StringBuffer hql = new StringBuffer();
		hql.append("SELECT distinct(site) FROM DataLocation");
		hql.append(" WHERE data_id = ");
		hql.append(dl.getDataSource().getId());
		List<Site> list = DBUtils.find(hql.toString());
		return list;
	}
	
	public Boolean publishAssociatedDataSourcetoRDA(DataLocation dl){
		RifcsIO rifcs = new RifcsIO();	
		try {
			rifcs.writeRifcsforDataSource(dl.getDataSource());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public Boolean publishAndOwn(DataLocation dl){
		String hql = String.format("FROM DataLocation WHERE data_id = %d and is_owner = true", 
				dl.getDataSource().getId());
		List<DataLocation> olddatalocations = DBUtils.find(hql);
		for (DataLocation olddatalocation : olddatalocations) {
			olddatalocation.setIsOwner(false);
			DBUtils.saveToDB(olddatalocation);
		}
		dl.setIsOwner(true);
		DBUtils.saveToDB(dl);
		RifcsIO rifcs = new RifcsIO();	
		try {
			rifcs.writeRifcsforDataSource(dl.getDataSource());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public Boolean publishSite(DataLocation dl){
		List<DataLocation> datalocations = DataLocationManager.getLocationBySiteandDataSource(dl.getSite(), dl.getDataSource());
		for (DataLocation dataLocation : datalocations) {
			dataLocation.setIsAvailable(true);
			DBUtils.saveToDB(dataLocation);
		}
		RifcsIO rifcs = new RifcsIO();	
		try {
			rifcs.writeRifcsforDataSource(dl.getDataSource());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}
