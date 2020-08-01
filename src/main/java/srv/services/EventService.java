package srv.services;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import srv.domain.event.Event;
import srv.domain.event.EventDao;
import srv.domain.event.eventype.EventType;
import srv.domain.event.eventype.EventTypeDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;
import srv.domain.user.User;
import srv.domain.user.UserDao;


/**
 * An instance of this class provides services to our controllers. It removes some dependency
 * details from the client code (controller code).  We provide general CRUD services and also
 * provide various filtering services related to scheduled events. Finally,  any computational
 * services related to events are provide as well.
 * 
 * @author mahiggs
 *
 */
@Service
public class EventService {

	private static Logger log = LoggerFactory.getLogger(EventService.class);

	@Autowired
	private EventDao eventDao;
		
	@Autowired
	private EventTypeDao eventTypeDao;
	
	@Autowired
	private ServiceClientDao serviceClientDao;
	
	@Autowired
	private UserDao userDao;

	/**
	 * Delegates to the dao in order to find the specified event from our
	 * data store.  
	 * 
	 * @param eid
	 * @return
	 * @throws Exception
	 */
	public Event eventById(int eid) throws Exception {

		if (eid <= 0) {
			throw new Exception(String.format("Invalid event id [%d]",eid));
		}
		
		return eventDao.fetchEventById(eid);
	}
	
	
	/**
	 * Creates a dummy event with default values so the user can eventually
	 * configure to taste.
	 * 
	 * @param eventTypeId id of the eventType  (must not be null).
	 * 
	 * @return newly created event
	 * 
	 * @throws Exception
	 */
	public Event createEventOfType(int eventTypeId) throws Exception {
		
		if (eventTypeId <= 0) {
			throw new Exception(String.format("Invalid event type id [%d]",eventTypeId));
		}
		
		/*
		 * Create a default dummy event
		 */
		Event ne = eventDao.create(
				"new event", 
				"location", 
				Integer.valueOf(1),  // contact 
				new java.util.Date(),
				eventTypeId, 
				false, 
				null, 
				Integer.valueOf(1), 
				null, 
				null, 
				"");
		
		log.debug("back with new event {}", ne.getEid());

		return ne;  
	}

	
	/**
	 * Given the event's unique id (eventId), we delete the event 
	 * from the backend database.  
	 * 
	 * @param eventId
	 * @throws Exception
	 */
	public void deleteEvent(int eventId) throws Exception {
		
		log.debug("deleting item {}", eventId);
		
		if (eventId <= 0) {
			throw new Exception(String.format("Invalid event id [%d]",eventId));
		}
		
		// TODO  what should we do with all logged hours that
		// refer to this event?
		
		eventDao.delete(eventId);
	}

	
	
	/**
	 * Returns all current events in our system. Throws an exception
	 * if our dao has a problem.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Event> allEvents() throws Exception {
	
		return eventDao.listAll();
	}
	
	/**
	 * Returns all of the current event types known to our system.  This
	 * is commonly used for populating user interface elements (selection lists).
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<EventType> allEventTypes() throws Exception {
		
		return eventTypeDao.listAll();
	}
	
	/**
	 * Returns all of the current service clients known to our system. This is
	 * commonly used for populating user interface elements (selection lists).
	 * Throws an exception if our dao encounters a problem.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<ServiceClient> allServiceClients() throws Exception {
		
		return serviceClientDao.listAll();
	}
	
	/**
	 * TODO currently our system has no way of listing only board member users
	 * so as of now we are displaying the entire list of users
	 * 
	 * Returns all the current board members known to our system. This is
	 * commonly used for populating user interface elements (selection lists).
	 * Throws an exception if our dao encounters a problem.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<User> allBoardMembers() throws Exception {
		
		return userDao.listAll();
	}
	/**
	 * Given the current event object, we save back to our data store with 
	 * the help of our DAO.  If any additional data transformations are necessary
	 * we do them here.   If any application defaults are enforced,  we do that
	 * here.
	 * <p>
	 * Note that if referential integrity or other database constraints are violated 
	 * an SQLException is thrown.  We let the controller figure out how to deal with
	 * that.
	 *  
	 * </p>
	 * 
	 * @param e
	 * @return
	 */
	public Event updateEvent(Event ev) throws Exception {
		return this.updateEvent(ev, 
				ev.getContact()==null?null:ev.getContact().getContactId(),
						ev.getServiceClient()==null?null:ev.getServiceClient().getScid()
						);
	}
	
	/**
	 * Alternate form of update in which we can supply the contact for the event via explicit
	 * integer (the contact id).   This is for programmers convenience an efficiency.  Note: this
	 * form can be used as long as the contact exists in our db.  Here we are not going to check
	 * for existence.
	 */
	public Event updateEvent(Event ev, Integer ctId, Integer scId) throws Exception {
		
		if (ev == null) return null; // do nothing / return nothing
		
		if (ctId <=0 ) ctId = null;
		
		log.debug("updating event {}", ev.getEid());
		
		eventDao.update(
				    ev.getEid(), 
					ev.getTitle(),
					ev.getAddress(),
					
					ctId,
					
					ev.getDate(),
					
					ev.getType().getEtid(),
					ev.isContinuous(),
					ev.getVolunteersNeeded(), 
					
					scId,
					
					ev.getNeededVolunteerHours(), 
					ev.getRsvpVolunteerHours(),
					ev.getNote()
					);
		
		return ev;
	}

	/**
	 * 
	 * Returns a list of events in our system based on the following parameters/filters. 
	 * Throws an exception if our dao has encountered a problem.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param eTypeId
	 * @param scId
	 * @param bmId
	 * @return
	 * @throws Exception
	 */
	public List<Event> filteredEvents(String startDate, String endDate, Integer eTypeId, Integer scId, Integer bmId) throws Exception {
		
		
		List<Event> results;
		try {
			if (startDate != null) {
				
				// Sets the startDate to the current date
				if (startDate.equals("now")) 
					startDate = currentDate().toString();

				/*
				 * For events in the previous months, sets the startDate to be the current date 
				 * and sets the endDate to be the current date minus the offset for the month.
				 */
				else if (startDate.contains("M")) {

					int offset = Integer.valueOf(startDate.substring(3, startDate.length() - 1));
					
					Timestamp lastMonth = effectiveDate(currentDate(), "month", offset);

					startDate = currentDate().toString();
					endDate = lastMonth.toString();
				}
			}	
			
			if (endDate != null) {
				
				// Sets the endDate to the current date
				if (endDate.equals("now")) 
					endDate = currentDate().toString();
				
				/*
				 * For events in the upcoming months, sets the startDate to be the current date plus the
				 * offset for the month and sets the endDate to be the current date.
				 */
				else if (endDate.contains("M")) {
							
			        int offset = Integer.valueOf(endDate.substring(3, endDate.length() - 1));
			
					Timestamp nextMonth = effectiveDate(currentDate(), "month", offset);//new Timestamp(myCal.getTime().getTime());
				
					endDate = currentDate().toString();
					startDate = nextMonth.toString();			
				}
			}
			
			if ((eTypeId != null) && (eTypeId <= 0)) {
				throw new Exception(String.format("Invalid event type id [%d]", eTypeId));
			}
			
			if ((scId != null) && (scId <= 0)) {
				throw new Exception(String.format("Invalid service client id [%d]", scId));
			}
			
			results = eventDao.listByFilter(startDate, endDate, eTypeId, scId, bmId); 
			
			log.debug("Size of filtered list is: " + results.size());
			
		} catch (NumberFormatException e) {
			throw new Exception("Unable to convert parameter to number.");
		}
		catch (SQLException e) {
			throw new Exception("Unable to filter events. "+e.getMessage());
			
		} catch (Exception e) {
			throw new Exception("Uh,oh.  That's embarrassing. Unable to filter events.");
		}
		
		return results;
	}
	
	/**
	 * Helper getter method for the current date. Separate method in order to provide
	 * for easy testing.
	 * 
	 * @return current date
	 */
	public Timestamp currentDate() {
		return new Timestamp(new Date().getTime());	
	}
	
	/**
	 * Helper method to calculate the duration of events. As of now
	 * it is only for last/next month but future implementations can
	 * include hours, days, weeks, etc. and a variable amount of time
	 * (i.e. 2 weeks from now, 5 days before).
	 * 
	 * @param base current date
	 * @param duration hours, weeks, months, etc.
	 * @param offset +/- value based on duration
	 * 
	 * @return
	 */
	public Timestamp effectiveDate(Timestamp base, String duration, int offset) {
		
		Calendar myCal = Calendar.getInstance();
		myCal.setTime(base);
	
		// Setting the new date based off of the month and offset
		if (duration.equalsIgnoreCase("month")) 
			myCal.add(Calendar.MONTH, offset);
			
		// can add other ifs for weeks, days hours etc
			
		return new Timestamp(myCal.getTime().getTime());
	}





	
}
