package srv.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ReportsController {
/**
 * Controller for admin generated reports
 * @author Nadia Hannon
 */
	
	@GetMapping("/reports")

	public ModelAndView aboutAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("reports/reports");//in reports sub folder of templates

		return mav;
	}
	
	@GetMapping("/reports/summary")
	public ModelAndView summaryAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("reports/summary");
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
	
	
}//end of ReportsController
