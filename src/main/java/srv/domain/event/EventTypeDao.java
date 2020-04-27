package srv.domain.event;

import java.util.List;

/**
 * Data Access Object Interface for EvenetType.java
 *  
 * @author Min Shim
 *
 */


public interface EventTypeDao {

	public List<EventType> listAll() throws Exception;
	
	public EventType create(String name, String description) throws Exception;

	/**
	 * @param etid unique EventType ID
	 * @throws Exception
	 */
	public void delete(int etid) throws Exception;

	public void update(int etid, String name, String description) throws Exception;

	/**
	 * @param etid unique EventType ID
	 * @return
	 * @throws Exception
	 */
	public EventType fetchEventTypeById(int etid) throws Exception;
	
}
