package srv.domain.serviceclient;

import java.util.List;

public interface ServiceClientDao {
	public List<ServiceClient> listAll() throws Exception;

	public ServiceClient create(String sc) throws Exception; 

	public void delete(int scid) throws Exception;

	public void update(int scid, String newVal) throws Exception;

	public ServiceClient fetchClientId(int scid) throws Exception;


}
