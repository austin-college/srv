package srv.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportsController {
/**
 * Controller for all reports
 * @author Nadia Hannon
 */
	
	
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/reports/admin")

	public ModelAndView aboutAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("reports/reports");//in reports sub folder of templates

		return mav;
	}
	
	@Secured({"ROLE_ADMIN"})
	@GetMapping("/reports/admin/summary")
	public ModelAndView summaryAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("reports/adminsummary");
	//freemarker variables
		mav.addObject("schoolYear", "2019/2020");
	mav.addObject("numStudentsVol", "1234");
	mav.addObject("numGreek", "15");
	mav.addObject("topGreek", "Lambda Omega Lambda");
	mav.addObject("numClub", "30");
	mav.addObject("topClub", "Rotaract");
	mav.addObject("numLoners", "58");
	mav.addObject("numNewPets", "12");
	mav.addObject("numRemovedPets", "2");
	mav.addObject("popPet", "Mike's Turtle Rescue");
	mav.addObject("totalPopPet", "137");
	mav.addObject("numGDS", "74");
	mav.addObject("numJan", "82");
	mav.addObject("signature", "Katy Roo");
		
		
		
		return mav;
	}
	
	
	
	@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN", "ROLE_SERVANT"})
	@GetMapping("/reports/servant/summary")
	public ModelAndView summaryServantAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("reports/usersummary");

		mav.addObject("name", "Wreck It Ralph");
		mav.addObject("popPet", "Hunter's Cat Rescue");
		mav.addObject("hours", "32.5");
		mav.addObject("hoursPop", "11");
		mav.addObject("greek", "Lambda Omega Lambda");
		mav.addObject("club", "Rotaract");
		mav.addObject("total", "43");

		
		
		mav.addObject("signature", "Katy Roo");
		
		return mav;
	}
	
	
	@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN", "ROLE_SERVANT"})
	@GetMapping("/reports/servant/log")
	public ModelAndView logServantAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("reports/userlog");

		mav.addObject("name", "Wreck It Ralph");
	
		
		
		return mav;
	}
	
	
	
}//end of ReportsController
