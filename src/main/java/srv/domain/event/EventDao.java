package srv.domain.event;

import java.util.Date;
import java.util.List;

import srv.domain.serviceclient.ServiceClient;

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
	public Event create(String title, 
			String addr, 
			Integer cid,
			Date date, 
			Integer eventTypeId, Boolean continuous,
			Integer volunteersNeeded, Integer scid, Double neededVolunteerHours, 
			Double rsvpVolunteerHours, String freeTextField) throws Exception;

	/**
	 * @param eid unique event ID
	 * @throws Exception
	 */
	public void delete(int eid) throws Exception;

	public void update(int eid, 
			String title, 
			String addr, 
			Integer cid,
			Date date, 
			Integer eventTypeId, 
			Boolean continuous,
			Integer volunteersNeeded, 
			Integer scid,
			Double neededVolunteerHours, 
			Double rsvpVolunteerHours, 
			String freeTextField) throws Exception;

	/**
	 * @param eid unique event ID
	 * @return
	 * @throws Exception
	 */
	public Event fetchEventById(int eid) throws Exception;
//	public ServiceClient listAllSrvC() throws Exception;
	public List<Event> listByFilter(String startDate, String endDate, Integer eTypeId, Integer scId, Integer bmId) throws Exception;
}
