package srv.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import srv.domain.event.Event;
import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.user.BoardMemberUser;
import srv.domain.user.BoardMemberUserDao;
import srv.domain.user.ServantUser;
import srv.domain.user.ServantUserDao;

/**
 * BoardMemberHoursListService populates a list of all 
 * service events to be approved for the BoardMemberController
 */
@Service
public class BoardMemberService {
	
	private static Logger log = LoggerFactory.getLogger(EventService.class);


	@Autowired 
	ServiceHoursDao serviceDao;
	
	@Autowired
	BoardMemberUserDao bmDao;
	
	@Autowired
	ServantUserDao srvUserDao;
	
	public BoardMemberService() {

	}
	
	
	/**
	 * Gets the current list of board member users from the database
	 */
	public List<BoardMemberUser> listAllBoardMemberUsers() throws Exception{
		return bmDao.listAllBoardMemberUsers();
	}
	
	/**
	 * Gets the current list of servant users that are not board members
	 */
	public List<ServantUser> nonBmUsers() throws Exception {
				
		return srvUserDao.nonBmUsers();
	}
	
	/**
	 * Deletes/demotes the specified board member user.
	 */
	public void delete(int id) throws Exception {
		
		log.debug("deleting item {}", id);
		
		if (id <= 0) {
			throw new Exception(String.format("Invalid user id [%d]",id));
		}
		bmDao.delete(id);
	}
	
	/** 
	 * Creates/promotes a servant user to a new board member user.
	 */
	public BoardMemberUser create(String username, boolean coChair) throws Exception{
		return bmDao.create(username, coChair);
	}
	
	
	
	/**@return ServiceHours list directly from the Dao
	 *TODO Only return services with "Pending as status"*/
	public List<ServiceHours> listHours() {	
		
		try {
			
			return serviceDao.listAll();
			
		} catch (Exception e) {
		
		e.printStackTrace();
		return null;
	}}
	
	public List<ServiceHours> listHoursToBeApproved(){	
		
		List<ServiceHours> allHrs;
		List<ServiceHours> tbaHrs = new ArrayList<ServiceHours>();
		try {
			allHrs = serviceDao.listAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
			}
		for(ServiceHours hrs : allHrs) {
			if ("Pending".equals(hrs.getStatus())) {
				tbaHrs.add(hrs);
			}
		}
		
		return tbaHrs;
	}
	
	/**Updates serviceHours object
	 * @param ID
	 * @param eName
	 * @param eName
	 * @param org
	 * @param hrsServed
	 * @param date
	 * @param desc
	 * @return ServiceHours
	 * */
	public ServiceHours updateHour(int id, String eName, String org, double hrsServed, Date date, String desc)   {

		int index = 0;
		List<ServiceHours> hrs;
		try {
			hrs = serviceDao.listAll();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hrs = new ArrayList<ServiceHours>();
		}
		for(ServiceHours h : hrs) {
			
			if(h.getShid() == id) {
				
				h.setEvent(new Event().setTitle(eName));
				h.setServedPet(new ServiceClient().setName(org));
				h.setHours(hrsServed);
				h.setDate(date);
				h.setDescription(desc);
			
				break;
			}
			
			index++;
		}
		
		return hrs.get(index);
	}


	public BoardMemberUser fetchById(Integer id) throws Exception {
		// TODO Auto-generated method stub
		return bmDao.fetchBoardMemberUserById(id.intValue());
	}


	public BoardMemberUser updateBoardMember(BoardMemberUser bm) throws Exception {
		
		bmDao.update(bm.getUid(), 
				bm.getIsCoChair(),
				bm.getAffiliation()!=null?bm.getAffiliation().getSgid():null,
				bm.getExpectedGradYear(), 
				bm.getHasCar(),
				bm.getCarCapacity(),
				bm.getContactInfo()!=null?bm.getContactInfo().getContactId():null);
		

		return fetchById(bm.getUid());
	}
	
}
