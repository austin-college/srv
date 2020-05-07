package srv.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import srv.domain.event.Event;

import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceHours.JdbcTemplateServiceHoursDao;
import srv.domain.serviceHours.ServiceHours;


/**
 * The methods in this class are just a temporary stand in until the ServiceHourDao is completed.
 * @author Lydia House
 *
 */
public class ServiceHoursService {
	
	public List<ServiceHours> hrs = new ArrayList<ServiceHours>();
	
	//autowire
	@Autowired
	JdbcTemplateServiceHoursDao serviceHoursDao; 
	
	
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
				h.setDescription(desc);
			
				break;
			}
			
			index++;
		}
		
		return hrs.get(index);
	}
	
	/**
	 * returns the total hours served in a semester. 
	 * @param id
	 * @return
	 */
	public double getSemTot(List<ServiceHours> hours) {
		double avg = 0;
		
		
		//get current month and date
//		Date date = new Date();
//		SimpleDateFormat yForm = new SimpleDateFormat("yyyy");
//		SimpleDateFormat mForm = new SimpleDateFormat("MM");
//		int year = Integer.parseInt(yForm.format(date));
//		int month = Integer.parseInt(mForm.format(date));
//		System.out.println(hours.get(0).getDate());
		//create a new list that only includes hours from the last semester
//		List<ServiceHours> refHours;
//		for(int i = 0; i < hours.size(); i++) {
//			if(hours.get(i).getDate())
//		}
		//can't do any of the above because none of the hours have dates and their events don't exist yet
		//for now just going to average the list of service hours until we can differentiate servants 
		
		//average list
		for(int i = 0; i < hours.size(); i++) {
			avg += hours.get(i).getHours();
		}
		avg = avg / hours.size();
		
		
		return avg;
	}
	
	public double getTermTot(List<ServiceHours> hours) {
		double avg = 0;
		
		//before this you would make a new list with the dates being from the last term
		for(int i = 0; i < hours.size(); i++) {
			avg += hours.get(i).getHours();
		}
		avg = avg / hours.size();
		
		
		return avg * 2;
	}
	
	public int getTotOrgs(List<ServiceHours> hours) {
		//before this there should be loop that weeds out all the duplicate orgs
		int orgs = hours.size();
		return orgs;
	}
	
	public double getAvgPerMo(List<ServiceHours> hours) {
		double avg = 0;
		
		//before this you would make a new list with the dates being from the last year
		for(int i = 0; i < hours.size(); i++) {
			avg += hours.get(i).getHours();
		}
		avg = avg / 12;
		
		
		return avg;
	}
	
	
	
}	
