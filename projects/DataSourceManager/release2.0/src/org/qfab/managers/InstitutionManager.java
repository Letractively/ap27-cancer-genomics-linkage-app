package org.qfab.managers;

import java.util.ArrayList;
import java.util.List;

import org.qfab.domains.Institution;
import org.qfab.utils.DBUtils;

public class InstitutionManager {
	
	public static ArrayList<Institution> getAllInstitutions() {
		String hql = "from Institution";
		List<Institution> list = DBUtils.find(hql);
		if (list.size() > 0) {
			return (ArrayList<Institution>) list;
		} else {
			return null;
		}		
	}

	public static Institution getInstitutionById(Integer id) {
		String hql = "from Institution instit where instit.id ="+id+"";
		List<Institution> list = DBUtils.find(hql);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}		
	}

}
