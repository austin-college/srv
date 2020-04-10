
package srv.controllers.splash;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

/**
 * This is the algorithm that prepares the response. 
 * 
 * @author mahiggs
 *
 */

@Controller
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

