package srv.domain.user;

import java.util.ArrayList;

import srv.domain.contact.Contact;

/**
 * 
 * @author hunter Couturier
 *
 *         User Class holds all information for the User object. This includes
 *         username, and will include email, firstname, lastname,
 *         phonenumber and more.
 */
public class User {

	private Integer uid; // unique id for each client for database
	private String username; // the user's identification info to log in example AJ Pritchard, apritchard18
	private Contact contactInfo; // Contact information for the user

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

	/**
	 * Takes an ArrayList, and from that list pulls 3 users from it and returns them
	 * as a user array
	 * 
	 * @param userList
	 * @return 3 Users from the list
	 * 
	 *
	 */
	public static User[] listAll(ArrayList<User> userList) {
		User[] returnedArray = new User[3];
		// case if there is only one item in the array

		if (userList.size() == 1) {
			returnedArray[0] = userList.get(0);
			returnedArray[1] = null;
			returnedArray[2] = null;

			return returnedArray;
		} else if (userList.size() == 2) {
			// case if there are two items only
			returnedArray[0] = userList.get(0);
			returnedArray[1] = userList.get(1);
			returnedArray[2] = null;

			return returnedArray;
		} else if (userList.size() == 3) {
			// case if there are three items only
			returnedArray[0] = userList.get(0);
			returnedArray[1] = userList.get(1);
			returnedArray[2] = userList.get(2);

			return returnedArray;
		} else if (userList.size() > 3) {
			// case for any value about 3
			int used1 = -1;
			int used2 = -1;
			int used3 = -1;
			/*
			 * block of code here calls math.random to find a random User to add to the list
			 * it then checks the other two numbers to make sure the user it entered to the
			 * list is unique
			 */
			used1 = (int) (Math.random() * userList.size() + 1);
			returnedArray[0] = userList.get(used1);

			do {
				used2 = (int) (Math.random() * userList.size() + 1);
			} while (used1 == used2);

			returnedArray[1] = userList.get(used2);

			do {
				returnedArray[2] = userList.get(used3);
			} while (used3 == used1 || used3 == used2);

			return returnedArray;
		} else {
			return null;
		}
	}

	/**
	 * Takes a username and a User ArrayList will then search the list for the ID
	 * 
	 * @param username
	 * @param userList
	 * @return null if not found; a User if its found
	 */
	public static User findUser(String username, ArrayList<User> userList) {
		if (userList.size() == 0) {
			return null;
		} else {
			int i = 0;
			while (i < userList.size()) {
				if (username == userList.get(i).getUsername()) {
					return userList.get(i);
				}
				i++;
			}
		}
		// if its not in the array
		return null;
	}
	
	/**
	 * Same as above but for the unique database id instead
	 * 
	 * @param uid
	 * @param userList
	 * @return
	 */
	public static User findUser(int uid, ArrayList<User> userList) {
		if (userList.size() == 0) {
			return null;
		} else {
			int i = 0;
			while (i < userList.size()) {
				if (uid == userList.get(i).getUid()) {
					return userList.get(i);
				}
				i++;
			}
		}
		// if its not in the array
		return null;
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
