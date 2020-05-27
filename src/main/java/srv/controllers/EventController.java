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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.event.Event;
import srv.domain.event.EventDao;
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
	public ModelAndView adminManageEventsAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("events/adminManageEvents");

		try {

			// Lists the current events in the event database in a table
			List<Event> myEvents = eventService.allEvents();
			
			mav.addObject("events", myEvents);


		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mav;
	}
	
	/**
	 * When the client needs to delete an event, this controller action will
	 * handle the request.  Note: we are using the DELETE HTTP method and embedding
	 * the item id as part of the URL (not a query parameter).   
	 *  
	 * @param id
	 * @return
	 */
	@DeleteMapping(value = "/events/{id}")
	public ResponseEntity<Integer> handleDeleteEvent(@PathVariable Integer id) {

    	try {
    		
			eventService.deleteEvent(id);
		    return new ResponseEntity<>(id, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}


	}

}
