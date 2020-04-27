package srv.services;

import java.util.ArrayList;
import java.util.List;

import srv.domain.event.Event;

import srv.domain.serviceClient.ServiceClient;

import srv.domain.serviceHours.ServiceHours;


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
				.setEvent(new Event().setTitle("Spending Time with Toys for Tots"))
				.setHours(6.0)
				.setStatus("Approved");
		ServiceHours b = new ServiceHours()
				.setShid(2)
				.setEvent(new Event().setTitle("Teaching Part Time"))
				.setHours(2.0)
				.setStatus("Pending");
		ServiceHours c = new ServiceHours()
				.setShid(3)
				.setEvent(new Event().setTitle("Working with Food House"))
				.setHours(4.0)
				.setStatus("Rejected");
		ServiceHours d = new ServiceHours()
				.setShid(4)
				.setEvent(new Event().setTitle("Volunteering at Service Station"))
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
	
	public ServiceHours updateHour(int id, String eName, String org, double hrsServed, String date, String desc)   {

		int index = 0;
		for(ServiceHours h : hrs) {
			
			if(h.getShid() == id) {
				
				h.setEvent(new Event().setTitle(eName));
				h.setServedPet(new ServiceClient().setName(org));
				h.setHours(hrsServed);
				h.setDate(date);
				h.setDescritpion(desc);
			
				break;
			}
			
			index++;
		}
		
		return hrs.get(index);
}
	
}	
