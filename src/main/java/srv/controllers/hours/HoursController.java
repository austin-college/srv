package srv.controllers.hours;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private static Logger log = LoggerFactory.getLogger(HoursController.class);

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

	/**
	 * Action to display add hours page. See addHours.html
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author Sameeha Khaled
	 */
	@GetMapping("/addHours")
	public ModelAndView addHoursAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("hours/addHours");

		return mav;
	}
}
