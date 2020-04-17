package srv.controllers.serviceclients;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceClient.ServiceClientDao;

@Controller
public class ServiceClientController {
	 /**
	    * List action displays the list of service clients (pets) page. See listClients.html template
	    * 
	    * @param request
	    * @param response
	    * @return
	    * 
	    * @author lahouse
	    */
	   @GetMapping("/list")
	   public ModelAndView listAction(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("serviceclients/listClients");
		   
		   ServiceClientDao doa = new JdbcTemplateServiceClientDao();
		   
		   
		   try {
			   
			List<ServiceClient> myClients = doa.listAll();
			
			mav.addObject("clients", myClients);
			
			} catch (Exception e) {
				
				System.err.println("\n\n ERROR ");
				System.err.println(e.getMessage());
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		   
		   return mav;
	   }
	   
	   
	   @GetMapping("/test/sc")
	   public ModelAndView splashAction(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("test/serviceClientTestView");
   
		   ServiceClientDao doa = new JdbcTemplateServiceClientDao();
		   
		   
		   try {
			   
			List<ServiceClient> myClients = doa.listAll();
			
			mav.addObject("clients", myClients);
			
			} catch (Exception e) {
				
				System.err.println("\n\n ERROR ");
				System.err.println(e.getMessage());
				
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   
		   
		   return mav;
	   }
}


	  