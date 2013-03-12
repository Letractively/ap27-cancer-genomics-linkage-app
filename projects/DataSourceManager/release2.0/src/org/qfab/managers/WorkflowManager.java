package org.qfab.managers;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.qfab.domains.DataLocation;
import org.qfab.domains.DataSource;
import org.qfab.domains.SubjectCode;
import org.qfab.domains.Workflow;
import org.qfab.domains.Site;
import org.qfab.utils.DBUtils;
import org.qfab.utils.PropertyLoader;
import org.qfab.utils.RifcsIO;
import org.qfab.domains.Person;

public class WorkflowManager {
	
	public static final  Properties props = PropertyLoader.loadProperties("config.properties");
	
	public static List<Workflow> getAll() {		
		String hql = "From Workflow";
		List<Workflow> list = DBUtils.find(hql);
		
		if (list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}
	public static Workflow getWorkflowById(int workflowId) {
		String hql = "FROM Workflow WHERE id = " + workflowId;
		List<Workflow> list = DBUtils.find(hql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}
	public static Workflow getWorkflowByURL(String workflowURL) {
		String hql = String.format("FROM Workflow WHERE url = '%s'", workflowURL);
		List<Workflow> list = DBUtils.find(hql);
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}
	public static Workflow getWorkflowByURLandId(String workflowURL, int workflowId) {
		StringBuffer hql = new StringBuffer(); 
		hql.append(String.format("FROM Workflow WHERE url = '%s'", workflowURL)); 		
		hql.append(" AND id != " + workflowId);
		List<Workflow> list = DBUtils.find(hql.toString());
		if (list != null && list.size() > 0){
			return list.get(0);
		} else {
			return null;
		}
	}	
	public Boolean saveOrUpdate(Integer workflowId, String currentUser,
			String wfName, String wfURL, String wfRights, String wfDescription, 
			List<Map<String, String>> wfSubjects, 
			String inName, String inURL, String inRights, String inDescription, 
			String outName, String outURL, String outRights, String outDescription,
			Integer siteId, Boolean hasDOI) {
		System.out.println("Save or Update Workflow");		
		if (workflowId != null) {						
			Workflow wf = getWorkflowById(workflowId);
			Person owner = wf.getPerson();			
			if(owner.getEmail().equals(currentUser)){
				//check if url already exists for another record
				Workflow wfurl = getWorkflowByURLandId(wfURL, workflowId);
				if (wfurl == null) {
					wf.setUrl(wfURL);
					wf.setName(wfName);
					wf.setDescription(wfDescription);
					wf.setRights(wfRights);
					
					wf.setInUrl(inURL);
					wf.setInName(inName);
					wf.setInDescription(inDescription);
					wf.setInRights(inRights);
					
					wf.setOutUrl(outURL);
					wf.setOutName(outName);
					wf.setOutDescription(outDescription);
					wf.setOutRights(outRights);
					
					Set<SubjectCode> subjectCodes = new HashSet<SubjectCode>();
					for (int i = 0; i < wfSubjects.size(); i++) {
						Map<String, String> formSubject = wfSubjects.get(i);						
						SubjectCode subj = SubjectCodeManager.getSubjectByCodeAndType(
						Integer.parseInt(formSubject.get("code")), formSubject.get("type"));
						if (subj == null) {
							subj = new SubjectCode();
							subj.setCode(formSubject.get("code"));
							subj.setType(formSubject.get("type"));
							DBUtils.saveToDB(subj);
						}
						subjectCodes.add(subj);
					}
					
					Site site = SiteManager.getSiteById(siteId);				
					wf.setSubjects(subjectCodes);
					wf.setSite(site);
					wf.setHasDoi(hasDOI);
					
					DBUtils.saveToDB(wf);
					
					if(wf.getIsWfPublished()){
						RifcsIO rifcs = new RifcsIO();						
						try{
							rifcs.writeRifcsforWorkflow(wf.getId().intValue(), wf.getUrl() );
							}catch (Exception e) {
								e.printStackTrace();
								
							}
						System.out.println("republished");
						
					}	
					return true;
				}else{
					System.out.println("this url exists already for another workflow with id: "+wfurl.getId());
					return false;
				}
			}else{
				//no edit rights, not owner of the workflow
				return false;
			}
		} else {
			System.out.println("Save new Workflow");
			Workflow wf = getWorkflowByURL(wfURL);
			if (wf == null) {
				wf = new Workflow (wfName, wfURL, wfRights, wfDescription, inName, inURL, inDescription, inRights, outName, outURL, outDescription, outRights, hasDOI);
				wf.setIsWfPublished(false);	
				wf.setIsHosted(true);
				
				Set<SubjectCode> subjectCodes = new HashSet<SubjectCode>();
				for (int i = 0; i < wfSubjects.size(); i++) {
					Map<String, String> formSubject = wfSubjects.get(i);
					SubjectCode subj = SubjectCodeManager.getSubjectByCodeAndType(
					Integer.parseInt(formSubject.get("code")), formSubject.get("type"));
					if (subj == null) {
						subj = new SubjectCode();
						subj.setCode(formSubject.get("code"));
						subj.setType(formSubject.get("type"));
						DBUtils.saveToDB(subj);
					}
					subjectCodes.add(subj);
				}
				Site site = SiteManager.getSiteById(siteId);
				Person person = PersonManager.getPersonByEmail(currentUser);
				wf.setSubjects(subjectCodes);
				wf.setSite(site);
				wf.setPerson(person);
				
				DBUtils.saveToDB(wf);
			
				return true;
			} else {
				Person owner = wf.getPerson();
				String email = owner.getEmail();
				if(email.equals(currentUser)){
					wf.setUrl(wfURL);
					wf.setName(wfName);
					wf.setDescription(wfDescription);
					wf.setRights(wfRights);
					
					wf.setInUrl(inURL);
					wf.setInName(inName);
					wf.setInDescription(inDescription);
					wf.setInRights(inRights);
					
					wf.setOutUrl(outURL);
					wf.setOutName(outName);
					wf.setOutDescription(outDescription);
					wf.setOutRights(outRights);
					
					Set<SubjectCode> subjectCodes = new HashSet<SubjectCode>();
					for (int i = 0; i < wfSubjects.size(); i++) {
						Map<String, String> formSubject = wfSubjects.get(i);						
						SubjectCode subj = SubjectCodeManager.getSubjectByCodeAndType(
						Integer.parseInt(formSubject.get("code")), formSubject.get("type"));
						if (subj == null) {
							subj = new SubjectCode();
							subj.setCode(formSubject.get("code"));
							subj.setType(formSubject.get("type"));
							DBUtils.saveToDB(subj);
						}
						subjectCodes.add(subj);
					}
					Site site = SiteManager.getSiteById(siteId);				
					wf.setSubjects(subjectCodes);
					wf.setSite(site);
					wf.setHasDoi(hasDOI);
					wf.setIsHosted(true);
					DBUtils.saveToDB(wf);					
					if(wf.getIsWfPublished()){
						//initiate republishing
						RifcsIO rifcs = new RifcsIO();						
						try{
							rifcs.writeRifcsforWorkflow(wf.getId().intValue(), wf.getUrl() );
							}catch (Exception e) {
								e.printStackTrace();
								
							}						
						System.out.println("republished");
					}	
					return true;
					
				}else{
					System.out.println("wf has already an existing owner");					
					return false;
				}

			}
			
		}
	}

public Boolean stopHosting(int workflowId){
	
	Workflow wf = WorkflowManager.getWorkflowById(workflowId);
	if(wf.getIsWfPublished()){
		//set no longer hosted		
		wf.setIsHosted(false);		
		wf.setUrl(props.getProperty("server.production.stophostingWf")+"?id="+workflowId);
		DBUtils.saveToDB(wf);
		//republishing to point to new landing page
		RifcsIO rifcs = new RifcsIO();		
		try{
			rifcs.writeRifcsforWorkflow(wf.getId().intValue(), wf.getUrl() );
			}catch (Exception e) {
				e.printStackTrace();
				
			}
	}else{
		//delete workflow
		DBUtils.delete(wf);
	}
	return true;
}
	
//	public void publish(String workflowId, String workflowURL){
//		Workflow wf;
//		if(workflowId != null){
//			String hql = "FROM Workflow WHERE id = " + workflowId;
//			List<Workflow> list = DBUtils.find(hql);		
//			if (list != null && list.size() > 0) {
//				wf =list.get(0);
//				wf.setIsWfPublished(true);
//				DBUtils.saveToDB(wf);
//				
//			}
//		}else{
//			if(workflowURL != null){
//				String hql = String.format("FROM Workflow WHERE url = '%s'", workflowURL);
//				List<Workflow> list = DBUtils.find(hql);
//				if (list != null && list.size() > 0) {
//					wf =list.get(0);
//					wf.setIsWfPublished(true);
//					DBUtils.saveToDB(wf);
//			}
//		}
//
//			
//		}
//		
//	}
}
