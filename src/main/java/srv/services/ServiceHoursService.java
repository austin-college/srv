package srv.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.domain.hours.JdbcTemplateServiceHoursDao;
import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.user.JdbcTemplateUserDao;
import srv.utils.UserUtil;

import org.slf4j.LoggerFactory;

@Service
public class ServiceHoursService {
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(ServiceHoursService.class);
	
	
	@Autowired
	ServiceHoursDao sHoursDao; 
	
	@Autowired
	EventService eventService; 
	@Autowired 
	UserUtil uu; 
	@Autowired 
	JdbcTemplateUserDao userDao; 
	
	
	
	public ServiceHoursService() {
	}
	
	
	
	public List<ServiceHours> listHours() throws Exception{	
		return sHoursDao.listAll(); 
	}
	
	
	
	
	public void removeServiceHour(int id) throws Exception  {
		sHoursDao.delete(id);
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
	public ServiceHours createServiceHour(int eventId) throws Exception {
		
		if(eventId <= 0) {
			
			throw new Exception(String.format("Invalid event id [%d]", eventId));
		}
		
		/*
		 * Fetching event using EventService and the eventId to 
		 * pre-populate the fields with default dummy values.
		 * 
		 */
		Event e = eventService.eventById(eventId);
		System.out.println(eventId);
		System.out.println(e);
		
		/*
		 * Using UserUtil to get the currentUser and the id
		 */
		uu = new UserUtil();
		
		EventType et = e.getType();
		
		/*
		 * Use EventType and Event to populate fields in dummy 
		 * service hour from DB 
		 */
		Integer scid = et.getDefClient().getScid();
		Integer uid = uu.currentUser().getUid();
		Integer eid = e.getEid();
		Double hrs = (double) et.getDefHours();
		Date date = e.getDate();
		String reflect = "Type your reflection here.";
		String descrip = et.getDescription();
		String status = "Pending";
		
		/*
		 * Create a default dummy service hour.
		 */
		ServiceHours sh = sHoursDao.create(scid, uid, eid, hrs, reflect, descrip, status);
				
		
		log.debug("back with new service hour {}", sh.getShid());
		
		return sh;
		
	}

	
	public ServiceHours updateHour(int id, String eName, String org, double hrsServed, Date date, String desc)   {
		
		//sHoursDao.update(id,  );
		
		return null;   
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
		
		if (hours.size()==0) return 0.0;
		
		//average list
		for(int i = 0; i < hours.size(); i++) {
			avg += hours.get(i).getHours();
		}
		avg = avg / hours.size();
		
		
		return avg;
	}
	
	public double getTermTot(List<ServiceHours> hours) {
		double avg = 0;
		
		if (hours.size()==0) return 0.0;
		
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
		
		if (hours.size()==0) return 0.0;
		
		//before this you would make a new list with the dates being from the last year
		for(int i = 0; i < hours.size(); i++) {
			avg += hours.get(i).getHours();
		}
		
		avg = avg / 12;
		
		return avg;
	}
	
	
	
}	
