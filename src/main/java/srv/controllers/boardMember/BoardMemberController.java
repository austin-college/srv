package srv.controllers.boardMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import srv.services.BoardMemberHoursListService;

/**
 * This is the algorithm that prepares the response
 * for the board member home page.
 *
 */

@Controller
public class BoardMemberController {

	private static Logger log = LoggerFactory.getLogger(BoardMemberController.class);
	BoardMemberHoursListService bmHrListSrv = new BoardMemberHoursListService();
	

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

		return mav;
	}
	
}
