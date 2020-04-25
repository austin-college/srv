package srv.services;

import java.util.ArrayList;
import java.util.List;

import srv.domain.ServiceHours;
import srv.domain.event.Event;

/**
 * The methods in this class are just a temporary stand in until the ServiceHourDao is completed.
 * @author Lydia House
 *
 */
public class ServiceHoursService {
	public List<ServiceHours> hrs = new ArrayList<ServiceHours>();
	
	public ServiceHoursService() {
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
	
	public List<ServiceHours> listHours(){	return hrs;}
	
	public void removeServiceHour(int id)  {

	
		for(ServiceHours h : hrs) { 
			if(h.getShid() == id) { 
			
				hrs.remove(h);
				break;
			 } 
		  }
	}
}	
