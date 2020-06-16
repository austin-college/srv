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
import srv.domain.user.User;
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
		
		User servant = uu.currentUser();
		System.out.println(servant.getUid());
		
		EventType et = e.getType();
		
		/*
		 * Use EventType and Event to populate fields in dummy 
		 * service hour from DB 
		 */
		Integer scid = et.getDefClient().getScid();
		log.debug("scid [{}]", scid);
		
		Integer uid = servant.getUid();
		log.debug("uid [{}]", uid);
		
		Integer eid = e.getEid();
		log.debug("eid [{}]", eid);
		
		Double hrs = Double.valueOf(et.getDefHours());
		log.debug("hrs [{}]", hrs);
		
		String reflect = "Type your reflection here.";
		log.debug("reflect [{}]", reflect);
		
		String descrip = et.getDescription();
		log.debug("descrip [{}]", descrip);
		
		String status = "Pending";
		log.debug("status [{}]", status);
		
		/*
		 * Create a default dummy service hour.
		 */
		// Integer scid, Integer uid, Integer eid, Double hours, String stat, String reflection,String description
		ServiceHours sh = sHoursDao.create(scid, uid, eid, hrs, status, reflect, descrip);
				
		
		log.debug("back with new service hour {}", sh.getShid());
		
		return sh;
		
	}

	
	/**
	 * Given the current ServiceHours object, we save it back to our DB with 
	 * the help of the ServiceHoursDao. Any additional data transformations are done here, 
	 * and application defaults are also applied here. 
	 * 
	 * @param sh
	 * @return
	 * @throws Exception
	 */
	public ServiceHours updateHour(ServiceHours sh)  throws Exception {
		
		if(sh == null) {
			
			throw new Exception(String.format("service hour is null", sh)); 
		}
		
		log.debug("updat");
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
