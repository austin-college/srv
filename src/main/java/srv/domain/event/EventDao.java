package srv.domain.event;

import java.util.List;

import srv.domain.contact.Contact;
import srv.domain.user.User;

public interface EventDao {

	public List<User> listAll() throws Exception;

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
	public Event create(String title, String addr, String date, int cid, String eventType, boolean continuous,
			int volunteersNeeded, int organizationId) throws Exception;

	/**
	 * @param eid unique event ID
	 * @throws Exception
	 */
	public void delete(int eid) throws Exception;

	/**
	 * @param eid    unique event ID
	 * @param newVal
	 * @throws Exception
	 */
	public void changePassword(int eid, String newPassword) throws Exception;

	/**
	 * @param eid unique event ID
	 * @return
	 * @throws Exception
	 */
	public Event fetchEventById(int eid) throws Exception;

	/**
	 * @param cid users contact id
	 * @return
	 * @throws Exception
	 */
	public Contact fetchEventContactById(int cid) throws Exception;

	/**
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public User fetchFromParticipantsList(int uid) throws Exception;
}
