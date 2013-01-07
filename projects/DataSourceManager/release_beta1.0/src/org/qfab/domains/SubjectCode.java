package org.qfab.domains;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="subject_codes")
public class SubjectCode {	
	public static enum TYPES { anzsrc_for, anzsrc_seo };
	
	private Long id;
	private String code;
	private String type;
	private String description;
	private Set<DataSource> datasources;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type= type;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="data_subject", 
			joinColumns={@JoinColumn(name="subject_id")},
			inverseJoinColumns={@JoinColumn(name="data_id")})
	public Set<DataSource> getDataSources() {
		return datasources;
	}
	public void setDataSources(Set<DataSource> datasources) {
		this.datasources = datasources;
	}

}
