package srv.controllers.home;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import srv.utils.UserUtil;

/**
 * 
 * this algorithm prepares the response 
 * @author Conor Mackey
 * 
 * 
 *
 */

@Controller
@EnableWebSecurity
public class HomeController {
	
	private static Logger log = LoggerFactory.getLogger(HomeController.class);

	
	@Autowired
	UserUtil userUtil;
	
	
	/**
	 * All requests to /home are protected.  The user must authenticate successfully.
	 * We check the kind of user and redirect the client request to the proper home
	 * destination based on their authorization/roles.
	 * 
	 * @see srv.util.UserUtil
	 * @param attributes
	 * @return
	 */
	
    @GetMapping("/home")
    public RedirectView redirectAll ( RedirectAttributes attributes) {
          	
      String destUrl = "/unknown";
      
      try {
		/*
		   * Order of evaluation is important since an ADMIN has all three roles
		   * and the boardMember has two roles.        
		   */
		  if (userUtil.userIsServant()) destUrl = "/srv/home/servant";
		  
		  if (userUtil.userIsBoardMember()) destUrl = "/srv/home/boardMember";
		  
		  if (userUtil.userIsAdmin()) destUrl = "/srv/home/admin";
		  
		} catch (Exception e) {
			log.error("Unknown user.  We cannot determine the user role."); 
			// do nothing. we will go to the /unknown destination.  But this 
			// is currently missing. 
		} 
      
      
      return new RedirectView(destUrl);
    }
    
    /**
     * displays the board member home page
     * @param request
     * @param response
     * @return
     */
	@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN"})
	@GetMapping("/home/boardMember")
	public ModelAndView boardMemberAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/boardMember");

		return mav;
	}
	
	/**
	 * displays the admin edit board member/co chair page
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/home/admin/editBM")
	public ModelAndView adminEditBMAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/editBM");

		return mav;
	}
	
	/**
	 * displays the admin home page
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/home/admin")
	public ModelAndView adminAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/admin");

		return mav;
	}
	
	/**
	 * displays the default home page for servants
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/home/servant")
	public ModelAndView servantAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/servant");
		
		return mav;
	}
}
