package org.qfab.managers;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.qfab.domains.DataOwner;
import org.qfab.domains.Institution;
import org.qfab.domains.Person;
import org.qfab.domains.Workflow;
import org.qfab.utils.DBUtils;
import org.qfab.utils.EmailManager;

public class PersonManager {
	
	public final static int PASSWORD_LENGTH = 12;
    private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz"; 


	public static Person getPersonByEmail(String email) {
	//System.out.println("PersonManager.getPersonByEmail called @ " + new java.util.Date());
	//System.out.println("Email: " + email);
	String hql = "from Person pe where pe.email = \'" +email+"\'";
	List<Person> list = DBUtils.find(hql);
	if (list != null && list.size() > 0) {
		return list.get(0);
	} else {
		return null;
	}		
}
	
	public static Person getPersonById(int personID) {
		String hql = String.format("FROM Person WHERE id = %d", personID);
		List<Person> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	
	public List<DataOwner> returnOwnedData(Person person){
		List<DataOwner> dataOwners = new ArrayList<DataOwner>();
		dataOwners.addAll(person.getAdminData());
		if (dataOwners.size() > 0) {
			return dataOwners;
		} else {
			return null;
		}		
	}
	
	
	public List<DataOwner> returnPartOfData(Person person){
		Set<DataOwner> dataOwners = person.getAdminData();
		List<DataOwner> partdata = new ArrayList<DataOwner>();
		for (DataOwner dataOwner : dataOwners) {
			if(!dataOwner.getIsOwner()){
				partdata.add(dataOwner);
			}
		}
		if (partdata.size() > 0) {
			return partdata;
		} else {
			return null;
		}		
	}
	
	
	public List<Workflow> returnOwnedWorkflow(Person person){
		Set <Workflow> workflows = person.getWorkflows();		
		List<Workflow> workflowlist = new ArrayList<Workflow>(workflows);
		if (workflowlist.size() > 0) {
			return workflowlist;
		} else {
			return null;
		}		
	}
	
	
	public static void saveOrUpdate(String firstname, String lastname, String email, String title, ArrayList<Integer> listIdInstitutions){
		System.out.println("PersonManager.saveOrUpdate called @ " + new java.util.Date());
		Person user= getPersonByEmail(email);
		if (user == null) {
			System.err.println("No user with email (" + email + ") in the database.");
		}		
		user.setFirstname(firstname);
		user.setLastname(lastname);
		user.setTitle(title);
		Set<Institution> listInstitutions = new HashSet<Institution>();
		for (int i = 0; i < listIdInstitutions.size(); i++) {
			Institution institution = InstitutionManager.getInstitutionById(listIdInstitutions.get(i));
			if (institution != null) {
				listInstitutions.add(institution);
			}
		}
		user.setInstitutions(listInstitutions);
		DBUtils.saveToDB(user);
	}
	
	
	//LATER: merge with saveOrUpdate
	public static Person registerNewUser(String firstname, String lastname, String email, 
			String title, Integer institutionID, boolean sendEmail){
		System.out.println("PersonManager.registerNewUser called @ " + new java.util.Date());
		String randomPassword = getRandomPassword(PASSWORD_LENGTH);	
		String encryptPassword = encryptSha(randomPassword);
		Person user = getPersonByEmail(email);
		if (user == null) {
			user = new Person();
			user.setEmail(email);
			user.setPassword(encryptPassword);
			user.setIsQfabAdmin(false);
			
			//optional fields
			if (firstname != null && firstname.length() >0) {
				user.setFirstname(firstname);
			}
			if (lastname != null && lastname.length() > 0) {
				user.setLastname(lastname);
			}
			if (title != null && title.length() > 0) {
				user.setTitle(title);
			}
			if (institutionID != null) {
				Set<Institution> listInstitutions = new HashSet<Institution>();
				Institution institution = InstitutionManager.getInstitutionById(institutionID);
				if (institution != null) {
					listInstitutions.add(institution);
				}
				user.setInstitutions(listInstitutions);
			}
			DBUtils.saveToDB(user);
			
			if (sendEmail) {
				EmailManager.sendRegistrationEmail(firstname, email, randomPassword);				
			}
			user=PersonManager.getPersonByEmail(email);
		}
		
		return user;
	}
	
public static void resetPassword(String email) {                
	String newPassword = getRandomPassword(PASSWORD_LENGTH);
    Person user= getPersonByEmail(email);                
    if (user != null) {
    	user.setPassword(encryptSha(newPassword));                        
    	DBUtils.saveToDB(user);                        
    	EmailManager.sendForgotPasswordEmail(user.getFirstname(),email, newPassword);                
    }
}
    
public static boolean changePassword(String email, String currentPassword, String newPassword) {
	Person user = getPersonByEmail(email);
	if (user != null && user.getPassword().equals(encryptSha(currentPassword))) {                        
		user.setPassword(encryptSha(newPassword));
		DBUtils.saveToDB(user);
		System.out.println("Password change successful");
		return true;
	}
	return false;
}
	
	
	//------Helper methods to generate a random password--//	 
    private static String getRandomPassword(int length) {
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        
        return sb.toString();
    }

	
	/**
	 * Performs a SHA encryption process on the incoming string parameter.
	 * @param inputStr
	 * @return SHA-encrypted string if successful, or null if there are problems.
	 */
	public static synchronized String encryptSha(String inputStr) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(inputStr.getBytes("UTF-8"));
			byte digest[] = md.digest();
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digest.length; i++) {
			    String hex = Integer.toHexString(0xff & digest[i]);
			    if (hex.length() == 1) sb.append('0');
			    sb.append(hex);
			}
			return sb.toString();
		}
		catch (Exception e) {
			return null;
		}
	}

}
