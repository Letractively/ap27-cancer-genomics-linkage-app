package org.qfab.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.ands.rifcs.base.Address;
import org.ands.rifcs.base.Collection;
import org.ands.rifcs.base.Description;
import org.ands.rifcs.base.Electronic;
import org.ands.rifcs.base.Identifier;
import org.ands.rifcs.base.Location;
import org.ands.rifcs.base.Name;
import org.ands.rifcs.base.NamePart;
import org.ands.rifcs.base.RIFCS;
import org.ands.rifcs.base.RIFCSException;
import org.ands.rifcs.base.RIFCSWrapper;
import org.ands.rifcs.base.RegistryObject;
import org.ands.rifcs.base.RelatedObject;
import org.ands.rifcs.base.Service;
import org.ands.rifcs.base.Subject;
import org.qfab.domains.DataLocation;
import org.qfab.domains.DataSource;
import org.qfab.domains.SubjectCode;
import org.qfab.domains.Institution;
import org.qfab.domains.Site;
import org.qfab.managers.DataLocationManager;
import org.qfab.managers.DataSourceManager;
import org.qfab.managers.SiteManager;

public class RifcsIO {
	
	private RIFCSWrapper rifcsWrapper;
	private RIFCS rifcs = null;

static {
	try {
		Properties props = PropertyLoader.loadProperties("config.properties");
		System.setProperty("RifcsDir",props.getProperty("rifcs.dir"));
		System.setProperty("serverprod",props.getProperty("server.production"));
		System.setProperty("originatingsource", props.getProperty("originating.source"));
	} catch (Exception e) {
			e.printStackTrace();
		}
	}

/**
 * @return the rifcsWrapper
 */
public RIFCSWrapper getRifcsWrapper() {
	return rifcsWrapper;
}

public File writeRifcsforDataSource(DataSource datasource) throws Exception{
	File file=null;
	try{
		rifcsWrapper = new RIFCSWrapper();
		rifcs = rifcsWrapper.getRIFCSObject();
		
		String rifcsDir = System.getProperty("RifcsDir");
		System.out.println("The rifcs.dir is "+rifcsDir);
		
		file = new File(rifcsDir+"datasource"+datasource.getId()+".xml");
		System.out.println("file is "+file.getAbsolutePath());
		if(file.exists()){
			System.out.println("file exists so we delete it...");
			file.delete();
		}else{
			System.out.println("This file doesn't exist");
		}
		RegistryObject r = rifcs.newRegistryObject();
		
		// set basic info for the registry object
		String theKey = "[YOUR.ANDS.KEY]/gvl-datasource-"+ datasource.getId();
		r.setKey(theKey);
		r.setGroup("QFAB");
		r.setOriginatingSource(System.getProperty("originatingsource"));
		
		//create Collection
		Collection coll = r.newCollection();
		coll.setType("dataset");
		
		// collection identifier
		Identifier i = coll.newIdentifier();
		i.setType("local");
		i.setValue("[YOUR.ANDS.KEY]/gvl-datasource-" + datasource.getId());
		coll.addIdentifier(i);
		
		// set primary name
		Name n = coll.newName();
		n.setType("primary");
		NamePart np = n.newNamePart();
		np.setValue(datasource.getName());
		n.addNamePart(np);
		coll.addName(n);
		
		// Set the landing page url
		Location l = coll.newLocation();
		Address a = l.newAddress();
		Electronic e = a.newElectronic();
		e.setValue(datasource.getUrl());
		e.setType("url");
		a.addElectronic(e);
		l.addAddress(a);
		coll.addLocation(l);
		
		// Set collections service as related objects
		setSiteforDatasource(coll, datasource);
		
		
		// subject Bioinformatics
		Set<SubjectCode> datasubjects = new HashSet<SubjectCode>();
		datasubjects = datasource.getSubjects();
		for (SubjectCode dataSubject : datasubjects) {
			System.out.println("in the datasubject loop");
			coll.addSubject(createSubject(coll, dataSubject.getCode(), dataSubject.getType()));
		}

		// Generate descriptions

		if (!(datasource.getDescription() == null)) {
			coll.addDescription(createDescription(coll, datasource.getDescription(), "full"));
		} else {				
			coll.addDescription(createDescription(coll, "", "full"));
		}
		if (!(datasource.getRights() == null)) {
			coll.addDescription(createDescription(coll, datasource.getRights(), "accessRights"));
		} else {
			coll.addDescription(createDescription(coll, "", "accessRights"));
		}
		
		// Add related information
		r.addCollection(coll);
		rifcs.addRegistryObject(r);

		
		//We Set up the sites for the collection
		List<Site> sites = DataSourceManager.getSupportedSites(datasource);
			for (Site uniquesite : sites) {
				RegistryObject rosite = rifcs.newRegistryObject();
				rosite = generateSiteRifcs(uniquesite,rosite);
				rifcs.addRegistryObject(rosite);
				List<DataLocation> datalocations = DataLocationManager.getLocationBySiteandDataSource(uniquesite, datasource);
				for (DataLocation dataLocation : datalocations) {
					dataLocation.setIsAvailable(true);
					DBUtils.saveToDB(dataLocation);
				}
				
			}
			
		

        OutputStream fileOutput = new FileOutputStream(file);
		rifcsWrapper.write(fileOutput);
		datasource.setIsPublished(true);
		DBUtils.saveToDB(datasource);
		
	}catch (Exception e) {
		e.printStackTrace();
	}
	return file;
	
}


private RegistryObject generateSiteRifcs(Site site, RegistryObject rosite){
	
	try{
		// set basic info for the registry object
		String theKey = "[YOUR.ANDS.KEY]/datasource-site-"+site.getId();
		rosite.setKey(theKey);
		rosite.setGroup("QFAB");
		rosite.setOriginatingSource(System.getProperty("originatingsource"));
		//Create Service
		Service serv;
		serv=rosite.newService();
		serv.setType("transform");
		
		
		Identifier i = serv.newIdentifier();
		i.setType("local");
		i.setValue("[YOUR.ANDS.KEY]/datasource-site-"+site.getId());
		serv.addIdentifier(i);
		
		// set primary name
		Name n = serv.newName();
		n.setType("primary");
		NamePart np = n.newNamePart();
		np.setValue(site.getName());
		n.addNamePart(np);
		serv.addName(n);
		
		// Set the landing page url
		Location l = serv.newLocation();
		Address a = l.newAddress();
		Electronic e = a.newElectronic();
		e.setValue(site.getUrl());
		e.setType("url");
		a.addElectronic(e);
		l.addAddress(a);
		serv.addLocation(l);
		
		// Set collections service as related objects
		setInstitutionRelatedObjects(serv, site);
		rosite.addService(serv);
		
	} catch (RIFCSException e1) {
		e1.printStackTrace();
	} catch (Exception e1) {
		e1.printStackTrace();
	}
		
			return rosite;
	}

private void setInstitutionRelatedObjects(Service serv, Site site) throws Exception {
	Institution nlainsti;
	nlainsti=site.getInstitution();
	String theNLAKey = nlainsti.getValue();
	RelatedObject ronla = serv.newRelatedObject();
	ronla.setKey(theNLAKey);
	ronla.addRelation("isPartOf", null, null, null);
	serv.addRelatedObject(ronla);
	
}

private void setSiteforDatasource(Collection coll, DataSource datasource) throws RIFCSException {
	List<Site> sites = DataSourceManager.getSupportedSites(datasource);
	for (Site uniquesite : sites) {
		String thesiteKey = "[YOUR.ANDS.KEY]/datasource-site-"+uniquesite.getId();
		RelatedObject ro = coll.newRelatedObject();
		ro.setKey(thesiteKey);
		ro.addRelation("isAvailableThrough", null, null, null);
		coll.addRelatedObject(ro);
	}
	
}


/**
 * Creates a subject for a service
 * @param service
 * @param value
 * @param type
 * @return
 * @throws RIFCSException
 */
private Subject createSubject(Collection coll, String value, String type) throws RIFCSException {
	Subject sub = coll.newSubject();
	sub.setType(type);
	sub.setValue(value);		
	return sub;
}



/**
 * Creates a description for a service
 * @param service
 * @param value
 * @param type
 * @return
 * @throws RIFCSException
 */
private Description createDescription(Collection coll, String value, String type) throws RIFCSException {
	Description desc = coll.newDescription();
	desc.setType(type);
	desc.setValue(value);
	
	return desc;
}
}
