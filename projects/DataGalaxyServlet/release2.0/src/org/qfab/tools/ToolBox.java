package org.qfab.tools;

import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.qfab.domain.Person;
import org.qfab.domain.DataCollection;
import org.qfab.domain.DataOwner;

import org.qfab.manager.PersonManager;
import org.qfab.manager.DataOwnerManager;
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

	public static DataOwner createDatasource( String url, String name, String abbrev, String description, String email) {
		
	    DataCollection newDc = null;
	    Person newDa = null;
	    
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
    	newDa = PersonManager.getDataAdminByEmail(email);
    	if(newDa==null){
    		newDa = new Person();
    		//we create a new User
    		newDa.setEmail(email);
    		newDa.initialise();
    		DBUtils.saveToDB(newDa);
    	}
    	
		DataOwner dataowner = new DataOwner();
    	DataOwner existingDataAdminRelation = DataOwnerManager.checkIfDataAdminRelationExists(newDc, newDa);
    	if(existingDataAdminRelation==null){    		
    		Date date = new Date();
    		dataowner.setDateCreated(date);
    		dataowner.setDataAdmin(newDa);
    		dataowner.setDataCollection(newDc); 
    		DataOwner existingDataOwner = DataOwnerManager.checkIfDataOwnerExists(newDc);
    		if(existingDataOwner ==null){
    			dataowner.setIsOwner(true);
    		}else{
    			dataowner.setIsOwner(false);
    		}
    		DBUtils.saveToDB(dataowner);
    		
    	}else{    		
    		dataowner = existingDataAdminRelation;
    		DataOwner existingDataOwner = DataOwnerManager.checkIfDataOwnerExists(newDc);
    		if(existingDataOwner ==null){    			
    			dataowner.setIsOwner(true);    				
    		}
    		DBUtils.saveToDB(dataowner);    
    	}

    return dataowner;
		
	}
	


}
