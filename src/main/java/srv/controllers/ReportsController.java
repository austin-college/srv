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
	return mav;
	}
	
	
}//end of ReportsController
