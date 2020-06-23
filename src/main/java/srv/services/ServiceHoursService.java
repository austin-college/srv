package srv.services;

import java.util.ArrayList;
import java.util.Date;
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
	public List<ServiceHours> filteredHours(Integer userId, Integer scId) throws Exception {
		
		if ((userId != null) && (userId <= 0)) {
			throw new Exception(String.format("Invalid user id [%d]", userId));
		}
		
		if ((scId != null) && (scId <= 0)) {
			throw new Exception(String.format("Invalid service client id [%d]", scId));
		}
		
		List<ServiceHours> results = sHoursDao.listByFilter(userId, scId);
		
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
	public ServiceHours createServiceHour(Integer scid, Integer eid, Double hours, String reflection, String description) throws Exception {
		
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
		ServiceHours sh = sHoursDao.create(scid, servant.getUid(), eid, hours, "Pending", reflection, description);	
		
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
	public void updateHour(Integer shid, Integer scid, Integer eid, Double hrsSrved, String reflection, String descr)  throws Exception {
		
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
				descr);
				
		
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

		String semId = SemesterUtil.currentSemester();
		log.debug("current semester: [{}]", semId);
		
		double total = 0.0;
		for (ServiceHours h : approvedHours(hours)) {
			
			if (semId.equals(SemesterUtil.semesterID(h.getDate()))) {
				total += h.getHours();
			}
		}
		
		return total;
	}
	
	
	
	
	public double totalAcademicYearHours(List<ServiceHours> hours) {
		
		String ayid = SemesterUtil.currentAcadYear();
		log.debug("current academic year: [{}]", ayid);
		
		double total = 0.0;
		for (ServiceHours h : approvedHours(hours)) {
			
			if (ayid.equals(SemesterUtil.acadYear(h.getDate()))) {
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
	

	
	
	
	
	public double averageHoursPerMonth(List<ServiceHours> hours) {
		
		// get date of earliest hour
		// get date of latest hour

		// empty list of double
		
		// for each yr/month between earliest and latest
		// add hours for that month
		
		// average hours per month
		
		return 0;
	}
	
	
	
}	
