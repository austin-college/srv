package srv.controllers.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * this algorithm prepares the response 
 * 	- Michael Higgs
 * 
 * @author Conor Mackey
 * 
 * [Conor's comments] Not sure how to make these show up after the user logs in as either a 
 * board member or an admin, but I thought this would be helpful to have for when someone
 * smarter than me takes over. 
 * 
 *
 */

@Controller
public class HomeController {
	
	@GetMapping("/home/boardMember")
	public ModelAndView boardMemberAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/boardMember");

		return mav;
	}
	
	@GetMapping("/home/admin")
	public ModelAndView adminAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/admin");

		return mav;
	}
}
