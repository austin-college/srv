package srv.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.services.EventService;

/**
 * 
 * this algorithm prepares the response 
 * @author Conor Mackey
 * @author mahiggs
 * 
 *
 */

@Controller
@EnableWebSecurity
public class EventController {

	private static Logger log = LoggerFactory.getLogger(EventController.class);
	
	@Autowired EventService eventService;

	/**
	 * displays the admin manage events page
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("events")
	public ModelAndView basePageAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("events/adminManageEvents");

		try {

			// Lists the current events in the event database in a table
			List<Event> myEvents = eventService.allEvents();
			List<EventType> types = eventService.allEventTypes();
			
			mav.addObject("events", myEvents);
			mav.addObject("evtypes", types);


		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mav;
	}
	
	
	/**
	 * Prepare and load the page that allows the user to edit a single event.  We 
	 * fetch the current event (it must exist) and any ancillary data that is required
	 * to configured the page form inputs.
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@GetMapping("events/edit/{id}")
	public ModelAndView editPageAction(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {

		
		ModelAndView mav = new ModelAndView("events/editor");

		try {

			/*
			 * ancillary support.   we need the list of all event types so the user
			 * can change the event type reference.
			 */
			List<EventType> types = eventService.allEventTypes();
			
			/*
			 * fetch the event
			 */
			Event theEvent = eventService.eventById(id.intValue());

			/*
			 * prepare the data model
			 */
			mav.addObject("event", theEvent);
			mav.addObject("evtypes", types);


		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mav;
	}
	
	
	/**
	 * Client side UI will post changes to the specified event here.  We update
	 * the backend database with values passed via query parameters and return
	 * success as the event id.   
	 * 
	 * @param id
	 * @return
	 */
	
	@PostMapping(value = "/events/update/{id}")
	public ModelAndView updateEventAction(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {

		try {
			/*
			 * fetch the event
			 */
			Event theEvent = eventService.eventById(id.intValue());
			
			String titleStr = request.getParameter("evTitle");
			log.debug(titleStr);
			if (titleStr != null) {
				titleStr = titleStr.trim();
				if (titleStr.length()>0) {
					log.debug("updating event {} title from [{}] to [{}]",theEvent.getTitle(), titleStr);
					theEvent.setTitle(titleStr);
				}
			}

			
			theEvent = eventService.updateEvent(theEvent);
			
			// everything is fine.... back to the event management base page
			return new ModelAndView("redirect:/events");

		} catch (Exception e) {

			// TODO:  flash error on page to user.
			
			return new ModelAndView("redirect:/events/edit/"+id);

		}

	}

	
	/**
	 * When the client needs to delete an event, this controller action will
	 * handle the request.  Note: we are using the DELETE HTTP method and embedding
	 * the item id as part of the URL (not a query parameter).   
	 *  
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/events/ajax/del/{id}")
	public ResponseEntity<Integer> ajaxDeleteEvent(@PathVariable Integer id) {

    	try {
    		
    		System.err.println("delete "+id);
    		log.debug("deleting event {}", id);
    		
			eventService.deleteEvent(id);
		    return new ResponseEntity<>(id, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}


	}

	
	/**
	 * Ajax method to create an event of the type specified (etid).  The 
	 * new object is only configured with default values.  We assume the 
	 * user will eventually update it.   Here we return the newly assigned
	 * event id so the client can request and edit as needed.
	 * 
	 * @param etid
	 * @return
	 */
	@PostMapping(value = "/events/ajax/new/{etid}")
	public ResponseEntity<Integer> ajaxNewEvent(@PathVariable Integer etid) {

    	try {
    		
    		log.debug("creating new event type={}", etid);
    		
			Event newev = eventService.createEventOfType(etid);
			
			// return the event id of the newly created object
		    return new ResponseEntity<>(newev.getEid(), HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}


	}
}
