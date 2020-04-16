package srv.domain.reason;

import java.util.List;

public interface ReasonDao {

	public List<Reason> listAll() throws Exception;
	
	public Reason create(String r) throws Exception; 

	public void delete(int rid) throws Exception;
	
	public void update(int rid, String newVal) throws Exception;

	public Reason getReasonById(int rid) throws Exception;
	

}