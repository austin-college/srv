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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.domain.event.eventype.EventTypeDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;
import srv.utils.ParamUtil;
import srv.utils.UserUtil;

@Controller
@EnableWebSecurity
public class EventTypeController {

	private static Logger log = LoggerFactory.getLogger(EventTypeController.class);
	
	@Autowired
	EventTypeDao etDao;
	
	@Autowired
	ServiceClientDao scDao;
	
	
	/**
	 * Displays the admin manage event types page.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("eventTypes")
	public ModelAndView basePageAction(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView("eventtypes/adminManageEventTypes");
		
		try {
			
			List<EventType> currentEvTypes = etDao.listAll();
			List<ServiceClient> currentClients = scDao.listAll();
			
			mav.addObject("evTypes", currentEvTypes);
			mav.addObject("clients", currentClients);

		} catch (Exception e) {
		
			e.printStackTrace();
		}
		
		return mav;		
	}
	
	/**
	 * Ajax call to fetch and return the ServiceClient by its id by using the 
	 * ServiceClientDao.
	 * 
	 * @param id
	 * @return
	 */
	@ResponseBody
	@GetMapping(value="/eventTypes/ajax/serviceClient/{id}", produces="application/json")
	public ResponseEntity<ServiceClient> ajaxFetchServiceClient(@PathVariable Integer id) {
		

    	try {
    		
    		System.err.println("fetch " + id);
    		log.debug("fetch service client {}", id);
    		
			ServiceClient sc = scDao.fetchClientById(id);
			
		    return new ResponseEntity<>(sc, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Ajax call to create and return the new EventType to the database.
	 * 
	 */
	@PostMapping("/eventTypes/ajax/addEt")
	public ModelAndView ajaxAddEventType(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView("/eventtypes/ajax_singleEtRow");

		response.setContentType("text/html");

		try {
			
			// fetch the data sent from the JavaScript function and verify the fields 
			String etName = request.getParameter("name");
			String etDescr = request.getParameter("descr");
		
			Double etDefHrs = ParamUtil.optionalDoubleParam(request.getParameter("defHrs"), "Default Hours must be numeric.");
			
			boolean pinHrs = ParamUtil.requiredBooleanParam(request.getParameter("pinHrs"), "Pin Hours must be selected.");
			Integer scid = ParamUtil.requiredIntegerParam(request.getParameter("scid"), "Service Client must be selected and be numeric.");
			
			// Make sure everything is coming okay
			log.debug(etName + " " +  etDescr + " " + etDefHrs + " " + pinHrs + " " + scid);
			
			// Create a new event type in the database then return it back to the callback function
			EventType newEvType = etDao.create(etName, etDescr, etDefHrs, pinHrs, scid);
					
			mav.addObject("etid", newEvType.getEtid());
			mav.addObject("etName", newEvType.getName());
			mav.addObject("description", newEvType.getDescription());
			mav.addObject("defHours", newEvType.getDefHours());
			mav.addObject("defClient", newEvType.getDefClient());
			mav.addObject("name", newEvType.getDefClient().getName());


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
	 * Ajax call to retrieve and return selected event type from the database.
	 */
	@ResponseBody
	@GetMapping(value="/eventTypes/ajax/eventType/{id}", produces="application/json")
	public ResponseEntity<EventType> ajaxFetchEventType(@PathVariable Integer id) {
		
		try {
			log.debug("fetch event type " + id);
			
			EventType evType = etDao.fetchEventTypeById(id);
			
			return new ResponseEntity<>(evType, HttpStatus.OK);
		
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Ajax call to update the specified EventType in the database.
	 * 
	 */
	@PostMapping("/eventTypes/ajax/editEt")
	public ModelAndView ajaxEditEventType(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView("/eventtypes/ajax_singleEtRow");

		response.setContentType("text/html");

		try {
			
			// fetch the data sent from the JavaScript function and verify the fields 
			String etName = request.getParameter("name");
			String etDescr = request.getParameter("descr");
			
			Double etDefHrs = ParamUtil.optionalDoubleParam(request.getParameter("defHrs"), "Default Hours must be numeric.");
			
			boolean pinHrs = ParamUtil.requiredBooleanParam(request.getParameter("pinHrs"), "Pin Hours must be selected.");
			Integer scid = ParamUtil.requiredIntegerParam(request.getParameter("scid"), "Service Client must be selected and be numeric.");
			Integer etid = ParamUtil.requiredIntegerParam(request.getParameter("etid"), "Event type id is required.");
			
			// Make sure everything is coming okay
			log.debug(etid + " " + etName + " " +  etDescr + " " + etDefHrs + " " + pinHrs + " " + scid);
			
			// Create update event type in the database then return it back to the callback function
			etDao.update(etid, etName, etDescr, etDefHrs, pinHrs, scid);
			
			EventType updatedEvType = etDao.fetchEventTypeById(etid);
			
			mav.addObject("etid", updatedEvType.getEtid());
			mav.addObject("etName", updatedEvType.getName());
			mav.addObject("description", updatedEvType.getDescription());
			mav.addObject("defHours", updatedEvType.getDefHours());
			mav.addObject("defClient", updatedEvType.getDefClient());
			mav.addObject("name", updatedEvType.getDefClient().getName());
		

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

	
}