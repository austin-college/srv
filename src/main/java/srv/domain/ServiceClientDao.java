package srv.domain;

import java.util.List;

import srv.domain.ServiceClient;

public interface ServiceClientDao {
	public List<ServiceClient> listAll() throws Exception;

	public ServiceClient create(String sc) throws Exception; 

	public void delete(int scid) throws Exception;

	public void update(int scid, String newVal) throws Exception;

	public ServiceClient fetchClientId(int scid) throws Exception;


}
