package srv.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import srv.domain.ServiceHours;
import srv.domain.event.Event;
import srv.domain.user.UserDao;

/**
 * The methods in this class are just a temporary stand in until the ServiceHourDao is completed.
 *
 */
public class BoardMemberHoursListService {

	@Autowired 
	UserDao dao;
	
	public List<ServiceHours> hrs = new ArrayList<ServiceHours>();
	
	
	public BoardMemberHoursListService() {
		initialize();
	}
	
	public void initialize() {
		ServiceHours a = new ServiceHours()
				.setShid(1)
				.setEventName(new Event().setTitle("Spending Time with Toys for Tots"))
				.setHours(6.0)
				.setStatus("Approved");
		
		ServiceHours b = new ServiceHours()
				.setShid(2)
				.setEventName(new Event().setTitle("Teaching Part Time"))
				.setHours(2.0)
				.setStatus("Pending");
		ServiceHours c = new ServiceHours()
				.setShid(3)
				.setEventName(new Event().setTitle("Working with Food House"))
				.setHours(4.0)
				.setStatus("Rejected");
		ServiceHours d = new ServiceHours()
				.setShid(4)
				.setEventName(new Event().setTitle("Volunteering at Service Station"))
				.setHours(2.0)
				.setStatus("Pending");
		
		hrs.add(a);
		hrs.add(b);
		hrs.add(c);
		hrs.add(d);
	}
	
	
}
