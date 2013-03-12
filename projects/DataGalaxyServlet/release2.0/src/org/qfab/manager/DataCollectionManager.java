package org.qfab.manager;

import java.util.List;

import org.qfab.domain.DataCollection;
import org.qfab.tools.DBUtils;


public class DataCollectionManager {

	public static DataCollection getDataSourceByURL(String url) {
			String hql = "from DataCollection dc where dc.url = \'" +url+"\'";
			List<DataCollection> list = DBUtils.find(hql);
			if (list.size() > 0) {
				return list.get(0);
			} else {
				return null;
			}		
		}
		
	}
	
