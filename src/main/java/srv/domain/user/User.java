package srv.domain.user;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		return true;
	}
	
	
}
