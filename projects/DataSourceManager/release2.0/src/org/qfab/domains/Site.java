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
@Table(name="site")
public class Site {
	
	private Long id;
	private Institution institution;
	private String name;
	private String description;
	private String rights;
	private String url;
	private Boolean isVerified;
	private Set<Person> siteAdmins;
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="institution_id")
	public Institution getInstitution() {
		return institution;
	}
	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRights() {
		return rights;
	}
	public void setRights(String rights) {
		this.rights = rights;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Column(name="is_verified")
	public Boolean getIsVerified() {
		return isVerified;
	}
	public void setIsVerified(Boolean isVerified) {
		this.isVerified = isVerified;
	}
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="site_admin", 
			joinColumns={@JoinColumn(name="site_id")},
			inverseJoinColumns={@JoinColumn(name="person_id")})
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.MERGE)
	public Set<Person> getSiteAdmins() {
		return siteAdmins;
	}
	
	public void setSiteAdmins(Set<Person> siteAdmins) {
		this.siteAdmins = siteAdmins;
	}
	
	

}
