package srv.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
/*Need to check above imports and make sure they are all useful or removed. (Previously HardCoded)*/
/**
 * The methods in this class are just a temporary stand in until the ServiceHourDao is completed.
 */
@Service
public class BoardMemberHoursListService {

	@Autowired 
	JdbcTemplateServiceHoursDao serviceDao;
	
	public List<ServiceHours> hrs;
	
	public BoardMemberHoursListService() {
		try {
			hrs = serviceDao.listAll();
		} catch (Exception e) {
			e.printStackTrace();
		}    
	}

	
	/**@return ServiceHours list*/
	public List<ServiceHours> listHours(){	return hrs;}
	
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
