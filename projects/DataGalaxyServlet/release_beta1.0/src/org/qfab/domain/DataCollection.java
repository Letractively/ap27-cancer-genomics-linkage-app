package org.qfab.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;



/**
 * This Class represents stores the information received from the Servlet.
 * @author p.chaumeil
 *
 */

@Entity
@Table(name="data")
public class DataCollection {
	
	private Long id;
	private String name;
	private String abbrev;
	private String description;
	private String url;
	private Boolean isPublished;
	private Set<DataLocation> datalocations = new HashSet<DataLocation>();


@Id
@GeneratedValue
public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

@Column(name="fullname",nullable=false)
public String getName() {
	return name;
}

public void setName(String name) {
	this.name = name;
}

@Column(nullable=false)
public void setAbbrev(String abbrev) {
	this.abbrev = abbrev;
}

public String getAbbrev() {
	return abbrev;
}

@Column(nullable=false)
public String getDescription() {
	return description;
}

public void setDescription(String description) {
	this.description = description;
}

@Column(nullable=false)
public String getUrl() {
	return url;
}

public void setUrl(String url) {
	this.url = url;
}

@Column(name="is_published",nullable=false)
public Boolean getIsPublished() {
	return isPublished;
}

public void setIsPublished(Boolean isPublished) {
	this.isPublished = isPublished;
}

@OneToMany(targetEntity=DataLocation.class,fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "dataCollection")
@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
public Set<DataLocation> getDatalocations() {
	return datalocations;
}

public void setDatalocations(Set<DataLocation> datalocations) {
	this.datalocations = datalocations;
}



}