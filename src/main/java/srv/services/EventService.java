package srv.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import srv.domain.event.Event;
import srv.domain.event.EventDao;
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
	private EventDao dao;
	
	
	@Autowired
	private EventTypeDao eventTypeDao;
	
	
	// create event given the event type
	public Event createEventOfType(int eventTypeId) throws Exception {
		
		// TODO
		
		return null;  // temporaily until implementation provided 
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
	}

	
	
	/**
	 * Returns all current events in our system. Throws an exception
	 * if our dao has a problem.
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<Event> allEvents() throws Exception {
	
		return dao.listAll();
	}
	
	
	/**
	 * Not sure about this yet.  
	 * 
	 * @param e
	 * @return
	 */
	public Event updateEvent(Event ev) throws Exception {
		
		// TODO
		
		return ev;   // temporarily
	}
	
	
}
