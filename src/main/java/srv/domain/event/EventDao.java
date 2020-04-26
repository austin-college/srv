package srv.domain.event;

import java.util.List;

import srv.domain.contact.Contact;
import srv.domain.user.User;

public interface EventDao {

	public List<Event> listAll() throws Exception;

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
	public Event create(String title, String addr, int cid, String date, String eventType, boolean continuous,
			int volunteersNeeded, int organizationId) throws Exception;

	/**
	 * @param eid unique event ID
	 * @throws Exception
	 */
	public void delete(int eid) throws Exception;

	public void update(int eid, String title, String addr, int cid, String date, String eventType, boolean continuous,
			int volunteersNeeded, int organizationId) throws Exception;

	/**
	 * @param eid unique event ID
	 * @return
	 * @throws Exception
	 */
	public Event fetchEventById(int eid) throws Exception;
}
