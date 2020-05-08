package srv.domain.event.eventype;

import java.util.List;

/**
 *  Data Access Object Interface for EventType.java that defines the standard operations
 *  (CRUD) to be performed on the EventType model object. 
 *  
 * @author Lydia House
 *
 */
public interface EventTypeDao {

	public List<EventType> listAll() throws Exception;
	
	public EventType create(String name, String description, int defHours,
			boolean pinHours, Integer scid, Integer sgid) throws Exception;

	/**
	 * @param etid unique EventType ID
	 * @throws Exception
	 */
	public void delete(int etid) throws Exception;

	public void update(int etid, String name, String description, int defHours,
			boolean pinHours, Integer scid, Integer sgid) throws Exception;

	/**
	 * @param etid unique EventType ID
	 * @return
	 * @throws Exception
	 */
	public EventType fetchEventTypeById(int etid) throws Exception;
	
}
