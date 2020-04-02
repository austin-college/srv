
package srv.controllers.about;

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
public class AboutController {
	

	   /**
	    * Splash action displays the splash page. See splash.html template
	    * 
	    * @param request
	    * @param response
	    * @return
	    * 
	    * @author lahouse
	    */
	   @GetMapping("/about")
	   public ModelAndView aboutAction(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("about/aboutBase");
		   
		   
		   return mav;
	   }
	   
	   @GetMapping("/about/aboutBase")
	   public ModelAndView aboutSegun(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("about/aboutBase/Segun");
		   
		   
		   return mav;
	   }
}

