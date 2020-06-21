package srv.domain.user;
import srv.domain.contact.Contact;
import srv.domain.serviceclient.ServiceClient;

/**
 * An instance of this class represents our Admins for the system,
 * they are an advanced user type that main control over our system
 * 
 * @author hunter
 * 
 *
 */
public class AdminUser extends User {
	
	// Default Constructor
	public AdminUser() { }
	
	public AdminUser(Integer uid, String username, Contact contactInfo) {
		super(uid, username, contactInfo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
				
		AdminUser other = (AdminUser) obj;
		
		if(other.getUid() == this.getUid())
			return true;
		return false;
		
	}	
}
