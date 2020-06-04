
package srv.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

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

