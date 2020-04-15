
package srv.controllers.serviceClient;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.reason.JdbcReasonDao;
import srv.domain.reason.JdbcTemplateReasonDao;
import srv.domain.reason.Reason;
import srv.domain.reason.ReasonDao;


/**
 * Attempting to add a database support for service client
 * 
 * @author lahouse
 *
 */
@Controller
public class ServiceClientController {
	

	   @GetMapping("/test/sc")
	   public ModelAndView splashAction(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("test/reasonTestView");
   
		   ReasonDao doa = new JdbcTemplateReasonDao();
		   
		   
		   try {
			   
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
