package srv.controllers.hours;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * This is the algorithm that prepares the response. 
 * 
 * @author mahiggs
 *
 */

@Controller
public class HoursController {
	

	   /**
	    * Splash action displays the splash page. See splash.html template
	    * 
	    * @param request
	    * @param response
	    * @return
	    * 
	    * @author Hunter Couturier
	    */
	   @GetMapping("/viewHours")
	   public ModelAndView splashAction(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("hours/viewHours");
		   
		   return mav;
	   }
	   
}
