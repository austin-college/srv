package srv.controllers;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import srv.domain.event.Event;
import srv.domain.hours.ServiceHours;
import srv.domain.user.ServantUser;
import srv.domain.user.ServantUserDao;
import srv.domain.user.User;
import srv.services.EventService;
import srv.services.ServiceHoursService;
import srv.utils.UserUtil;
import srv.domain.spotlight.SpotLightDao;

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

	@Autowired
	ServiceHoursService hrSvc;

	@Autowired
	EventService evSvc;

	@Autowired
	ServantUserDao srvUserDao;


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
			if(userUtil.userIsNewToSystem()) {
				
				destUrl = "/srv/home/servant/servantProfileUpdate";
				userUtil.currentUser();
				
			} else if (userUtil.userIsServant()) destUrl = "/srv/home/servant";

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

		try {
			
			/*
			 * This block of code retrieves the BM user id if one is existing
			 * so that it can be used as a freemarker variable in html
			 */
			
			Integer bmUserId = userUtil.currentUser().getUid();
				
				mav.addObject("uid", bmUserId);
			
		}	catch (Exception e1) {
			
			e1.printStackTrace();
			
		}
		
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

		try {

			/*
			 *This block of code is meant to figure out if the user is new to our system
			 *If they are not new, they pass by this check, but if they are new to the system
			 *we create a profile for this new user and send them to the edit profile page 
			 */
			if(srvUserDao.fetchServantUserById(userUtil.currentUser().getUid()) == null) {

				srvUserDao.create(userUtil.currentUser().getUsername(), userUtil.currentUser().getUid(), 2020, false, 0);
				mav.clear();
				mav = new ModelAndView("home/servantProfileUpdate");


			} else {

				List<Event> upcomingEvents = evSvc.filteredEvents(null, "now+1M", null, null, null);
				User currentUser = userUtil.currentUser();

				ServantUser currentSrvUser = srvUserDao.fetchServantUserById(currentUser.getUid());

				List<ServiceHours> userHours = hrSvc.userHours(currentUser.getUid());
				double semesterTotalHrs = hrSvc.totalSemesterHours(userHours);
				
				
				int approvedHrsNum = 0;
				int rejectedHrsNum = 0;
				int pendingHrsNum = 0;
				
				for (ServiceHours hr : userHours) {
					
					if (hr.getStatus().equals("Approved"))
						approvedHrsNum++;
					
					else if (hr.getStatus().equals("Rejected"))
						rejectedHrsNum++;
					
					else
						pendingHrsNum++;
				}
		
				mav.addObject("name", currentSrvUser.getContactInfo().fullName());
				mav.addObject("email", currentSrvUser.getContactInfo().getEmail());
				mav.addObject("primaryPhone", currentSrvUser.getContactInfo().getPrimaryPhone());
				mav.addObject("gradYear", currentSrvUser.getExpectedGradYear());
				mav.addObject("affiliation", currentSrvUser.getAffiliation());
				mav.addObject("hasCar", currentSrvUser.getHasCar());
				mav.addObject("capacity", currentSrvUser.getCarCapacity());
				mav.addObject("events", upcomingEvents);
				mav.addObject("semTot", semesterTotalHrs);
				mav.addObject("approvedHrs", approvedHrsNum);
				mav.addObject("pendingHrs", pendingHrsNum);
				mav.addObject("rejectedHrs", rejectedHrsNum);
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return mav;
	}

	/**
	 * displays the servant update profile page
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN", "ROLE_SERVANT"})
	@GetMapping("/home/servant/servantProfileUpdate")
	public ModelAndView servantUpdateProfile(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("home/servantProfileUpdate");

		return mav;
	}
	
	
	
	
}
