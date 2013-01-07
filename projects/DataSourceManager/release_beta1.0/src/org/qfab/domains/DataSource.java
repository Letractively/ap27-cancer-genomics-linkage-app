package org.qfab.domains;

import java.util.HashSet;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="data")
public class DataSource {
	
	private	Long id;
	private String name;
	private String abbrev;
	private String url;
	private String description;
	private String rights;
	private Boolean isPublished;
	private Set<SubjectCode> subjects;
	private Set<DataLocation> datalocations = new HashSet<DataLocation>();
	
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="fullname")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAbbrev() {
		return abbrev;
	}
	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	
	@Column(name="is_published")
	public Boolean getIsPublished() {
		return isPublished;
	}
	public void setIsPublished(Boolean isPublished) {
		this.isPublished = isPublished;
	}
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="data_subject",
		joinColumns={@JoinColumn(name="data_id")},
		inverseJoinColumns={@JoinColumn(name="subject_id")})
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Set<SubjectCode> getSubjects() {
		return subjects;
	}
	public void setSubjects(Set<SubjectCode> subjects) {
		this.subjects = subjects;
	}
	
	@OneToMany(targetEntity=DataLocation.class,fetch=FetchType.EAGER, 
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, 
			mappedBy = "dataSource")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Set<DataLocation> getDatalocations() {
		return datalocations;
	}
	
	public void setDatalocations(Set<DataLocation> datalocations) {
		this.datalocations = datalocations;
	}

	//-----------------------Constructors----------------------------//
	public DataSource() {}
	
	public DataSource(String name, String abbrev, String url, 
			String rights, String description) {
		this.name = name;
		this.abbrev = abbrev;
		this.url = url;
		this.rights = rights;
		this.description = description;
	}
	
	
	//-----------------------Extra Methods----------------------------//
		
/*	public Set<SubjectCode> returnSubjectsByType(SubjectCode.TYPES type) {
		String typeStr = type.toString();
		Set<SubjectCode> subset = new HashSet<SubjectCode>(); 
		Set<SubjectCode> allsubjects = this.getSubjects();
		if (allsubjects != null) {
			for(SubjectCode sub : allsubjects) {
				if (sub.getType().equals(typeStr)) {
					subset.add(sub);
				}
			}
		}
		return subset;
	}*/

}
