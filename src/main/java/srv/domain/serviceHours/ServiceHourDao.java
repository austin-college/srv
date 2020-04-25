package srv.domain.serviceHours;

import java.util.List;


/**
 * Data Access Object Interface for ServiceHours.java. Defines the standard
 * operations (CRUD) to be performed on ServiceHours model object. 
 * 
 * @author fancynine9
 *
 */
public interface ServiceHourDao {

	public List<ServiceHours> listAll() throws Exception;
	
	public ServiceHours create(Integer shid, Integer scid, Integer uid, Integer eid, Double hours, String stat) throws Exception;

	public void delete(int shid) throws Exception;
	
	public void update(Integer shid, Integer scid, Integer uid, Integer eid, Double hours, String stat) throws Exception;
	
	public ServiceHours fetchHoursById(int shid) throws Exception;
	
	
}
