package srv.domain.serviceClient;

import java.util.List;

/**
 *  Data Access Object Interface for ServiceClient.java that defines the standard operations
 *  (CRUD) to be performed on the ServiceClient model object. 
 *  
 * @author Lydia House
 *
 */
public interface ServiceClientDao {
	public List<ServiceClient> listAll() throws Exception;

	public ServiceClient create(String name, Integer cid, String bm, String cat) throws Exception; 

	public void delete(int scid) throws Exception;

	public void update(int scid, String newVal) throws Exception;

	public ServiceClient fetchClientId(int scid) throws Exception;


}
