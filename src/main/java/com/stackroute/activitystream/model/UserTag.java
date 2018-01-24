package com.stackroute.activitystream.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

/*
 * The class "UserTag" will be acting as the data model for the user_tag Table in the database. Please
 * note that this class is annotated with @Entity annotation. Hibernate will scan all package for 
 * any Java objects annotated with the @Entity annotation. If it finds any, then it will begin the 
 * process of looking through that particular Java object to recreate it as a table in your database.
 */
@Entity
@Component
public class UserTag {

	/*
	 * This class should have three fields (userTagId,username,tag). Out of
	 * these three fields, the field userTagId should be the primary key and
	 * should be generated. This class should also contain the getters and
	 * setters for the fields.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userTagId;
	private String userName;
	private String tag;

	public UserTag() {
	}

	public UserTag(String username, String tag) {
		this.userName = username;
		this.tag = tag;
	}

	public int getUserTagId() {
		return userTagId;
	}

	public void setUserTagId(int userTagId) {
		this.userTagId = userTagId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}
}