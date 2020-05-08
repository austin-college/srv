package srv.domain.servicegroup;

import java.util.List;

/**
 *  Data Access Object Interface for ServiceGroup.java that defines the standard operations
 *  (CRUD) to be performed on the ServiceGroup model object. 
 *  
 * @author Lydia House
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
