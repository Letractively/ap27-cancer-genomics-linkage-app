package org.qfab.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.RandomStringUtils;
import org.qfab.tools.ToolBox;


@Entity
@Table(name="data_owner")	
public class DataOwner {
	private Long id;
	private DataCollection dataCollection;
	private Person dataAdmin;
	private Boolean isOwner;
	private Date dateCreated;
	private Boolean isAvailable;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="is_owner")
	public Boolean getIsOwner() {
		return isOwner;
	}
	public void setIsOwner(Boolean isOwner) {
		this.isOwner = isOwner;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="data_id")
	@org.hibernate.annotations.ForeignKey(name="FK_d_l_data")
	public DataCollection getDataCollection() {
		return dataCollection;
	}
	public void setDataCollection (DataCollection dataCollection) {
		this.dataCollection = dataCollection;
	}
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="person_id")
	@org.hibernate.annotations.ForeignKey(name="FK_d_l_person")
	public Person getDataAdmin() {
		return dataAdmin;
	}
	public void setDataAdmin (Person dataAdmin) {
		this.dataAdmin = dataAdmin;
	}
}
