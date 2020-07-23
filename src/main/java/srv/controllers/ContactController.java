package srv.controllers;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.services.ContactService;

@Controller
@EnableWebSecurity
public class ContactController {

	private static Logger log = LoggerFactory.getLogger(ContactController.class);
	
	private static final String FM_KEY_ERROR = "errMsg";
	
	@Autowired ContactService contactService;
	
	@ResponseBody
	@GetMapping(value = "/ajax/contacts/{id}/json", produces="application/json")
	public ResponseEntity<?> ajaxFetchContact_JSON(@PathVariable Integer id) {
		
    	try {
    		
    		System.err.println("fetch "+id);
    		log.debug("fetch contact {}", id);
    		
			Contact c = contactService.contactById(id);
			
		    return new ResponseEntity<Contact>(c, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<String>("invalid contact id",HttpStatus.NOT_FOUND);
		}


	}
	
	
	/**
	 * We use this method to retrieve a chunk of HTML describing a specified contact.
	 * Because we are returning HTML, we expect the caller to specify which format we
	 * want.  For example, "contact_dialog" will result in our expanded dialog form
	 * view.  The "contact_compact" will result in a compact small readonly view of 
	 * the current contact.    
	 * 
	 * <p>
	 * The templates must be present in the contacts folder in our FM templates area.
	 * 
	 * @param request
	 * @param response
	 * @param id
	 * @param mavStr name of FM template to use
	 * @return
	 */
	@GetMapping(value = "/ajax/contacts/{id}/html")
	public ModelAndView ajaxFetchContact_HTML(HttpServletRequest request, 
			HttpServletResponse response, 
			@PathVariable Integer id,
			@RequestParam(required = false) String template
			) {
		
		/*
		 * If unspecified, we default to use the compact template.  Should be
		 * most common use case for this site.
		 */
		if (template == null || template.trim().length()==0) 
			template = "contact_compact";
		
		ModelAndView mav = new ModelAndView("contacts/"+template);
		response.setContentType(MediaType.TEXT_HTML_VALUE);
		
		try {
			
			
			/*
			 * fetch the event
			 */
			log.debug("fetching contact {}",id);
			Contact c = contactService.contactById(id);
					
			mav.addObject("contact", c);
		
			// everything is fine.... back to the event management base page
			return mav;

		} catch (Exception e) {
				
			mav.addObject(FM_KEY_ERROR,e.getMessage());
			return mav;
		}

	}
	
	
	@GetMapping(value = "/ajax/contacts")
	public ModelAndView ajaxFetchAllContacts_HTML(HttpServletRequest request, 
			HttpServletResponse response, 
			@RequestParam(required = false) String template
			) {
		
		/*
		 * If unspecified, we default to use the compact template.  Should be
		 * most common use case for this site.
		 */
		if (template == null || template.trim().length()==0) 
			template = "contact_selection_dialog";
		
		ModelAndView mav = new ModelAndView("contacts/"+template);
		response.setContentType(MediaType.TEXT_HTML_VALUE);
		
		try {
			
			
			/*
			 * fetch the event
			 */
			log.debug("fetching all contacts ");
			List<Contact> clst = contactService.allContacts();
					
			mav.addObject("contacts", clst);
		
			return mav;

		} catch (Exception e) {
				
			mav.addObject(FM_KEY_ERROR,e.getMessage());
			return mav;
		}

	}
	
	
	
	@PostMapping(value = "/ajax/contacts/{id}")
	public ResponseEntity<?> ajaxUpdateContact(@PathVariable Integer id,
			@RequestParam(required = false) String fname,
			@RequestParam(required = false) String lname,
			@RequestParam(required = false) String pphone,
			@RequestParam(required = false) String sphone,
			@RequestParam(required = false) String email,
			@RequestParam(required = false) String street,
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String state,
			@RequestParam(required = false) String zip) {

    	try {
    		
    		log.debug("updating contact {}: {} {}", id, fname, lname);
    		
			contactService.updateContact(id, fname, lname, pphone, sphone, email, street, city, state, zip);
			
		    return this.ajaxFetchContact_JSON(id);
		    
		} catch (Exception e) {
			return new ResponseEntity<String>("invalid id",HttpStatus.NOT_FOUND);
		}


	}
}
