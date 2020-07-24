package srv.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.event.EventDao;
import srv.domain.event.eventype.EventType;
import srv.domain.serviceclient.JdbcTemplateServiceClientDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;
import srv.domain.user.User;
import srv.services.EventService;
import static srv.utils.ParamUtil.*;

import srv.utils.FlashUtil;
import srv.utils.UserUtil;

/**
 * An instance of this class responds to requests to our server regarding events management. The 
 * request "handler" methods below are invoked by the spring framework for the module (collection 
 * of pages and dialogs) that manage events:  listing events,  creating, updating, deleting.
 * 
 * 
 * @author Conor Mackey
 * @author mahiggs
 * 
 *
 */

@Controller
@EnableWebSecurity
public class EventController {

	private static final String FM_KEY_ERROR = "errMsg";
	private static final String REGEX_VALID_DATE = "^now([+-][1-9]+)M$";
	private static final int GROUP_MONTH_OFFSET = 1;

	private static Logger log = LoggerFactory.getLogger(EventController.class);
	
	@Autowired EventService eventService;

	@Autowired
	UserUtil userUtil;
	
	/**
	 * Displays the admin manage events page. Allows for filtering the events table 
	 * based off of the query parameters
	 * 
	 * @param request
	 * @param response
	 * @param before 	query parameter for filtering events before current date
	 * @param after 	query parameter for filtering events after current date
	 * @param eType		query parameter for filtering events by event type
	 * @param sc		query parameter for filtering events by service client
	 * @param bm		query parameter for filtering events by board member
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("events")
	public ModelAndView basePageAction(RedirectAttributes attrs, @RequestParam(required = false) String before,  @RequestParam(required = false) String after,
			@RequestParam(required = false) String eType, @RequestParam(required = false) String sc, @RequestParam(required = false) String bm) {

		ModelAndView mav = new ModelAndView("events/adminManageEvents");
				
		try {
			
			List<Event> myEvents;
			List<EventType> types = eventService.allEventTypes();
			List<ServiceClient> clients = eventService.allServiceClients();
			List<User> boardMembers = eventService.allBoardMembers();
			
			// Checks to see if the current user is an admin or a board member, if so displays the CRUD are gone
			// otherwise the buttons are gone.
			mav.addObject("userAdmin", userUtil.userIsAdmin());
			mav.addObject("userBoardMember", userUtil.userIsBoardMember());

			
			mav.addObject("evtypes", types);
			mav.addObject("sClients", clients);
			mav.addObject("users", boardMembers);
			
			String beforeP = null;
			String afterP = null;
			Integer eTypeP = null;
			Integer scP = null;
			Integer bmP = null;
			
			mav.addObject("beforeSelected", 0); // turns the toggle button for before off
			mav.addObject("afterSelected", 0); // turns the toggle button for after off
			mav.addObject("monthSelected", 0); // turns the toggle button for last month off
			mav.addObject("selectedEtid", 0); // sets the combo box for event types to 'List All' 
			mav.addObject("selectedScid", 0); // sets the combo box for service clients to 'List All'
		
			// Filtering by past events
			if (before != null) {
				
				beforeP = before;
				if (before.equals("now")) {
					mav.addObject("beforeSelected", 1);   // turn toggle switch on
				}
				else {
					String monStr = matchingString(before, REGEX_VALID_DATE, GROUP_MONTH_OFFSET, "invalid before date expression");
					int mon = Integer.parseInt(monStr);
					log.debug("months: {}",mon);
					mav.addObject("beforeSelected", 1);
					mav.addObject("monthSelected", mon);
					
				}
			}
			
			// Filtering by future events
			if (after != null) {
				
				afterP = after;
				if (after.equals("now")) {
					mav.addObject("afterSelected", 1);
				} else {
					String monStr = matchingString(after, REGEX_VALID_DATE, GROUP_MONTH_OFFSET, "invalid after date expression");
					int mon = Integer.parseInt(monStr);
					log.debug("months: {}",mon);
					mav.addObject("afterSelected", 1);					
					mav.addObject("monthSelected", mon);
				}
			}
			
			// Filtering by event type
			if (eType != null) {
				eTypeP = optionalIntegerParam(eType, "Invalid Event Type ID; must be a positive integer.");
				mav.addObject("selectedEtid", eTypeP);
			}
			
			// Filtering by service client
			if (sc != null) {
				scP = optionalIntegerParam(sc, "Invalid Service client ID; must be a positive integer.");
				mav.addObject("selectedScid", scP);
			}
			
			// TODO needs board member dao support before this to work
			if (bm != null) {
				bmP = optionalIntegerParam(bm, "Invalid BoardMember ID; must be a positive integer.");
				mav.addObject("selectedBmid", bmP);				
			}
			
			myEvents = eventService.filteredEvents(beforeP, afterP, eTypeP, scP, bmP);			

			mav.addObject("events", myEvents);

			
		} catch (NumberFormatException e) {
			mav.addObject(FM_KEY_ERROR, String.format("Unable to convert to number: %s", e.getMessage()));
		}
		catch (Exception e) {

			// report any errors to an element on the page; assumes there is an element in our template.
			log.error(e.getMessage());
			mav.addObject(FM_KEY_ERROR, e.getMessage());
			
		}

		return mav;
	}
	
	/**
	 * Prepare and load the page that displays the details of a single event.  We 
	 * fetch the current event (it must exist) and any ancillary data that is required
	 * to configured the page form inputs. For servant and board member users. Admin
	 * users can also come here but are also displayed a dialog on the events manager page.
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@GetMapping("events/view/{id}")
	public ModelAndView viewEventPageAction(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
		
		ModelAndView mav = new ModelAndView("events/viewer");

		try {
			// fetch the event
			Event theEvent = eventService.eventById(id.intValue());
			
			mav.addObject("contact", theEvent.getContact());
			mav.addObject("eid", theEvent.getEid());
			mav.addObject("userAdmin", userUtil.userIsAdmin());
			mav.addObject("evName", theEvent.getTitle());
			mav.addObject("evDescr", theEvent.getNote());
			mav.addObject("volunteersNeeded", theEvent.getVolunteersNeeded());
			mav.addObject("hrsNeeded", theEvent.getNeededVolunteerHours());
			mav.addObject("rsvpHrs", theEvent.getRsvpVolunteerHours());
			mav.addObject("location", theEvent.getAddress());
			mav.addObject("date", theEvent.getDate());
			mav.addObject("sponsorName", theEvent.getType().getName());
			mav.addObject("sponsorDescr", theEvent.getType().getDescription());
			mav.addObject("srvClient", theEvent.getServiceClient().getName());
			mav.addObject("name", theEvent.getContact().fullName());
			mav.addObject("mainPhoneNum", theEvent.getContact().getPrimaryPhone());
			mav.addObject("otherPhoneNum", theEvent.getContact().getSecondaryPhone());
			mav.addObject("email", theEvent.getContact().getEmail());

		} catch (Exception e) {

			log.error(e.getMessage());
			mav.addObject(FM_KEY_ERROR, e.getMessage());
			
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

		// if flash err, add to model
		mav.addObject(FM_KEY_ERROR, FlashUtil.getFlashAttr(request, FM_KEY_ERROR));

		try {

			/*
			 * ancillary support.   we need the list of all event types so the user
			 * can change the event type reference.
			 */
			List<EventType> types = eventService.allEventTypes();
			
			List<ServiceClient> svcClient = eventService.allServiceClients();
			/*
			 * fetch the event
			 */
			Event theEvent = eventService.eventById(id.intValue());

			/*
			 * prepare the data model
			 */
			mav.addObject("event", theEvent);
			mav.addObject("evtypes", types);
			mav.addObject("svcClients", svcClient);


		} catch (Exception e) {

			log.error(e.getMessage());
			mav.addObject(FM_KEY_ERROR, e.getMessage());

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
	public RedirectView updateEventAction(HttpServletRequest request, RedirectAttributes redirectAttrs, @PathVariable Integer id) {

		try {
			/*
			 * fetch the event
			 */
			Event theEvent = eventService.eventById(id.intValue());
			
			// Harvesting the data
			String titleStr = requiredNonEmptyString(request.getParameter("evTitle"),"Event Title is required.");
			theEvent.setTitle(titleStr);
			

			String addressStr = requiredNonEmptyString(request.getParameter("evAddress"),"Event Location is required.");
			theEvent.setAddress(addressStr);

			
			Date tempDate = requiredDateLike(request.getParameter("evDate"), "yyyy/MM/dd hh:mm", "Event Date required." );
			theEvent.setDate(tempDate);
			
			
			int numVn = requiredIntegerParam(request.getParameter("evVN"), "Number of Volunteers is a required positive integer.");
			theEvent.setVolunteersNeeded(numVn);
			
			
			double volHr = requiredDoubleParam(request.getParameter("evNVH"),"Needed Volunteer Hours is required positive integer.");
			theEvent.setNeededVolunteerHours(volHr);
			
			
			double rsvp = requiredDoubleParam(request.getParameter("evRsvp"), "Requested Hours must be a valid real number.");
			theEvent.setRsvpVolunteerHours(rsvp);
			
			
			
			String noteStr = request.getParameter("evNote");
			
			if(noteStr != null) {
				noteStr = noteStr.trim();
				if(noteStr.length()>0) {
					log.debug("updating event note from {} to [{}]",theEvent.getNote(), noteStr);
					theEvent.setNote(noteStr);
				} else {
					theEvent.setNote("");
				}
			} else {
				theEvent.setNote("");
			}
			
			
			
			String contStr = request.getParameter("evContinuous");
			
			log.debug("evContinuous " + contStr);
			if(contStr != null) {
				contStr = contStr.trim();
				if(contStr.length()>0) {
					log.debug("updating event continuous from {} to [{}]",theEvent.isContinuous(), contStr);
					boolean isCon = contStr.equals("on");
					theEvent.setContinous(isCon);
				} else {
					throw new Exception("Continuous is required");
				}
			} else {
				theEvent.setContinous(false);
			}
			
			
			Integer ctId = requiredIntegerParam(request.getParameter("evContactId"), "Contact is missing.");


			// update the finalized event
			theEvent = eventService.updateEvent(theEvent, ctId);
			
			
			// everything is fine.... back to the event management base page
			return new RedirectView("/events",true);

		} catch (Exception e) {	

			log.error(e.getMessage());
			e.printStackTrace();
			
		    FlashUtil.addFlashAttr(request, FM_KEY_ERROR, e.getMessage());
		    
			return new RedirectView("/events/edit/"+id, true);
		}

	}





	
	private void setEventContactById(Event theEvent, int ctId) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Find the event
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@GetMapping(value = "/events/ajax/event/{id}/contact")
	public ModelAndView ajaxFetchEventContact_HTML(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
		ModelAndView mav = new ModelAndView("events/contact");
		response.setContentType(MediaType.TEXT_HTML_VALUE);
		
		try {
			
			
			/*
			 * fetch the event
			 */
			log.debug("fetching event {}",id);
			Event theEvent = eventService.eventById(id.intValue());
					
			mav.addObject("event",theEvent);
			mav.addObject("contact", theEvent.getContact());
		
			// everything is fine.... back to the event management base page
			return mav;

		} catch (Exception e) {
				
			mav.addObject(FM_KEY_ERROR,e.getMessage());
			return mav;
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
	public ResponseEntity<?> ajaxDeleteEvent(@PathVariable Integer id) {

    	try {
    		
    		System.err.println("delete "+id);
    		log.debug("deleting event {}", id);
    		
			eventService.deleteEvent(id);
		    return new ResponseEntity<Integer>(id, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<String>("invalid id",HttpStatus.NOT_FOUND);
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
	public ResponseEntity<?> ajaxNewEvent(@PathVariable Integer etid) {
		ModelAndView mav = new ModelAndView("events/eventDetails");
    	try {
    		
    		log.debug("creating new event type={}", etid);
			Event newev = eventService.createEventOfType(etid);

			// return the event id of the newly created object
		    return new ResponseEntity<Integer>(newev.getEid(), HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<String>("invalid event type id",HttpStatus.NOT_FOUND);
		}

	}
	
	
	@ResponseBody
	@GetMapping(value = "/events/ajax/event/{id}", produces="application/json")
	public ResponseEntity<?> ajaxFetchEvent(@PathVariable Integer id) {
		
    	try {
    		
    		System.err.println("fetch "+id);
    		log.debug("fetch event {}", id);
    		
			Event ev = eventService.eventById(id);
			
		    return new ResponseEntity<Event>(ev, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<String>("invalid event id",HttpStatus.NOT_FOUND);
		}


	}
	
	/**
	 * Ajax method to view the details of a single event. This controller prepares and returns
	 * the html of the event's detail for the dialog. For admin users.
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @return
	 */
	@GetMapping("events/ajax/event/{id}/html")
	public ModelAndView ajaxViewEvent_HTML(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
		
		ModelAndView mav = new ModelAndView("events/eventDetails");

		try {
			// fetch the event
			Event theEvent = eventService.eventById(id.intValue());
			
			mav.addObject("contact", theEvent.getContact());
			mav.addObject("eid", theEvent.getEid());
			mav.addObject("userAdmin", userUtil.userIsAdmin());
			mav.addObject("evName", theEvent.getTitle());
			mav.addObject("evDescr", theEvent.getNote());
			mav.addObject("volunteersNeeded", theEvent.getVolunteersNeeded());
			mav.addObject("hrsNeeded", theEvent.getNeededVolunteerHours());
			mav.addObject("rsvpHrs", theEvent.getRsvpVolunteerHours());
			mav.addObject("location", theEvent.getAddress());
			mav.addObject("date", theEvent.getDate());
			mav.addObject("sponsorName", theEvent.getType().getName());
			mav.addObject("sponsorDescr", theEvent.getType().getDescription());
			//handle the case that there is no service client with an if statement
			String tName = null; 
			if(theEvent.getServiceClient() != null) {
				tName = theEvent.getServiceClient().getName();
			}
			mav.addObject("srvClient", tName);
			// do the same here 
			String cName = null,  phone1 = null, phone2 = null, email = null; 
			if(theEvent.getContact() != null) {
				cName = theEvent.getContact().fullName();
				phone1 = theEvent.getContact().getPrimaryPhone();
				phone2 = theEvent.getContact().getSecondaryPhone();
				email = theEvent.getContact().getEmail();
			}
			
			mav.addObject("name", cName);
//    		mav.addObject(eventService.allServiceClients());
			
			mav.addObject("mainPhoneNum", phone1);
			mav.addObject("otherPhoneNum", phone2);
			mav.addObject("email", email);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mav;
	}

	
}