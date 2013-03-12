package org.qfab.manager;

import java.util.List;


import org.qfab.domain.Person;
import org.qfab.tools.DBUtils;

public class PersonManager {
	
	public static Person getDataAdminByEmail(String email) {
		

		String hql = "from Person p where p.email = \'" +email+"\'";
		List<Person> list = DBUtils.find(hql);
		
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}		
	}

}
