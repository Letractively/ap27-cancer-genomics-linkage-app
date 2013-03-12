package org.qfab.managers;

import java.util.List;

import org.qfab.domains.SubjectCode;
import org.qfab.utils.DBUtils;

public class SubjectCodeManager {

	
	public static SubjectCode getSubjectByCodeAndType(Integer code, String type) {
		System.out.println("in getSubjectByCodeAndType");
		String hql = String.format("FROM SubjectCode WHERE code = %d AND type = '%s'",code, type);
		List<SubjectCode> list = DBUtils.find(hql);
		if (list.size() > 0) {
			System.out.println("We return 1");
			return list.get(0);
		} else {
			System.out.println("We return zero");
			return null;
		}		
	}
}
