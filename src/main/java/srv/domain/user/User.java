package srv.domain.user;

import java.util.ArrayList;
import java.io.Serializable;

import srv.AppConstants;
import srv.domain.contact.Contact;

/**
 * An instance of this class represents a user entity, a person who will have access to the web app.
 * The information contained includes, a unique integer id, the username (ex: lhouse18), and their contact information.
 * 
 * @author hunter Couturier
 *
 */
public class User implements Serializable {

	private Integer uid; // unique id for each client for database
	private String username; // the user's identification info to log in example AJ Pritchard, apritchard18
	private Contact contactInfo; // Contact information for the user

	private String roll = AppConstants.ROLE_SERVANT;
	
	
	public String getRoll() {
		return roll;
	}

	public void setRoll(String roll) {
		this.roll = roll;
	}

	/**
	 * Constructs a user with a set userID a
	 * 
	 * @param userID
	 */
	public User(String username) {
		setUsername(username);
	}

	public User(Integer uid, String userID, Contact contactInfo) {
		this.uid = uid;
		this.username = userID;
		this.contactInfo = contactInfo;
	}

	public User() {

	}


	public String getUsername() {
		return username;
	}

	public User setUsername(String username) {
		this.username = username;
		return this;
	}

	public Integer getUid() {
		return uid;
	}

	public User setUid(Integer uid) {
		this.uid = uid;
		return this;
	}

	public Contact getContactInfo() {
		return contactInfo;
	}

	public User setContactInfo(Contact contactInfo) {
		this.contactInfo = contactInfo;
		return this;
	}
}
