package org.qfab.managers;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.metamodel.ListAttribute;

import org.qfab.domains.DataOwner;
import org.qfab.domains.DataSource;
import org.qfab.utils.DBUtils;
import org.qfab.utils.EmailManager;

public class DataOwnerManager {
	
	public static DataOwner getDataOwnerByPersonAndData(Integer userId,Integer dataId){
		System.out.println("DataOwnerManager.getDataOwnerByPersonAndData called @ " + new java.util.Date());
		
		String hql = String.format("FROM DataOwner WHERE data_id= %d and person_id=%d", dataId,userId);
		List<DataOwner> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public static DataOwner getDataOwnerById(Integer id){
		System.out.println("DataOwnerManager.getDataOwnerById called @ " + new java.util.Date());
		StringBuffer hql = new StringBuffer();
		hql.append(String.format("FROM DataOwner WHERE id=%d",id));
		List<DataOwner> list = DBUtils.find(hql.toString());
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public static DataOwner stopAdmin(Integer dataOwnerId){
		DataOwner dataO = getDataOwnerById(dataOwnerId);
		dataO.setIsOwner(false);
		DBUtils.saveToDB(dataO);
		findNextOwner(dataO.getDataSource());
		return dataO;
	}
	
	public static Boolean stopAdminAndRemove(Integer dataOwnerId){
		System.out.println("DataOwnerManager.stopAdminAndRemove called @ " + new java.util.Date());
		DataOwner dataO = getDataOwnerById(dataOwnerId);
		findNextOwner(dataO.getDataSource());
		
		StringBuffer hql = new StringBuffer();
		hql.append(String.format("Delete FROM DataOwner WHERE id=%d",dataO.getId()));
		DBUtils.delete(hql.toString());
		return true;
	}

	
	
	private static void findNextOwner(DataSource dataSource) {
		List<DataOwner> listcurrentmanager = DataSourceManager.getAdminsForDataSource(dataSource);
		if(listcurrentmanager==null || listcurrentmanager.size()==0){
			List<DataOwner> listowners = new ArrayList<DataOwner>();
			listowners.addAll(dataSource.getAdminData());
			DataOwner newowner = listowners.get(0);
			newowner.setIsOwner(true);
			DBUtils.saveToDB(newowner);
			EmailManager.sendNewDataOwnerEmail(newowner.getPerson().getEmail(), newowner.getPerson().getFirstname(), dataSource.getName());
		}
	}

}
