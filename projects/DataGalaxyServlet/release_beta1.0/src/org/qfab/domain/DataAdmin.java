package org.qfab.domain;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.RandomStringUtils;
import org.qfab.tools.ToolBox;


@Entity
@Table(name="person")	
public class DataAdmin {
	
	
	private Long id;
	private String email;
	private String unencryptedPwd;
	private String password;
	private Boolean isQfabAdmin;
	private Set<DataLocation> datalocations = new HashSet<DataLocation>();
	public final static int PASSWORD_LENGTH = 12;
    private static final String charset = "!0123456789abcdefghijklmnopqrstuvwxyz"; 
	
	
	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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

	
	public void setIsQfabAdmin(Boolean isQfabAdmin) {
		this.isQfabAdmin = isQfabAdmin;
	}
	
	@Column(name="is_qfab_admin")
	public Boolean getIsQfabAdmin() {
		return isQfabAdmin;
	}
	@Transient
	public String getUnencryptedPwd() {
		return unencryptedPwd;
	}
	public void setUnencryptedPwd(String unencryptedPwd) {
		this.unencryptedPwd = unencryptedPwd;
	}
	
	public void setDatalocations(Set<DataLocation> datalocations) {
		this.datalocations = datalocations;
	}
	
	@OneToMany(targetEntity=DataLocation.class,fetch=FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, mappedBy = "dataAdmin")
	@org.hibernate.annotations.Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
	public Set<DataLocation> getDatalocations() {
		return datalocations;
	}
	
	
	public void initialise(){
		this.generatePassword();
		this.setIsQfabAdmin(false);
	}
	
	private void generatePassword() {
		
//		String pwd = RandomStringUtils.random(12);
		Random rand = new Random(System.currentTimeMillis());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        
        String pwd=sb.toString();
		this.setUnencryptedPwd(pwd);
		this.setPassword(ToolBox.encryptSha(pwd));
		
		
	}
	
	
}
