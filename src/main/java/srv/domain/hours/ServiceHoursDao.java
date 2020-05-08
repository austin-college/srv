package srv.domain.hours;

import java.util.List;


/**
 * Data Access Object Interface for ServiceHours.java. Defines the standard
 * operations (CRUD) to be performed on ServiceHours model object. 
 * 
 * @author fancynine9
 *
 */
public interface ServiceHoursDao {

	public List<ServiceHours> listAll() throws Exception;
	
	public ServiceHours create(Integer scid, Integer uid, Integer eid, double hours, String stat, String reflection, String description) throws Exception;

	public void delete(int shid) throws Exception;
	
	public void update(Integer shid, Integer scid, Integer uid, Integer eid, double hours, String stat, String reflection, String description) throws Exception;
	
	public ServiceHours fetchHoursById(int shid) throws Exception;
	
	
}
