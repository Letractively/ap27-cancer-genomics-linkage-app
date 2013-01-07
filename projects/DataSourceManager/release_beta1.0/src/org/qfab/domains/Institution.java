package org.qfab.domains;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="institution")
public class Institution {
	
	private Long id;
	private String name;
	private String value;
	private Set<Person> persons;
	
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
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="nla_value")
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
	public void setPersons(Set<Person> persons) {
		this.persons = persons;
	}
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="person_institute", 
			joinColumns={@JoinColumn(name="institution_id")},
			inverseJoinColumns={@JoinColumn(name="person_id")})
	public Set<Person> getPersons() {
		return persons;
	}
	
	

}
