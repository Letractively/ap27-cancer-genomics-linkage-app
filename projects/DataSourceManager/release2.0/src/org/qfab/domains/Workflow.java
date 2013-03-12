package org.qfab.domains;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="workflow")
public class Workflow {
	private	Long id;
	private String name;
	private String description;
	private String rights;
	private String url;
	private Person person;
	private Site site;
	private Boolean hasDOI;
	private String doi;
	private String galaxyId;
	private Boolean isWfPublished;
	private String data_in_name;
	private String data_in_description;
	private String data_in_url;
	private String data_in_rights;	
	private String data_out_name;
	private String data_out_description;
	private String data_out_url;
	private String data_out_rights;
	private Set<SubjectCode> subjects;
	private Boolean isHosted;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String wfname) {
		this.name = wfname;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}	
	
	public void setRights(String rights) {
		this.rights = rights;
	}
	public String getRights() {
		return rights;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="person_id")
	public Person getPerson() {
		return person;
	}
	public void setPerson(Person person) {
		this.person = person;
	}
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	
	@JoinColumn(name="site_id")
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	
	@Column(name="has_doi")
	public Boolean getHasDoi() {
		return hasDOI;
	}
	public void setHasDoi(Boolean hasDOI) {
		this.hasDOI = hasDOI;
	}
	
	
	public String getDoi() {
		return doi;
	}
	public void setDoi(String doi) {
		this.doi = doi;
	}
	
	@Column(name="galaxy_id")
	public String getGalaxyId() {
		return galaxyId;
	}
	public void setGalaxyId(String galaxyId) {
		this.galaxyId = galaxyId;
	}
	
	@Column(name="is_wf_published")
	public Boolean getIsWfPublished() {
		return isWfPublished;
	}
	public void setIsWfPublished(Boolean isWfPublished) {
		this.isWfPublished = isWfPublished;
	}
	
	@Column(name="data_in_name")
	public String getInName() {
		return data_in_name;
	}
	public void setInName(String data_in_name) {
		this.data_in_name = data_in_name;
	}
	
	@Column(name="data_in_description")
	public String getInDescription() {
		return data_in_description;
	}
	public void setInDescription(String data_in_description) {
		this.data_in_description = data_in_description;
	}
	
	@Column(name="data_in_url")
	public String getInUrl() {
		return data_in_url;
	}
	public void setInUrl(String data_in_url) {
		this.data_in_url = data_in_url;
	}
	
	@Column(name="data_in_rights")
	public String getInRights() {
		return data_in_rights;
	}
	public void setInRights(String data_in_rights) {
		this.data_in_rights = data_in_rights;
	}
	
	@Column(name="data_out_name")
	public String getOutName() {
		return data_out_name;
	}
	public void setOutName(String data_out_name) {
		this.data_out_name = data_out_name;
	}

	@Column(name="data_out_description")
	public String getOutDescription() {
		return data_out_description;
	}
	public void setOutDescription(String data_out_description) {
		this.data_out_description = data_out_description;
	}

	@Column(name="data_out_url")
	public String getOutUrl() {
		return data_out_url;
	}
	public void setOutUrl(String data_out_url) {
		this.data_out_url = data_out_url;
	}

	@Column(name="data_out_rights")
	public String getOutRights() {
		return data_out_rights;
	}
	public void setOutRights(String data_out_rights) {
		this.data_out_rights = data_out_rights;
	}
	
	@Column(name="is_hosted")
	public Boolean getIsHosted() {
		return isHosted;
	}
	public void setIsHosted(Boolean isHosted) {
		this.isHosted = isHosted;
	}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="workflow_subject",
		joinColumns={@JoinColumn(name="workflow_id")},
		inverseJoinColumns={@JoinColumn(name="subject_id")})
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.MERGE)
	public Set<SubjectCode> getSubjects() {
		return subjects;
	}
	public void setSubjects(Set<SubjectCode> subjects) {
		this.subjects = subjects;
	}
	

	
	
	//-----------------------Constructors----------------------------//
			public Workflow() {}
			
			public Workflow(String name, String url, String rights, String description, String data_in_name, String data_in_url, String data_in_description, String data_in_rights, String data_out_name, String data_out_url, String data_out_description, String data_out_rights, Boolean hasDOI) {
				this.name = name;			
				this.url = url;
				this.rights = rights;
				this.description = description;
				this.data_in_name = data_in_name;			
				this.data_in_url = data_in_url;
				this.data_in_rights = data_in_rights;
				this.data_in_description = data_in_description;
				this.data_out_name = data_out_name;			
				this.data_out_url = data_out_url;
				this.data_out_rights = data_out_rights;
				this.data_out_description = data_out_description;
				this.hasDOI = hasDOI;
			}
		

}
