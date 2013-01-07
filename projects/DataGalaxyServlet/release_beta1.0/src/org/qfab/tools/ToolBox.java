package org.qfab.tools;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.qfab.domain.DataAdmin;
import org.qfab.domain.DataCollection;
import org.qfab.domain.DataLocation;
import org.qfab.manager.DataAdminManager;
import org.qfab.manager.DataLocationManager;
import org.qfab.manager.DataCollectionManager;

import sun.misc.BASE64Encoder;

public class ToolBox {
	
	
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
			System.out.println(sb.toString());
			return sb.toString();
		}
		catch (Exception e) {
			return null;
		}
	}
		
//	public static synchronized String encryptSha(String inputStr) {
//		try {
//			MessageDigest md = MessageDigest.getInstance("SHA");
//			md.update(inputStr.getBytes("UTF-8"));
//			byte digest[] = md.digest();
//			return (new BASE64Encoder()).encode(digest);
//		}
//		catch (Exception e) {
//			return null;
//		}
//	}

	public static DataLocation createDatasource(String url, String name, String abbrev,
			String description, String email) {
	    DataCollection newDc = null;
	    DataAdmin newDa = null;
	    
		//We need to check if the datasource already exist in the database
		newDc =DataCollectionManager.getDataSourceByURL(url);
	    if(newDc==null){
	    	newDc = new DataCollection();
	    	//we create the new DataCollection
	    	newDc.setDescription(description);
	    	newDc.setName(name);
	    	newDc.setAbbrev(abbrev);
	    	newDc.setUrl(url);
	    	newDc.setIsPublished(false);
	    	DBUtils.saveToDB(newDc);
	    }
	    
	    
	    
	    //We check if the user already exists 
    	newDa = DataAdminManager.getDataAdminByEmail(email);
    	if(newDa==null){
    		newDa = new DataAdmin();
    		//we create a new User
    		newDa.setEmail(email);
    		newDa.initialise();
    		DBUtils.saveToDB(newDa);
    	}
    	
		DataLocation datalocation = new DataLocation();
		//We check if there is already a combination of User/DataCollection
    	DataLocation existingDatalocation = DataLocationManager.checkIfLocationExists(newDc, newDa);
    	
    	
    	if(existingDatalocation==null){
		Date date = new Date();
		datalocation.setDateCreated(date);
		datalocation.setDataAdmin(newDa);
		datalocation.setIsAvailable(false);
		datalocation.setDataCollection(newDc);

		//If the Datasource already has an owner
		System.out.println("The size of datalocation is "+newDc.getDatalocations().size());
		if(newDc.getDatalocations().size()>0){
			datalocation.setIsOwner(false);
		}else{
			datalocation.setIsOwner(true);
		}
		
    	    DBUtils.saveToDB(datalocation);
    	}else{
    		datalocation=existingDatalocation;
    	}
    return datalocation;
		
	}
	

}
