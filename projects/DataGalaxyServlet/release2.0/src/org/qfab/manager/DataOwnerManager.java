package org.qfab.manager;

import java.util.List;

import org.qfab.domain.Person;
import org.qfab.domain.DataOwner;
import org.qfab.domain.DataCollection;
import org.qfab.tools.DBUtils;

public class DataOwnerManager {
	
	public static DataOwner checkIfDataAdminRelationExists(DataCollection datacollection,Person dataadmin) {
		String hql = "from DataOwner do where do.dataCollection.id = "+datacollection.getId()+" and do.dataAdmin.id = "+dataadmin.getId();		
		List<DataOwner> list = DBUtils.find(hql);
		
		if (list.size() > 0) {
			
			return list.get(0);
		} else {
			return null;
		}		

	}
	
	public static DataOwner checkIfDataOwnerExists(DataCollection datacollection) {
		String hql = "from DataOwner do where do.dataCollection.id = "+datacollection.getId()+" and do.isOwner = 1";		
		List<DataOwner> list = DBUtils.find(hql);
		
		if (list.size() > 0) {
			
			return list.get(0);
		} else {
			return null;
		}		

	}

}
