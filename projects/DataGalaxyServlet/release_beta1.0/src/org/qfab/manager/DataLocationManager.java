package org.qfab.manager;

import java.util.List;

import org.qfab.domain.DataAdmin;
import org.qfab.domain.DataCollection;
import org.qfab.domain.DataLocation;
import org.qfab.tools.DBUtils;


public class DataLocationManager {

	public static DataLocation checkIfLocationExists(DataCollection datacollection,DataAdmin dataadmin) {
			String hql = "from DataLocation dl where dl.dataCollection.id = "+datacollection.getId()+" and dl.dataAdmin.id = "+dataadmin.getId();
			
			List<DataLocation> list = DBUtils.find(hql);
			
			if (list.size() > 0) {
				
				return list.get(0);
			} else {
				return null;
			}		
		}
		
	}
	