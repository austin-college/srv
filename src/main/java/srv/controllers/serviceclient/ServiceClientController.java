
package srv.controllers.serviceclient;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.serviceclient.JdbcTemplateServiceClientDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;


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
		   
		   ModelAndView mav = new ModelAndView("test/serviceClientTestView");
   
		   ServiceClientDao doa = new JdbcTemplateServiceClientDao();
		   
		   
		   try {
			   
			List<ServiceClient> myClients = doa.listAll();
			
			mav.addObject("serviceClients", myClients);
			
			} catch (Exception e) {
				
				System.err.println("\n\n ERROR ");
				System.err.println(e.getMessage());
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		   
		   return mav;
	   }
	   
}
