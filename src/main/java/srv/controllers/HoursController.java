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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.domain.event.eventype.JdbcTemplateEventTypeDao;
import srv.domain.hours.ServiceHours;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.user.JdbcTemplateUserDao;
import srv.services.EventService;
import srv.services.ServiceHoursService;
import srv.utils.ParamUtil;
import srv.utils.UserUtil;

/**
 * This is the algorithm that prepares the response.
 * 
 * @author mahiggs
 *
 */

@Controller
@EnableWebSecurity
@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN", "ROLE_SERVANT"})
public class HoursController {
	
	private static Logger log = LoggerFactory.getLogger(HoursController.class);
	
	@Autowired
	ServiceHoursService hrSvc;
	
	@Autowired
	EventService evSvc;
	
	@Autowired
	UserUtil userUtil;
	
	
	
	/**
	 * Splash action displays the splash page. See splash.html template
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author Hunter Couturier
	 */
	@GetMapping("/hours")
	public ModelAndView handleBasePageRequest(HttpServletRequest request, HttpServletResponse response, @RequestParam(required = false) String sc,
			@RequestParam(required = false) String month) {

		ModelAndView mav = new ModelAndView("hours/viewHours");
		
		try {
			
			log.debug("fetching events");
			List<Event> events = evSvc.allEvents();
			log.debug("...{} events detected", events.size());
			
			Integer userId;
			List<ServiceHours> filteredHours;
			List<ServiceClient> sponsors = hrSvc.listCurrentSponsors();
			
			mav.addObject("sClients", sponsors);
			mav.addObject("events", events);
			mav.addObject("userAdmin", userUtil.userIsAdmin());

			log.debug("fetching hours");
			List<ServiceHours> hours = hrSvc.listHours();
			log.debug("...{} hours detected.",hours.size());
			
			Integer scP = null;
			String monthNameP = null;
			
			mav.addObject("selectedScid", 0); // sets the combo box for service clients to 'List All'
			mav.addObject("selectedMonth", 0); // sets the combo box for months to 'List All'
			
			// Filtering by service client
			if (sc != null) {
				scP = Integer.valueOf(sc);
				mav.addObject("selectedScid", Integer.valueOf(sc));
			}
			
			// Filtering by month
			if (month != null) {
				monthNameP = month;
				mav.addObject("selectedMonth", monthNameP);
			}
				
			/*
			 * Get the current user's id. If they are an admin set it to null
			 * so they can see all service hours
			 */
			if (userUtil.userIsAdmin())
				userId = null;
			else
				userId = userUtil.currentUser().getUid();
			
			filteredHours = hrSvc.filteredHours(userId, scP, monthNameP);
			
			mav.addObject("hours", filteredHours);
			
			mav.addObject("semTot", hrSvc.getSemTot(hours)); //total hours served per semester
			mav.addObject("termTot", hrSvc.getTermTot(hours)); //total hours served per term
			mav.addObject("totOrgs", hrSvc.getTotOrgs(hours)); //total organizations helped
			mav.addObject("avgPerMo", hrSvc.getAvgPerMo(hours)); //average hours served per month

			
		} catch (Exception e1) {
			
			log.error(e1.getMessage());
			
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return mav;
	}

	/**
	 * When the client needs to delete a service hour, this controller action will
	 * handle the request.  Note: we are using the DELETE HTTP method and embedding
	 * the item id as part of the URL (not a query parameter).   
	 *  
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/hours/ajax/del/{id}")
	public ResponseEntity<Integer> ajaxDeleteServiceHour(@PathVariable Integer id) {

		try {
    		
    		log.error("delete "+ id);
    		log.debug("deleting service hour {}", id);
    		
    		hrSvc.removeServiceHour(id);
		    
    		return new ResponseEntity<>(id, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}


	}
	
	/**
	 * Ajax renders a new page updating the selected service hour from the table.
	 * 
	 * @param request
	 * @param response
	 * @return MAV of the updated service hour row to the table
	 */
	@PostMapping("/hours/ajax/editHr")
	public ModelAndView ajaxServiceHourUpdate(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView("/hours/ajax_singleHourRow");
		
		response.setContentType("text/html"); 
		
		try {
			
			// fetch the data sent from the JavaScript function and verify the fields
			Integer shid = ParamUtil.requiredIntegerParam(request.getParameter("shid"), "Service Hours must be selected.");
			Integer scid = ParamUtil.requiredIntegerParam(request.getParameter("scid"), "Service Client must be selected.");
			Integer eid = ParamUtil.requiredIntegerParam(request.getParameter("eid"), "Event must be selected.");
			Double hrs = ParamUtil.requiredDoubleParam(request.getParameter("hrSrved"), "Hours Served must be filled and be numeric.");
			String reflection = request.getParameter("reflect");
			String descr = request.getParameter("descr");
			
			// create a new service hr in the database then return it back to the callback function
			hrSvc.updateHour(shid, scid,  eid, hrs,  reflection, descr);
			
			ServiceHours updatedSrvHr = hrSvc.serviceHourById(shid);
			
			mav.addObject("shid", updatedSrvHr.getShid());
			mav.addObject("title", updatedSrvHr.getEvent().getTitle());
			mav.addObject("hours", updatedSrvHr.getHours());
			mav.addObject("status", updatedSrvHr.getStatus());
			
			
			
		} catch (Exception e) {
			log.error("\n\n ERROR ");
			log.error(e.getMessage());
			
			e.printStackTrace();
			
			response.setStatus(410);
			
			
			mav = new ModelAndView("/error");
			
			mav.addObject("errMsg", e.getMessage());
		}
		

	
		return mav;
		
	}

	/**
	 * Ajax call to create and return the new ServiceHour to the database
	 * 
	 */
	@PostMapping("/hours/ajax/addHr")
	public ModelAndView ajaxAddServiceHour(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("/hours/ajax_singleHourRow");

		response.setContentType("text/html");
				
		try {
		
			// fetch the data sent from the JavaScript function and verify the fields
			Integer scid = ParamUtil.requiredIntegerParam(request.getParameter("scid"), "Service Client must be selected.");
			Integer eid = ParamUtil.requiredIntegerParam(request.getParameter("eid"), "Event must be selected.");
			Double hrs = ParamUtil.requiredDoubleParam(request.getParameter("hrServed"), "Hours Served must be filled and be numeric.");
			String reflection = request.getParameter("reflect");
			String descr = request.getParameter("descr");
			
			// create a new service hr in the database then return it back to the callback function
			ServiceHours newSrvHr = hrSvc.createServiceHour(scid,  eid, hrs,  reflection, descr);
			
			mav.addObject("shid", newSrvHr.getShid());
			mav.addObject("title", newSrvHr.getEvent().getTitle());
			mav.addObject("hours", newSrvHr.getHours());
			mav.addObject("status", newSrvHr.getStatus());
			
			
			
		} catch (Exception e) {
			log.error("\n\n ERROR ");
			log.error(e.getMessage());
			
			e.printStackTrace();
			
			response.setStatus(410);
			
			
			mav = new ModelAndView("/error");
			
			mav.addObject("errMsg", e.getMessage());
		}
		
		return mav;

	}
	
	/**
	 * Ajax call to retrieve and return selected service hour from the database.
	 */
	@ResponseBody
	@GetMapping(value="/hours/ajax/hour/{id}", produces="application/json")
	public ResponseEntity<ServiceHours> ajaxFetchServiceHour(@PathVariable Integer id) {
		
		try {
			System.err.println("fetch service hour " + id);
			log.debug("fetch service hour {}", id);
			
			ServiceHours sh = hrSvc.serviceHourById(id);
			
			return new ResponseEntity<>(sh, HttpStatus.OK);
			
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/** 
	 * This request handle renders an entire page useful for testing only.   This
	 * is not part of our actual site.
	 */
	@GetMapping("/test/hours")
	public ModelAndView handleReasonRequest(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("test/hoursTestView");


		try {

			int cnt = hrSvc.listHours().size();

			mav.addObject("count",cnt);

			List<ServiceHours> myHours = hrSvc.listHours();

			mav.addObject("serviceHours", myHours );

		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		return mav;
	}
}
