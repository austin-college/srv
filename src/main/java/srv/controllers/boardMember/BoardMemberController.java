package srv.controllers.boardMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import srv.services.BoardMemberHoursListService;

/**
 * This is the algorithm that prepares the response
 * for the board member home page.
 *
 */

@Controller
@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN" })
public class BoardMemberController {

	
	@Autowired
	BoardMemberHoursListService bmHrListSrv;

	/**
	 * Maps boardMember.html template to /boardMember
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/boardMember")
	public ModelAndView splashAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/boardMember");
		
		mav.addObject("hours", bmHrListSrv.listHoursToBeApproved());

		return mav;
	}
	
}
