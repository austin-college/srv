package srv.domain;

/**
 * @author Emma Driscoll
 * This class is a data holder for Service groups(organizations on campus) info. 
 *
 */
public class ServiceGroup {
	
	private String name; // name of group
	private Contact contact; // contact for head of group
	
	public ServiceGroup(String name, Contact contact) {
		super();
		this.name = name;
		this.contact = contact;
	}
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
