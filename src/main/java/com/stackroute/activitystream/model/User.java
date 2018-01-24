package com.stackroute.activitystream.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.springframework.stereotype.Component;

/*
 * The class "User" will be acting as the data model for the user Table in the database. Please
 * note that this class is annotated with @Entity annotation. Hibernate will scan all package for 
 * any Java objects annotated with the @Entity annotation. If it finds any, then it will begin the 
 * process of looking through that particular Java object to recreate it as a table in your database.
 */
@Entity
@Component
public class User {

	/*
	 * This class should have three fields (username,name,password). Out of these
	 * three fields, the field username should be the primary key. This class should
	 * also contain the getters and setters for the fields.
	 */
	@Id
	private String username;
	private String name;
	private String password;

	public User(String username, String name, String password) {
		this.name = name;
		this.username = username;
		this.password = password;
	}

	public User() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}