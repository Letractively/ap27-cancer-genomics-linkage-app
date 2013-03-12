package org.qfab.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.qfab.domains.Institution;
import org.qfab.domains.Person;
import org.qfab.domains.Site;
import org.qfab.utils.DBUtils;
import org.qfab.utils.EmailManager;

public class SiteManager {

	
	public static Site getSiteById(int siteId) {
		System.out.println("SiteManager.getSiteById called @ " + new java.util.Date());
		System.out.println("Site ID: " + siteId);
		
		String hql = "FROM Site WHERE id = " + siteId;
		List<Site> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public static Site getSiteByURL(String url){
		String hql = "from Site si where si.url=\'"+url+"\'";
		List<Site> list = DBUtils.find(hql);
		
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}	
	}
	
	public static List<Site> getAllSites() {
		String hql = "from Site WHERE is_verified is true";
		List<Site> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	
	//TODO:Merge getAllSites and getSites, maybe according to param if all sites or all VERIFIED sites	
	public static List<Site> getSites() {
		String hql = "from Site";
		List<Site> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	
	public static List<Site> getSitesforPerson(String email) {
		Person person = PersonManager.getPersonByEmail(email);
		List<Site> list = new ArrayList<Site>();
		list.addAll(person.getAdminSites());
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	
	public static Boolean addNewAdmins(String userEmail, Integer id , HashSet<String> newusers){
		Boolean success = new Boolean(false);
		Site site = SiteManager.getSiteById(id);
		Set<Person> admins = site.getSiteAdmins();
		for (String newuser : newusers) {
			Person person = PersonManager.getPersonByEmail(newuser);
			if(person==null){
				person=PersonManager.registerNewUser("", "", newuser, "", null, true);
			}
			EmailManager.sendNewSiteAdminEmail(newuser,userEmail , site.getName());
			admins.add(person);
		}
		
		success = saveOrUpdate(id,admins);
		return success;
	}
	
	public static Boolean saveOrUpdate(Integer id, Set<Person> admins){
		Site site = getSiteById(id);
		site.setSiteAdmins(admins);
		DBUtils.saveToDB(site);
		return true;
	}
	
	public static Boolean registerNewSite(String name, String description, 
		String url, String rights, Integer institutionID, String email,Boolean isAdmin){
		System.out.printf("SiteManager.registerNewSite called @ %s\n", new java.util.Date());
		System.out.println("Name: " + name);
		System.out.println("Description: " + description);
		System.out.println("Rights: " + rights);
		System.out.println("Url: " + url);
		System.out.println("Institute: " + institutionID);
		System.out.println("Email: "+ email);
		
		Site site = getSiteByURL(url);
		if (site == null) {
			site = new Site();		
			site.setName(name);
			site.setDescription(description);
			site.setRights(rights);
			site.setUrl(url);
			Institution institution = InstitutionManager.getInstitutionById(institutionID);
			site.setInstitution(institution);
			if(isAdmin){
				Set<Person> persons = new HashSet<Person>();
				persons.add(PersonManager.getPersonByEmail(email));
				site.setSiteAdmins(persons);
			}
			site.setIsVerified(false);		
			DBUtils.saveToDB(site);
			EmailManager.sendSiteVerificationEmail(email, site.getName(), site.getDescription(), site.getUrl(), site.getRights(), site.getInstitution().getName());
			System.out.println("New site is registered.");
			return true;		
		} else {			
			System.out.println("Site already exists. No registration done.");
			return false;		
		}
		
	}

}
