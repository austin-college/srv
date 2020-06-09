package srv.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import srv.domain.event.Event;
import srv.domain.hours.JdbcTemplateServiceHoursDao;
import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.serviceclient.ServiceClient;
import org.slf4j.LoggerFactory;

@Service
public class ServiceHoursService {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(ServiceHoursService.class);
	
	public List<ServiceHours> hrs;
	
	//autowire
	@Autowired
	ServiceHoursDao sHoursDao; 
	
	
	/**
	 * No argument default constructor that initializes an empty arraylist
	 * of ServiceHours
	 */
	public ServiceHoursService() {
		
	hrs = new ArrayList<ServiceHours>();
	
	}
	
	/**
	 * Populates a list of ServiceHours using the ServiceHours DAO to retrieve the data.
	 * Throws an exception if there is an issue with the DAO.
	 */
	public void initialize() throws Exception {
		
		hrs = sHoursDao.listAll();
		/*
		 * ServiceHours a = new ServiceHours() .setShid(1) .setEvent(new
		 * Event().setTitle("Spending Time with Toys for Tots")) .setHours(6.0)
		 * .setStatus("Approved"); ServiceHours b = new ServiceHours() .setShid(2)
		 * .setEvent(new Event().setTitle("Teaching Part Time")) .setHours(2.0)
		 * .setStatus("Pending"); ServiceHours c = new ServiceHours() .setShid(3)
		 * .setEvent(new Event().setTitle("Working with Food House")) .setHours(4.0)
		 * .setStatus("Rejected"); ServiceHours d = new ServiceHours() .setShid(4)
		 * .setEvent(new Event().setTitle("Volunteering at Service Station"))
		 * .setHours(2.0) .setStatus("Pending");
		 * 
		 * hrs.add(a); hrs.add(b); hrs.add(c); hrs.add(d);
		 */
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
	
	/**
	 * Creates a dummy service hour with default values so user can eventually configure them to taste. 
	 * 
	 * @param serviceHourId id of ServiceHour (cannot be null)
	 * 
	 * @return new dummy service hour
	 * 
	 * @throws Exception
	 */
	public ServiceHours createServiceHour(int serviceHourId) throws Exception {
		
		
	}
	
	public ServiceHours updateHour(int id, String eName, String org, double hrsServed, Date date, String desc)   {

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
	
	public int getAvgPerMo(List<ServiceHours> hours) {
		double avg = 0;
		
		//before this you would make a new list with the dates being from the last year
		for(int i = 0; i < hours.size(); i++) {
			avg += hours.get(i).getHours();
		}
		avg = avg / 12;
		int refinedAvg = (int) avg;
		
		return refinedAvg;
	}
	
	
	
}	
