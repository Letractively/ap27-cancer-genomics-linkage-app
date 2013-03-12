package org.qfab.domain;

import java.util.Date;

import org.hibernate.Session;
import org.qfab.manager.PersonManager;
import org.qfab.manager.DataOwnerManager;
import org.qfab.manager.DataCollectionManager;
import org.qfab.tools.DBUtils;
import org.qfab.tools.ToolBox;

public class TestDataCollection {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		

		
///*	    Session session = DBUtils.currentSession();
//	    session.beginTransaction();*/
//	    
//	    DataCollection newDc = null;
//	    Person newDa = null;
//	    Boolean isNewDatasource =false;
//	    Boolean isNewUser =false;
//	    String email = new String("test@qfab.org");
//	    String description = new String("This is a description");
//	    String name = new String("This is a nameTOTO");
//	    String abbrev = new String ("ABBREV");
//	    String url = new String("http://gvl.qfab.com");
//
//	    
//		//We need to check if the datasource already exist in the database
//		newDc =DataCollectionManager.getDataSourceByURL(url);
//	    if(newDc==null){
//	    	newDc = new DataCollection();
//	    	//we create the new DataCollection
//	    	newDc.setDescription(description);
//	    	newDc.setName(name);
//	    	newDc.setAbbrev(abbrev);
//	    	newDc.setUrl(url);
//	    	newDc.setIsPublished(false);
//		    isNewDatasource=true;
//	    }
//	    
//	    
//	    
//	    //We check if the user already exists 
//    	newDa = PersonManager.getDataAdminByEmail(email);
//    	if(newDa==null){
//    		newDa = new Person();
//    		//we create a new User
//    		newDa.setEmail(email);
//    		newDa.initialise();
//    		DBUtils.saveToDB(newDa);
//    		isNewUser=true;
//    	}
//    	
//
//    		DataOwner dataowner = new DataOwner();
//    		Date date = new Date();
//    		dataowner.setDateCreated(date);
//    		//dataowner.setPerson(newDa);
//    		dataowner.setDataCollection(newDc);
//
//    		//If the Datasource already has an owner
////    		if(newDc.getDataOwner().size()>0){
////    			dataowner.setIsOwner(false);
////    		}else{
////    			dataowner.setIsOwner(true);
////    		}
//    		
//    	DataOwner existingDatalocation = DataOwnerManager.checkIfDataOwnerExists(newDc, newDa);
//    	if(existingDatalocation==null){
//    		DBUtils.saveToDB(dataowner);
//    	}else{
//    		System.out.println("The combination already exist");
//    	}
//	    System.out.println("Id is "+dataowner.getId());
    
	    
	}

}
