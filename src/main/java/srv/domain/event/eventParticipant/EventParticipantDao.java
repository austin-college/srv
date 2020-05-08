package srv.domain.event.eventParticipant;

import java.util.List;


/**
 *  Data Access Object Interface for EventParticipant.java that defines the standard operations
 *  (CRUD) to be performed on the EventParticipant model object. 
 *  
 * @author Lydia House
 *
 */
public interface EventParticipantDao {

	public List<EventParticipant> listAll() throws Exception;

	/**
	 * @param eventID
	 * @param addr
	 * @param date
	 * @param cid
	 * @param eventType
	 * @param continuous
	 * @param volunteersNeeded
	 * @param organizationId
	 * @return
	 * @throws Exception
	 */
	public EventParticipant create(int eid, int uid) throws Exception;

	/**
	 * @param eid unique EventParticipant ID
	 * @throws Exception
	 */
	public void delete(int epid) throws Exception;
	
	public void update(int epid, int neweid, int newuid) throws Exception;

	/**
	 * @param eid unique EventParticipant ID
	 * @return
	 * @throws Exception
	 */
	public EventParticipant fetchEventParticipantById(int epid) throws Exception;
	
	public List<EventParticipant> fetchAllEventParticipantsByEventId(int eid) throws Exception;
}
