package srv.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import srv.domain.event.Event;
import srv.domain.event.EventDao;
import srv.domain.event.eventype.EventType;
import srv.domain.event.eventype.EventTypeDao;


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
				null,  // no contact id yet
				new java.util.Date(),
				eventTypeId, 
				false, 
				null, 
				null, 
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
		
		if (ev == null) return null; // do nothing / return nothing
		
		
		log.debug("updating event {}", ev.getEid());
		
		eventDao.update(
				    ev.getEid(), 
					ev.getTitle(),
					ev.getAddress(),
					
					ev.getClass()==null?null:ev.getContact().getContactId(),
					ev.getDate(),
					
					ev.getType().getEtid(),
					ev.isContinuous(),
					ev.getVolunteersNeeded(), 
					null,
					ev.getNeededVolunteerHours(), 
					ev.getRsvpVolunteerHours(),
					ev.getNote()
					);
		
		return ev;   
	}
	
	
}
