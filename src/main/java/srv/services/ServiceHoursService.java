package srv.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.domain.hours.JdbcTemplateServiceHoursDao;
import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;
import srv.domain.user.JdbcTemplateUserDao;
import srv.domain.user.User;
import srv.utils.SemesterUtil;
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
	ServiceClientDao sClientDao;	
	
	@Autowired
	SemesterUtil semUtil;
	
	
	public ServiceHoursService() {
	}
	
	/**
	 * fetches the current user's id 
	 * 
	 * @return
	 * @throws Exception
	 */
	public int currentUserId() throws Exception {
		return uu.currentUser().getUid();
	}
	
	public List<ServiceHours> listHours() throws Exception{	
		return sHoursDao.listAll(); 
	}
	
	/**
	 * Delegates to the dao in order to find the specified service
	 * hour from our data store.
	 * 
	 * @param shid
	 * @return
	 * @throws Exception
	 */
	public ServiceHours serviceHourById(int shid) throws Exception {
		
		if (shid <= 0) {
			throw new Exception(String.format("Invalid service hour id [%d]", shid));
		}
		
		return sHoursDao.fetchHoursById(shid);
	}

	/**
	 * Get the current user's service hours.
	 * 
	 * @param uid
	 * @return
	 * @throws Exception
	 */
	public List<ServiceHours> userHours(int uid) throws Exception {
		return sHoursDao.fetchHoursByUserId(uid);
	}
	
	/**
	 * Change the hour's status and return it back
	 * @param id
	 * @throws Exception
	 */
	public ServiceHours changeStatus(int shid, String status, String feedback) throws Exception {
		
		sHoursDao.changeHourStatusWithFeedback(shid, status, feedback);
		
		return serviceHourById(shid);
	}
	
	public void removeServiceHour(int id) throws Exception  {		
		sHoursDao.delete(id);
	}
	
	/**
	 * Returns a list of service clients/sponsors in the database
	 * @return
	 * @throws Exception
	 */
	public List<ServiceClient> listCurrentSponsors() throws Exception {
		return sClientDao.listAll();
	}
	
	/**
	 * Returns a list of service hours in our system based on the following parameters/filters.
	 * Throws an exception if our dao has encountered a problem
	 * 
	 * @param scId
	 * @throws Exception
	 */
	public List<ServiceHours> filteredHours(Integer userId, Integer scId, String monthName, String status, String year) throws Exception {
		
		
		if ((userId != null) && (userId <= 0)) 
			throw new Exception(String.format("Invalid user id [%d]", userId));
				
		if ((scId != null) && (scId <= 0)) 
			throw new Exception(String.format("Invalid service client id [%d]", scId));
		
		if ((monthName != null) && (monthName.length() <= 0)) 
			throw new Exception(String.format("Invalid month name [%s]", monthName));
			
		/*
		 * When the 'List All' value is selected, we make the month name null in
		 * order to display all months
		 */
		if ((monthName != null) && (monthName.equals("List All")))
			monthName = null;
		
		if ((status != null) && (status.length() <= 0))
			throw new Exception(String.format("Invalid status [%s]", status));
		
		/*
		 * When the 'List All' value is selected, we make the status null in order
		 * to display all status
		 */
		if ((status != null) && (status.equals("List All")))
			status = null;
		
		if ((year != null) && (year.length() <= 0))
			throw new Exception(String.format("Invalid year [%s]", year));
		
		/* 
		 * When the 'List All' value is selected, we make the year null in
		 * order to display all years
		 */
		if ((year != null) && (year.equals("List All")))
			year = null;
		
		log.debug("userid:[{}]",userId);
		log.debug("scid:[{}]",scId);
		log.debug("month:[{}]",monthName);
		log.debug("status:[{}]",status);
		log.debug("year:[{}]",year);
		
		List<ServiceHours> results = sHoursDao.listByFilter(userId, scId, monthName, status, year);
		
		log.debug("Size of filtered hours list is: " + results.size());
		
		return results;
	}
	
	
	/**
	 * Creates a service hour 
	 * 
	 * @param serviceHourId id of ServiceHour (cannot be null)
	 * 
	 * @return new dummy service hour
	 * 
	 * @throws Exception
	 */
	public ServiceHours createServiceHour(Integer scid, Integer eid, Double hours, String reflection, String description, String contactName, String contactContact) throws Exception {
		
		if(eid <= 0) {
			
			throw new Exception(String.format("Invalid event id [%d]", eid));
		}
		
		if (scid <= 0 ) {
			throw new Exception(String.format("Invalid service client id [%d]", scid));

		}
		
		/*
		 * Using UserUtil to get the currentUser and the id
		 */
		
		User servant = uu.currentUser();
		log.debug("current user id is: " + servant.getUid());
		

		/*
		 * Create a service hour.
		 */
		ServiceHours sh = sHoursDao.create(scid, servant.getUid(), eid, hours, "Pending", reflection, description, contactName, contactContact);	
		
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
	public void updateHour(Integer shid, Integer scid, Integer eid, Double hrsSrved, String reflection, String descr, String contactName, String contactContact)  throws Exception {
		
		if(shid == null) {
			
			throw new Exception(String.format("service hour is null", shid)); 
		}
		
		log.debug("updating service hour [{}]", shid);
		
		sHoursDao.update(
				shid,
				scid,
				uu.currentUser().getUid(),
				eid,
				hrsSrved,
				ServiceHours.STATUS_PENDING,
				reflection,
				descr,
				contactName,
				contactContact);
				
		
	}
	
	
	/**
	 * Given a generic list of service hours, we filter down to a list of approved, non-null
	 * hours.
	 * 
	 * @param hoursList
	 * @return
	 */
	public List<ServiceHours>  approvedHours(List<ServiceHours> hoursList) {
		
		List<ServiceHours> results = new ArrayList<ServiceHours>(hoursList.size());
		
		for (ServiceHours h : hoursList) {

			if (!ServiceHours.STATUS_APPROVED.equals(h.getStatus())) continue;  // skip if not approved. 
				
			if (h.getDate() == null) continue;  // skip if no date.   should not happen
			
			results.add(h);
		}

		return results;
	}
	
	
	/**
	 * returns the total hours served in the current semester.  We 
	 * use the semesterId as the basis.   We iterate over all hours
	 * specified and total only those that were performed on a date
	 * with the same semester id.
	 *  
	 * @param id
	 * @return
	 */
	public double totalSemesterHours(List<ServiceHours> hours) {

		String semId = currentSemester();
		log.debug("current semester: [{}]", semId);
		
		double total = 0.0;
		for (ServiceHours h : approvedHours(hours)) {
			
			String semtag =  semUtil.semesterID(h.getDate());
			log.debug("id=[{}], status=[{}], hours=[{}], semester=[{}]",h.getShid(), h.getStatus(), h.getHours(), semtag);
			if (semId.equals(semtag)) {
				log.debug("valid hours [{}]", h.getHours());
				total += h.getHours();
			}
		}
		
		return total;
	}
	
	
	/**
	 * Returns the current semester,  can be overridden in testing
	 * to return a date good for our testing fixtures.
	 * 
	 * @return  current semester id string, like "2020FA"
	 */
	public String currentSemester() {
		return semUtil.currentSemester();
	}
	
	
	/**
	 * @return current academic year id string, like "AY2020/2021"
	 */
	public String currentAcadYear() {
		return semUtil.currentAcadYear();
	}
	
	
	/**
	 * 
	 * @param hours
	 * @return
	 */
	public double totalAcademicYearHours(List<ServiceHours> hours) {
		
		String ayid = currentAcadYear();
		log.debug("current academic year: [{}]", ayid);
		
		double total = 0.0;
		for (ServiceHours h : approvedHours(hours)) {
			
			if (ayid.equals(semUtil.acadYear(h.getDate()))) {
				total += h.getHours();
			}
		}
		
		return total;
	}

	
	
	public int totalSponsorsCount(List<ServiceHours> hours) {
		
		Set<ServiceClient> clients = new HashSet<ServiceClient>();
		
		for (ServiceHours h : approvedHours(hours)) {
			
			clients.add(h.getServedPet());
		}
		
		return clients.size();
	}
	

	
	
	
	/**
	 * Computes the monthly average of hours in the specified list. We
	 * build a hash table of summed totals for each unique month.  A
	 * month is identified by (YYYY, MM). As we iterate over our list
	 * of hours, we fetch from total from the table.  If absent, we 
	 * start a new totaling sum (double).   If existing, we add to the
	 * existing.  Either way, we store back into the hash table. 
	 * <p>
	 * After which, we can average the monthly totals.  
	 *  
	 * @param hours
	 * @return the mean/average monthly total hours.
	 */
	public double averageHoursPerMonth(List<ServiceHours> hours) {
		
		HashMap<String, Double> map = new HashMap<String, Double>(); 
				
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM");

		for (ServiceHours h : approvedHours(hours)) {
			
			String key = fmt.format(h.getDate());

			// if this is the first encounter, then initialize our
			// associated double total to the hours
			Double d = Double.valueOf(h.getHours());
			
			// if we've seen it before,  add to the double total
			// in hour hash table.
			if (map.containsKey(key)) {
				d = map.get(key) + h.getHours();
			} 
			
			// either way, put it back into the table for this month.
			map.put(key, d);
		}
		
		
		/*
		 * Ok...so now we have all approved hours partitioned into separate monthly totals
		 * in our hash table... the key is the month id string "yyyy-MM" and the value the 
		 * total hours for that month.   Time to average..... 
		 */
		
		int count = 0;
		double total = 0.0;
		
		// for each key in the map, fetch the value and add to our averaging total
		for (String k : map.keySet()) {
			count++;
			total += map.get(k).doubleValue();
		}

		if (count == 0) return 0.0;
		return total/count;
		
	}
	
	
	
}	
