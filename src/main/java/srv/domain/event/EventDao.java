package srv.domain.event;

import java.util.List;

/**
 *  Data Access Object Interface for Event.java that defines the standard operations
 *  (CRUD) to be performed on the Event model object. 
 *  
 * @author Lydia House
 *
 */
public interface EventDao {

	public List<Event> listAll() throws Exception;

	/**
	 * @param addr
	 * @param cid
	 * @param date
	 * @param eventTypeId
	 * @param continuous
	 * @param volunteersNeeded
	 * @param organizationId
	 * @param neededVolunteerHours TODO
	 * @param rsvpVolunteerHours TODO
	 * @param freeTextField TODO
	 * @param eventID
	 * @return
	 * @throws Exception
	 */
	public Event create(String title, String addr, int cid, String date, int eventTypeId, boolean continuous,
			int volunteersNeeded, int organizationId, double neededVolunteerHours, double rsvpVolunteerHours, String freeTextField) throws Exception;

	/**
	 * @param eid unique event ID
	 * @throws Exception
	 */
	public void delete(int eid) throws Exception;

	public void update(int eid, String title, String addr, int cid, String date, int eventTypeId, boolean continuous,
			int volunteersNeeded, int organizationId, double neededVolunteerHours, double rsvpVolunteerHours, String freeTextField) throws Exception;

	/**
	 * @param eid unique event ID
	 * @return
	 * @throws Exception
	 */
	public Event fetchEventById(int eid) throws Exception;
}
