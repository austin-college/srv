
package srv.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import srv.domain.event.Event;
import srv.services.EventService;

/**
 * A Controller object that renders responses for the splash (home) page of the webapp 
 * that is accessible to all public users.
 * 
 * @author mahiggs
 *
 */

@Controller
@EnableWebSecurity
public class SplashController {
	
	@Autowired
	EventService evSvc;

	   /**
	    * Splash action displays the splash page. See splash.html template
	    * 
	    * @param request
	    * @param response
	    * @return
	    * 
	    * @author lahouse
	    */
	   @GetMapping("/splash")
	   public ModelAndView splashAction(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("splash/splash");
	
			try {
				
				List<Event> upcomingEvents = evSvc.filteredEvents(null, "now+1M", null, null, null);
				mav.addObject("events", upcomingEvents);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		   
		   
		   return mav;
	   }
	   

	   /**
	    * Provide the mapping if the user did not know to enter through the splash page.  In this
	    * case, we send back a redirect to the requester.
	    * 
	    * @param attributes
	    * @return
	    */
	    @GetMapping("/")
	    public RedirectView redirectAll (
	    		
	      RedirectAttributes attributes) {
	    	
	      return new RedirectView("/srv/splash");
	        
	    }
}

