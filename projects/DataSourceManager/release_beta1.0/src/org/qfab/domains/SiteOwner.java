package org.qfab.domains;

public class SiteOwner {
	private int siteID;
	private int ownerID;
	private int dataLocationID;
	private String siteName;
	private String ownerEmail;
	private boolean isOwner;
	
	
	public int getSiteID() {
		return this.siteID;
	}	
	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}
	
	public int getOwnerID() {
		return this.ownerID;
	}
	public void setOwnerID(int ownerID) {
		this.ownerID = ownerID;
	}
	
	public String getOwnerEmail() {
		return this.ownerEmail;
	}	
	public void setOwnerEmail(String email) {
		this.ownerEmail = email;
	}
	
	public String getSiteName() {
		return this.siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	
	public boolean getIsOwner() {
		return this.isOwner;
	}
	public void setIsOwner(Boolean isOwner) {
		System.out.println("isOwner: " + isOwner);
		this.isOwner = isOwner;
		System.out.println("set isOwner: " + this.isOwner);
	}
	
	public int getDataLocationID() {
		return this.dataLocationID;
	}
	public void setDataLocationID(int dataLocationID) {
		this.dataLocationID = dataLocationID;
	}
}
