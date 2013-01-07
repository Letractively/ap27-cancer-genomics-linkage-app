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
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name="person")
public class Person {
	
	private Long id;
	private String firstname;
	private String lastname;
	private String title;
	private String email;
	private String password;
	private Boolean isQfabAdmin;
	private Set<Institution> institutions;
	private Set<DataLocation> datalocations;
	
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name="is_qfab_admin")
	public Boolean getIsQfabAdmin() {
		return isQfabAdmin;
	}
	public void setIsQfabAdmin(Boolean isQfabAdmin) {
		this.isQfabAdmin = isQfabAdmin;
	}
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="person_institute", 
			joinColumns={@JoinColumn(name="person_id")},
			inverseJoinColumns={@JoinColumn(name="institution_id")})
	public Set<Institution> getInstitutions() {
		return institutions;
	}
	public void setInstitutions(Set<Institution> institutions) {
		this.institutions = institutions;
	}
	
	@OneToMany(targetEntity=DataLocation.class,fetch=FetchType.EAGER, 
			cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, 
			mappedBy = "person")
	public Set<DataLocation> getDatalocations() {
		return datalocations;
	}
	public void setDatalocations(Set<DataLocation> datalocations) {
		this.datalocations = datalocations;
	}
		
}
