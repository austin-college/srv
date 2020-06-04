package srv.controllers;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import srv.domain.hours.ServiceHours;
import srv.domain.user.JdbcTemplateUserDao;
import srv.services.ServiceHoursService;

/**
 * This is the algorithm that prepares the response.
 * 
 * @author mahiggs
 *
 */

@Controller
@EnableWebSecurity
@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN", "ROLE_SERVANT"})
public class HoursController {
	
	@Autowired
	ServiceHoursService hrSvc;
	
	//TODO BAD CODE SEE USER UTIL FOR MORE INFO
	@Autowired
	JdbcTemplateUserDao uDao;
	
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

		mav.addObject("hours", hrSvc.listHours());
	
		
		try {
			mav.addObject("semTot", hrSvc.getSemTot(hrSvc.listHours())); //total hours served per semester
			mav.addObject("termTot", hrSvc.getTermTot(hrSvc.listHours())); //total hours served per term
			mav.addObject("totOrgs", hrSvc.getTotOrgs(hrSvc.listHours())); //total organizations helped
			mav.addObject("avgPerMo", hrSvc.getAvgPerMo(hrSvc.listHours())); //average hours served per month
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		
		return mav;
	}
	
	/**
	 * Ajax renders a new page removing the selected service hour from the table.
	 * 
	 * @param request
	 * @param response
	 * @return MAV of the deleted service hour row to the table
	 */
	@PostMapping("/ajax/delHour")
	public ModelAndView ajaxServiceHourDelete(HttpServletRequest request, HttpServletResponse response)	{
		
		response.setContentType("text/html");  // Ajax responses will be html snippets.
		
		int id = Integer.parseInt(request.getParameter("ID")); // Holds the service hour's ID parameter
		
		//hrSvc.removeServiceHour(id);
		
		//System.out.println(hrSvc.listHours().size());
		/*
		 * Prepare and render the response of the template's model for the HTTP response
		 */
		ModelAndView mav = new ModelAndView("/hours/ajax_delServiceHr");
		mav.addObject("shid", id);
	
		return mav;
			
	}
	
	/**
	 * Ajax renders a new page updating the selected service hour from the table.
	 * 
	 * @param request
	 * @param response
	 * @return MAV of the updated service hour row to the table
	 */
	@PostMapping("/ajax/editHour")
	public ModelAndView ajaxServiceHourUpdate(HttpServletRequest request, HttpServletResponse response) {
			
		response.setContentType("text/html"); 
		
		// Getting the data from function call in the javascript file so we can pass it to the update method in the service class
		int id = Integer.parseInt(request.getParameter("ID")); 
		String eventName = request.getParameter("eventName"); 
		double hrs = Double.parseDouble(request.getParameter("hrsServed"));
		String orgName = request.getParameter("org");
		String date = request.getParameter("hrDate");
		String description = request.getParameter("desc");

		
	
		// Delegates to the Committee Service object for help on updating a committee.
	//	ServiceHours updatedHour = hrSvc.updateHour(id, eventName, orgName, hrs, date, description);
	
			
		/*
		 * Prepare and render the response of the template's model for the HTTP response
		 */
		ModelAndView mav = new ModelAndView("/hours/ajax_singleHourRow");

//		mav.addObject("shid", updatedHour.getShid() );
//		mav.addObject("title", updatedHour.getEventName().getTitle());
//		mav.addObject("hours", updatedHour.getHours());

	
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
	
	/** 
	    * This request handle renders an entire page useful for testing only.   This
	    * is not part of our actual site.
	    */
	   @GetMapping("/test/hours")
	   public ModelAndView handleReasonRequest(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("test/hoursTestView");


		   try {
			   
			int cnt = hrSvc.listHours().size();
			
			mav.addObject("count",cnt);
			
			List<ServiceHours> myHours = hrSvc.listHours();
			
			mav.addObject("serviceHours", myHours );
			
			} catch (Exception e) {
				
				System.err.println("\n\n ERROR ");
				System.err.println(e.getMessage());
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		   
		   return mav;
	   }
}