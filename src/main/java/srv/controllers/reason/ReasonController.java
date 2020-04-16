
package srv.controllers.reason;

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
 * Just an example that will eventually be removed.
 * 
 * @author mahiggs
 *
 */
@Controller
public class ReasonController {
	

	   @GetMapping("/test/reason")
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

