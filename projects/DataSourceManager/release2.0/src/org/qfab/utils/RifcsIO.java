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
import org.ands.rifcs.base.Party;
import org.ands.rifcs.base.RIFCS;
import org.ands.rifcs.base.RIFCSException;
import org.ands.rifcs.base.RIFCSWrapper;
import org.ands.rifcs.base.RegistryObject;
import org.ands.rifcs.base.RelatedObject;
import org.ands.rifcs.base.Relation;
import org.ands.rifcs.base.Service;
import org.ands.rifcs.base.Subject;
import org.qfab.domains.DataLocation;
import org.qfab.domains.DataSource;
import org.qfab.domains.SubjectCode;
import org.qfab.domains.Institution;
import org.qfab.domains.Site;
import org.qfab.domains.Workflow;
import org.qfab.managers.DataLocationManager;
import org.qfab.managers.DataSourceManager;
import org.qfab.managers.SiteManager;
import org.qfab.managers.WorkflowManager;

public class RifcsIO {
	
	private RIFCSWrapper rifcsWrapper;
	private RIFCS rifcs = null;

static {
	try {
		Properties props = PropertyLoader.loadProperties("config.properties");
		System.setProperty("RifcsDir",props.getProperty("rifcs.dir"));
		System.setProperty("serverprod",props.getProperty("server.production"));
		System.setProperty("originatingsource", props.getProperty("originating.source"));
		System.setProperty("ServiceKey", props.getProperty("service.key"));
		System.setProperty("CollectionKey", props.getProperty("collection.key"));
		System.setProperty("PartyKey", props.getProperty("party.key"));
		System.setProperty("ActivityKey", props.getProperty("activity.key"));
		System.setProperty("WorkflowKey", props.getProperty("workflow.key"));
		System.setProperty("WorkflowInput", props.getProperty("wf.input.collection.key"));
		System.setProperty("WorkflowOutput", props.getProperty("wf.output.collection.key"));
		System.setProperty("GVLKey", props.getProperty("relatedObject.GVL.key"));
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
		System.out.println("The rifcs.test.dir is "+rifcsDir);
		
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
		String theKey = System.getProperty("CollectionKey")+ datasource.getId();
		r.setKey(theKey);
		r.setGroup("QFAB");
		r.setOriginatingSource(System.getProperty("originatingsource"));
		
		//create Collection
		Collection coll = r.newCollection();
		coll.setType("dataset");
		
		// collection identifier
		Identifier i = coll.newIdentifier();
		i.setType("local");
		i.setValue(System.getProperty("CollectionKey") + datasource.getId());
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
		List<Site> sites = DataSourceManager.getPublishedSites(datasource);
			for (Site uniquesite : sites) {
				if(uniquesite.getIsVerified()){
				RegistryObject rosite = rifcs.newRegistryObject();
				rosite = generateSiteRifcs(uniquesite,rosite);
				rifcs.addRegistryObject(rosite);
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

public boolean writeRifcsforWorkflow(int workflowId, String workflowURL) throws Exception{
	Workflow workflow = null;
	if (workflowId != 0){
		//int wfId = Integer.parseInt(workflowId);
		workflow = WorkflowManager.getWorkflowById(workflowId);
	}else{
		workflow = WorkflowManager.getWorkflowByURL(workflowURL);
	}
	File file=null;
	try{
		rifcsWrapper = new RIFCSWrapper();
		rifcs = rifcsWrapper.getRIFCSObject();
		
		String rifcsDir = System.getProperty("RifcsDir");
		System.out.println("The rifcs.dir is "+rifcsDir);
		
		file = new File(rifcsDir+"workflow"+workflow.getId()+".xml");
		System.out.println("file is "+file.getAbsolutePath());
		if(file.exists()){
			System.out.println("file exists so we delete it...");
			file.delete();
		}else{
			System.out.println("This file doesn't exist");
		}
		RegistryObject r = rifcs.newRegistryObject();
		
		// set basic info for the registry object
		String theKey = System.getProperty("WorkflowKey")+ workflow.getId();
		r.setKey(theKey);
		r.setGroup("QFAB");
		r.setOriginatingSource(System.getProperty("originatingsource"));
		
		// create service
		Service s = r.newService();
		s.setType("transform");
		
		// service identifier
		Identifier i = s.newIdentifier();
		i.setType("local");
		i.setValue( System.getProperty("WorkflowKey")+ workflow.getId());
		s.addIdentifier(i);
		
		// set primary name
		Name n = s.newName();
		n.setType("primary");
		NamePart np = n.newNamePart();
		np.setValue(workflow.getName());
		n.addNamePart(np);
		s.addName(n);
		
		// Set the landing page url
		Location l = s.newLocation();
		Address a = l.newAddress();
		Electronic e = a.newElectronic();		
		e.setValue(workflow.getUrl());		
		e.setType("url");
		a.addElectronic(e);
		l.addAddress(a);
		s.addLocation(l);
		
		// subject Bioinformatics
		Set<SubjectCode> datasubjects = new HashSet<SubjectCode>();
		datasubjects = workflow.getSubjects();
		for (SubjectCode dataSubject : datasubjects) {			
			s.addSubject(createSubject(s, dataSubject.getCode(), dataSubject.getType()));
		}
		
		// Generate descriptions
		if (!(workflow.getDescription() == null)) {
			s.addDescription(createDescription(s, workflow.getDescription(), "full"));
		} else {				
			s.addDescription(createDescription(s, "", "full"));
		}
		// Generate rights
		//TODO:check what are access policy should it replace description accessRights?
		if (!(workflow.getRights() == null)) {
			s.addDescription(createDescription(s, workflow.getRights(), "accessRights"));
		} else {
			s.addDescription(createDescription(s, "", "accessRights"));
		}
		//Add workflow owner as Related Object "isManagedBy"
		//create Party
		RegistryObject ro = rifcs.newRegistryObject();		
		ro = createParty(workflow,System.getProperty("PartyKey")+workflow.getPerson().getId());		
		RelatedObject roOwner = s.newRelatedObject();
		roOwner.setKey(ro.getKey());
		roOwner.addRelation("isManagedBy", null, null, null);
		s.addRelatedObject(roOwner);
		rifcs.addRegistryObject(ro);
		
		
		if (!(workflow.getInUrl() == null)){
			RegistryObject rIn = rifcs.newRegistryObject();
			rIn = createWorkflowCollection(workflow,System.getProperty("WorkflowInput")+workflow.getPerson().getId(), true);		
			RelatedObject roIn = s.newRelatedObject();
			roIn.setKey(rIn.getKey());
			roIn.addRelation("operatesOn", null, null, null);
			s.addRelatedObject(roIn);
			rifcs.addRegistryObject(rIn);
		}
		if (!(workflow.getOutUrl() == null)){
			RegistryObject rOut = rifcs.newRegistryObject();
			rOut = createWorkflowCollection(workflow,System.getProperty("WorkflowOutput")+workflow.getPerson().getId(), false);		
			RelatedObject roOut = s.newRelatedObject();
			roOut.setKey(rOut.getKey());
			roOut.addRelation("produce", null, null, null);
			s.addRelatedObject(roOut);
			rifcs.addRegistryObject(rOut);
		}
		
		RegistryObject rsite = rifcs.newRegistryObject();
		rsite = generateSiteRifcs(workflow.getSite(),rsite);
		RelatedObject rosite = s.newRelatedObject();
		rosite.setKey(rsite.getKey());
		s.addRelatedObject(rosite);
		rosite.addRelation("isSupportedBy", null, null, null);
		rifcs.addRegistryObject(rsite);	
		
		
		RelatedObject roGVL = s.newRelatedObject();
		roGVL.setKey(System.getProperty("GVLKey"));
		roGVL.addRelation("hasAssociationWith", null, null, null);
		s.addRelatedObject(roGVL);
		
		r.addService(s);
		
		rifcs.addRegistryObject(r);
		OutputStream fileOutput = new FileOutputStream(file);
		rifcsWrapper.write(fileOutput);
		workflow.setIsWfPublished(true);
		DBUtils.saveToDB(workflow);
		
	}catch (Exception e) {
	e.printStackTrace();
	return false;
	}
return true;

}

private RegistryObject generateSiteRifcs(Site site, RegistryObject rosite){
	
	try{
		// set basic info for the registry object
		String theKey = System.getProperty("ServiceKey")+site.getId();
		rosite.setKey(theKey);
		rosite.setGroup("QFAB");
		rosite.setOriginatingSource(System.getProperty("originatingsource"));
		//Create Service
		Service serv;
		serv=rosite.newService();
		serv.setType("transform");
		
		
		Identifier i = serv.newIdentifier();
		i.setType("local");
		i.setValue(System.getProperty("ServiceKey")+site.getId());
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
	List<Site> sites = DataSourceManager.getPublishedSites(datasource);
	for (Site uniquesite : sites) {
		if(uniquesite.getIsVerified()){
		String thesiteKey = System.getProperty("ServiceKey")+uniquesite.getId();
		RelatedObject ro = coll.newRelatedObject();
		ro.setKey(thesiteKey);
		ro.addRelation("isAvailableThrough", null, null, null);
		coll.addRelatedObject(ro);
		}
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

/**
 * Generates a subject for a service
 * @param service
 * @param value
 * @param type
 * @return
 * @throws RIFCSException
 */

private Subject createSubject(Service s, String value, String type) throws RIFCSException {
	Subject sub = s.newSubject();
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
private Description createDescription(Service s, String value, String type) throws RIFCSException {
	Description des = s.newDescription();
	des.setType(type);
	des.setValue(value);
	
	return des;
}

/**
 * Generates a Related object
 * @param key
 * @return
 * @throws RIFCSException
 */
private RelatedObject createRelatedObject(Service s, String key, String relationType) throws RIFCSException {
	RelatedObject ro = s.newRelatedObject();
	Relation r = ro.newRelation();
	r.setType(relationType);
	ro.setKey(key);
	ro.addRelation(r);			
	return ro;
}
/**
 * Generates a Registry object party
 * @param key
 * @return
 * @throws RIFCSException
 */
private RegistryObject createParty(Workflow workflow, String key) throws RIFCSException {
	RegistryObject r = rifcs.newRegistryObject();
	r.setKey(key);
	r.setGroup("QFAB");
	r.setOriginatingSource(System.getProperty("originatingsource"));
	Party p = r.newParty();	
	p.setType("party");
    Identifier id = p.newIdentifier();
    id.setType("local");
    id.setValue(key);
    p.addIdentifier(id);

	if(!((workflow.getPerson().getFirstname() == null) && (workflow.getPerson().getLastname()==null))){
		p.newName();
		Name name = p.newName();
	    name.setType("primary");
	    NamePart np = name.newNamePart();
	    np.setType("family");
	    np.setValue(workflow.getPerson().getLastname());
	    name.addNamePart(np);
	    
	    np = name.newNamePart();
	    np.setType("given");
	    np.setValue(workflow.getPerson().getFirstname());
	    name.addNamePart(np);
	    
	    p.addName(name);
	}
    
    Location location = p.newLocation();
    Address address = location.newAddress();
    Electronic el = address.newElectronic();
    el.setType("email");
    el.setValue(workflow.getPerson().getEmail());
    address.addElectronic(el);
    location.addAddress(address);
    p.addLocation(location); 
    
	r.addParty(p); 		
	return r;
}
/**
 * Generates a Registry object collection (input or output) for workflows
 * @param key
 * @return
 * @throws RIFCSException
 */
private RegistryObject createWorkflowCollection(Workflow workflow, String key, boolean inputdata) throws RIFCSException {
	RegistryObject r = rifcs.newRegistryObject();
	r.setKey(key);
	r.setGroup("QFAB");
	r.setOriginatingSource(System.getProperty("originatingsource"));
	Collection coll = r.newCollection();
	coll.setType("dataset");
	
	Identifier i = coll.newIdentifier();
	i.setType("local");
	i.setValue(key);
	coll.addIdentifier(i);
	
	if(inputdata){
		if(!(workflow.getInName() ==null)){
			Name name = coll.newName();
			name.setType("primary");
			NamePart np = name.newNamePart();
			np.setValue(workflow.getInName());
			name.addNamePart(np); 		
			coll.addName(name);
		}
		Location location = coll.newLocation();
		Address address = location.newAddress();
		Electronic ele = address.newElectronic();
		ele.setType("url");
		ele.setValue(workflow.getInUrl());
		address.addElectronic(ele);
		//Add Adresse to Location
		location.addAddress(address);
		//Add Location to collection
		coll.addLocation(location);
		//add description if available
		if(!(workflow.getInDescription() ==null)){
			coll.addDescription(createDescription(coll, workflow.getInDescription(), "full"));
		}
		
	}else{		
		if(!(workflow.getOutName() ==null)){
			Name name = coll.newName();
			name.setType("primary");
			NamePart np = name.newNamePart();
			np.setValue(workflow.getOutName());
			name.addNamePart(np); 		
			coll.addName(name);
		}
		Location location = coll.newLocation();
		Address address = location.newAddress();
		Electronic ele = address.newElectronic();
		ele.setType("url");
		ele.setValue(workflow.getOutUrl());
		address.addElectronic(ele);
		//Add Adresse to Location
		location.addAddress(address); 
		//Add Location to collection
		coll.addLocation(location);
		//add description if available
		if(!(workflow.getOutDescription() ==null)){
			coll.addDescription(createDescription(coll, workflow.getOutDescription(), "full"));
		}
	}
	
	

	r.addCollection(coll);
	return r;	
}

}
