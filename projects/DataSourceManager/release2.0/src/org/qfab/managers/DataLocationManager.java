package org.qfab.managers;

import java.util.List;

import org.qfab.domains.DataLocation;
import org.qfab.domains.DataSource;
import org.qfab.domains.Site;
import org.qfab.utils.DBUtils;

public class DataLocationManager {

	
	public static DataLocation getLocationById(int dataLocationId) {
		System.out.println("DataLocationManager.getDataLocation called @ " + new java.util.Date());
		String hql = "FROM DataLocation WHERE id = " + dataLocationId;
		List<DataLocation> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	
	public static DataLocation getLocationBySiteandDataSource(Integer siteId, Integer dataSourceId) {
		String hql = String.format("from DataLocation dl where dl.dataSource.id=%d and dl.site.id=%d",dataSourceId,siteId);
		List<DataLocation> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}	
	
	public static DataLocation publishDataLocation(Integer dataLocationId){
		DataLocation dl = getLocationById(dataLocationId);
		dl.setIsDlPublished(true);
		DBUtils.saveToDB(dl);
		DataSourceManager.publish(dl.getDataSource());
		return dl;
	}
	
	public static DataLocation unPublishDataLocation(Integer dataLocationId){
		DataLocation dl = getLocationById(dataLocationId);
		dl.setIsDlPublished(false);
		DBUtils.saveToDB(dl);
		DataSourceManager.publish(dl.getDataSource());
		return dl;
	}
	
	public static DataLocation removePublishedSite(Integer dataLocationId){
		DataLocation dl = getLocationById(dataLocationId);
		dl.setIsDlPublished(false);
		dl.setDateDeleted(new java.util.Date());
		DBUtils.saveToDB(dl);
		DataSourceManager.publish(dl.getDataSource());
		return dl;
	}
	
	public static DataLocation removesite(Integer dataLocationId){
		DataLocation dl = getLocationById(dataLocationId);
		dl.setDateDeleted(new java.util.Date());
		DBUtils.saveToDB(dl);
		return dl;
	}
	
	public static DataLocation undoChange(Integer dataLocationId){
		DataLocation dl = getLocationById(dataLocationId);
		dl.setDateDeleted(null);
		DBUtils.saveToDB(dl);
		return dl;
	}
	
	
}
