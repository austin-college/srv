package srv.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.user.User;
import srv.services.EventService;
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
	public ModelAndView basePageAction(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String before,  @RequestParam(required = false) String after,
			@RequestParam(required = false) String eType, @RequestParam(required = false) String sc, @RequestParam(required = false) String bm) {

		ModelAndView mav = new ModelAndView("events/adminManageEvents");
				
		try {
			
			List<Event> myEvents;
			List<EventType> types = eventService.allEventTypes();
			List<ServiceClient> clients = eventService.allServiceClients();
			List<User> boardMembers = eventService.allBoardMembers();
			
			// Checks to see if the current user is an admin or a board member, if so displays the CRUD are gone
			// otherwise the buttons are gone.
			mav.addObject("userAdmin",userUtil.userIsAdmin());
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
				mav.addObject("beforeSelected", 1);
				
				// Filtering by last month's events
				if (before.equals("now-1M"))
					mav.addObject("monthSelected", 1);
			}
			
			// Filtering by future events
			if (after != null) {
				
				afterP = after;
				mav.addObject("afterSelected", 1);
				
				// Filtering by next month's events
				if (after.equals("now+1M"))
					mav.addObject("monthSelected", 1);
			}
			
			// Filtering by event type
			if (eType != null) {
				eTypeP = Integer.valueOf(eType);
				mav.addObject("selectedEtid", Integer.valueOf(eType));
			}
			
			// Filtering by service client
			if (sc != null) {
				scP = Integer.valueOf(sc);
				mav.addObject("selectedScid", Integer.valueOf(sc));
			}
			
			// TODO needs board member dao support before this to work
			if (bm != null) {
				bmP = Integer.valueOf(bm);
				mav.addObject("selectedBmid", Integer.valueOf(bm));
			}
			
			myEvents = eventService.filteredEvents(beforeP, afterP, eTypeP, scP, bmP);			

			mav.addObject("events", myEvents);

		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mav;
	}
	
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
			mav.addObject("mainPhoneNum", theEvent.getContact().getPhoneNumMobile());
			mav.addObject("otherPhoneNum", theEvent.getContact().getPhoneNumWork());
			mav.addObject("email", theEvent.getContact().getEmail());
//			
		} catch (Exception e) {
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
			
			// Harvesting the data
			String titleStr = request.getParameter("evTitle");
			
			// Verifying the data
			log.debug(titleStr);
			if (titleStr != null) {
				titleStr = titleStr.trim();
				// case when the title is not empty
				if (titleStr.length()>0) {
					log.debug("updating event title from {} to [{}]",theEvent.getTitle(), titleStr);
					theEvent.setTitle(titleStr);
				}  // case when the title is empty
				else {
					throw new Exception("Event Title is required");
				}
			} 
			
			/*
			 * Following the same steps above for rest of the fields:
			 * Harvest each variables, verify each variables, set with new value
			 */
			String addressStr = request.getParameter("evAddress");
			
			log.debug(addressStr);
			if(addressStr != null) {
				addressStr = addressStr.trim();
				if(addressStr.length()>0) {
					log.debug("updating event address from {} to [{}]",theEvent.getAddress(), addressStr);
					theEvent.setAddress(addressStr);
				} else {
					throw new Exception("Event Address is required");
				}
			} else {
				throw new Exception("Event Address is required");
			}
			
			String dateStr = request.getParameter("evDate");
			
			log.debug(dateStr);
			if(dateStr != null) {
				dateStr = dateStr.trim();
				if(dateStr.length()>0) {
					Date tempDate = new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(dateStr);
					log.debug("updating event date from {} to [{}]",theEvent.getDate(), tempDate);
					theEvent.setDate(tempDate);
				} else {
					throw new Exception("Event Date is required");
				}
			} else {
				throw new Exception("Event Date is required");
			}

			
			String numVNStr = request.getParameter("evVN");
			
			if(numVNStr != null) {
				int numVN = Integer.parseInt(numVNStr);
				if(numVN > 0) {
					log.debug("updating event VN from {} to [{}]",theEvent.getVolunteersNeeded(), numVN);
						theEvent.setVolunteersNeeded(numVN);
				}	else {
					throw new Exception("Number of Volunteers Needed field should not be 0.");
				}
			} else {
				throw new Exception("Number of Volunteers Needed field is empty");
			}
			
			
			String volHrStr = request.getParameter("evNVH");
			
			if(volHrStr != null) {
				double volHr = Double.parseDouble(volHrStr);
				if(volHr > 0) {
					log.debug("updating event NVH from {} to [{}]",theEvent.getNeededVolunteerHours(), volHr);
						theEvent.setNeededVolunteerHours(volHr);
				}	else {
					throw new Exception("Needed Volunteer Hours field is 0.");
				}
			} else {
				throw new Exception("Number of Volunteer Hours field is empty");
			}
			
			
			String rsvpStr = request.getParameter("evRsvp");
			
			if (rsvpStr != null	) {
				double rsvp = Double.parseDouble(rsvpStr);
				if(rsvp > 0) {
					log.debug("updating event rsvp from {} to [{}]",theEvent.getRsvpVolunteerHours(), rsvp);
						theEvent.setRsvpVolunteerHours(rsvp);
				}	else {
					throw new Exception("Registerd Hours field is 0.");
				}	
			} else {
				throw new Exception("Registered Hours field is empty");
			}
			
			
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
//			
//			// To apply each variable changes, we will call harvest each variables in contact object
//			String contactFNStr = request.getParameter("evContact_firstName");
//
//			log.debug(contactFNStr);
//			if(contactFNStr != null) {
//				contactFNStr = contactFNStr.trim();
//				if(contactFNStr.length()>0) {
//					log.debug("updating event {} contact first name from [{}] to [{}]",
//							theEvent.getContact().getFirstName(), contactFNStr);
//					theEvent.getContact().setFirstName(contactFNStr);
//				} else {
//					throw new Exception("Contact's first name is required");
//				}
//			}
//			
//			String contactLNStr = request.getParameter("evContact_lastName");
//			
//			if(contactLNStr != null) {
//				contactLNStr = contactLNStr.trim();
//				if(contactLNStr.length()>0) {
//					log.debug("updating event {} contact first name from [{}] to [{}]",
//							theEvent.getContact().getLastName(), contactLNStr);
//					theEvent.getContact().setLastName(contactLNStr);
//				} else {
//					throw new Exception("Contact's Last name is required");
//				}
//			}
//			
//			String contactEmailStr = request.getParameter("evContact_email");
//			
//			if(contactEmailStr != null) {
//				contactEmailStr = contactEmailStr.trim();
//				if(contactEmailStr.length()>0) {
//					log.debug("updating event {} contact first name from [{}] to [{}]",
//							theEvent.getContact().getEmail(), contactEmailStr);
//					theEvent.getContact().setEmail(contactEmailStr);
//				} else {
//					throw new Exception("Contact's email is required");
//				}
//			}
			

			// update the finalized event
			theEvent = eventService.updateEvent(theEvent);
			
			// everything is fine.... back to the event management base page
			return new ModelAndView("redirect:/events");

		} catch (Exception e) {	

			log.error(e.getMessage());
			e.printStackTrace();
			
			return new ModelAndView("redirect:/events/edit/"+id);

		}

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
	public ModelAndView ajaxFetchEventContact(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
		
		try {
			/*
			 * fetch the event
			 */
			log.debug("fetching event {}",id);
			Event theEvent = eventService.eventById(id.intValue());
			
			ModelAndView mav = new ModelAndView("events/contact");
			
			mav.addObject("event",theEvent);
			mav.addObject("contact", theEvent.getContact());
			
			// everything is fine.... back to the event management base page
			return mav;

		} catch (Exception e) {

			// TODO:  flash error on page to user.
			
			return new ModelAndView("redirect:/events/");

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
	
	
	@ResponseBody
	@GetMapping(value = "/events/ajax/event/{id}", produces="application/json")
	public ResponseEntity<Event> ajaxFetchEvent(@PathVariable Integer id) {
		
    	try {
    		
    		System.err.println("fetch "+id);
    		log.debug("fetch event {}", id);
    		
			Event ev = eventService.eventById(id);
			
		    return new ResponseEntity<>(ev, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}


	}
	
	@GetMapping("events/ajax/event/{id}/html")
	public ModelAndView ajaxViewEventHtml(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
		
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
			mav.addObject("srvClient", theEvent.getServiceClient().getName());
			mav.addObject("name", theEvent.getContact().fullName());
			mav.addObject("mainPhoneNum", theEvent.getContact().getPhoneNumMobile());
			mav.addObject("otherPhoneNum", theEvent.getContact().getPhoneNumWork());
			mav.addObject("email", theEvent.getContact().getEmail());
//			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mav;
	}
	
}
