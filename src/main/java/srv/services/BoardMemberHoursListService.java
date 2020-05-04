package srv.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import srv.domain.serviceHours.JdbcTemplateServiceHoursDao;
import srv.domain.serviceHours.ServiceHours;
import srv.domain.serviceHours.ServiceHoursDao;
import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceClient.ServiceClientDao;
import srv.domain.user.ServantUser;
import srv.domain.user.User;
import srv.domain.user.UserDao;
/*TODO Need to check above imports and make sure they are all useful or removed. (Previously HardCoded examples used some)*/
/**
 * BoardMemberHoursListService populates a list of all 
 * service events to be approved for the BoardMemberController
 */
@Service
public class BoardMemberHoursListService {

	@Autowired 
	ServiceHoursDao serviceDao;
	/**
	 * 
	 * */
	public BoardMemberHoursListService() {

	}
	
	/**@return ServiceHours list directly from the Dao*/
	public List<ServiceHours> listHours(){	try {
		return serviceDao.listAll();
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		return null;
	}}
	
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
	public ServiceHours updateHour(int id, String eName, String org, double hrsServed, String date, String desc)   {

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
	
}
