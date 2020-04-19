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
 * 
 * @author Conor Mackey
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
