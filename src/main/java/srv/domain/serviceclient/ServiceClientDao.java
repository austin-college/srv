package srv.domain.serviceclient;

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

	public ServiceClient create(String name, Integer cid1, Integer cid2, Integer bmId, String cat) throws Exception; 

	public void delete(int scid) throws Exception;

	public void update(int scid, String name, Integer cid1, Integer cid2, Integer bmId, String cat) throws Exception;

	public ServiceClient fetchClientById(int scid) throws Exception;
}
