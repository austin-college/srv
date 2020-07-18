package srv.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.hours.ServiceHoursDao;
import srv.domain.user.BoardMemberUserDao;
import srv.services.BoardMemberHoursListService;

/**
 * This is the algorithm that prepares the response
 * for the board member home page.
 *
 */

@Controller
@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN" })
public class BoardMemberController {

	private static final String FM_KEY_ERROR = "errMsg";

	private static Logger log = LoggerFactory.getLogger(EventController.class);

	@Autowired
	BoardMemberHoursListService bmHrListSrv;
	
	@Autowired
	BoardMemberUserDao bmDao;

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
	
	/**
	 * displays the admin edit board member/co chair base page
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/boardmembers")
	public ModelAndView adminManageBoardMemberBasePage(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/adminManageBoardMembers");

		try {
			
			mav.addObject("boardMembers", bmDao.listAllBoardMemberUsers());
			
		} catch (Exception e) {
			
			// report any errors to an element on the page; assumes there is an element in our template.
			log.error(e.getMessage());
			mav.addObject(FM_KEY_ERROR, e.getMessage());
		}
		return mav;
	}
	///srv/boardmembers/ajax/del/" 
	
	/**
	 * When the client needs to delete a board member, this controller action will
	 * handle the request.
	 *   
	 * @param id
	 * @return
	 */
	@PostMapping(value = "/boardmembers/ajax/del/{id}")
	public ResponseEntity<?> ajaxDeleteBoardMember(@PathVariable Integer id) {

    	try {
    		
    		log.debug("deleting event {}", id);
    		
			bmDao.delete(id);
		    return new ResponseEntity<Integer>(id, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<String>("invalid id",HttpStatus.NOT_FOUND);
		}


	}
}
