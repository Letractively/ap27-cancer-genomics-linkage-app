package org.qfab.managers;

import java.util.List;

import org.qfab.domains.SubjectCode;
import org.qfab.utils.DBUtils;

public class SubjectCodeManager {

	
	public static SubjectCode getSubjectByCodeAndType(Integer code, String type) {
		String hql = String.format("FROM SubjectCode WHERE code = %d AND type = '%s'",code, type);
		List<SubjectCode> list = DBUtils.find(hql);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}		
	}
}
