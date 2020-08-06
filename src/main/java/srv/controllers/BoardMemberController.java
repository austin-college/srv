package srv.controllers;

import java.util.List;

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
import srv.domain.user.BoardMemberUser;
import srv.domain.user.BoardMemberUserDao;
import srv.domain.user.ServantUser;
import srv.domain.user.ServantUserDao;
import srv.services.BoardMemberService;
import srv.utils.ParamUtil;

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
	BoardMemberService bmSrv;

	/**
	 * Maps boardMember.html template to /boardMember
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/boardMember")
	public ModelAndView splashAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("bm/boardMember");
		
		mav.addObject("hours", bmSrv.listHoursToBeApproved());

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

		ModelAndView mav = new ModelAndView("bm/bm_manage_page");

		try {
							
			mav.addObject("boardMembers", bmSrv.listAllBoardMemberUsers());
			mav.addObject("srvUsers", bmSrv.nonBmUsers());

			
		} catch (Exception e) {
			
			// report any errors to an element on the page; assumes there is an element in our template.
			log.error(e.getMessage());
			mav.addObject(FM_KEY_ERROR, e.getMessage());
		}
		return mav;
	}


	@GetMapping("/boardmembers/ajax/bm/{id}")
	public ModelAndView ajaxFetchBoardMemberDialog(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
		ModelAndView mav = new ModelAndView("/bm/bm_edit_dialog");
		
		
		try {
			BoardMemberUser bmUser = bmSrv.fetchById(id);
			
			mav.addObject("bm",bmUser);
			
		} catch (Exception e) {

			log.error(e.getMessage());
			mav.addObject(FM_KEY_ERROR, e.getMessage());

			e.printStackTrace();
		}
		
		return mav;
	}
	
	
	@PostMapping("/boardmembers/ajax/bm/{id}")
	public ModelAndView ajaxSaveBoardMemberInfo(HttpServletRequest request, HttpServletResponse response, @PathVariable Integer id) {
		ModelAndView mav = new ModelAndView("/bm/bm_single_row");
		
		try {
			
			Boolean chairFlag = ParamUtil.requiredBooleanParam(request.getParameter("chair"), "Co Chair boolean required.");
			Integer gradYr = ParamUtil.requiredIntegerParam(request.getParameter("grad"), "board member grad year is required.");
			Boolean carFlag = ParamUtil.requiredBooleanParam(request.getParameter("car"), "Car boolean required.");
			Integer carCapacity = ParamUtil.requiredIntegerParam(request.getParameter("carcap"), "Car capacity is required.");
			
			BoardMemberUser bmUser = bmSrv.fetchById(id);

			/*
			 * now update some portion of it.
			 */
			bmUser.setIsCoChair(chairFlag);
			bmUser.setExpectedGradYear(gradYr);
			bmUser.setHasCar(carFlag);
			bmUser.setCarCapacity(carCapacity);
			
			/*
			 * commit back to database
			 */
			bmUser = bmSrv.updateBoardMember(bmUser);
			log.debug("back from update: {}",bmUser);
			
			mav.addObject("bm",bmUser);
			
		} catch (Exception e) {

			log.error(e.getMessage());
			mav.addObject(FM_KEY_ERROR, e.getMessage());

			e.printStackTrace();
		}
		
		return mav;
	}
	
	
	
	
	/**
	 * Ajax call to create/promote and return a new board member user to the database
	 */
	@PostMapping("/boardmembers/ajax/new")
	public ModelAndView ajaxCreateBoardMember(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("/bm/ajax_singleBoardMemberRow");
		
		response.setContentType("text/html");
		
		try {
			
			// fetch the data sent from the JavaScript function and verify the fields
			String username = ParamUtil.requiredNonEmptyString(request.getParameter("username"), "Username is required.");
			
			// create/promote a new board member user, setting the default value of co-chair to false - might remove later
			BoardMemberUser newBmUser = bmSrv.create(username, false);
			
			mav.addObject("uid", newBmUser.getUid());
			mav.addObject("isCoChair", newBmUser.getIsCoChair());
			mav.addObject("username", newBmUser.getUsername());
			mav.addObject("fullName", newBmUser.getContactInfo().fullName());
			mav.addObject("email", newBmUser.getContactInfo().getEmail());
			mav.addObject("primaryPhone", newBmUser.getContactInfo().getPrimaryPhone());
			mav.addObject("expectedGradYear", newBmUser.getExpectedGradYear());
			
			
		} catch (Exception e) {
			
			log.error("\n\n ERROR ");
			log.error(e.getMessage());
			
			e.printStackTrace();
			
			response.setStatus(410);
			
			
			mav = new ModelAndView("/error");
			
			mav.addObject("errMsg", e.getMessage());
		}
		
		return mav;
		
	}
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
    		
			bmSrv.delete(id);
		    return new ResponseEntity<Integer>(id, HttpStatus.OK);
		    
		} catch (Exception e) {
			return new ResponseEntity<String>("invalid id",HttpStatus.NOT_FOUND);
		}


	}
}
