package org.qfab.manager;

import java.util.List;


import org.qfab.domain.DataAdmin;
import org.qfab.tools.DBUtils;

public class DataAdminManager {
	
	public static DataAdmin getDataAdminByEmail(String email) {
		

		String hql = "from DataAdmin da where da.email = \'" +email+"\'";
		List<DataAdmin> list = DBUtils.find(hql);
		
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}		
	}

}
