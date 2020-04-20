package srv.domain.user;

import java.util.ArrayList;

import srv.domain.contact.Contact;

/**
 * 
 * @author hunter Couturier
 *
 *         User Class holds all information for the User object. This includes
 *         username, password and will include email, firstname, lastname,
 *         phonenumber and more.
 */
public class User {

	private Integer uid; // unique id for each client
	private String name; // user's name
	private String role; // the role of the user, we can take it via a string and figure out what role they are fairly easily
	private String userID; // the user's identification info to log in
	private String password; // the user's password to log in
	private Double totalHoursServed; // the total amount of hours served by the user
	private Contact contactInfo; // Contact information for the user
	private Integer cid; // unique ID for contact

	/**
	 * Constructs a user with a set userID and Password
	 * 
	 * @param userID
	 * @param password
	 */
	public User(String userID, String password) {
		setUserID(userID);
		setPassword(password);
	}

	public User(Integer uid, String userID, String password, Double totalHoursServed, Contact contactInfo) {
		this.uid = uid;
		this.userID = userID;
		this.password = password;
		this.totalHoursServed = totalHoursServed;
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
	 * Takes a userID and a User ArrayList will then search the list for the ID
	 * 
	 * @param userID
	 * @param userList
	 * @return null if not found; a User if its found
	 */
	public static User findUser(String userID, ArrayList<User> userList) {
		if (userList.size() == 0) {
			return null;
		} else {
			int i = 0;
			while (i < userList.size()) {
				if (userID == userList.get(i).getUserID()) {
					return userList.get(i);
				}
				i++;
			}
		}
		// if its not in the array
		return null;
	}

	public String getUserID() {
		return userID;
	}

	public User setUserID(String userID) {
		this.userID = userID;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public User setPassword(String password) {
		this.password = password;
		return this;
	}

	public Double getTotalHoursServed() {
		return totalHoursServed;
	}

	public User setTotalHoursServed(Double totalHoursServed) {
		this.totalHoursServed = totalHoursServed;
		return this;
	}

	public Integer getUid() {
		return uid;
	}

	public User setUid(Integer uid) {
		this.uid = uid;
		return this;
	}

	public Integer getCid() {
		return cid;
	}

	public User setCid(Integer cid) {
		this.cid = cid;
		return this;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Contact getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(Contact contactInfo) {
		this.contactInfo = contactInfo;
	}
}
