package srv.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import srv.domain.serviceHours.ServiceHours;
import srv.domain.serviceHours.ServiceHoursDao;
import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.user.User;
import srv.domain.user.UserDao;
/*Need to check above imports and make sure they are all useful or removed. (Previously HardCoded)*/
/**
 * The methods in this class are just a temporary stand in until the ServiceHourDao is completed.
 */
public class BoardMemberHoursListService {

	@Autowired 
	UserDao userDao;
	ServiceHoursDao serviceDao;
	
	public List<ServiceHours> hrs = new ArrayList<ServiceHours>();
	
	
	public BoardMemberHoursListService() {
		initialize();
	}
	
	public void initialize() {
		/*HardCoded users to be replaced*/
		Contact blankContact = new Contact();
		User srv1 = new User(1,"602232", 6.0, blankContact);
		User srv2 = new User(2,"618826",77.0,blankContact);
		User srv3 = new User(3,"652286", 99.0,blankContact);
		User srv4 = new User(4,"696632", 3.0, blankContact);
		
		ServiceHours a = new ServiceHours()
				.setShid(1)
				.setEventName(new Event().setTitle("Spending Time with Toys for Tots"))
				.setHours(6.0)
				.setStatus("Approved")
				.setServant(srv1);
		ServiceHours b = new ServiceHours()
				.setShid(2)
				.setEventName(new Event().setTitle("Teaching Part Time"))
				.setHours(2.0)
				.setStatus("Pending")
				.setServant(srv1);
		ServiceHours c = new ServiceHours()
				.setShid(3)
				.setEventName(new Event().setTitle("Working with Food House"))
				.setHours(4.0)
				.setStatus("Rejected")
				.setServant(srv1);
		ServiceHours d = new ServiceHours()
				.setShid(4)
				.setEventName(new Event().setTitle("Volunteering at Service Station"))
				.setHours(2.0)
				.setStatus("Pending")
				.setServant(srv1);
		
		hrs.add(a);
		hrs.add(b);
		hrs.add(c);
		hrs.add(d);
	}
	
	
}
