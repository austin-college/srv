package srv.controllers.serviceclients;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceClient.ServiceClientDao;

@Controller
public class ServiceClientController {
	
	
	 @Autowired
	 ServiceClientDao doa;
	
	
	 /**
	    * List action displays the list of service clients (pets) page. See listClients.html template
	    * 
	    * @param request
	    * @param response
	    * @return
	    * 
	    * @author lahouse
	    */
	   @GetMapping("/sc/list")
	   public ModelAndView listAction(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("serviceclients/listClients");

		   
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

	   /**
	    *  Ajax action that renders a new page removing the selected service client from the table.
	    * 
	    * 
	    * @param request
	    * @param response
	    * @return MAV of the deleted service client row of the table
	    */
	   @PostMapping("/ajax/delServiceClient")
	   public ModelAndView ajaxServiceClientDelete(HttpServletRequest request, HttpServletResponse response) {

		   response.setContentType("text/html");

		   int id = Integer.parseInt(request.getParameter("ID")); 
		   
			/*
			 * Prepare and render the response of the template's model for the HTTP response
			 */
		   ModelAndView mav = new ModelAndView("/serviceclients/ajax_delServiceClient");

		   try {
			   
			   doa.delete(id);   
			   //return id;
			   mav.addObject("scid", id);
			   
		   } catch (Exception e) {
			   System.err.println("\n\n ERROR ");
			   System.err.println(e.getMessage());
			   
		//	   return -1;
		   }
		   
		   return mav;		   
	   }
	   
	   
	   
	   
	   @GetMapping("/test/sc")
	   public ModelAndView splashAction(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("test/serviceClientTestView");
   
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
	   
//	   @GetMapping("/boardMember")
//		public ModelAndView managePetAction(HttpServletRequest request, HttpServletResponse response) {
//
//			ModelAndView mav = new ModelAndView("home/managePet");
//
//			return mav;
//		}
//	   
//	   @GetMapping("/updatePet")
//		public ModelAndView updatePetAction(HttpServletRequest request, HttpServletResponse response) {
//
//			ModelAndView mav = new ModelAndView("home/updatePet");
//
//			return mav;
//		}
}


	  