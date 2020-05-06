package srv.controllers.home;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.event.EventDao;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceClient.ServiceClientDao;
import srv.utils.UserUtil;

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
@EnableWebSecurity
public class HomeController {
	
	private static Logger log = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired
	EventDao dao;

	/**
	 * All requests to /home are protected.  The user must authenticate successfully.
	 * We check the kind of user and redirect the client request to the proper home
	 * destination based on their authorization/roles.
	 * 
	 * @see srv.util.UserUtil
	 * @param attributes
	 * @return
	 */
	@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN", "ROLE_SERVANT" })
    @GetMapping("/home")
    public RedirectView redirectAll ( RedirectAttributes attributes) {
          	
      String destUrl = "/unknown";
      
      try {
		/*
		   * Order of evaluation is important since an ADMIN has all three roles
		   * and the boardMember has two roles.        
		   */
		  if (UserUtil.userIsServant()) destUrl = "/srv/home/servant";
		  
		  if (UserUtil.userIsBoardMember()) destUrl = "/srv/home/boardMember";
		  
		  if (UserUtil.userIsAdmin()) destUrl = "/srv/home/admin";
		  
		} catch (Exception e) {
			log.error("Unknown user.  We cannot determine the user role."); 
			// do nothing. we will go to the /unknown destination.  But this 
			// is currently missing. 
		} 
      
      
      return new RedirectView(destUrl);
    }
    
    
	@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN"})
	@GetMapping("/home/boardMember")
	public ModelAndView boardMemberAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/boardMember");

		return mav;
	}
	@Secured("ROLE_ADMIN")
	@GetMapping("/home/admin/editBM")
	public ModelAndView adminEditBMAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/editBM");

		return mav;
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/home/admin/manageEvents")
	public ModelAndView adminManageEventsAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/adminManageEvents");
		
		try {
			
			// Lists the current events in the event database in a table
			List<Event> myEvents = dao.listAll();
			mav.addObject("events", myEvents);
			

		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mav;
	}
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/home/admin")
	public ModelAndView adminAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/admin");

		return mav;
	}
	
	@GetMapping("/home/servant")
	public ModelAndView servantAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/servant");

		return mav;
	}
}
