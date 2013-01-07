package org.qfab.managers;

import java.util.ArrayList;
import java.util.List;
import java.lang.StringBuffer;

import org.qfab.domains.Institution;
import org.qfab.domains.Site;
import org.qfab.utils.DBUtils;
import org.qfab.utils.EmailManager;

public class SiteManager {
	
	public static Site getSiteByName(String name){
		String hql = "from Site si where si.name=\'"+name+"\'";
		List<Site> list = DBUtils.find(hql);
		
		if (list.size() > 0) {
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
	
	public static List<Site> getSiteByIDs(List<Integer> siteIds) {
		StringBuffer hql = new StringBuffer();
		hql.append("FROM Site WHERE id in (");
		for(int site : siteIds) {
			hql.append(site);
			hql.append(",");
		}
		hql.replace(hql.length()-1, hql.length(), ")");
		List<Site> sites = DBUtils.find(hql.toString());
		return sites;
	}
	

	public static Boolean registerNewSite(String name, String description, 
			String url, String rights, int institutionID, String email){
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
	
	public static List<Site> getAllSites() {
		String hql = "from Site";
		List<Site> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	public static List<Site> getRemainingSites(List<Site> sites) {
		StringBuffer hql = new StringBuffer();
		if (sites != null && sites.size() > 0) {
			hql.append("from Site where id not in (");
			for(Site site : sites) {
				hql.append(site.getId());
				hql.append(",");
			}		
			hql.replace(hql.length()-1, hql.length(), ")");
		} else {
			hql.append("from Site");
		}
		List<Site> list = DBUtils.find(hql.toString());
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return new ArrayList<Site>();
		}
	}
}
