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
	    * TODO this should return a string where if successful (found the id) returns "okay" and
	    * if unsuccessful returns "error" with error message explaining why. The corresponding jquery callback
	    * method in listClients.js in the delClient function should handle when an exception is thrown. It is not returning
	    * a string as of now because upon trying to delete I get a 404 not found error.
	    * 
	    * @param request
	    * @param response
	    * @return MAV of the deleted service client row of the table
	    */
	   @PostMapping("/ajax/delServiceClient")
	   public ModelAndView ajaxServiceClientDelete(HttpServletRequest request, HttpServletResponse response) {

		   response.setContentType("text/html");
		   //response.setContentType("text/text"); will tell the js that we are expecting text back

		   int id = Integer.parseInt(request.getParameter("ID")); 
		   
			/*
			 * Prepare and render the response of the template's model for the HTTP response
			 */
		   ModelAndView mav = new ModelAndView("/serviceclients/ajax_delServiceClient");

		   try {
			   
			   doa.delete(id);   
			   mav.addObject("scid", id);
			   
			 //  return "Okay";	
			   
		   } catch (Exception e) {
			   System.err.println("\n\n ERROR ");
			   System.err.println(e.getMessage());
			   
		//	   return "Error" + e.getMessage();
		   }
		   
		  return mav;
		  	   
	   }
	   
	   /**
	    * Adding a new row to the service client for service client list.
	    * TODO figure out what to do with board member and contacts, for now making them null or unknown
	    * figure out if we are keeping address information
	    * 
	    * @param request
	    * @param response
	    * @return
	    */
	   @PostMapping("/ajax/addServiceClient")
	   public ModelAndView ajaxServiceClientCreate(HttpServletRequest request, HttpServletResponse response) {

		   response.setContentType("text/html");
	   
		   String name = request.getParameter("name");
		   String category = request.getParameter("cat");
		   
			/*
			 * Prepare and render the response of the template's model for the HTTP response
			 */
		   ModelAndView mav = new ModelAndView("/serviceclients/ajax_singleClientRow");

		   try {
			
			   ServiceClient newClient = doa.create(name, 1, 2, "John Smith", category);
			   
			   mav.addObject("scid", newClient.getScid());
			   mav.addObject("name", newClient.getName());
			   mav.addObject("category", newClient.getCategory());
			   

			   
		   } catch (Exception e) {
			   System.err.println("\n\n ERROR ");
			   System.err.println(e.getMessage());
			   
	
		   }
		   
		  return mav;
		  	   
	   }
	   
	   /**
	    * TODO
	    * Updating an existing service client in the service client list but at this point in time
	    * only updating service client's name and category.
	    * 
	    *  Need to figure out how to handle board member and contact data
	    * 
	    * @param request
	    * @param response
	    * @return
	    */
	   @PostMapping("/ajax/editServiceClient")
	   public ModelAndView ajaxServiceClientUpdate(HttpServletRequest request, HttpServletResponse response) {

		   response.setContentType("text/html");
	   
		   int id = Integer.parseInt(request.getParameter("ID")); 
		   String name = request.getParameter("name");
		   String category = request.getParameter("cat");
		   
			/*
			 * Prepare and render the response of the template's model for the HTTP response
			 */
		   ModelAndView mav = new ModelAndView("/serviceclients/ajax_singleClientRow");

		   try {
			
			   doa.update(id, name, 1, 2, "John Smith", category);
			   
			   ServiceClient updatedClient = doa.fetchClientById(id);
			   
			   mav.addObject("scid", updatedClient.getScid());
			   mav.addObject("name", updatedClient.getName());
			   mav.addObject("category", updatedClient.getCategory());  

			   
		   } catch (Exception e) {
			   System.err.println("\n\n ERROR ");
			   System.err.println(e.getMessage());
			   
	
		   }
		   
		  return mav;
		  	   
	   }
	   
	   /**
	    * Testing the ServiceClient dao
	    * @param request
	    * @param response
	    * @return
	    */
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


	  