
package srv.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.reason.JdbcReasonDao;
import srv.domain.reason.JdbcTemplateReasonDao;
import srv.domain.reason.Reason;
import srv.domain.reason.ReasonDao;
import srv.services.reason.ReasonService;


/**
 * Just an example that will eventually be removed.
 * 
 * @author mahiggs
 *
 */
@Controller
@EnableWebSecurity
public class ReasonController {
	

		@Autowired
		ReasonDao doa;
		
		@Autowired
		ReasonService service;
	
	   /** 
	    * This request handle renders an entire page useful for testing only.   This
	    * is not part of our actual site.
	    */
	   @GetMapping("/test/reason")
	   public ModelAndView handleReasonRequest(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("test/reasonTestView");
   

		   try {
			   
			int cnt = service.reasonCount();
			
			mav.addObject("count",cnt);
			
			List<Reason> myReasons = doa.listAll();
			
			mav.addObject("reasons", myReasons );
			
			} catch (Exception e) {
				
				System.err.println("\n\n ERROR ");
				System.err.println(e.getMessage());
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		   
		   return mav;
	   }
	   
}

