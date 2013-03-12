package org.qfab.domains;

import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="data_location")
public class DataLocation {
	
	private Long id;
	private DataSource dataSource;
	private Site site;
	private Boolean isDlPublished;
	private Date dateCreated;
	private Date dateDeleted;

	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	@JoinColumn(name="data_id")
	public DataSource getDataSource() {
		return dataSource;
	}
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	
	@ManyToOne(fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="site_id")
	public Site getSite() {
		return site;
	}
	public void setSite(Site site) {
		this.site = site;
	}
	
	@Column(name="is_dl_published")
	public Boolean getIsDlPublished() {
		return isDlPublished;
	}
	public void setIsDlPublished(Boolean isDlPublished) {
		this.isDlPublished = isDlPublished;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_created")
	public Date getDateCreated() {
		return dateCreated;
	}
	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}
	
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_deleted")
	public Date getDateDeleted() {
		return dateDeleted;
	}
	public void setDateDeleted(Date DateDeleted) {
		this.dateDeleted = DateDeleted;
	}
	
}
