package srv.domain.servicegroup;

import java.util.List;

import srv.domain.contact.Contact;

/**
 * Data Access Object Interface for EvenetType.java
 * 
 * @author Min Shim
 *
 */
public interface ServiceGroupDao {

	public List<ServiceGroup> listAll() throws Exception;
	
	// Note that cid is the contactId, same as contactInfo from ServiceGroup.java
	public ServiceGroup create(String shortName, String title, int cid) throws Exception;
	
	public void delete(int sgid) throws Exception;
	
	public void update(int sgid, String shortName, String title, int cid) throws Exception;
	
	public ServiceGroup fetchServiceGroupById(int sgid) throws Exception;
}
