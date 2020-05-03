package srv.controllers.serviceclients;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceClient.ServiceClientDao;

@Controller
public class ServiceClientController {


	@Autowired
	ServiceClientDao doa;

	@GetMapping("/sc/list")
	public ModelAndView listAction(HttpServletRequest request, HttpServletResponse response) { 
		ModelAndView mav = new ModelAndView("serviceclients/listClients"); 

		try {

			List<ServiceClient> myClients = doa.listAll();

			mav.addObject("clients", myClients);
			//	mav.addObject("pcAddr","E Main Street");
			System.out.println( myClients.get(0).getMainContact().getStreet());

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
	 * When a user selects on a row in the service clients table, a dialog box displays that selected
	 * client's information, even that which is not displayed in the table (such as contacts' phone numbers).
	 * 
	 * ajaxScInfo is called by the scInfo function in listClients.js in order to obtain the selected 
	 * service client's information from the database and to return it back to the listClients.js so that the
	 * js file has access to the information in order to populate the fields in the service client information
	 * dialog box. 
	 * 
	 * Note there is most likely a better way to do this.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author lahouse
	 */
	@GetMapping("/ajax/infoServiceClient")
	public ModelAndView ajaxScInfo(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("text/html");

		ModelAndView mav = new ModelAndView("/serviceclients/scInfo");

		int id = Integer.parseInt(request.getParameter("ID")); // Harvests the selected client's ID

		try {

			// Fetches the selected service client from the database
			ServiceClient selectedClient = doa.fetchClientById(id);

			// Adds the selected service client's information to an html snippet so that we can access it
			// in listClients.js in order to populate the fields in the dialog box in listClients.html
			mav.addObject("name", selectedClient.getName());
			mav.addObject("bm", selectedClient.getBoardMember());
			mav.addObject("cat", selectedClient.getCategory());
			mav.addObject("mcFirstName", selectedClient.getMainContact().getFirstName());
			mav.addObject("mcLastName", selectedClient.getMainContact().getLastName());
			mav.addObject("mcEmail", selectedClient.getMainContact().getEmail());
			mav.addObject("mcWorkPhone", selectedClient.getMainContact().getPhoneNumWork());
			mav.addObject("mcMobilePhone", selectedClient.getMainContact().getPhoneNumMobile());
			mav.addObject("mcStreet", selectedClient.getMainContact().getStreet());
			mav.addObject("mcCity", selectedClient.getMainContact().getCity());
			mav.addObject("mcState", selectedClient.getMainContact().getState());
			mav.addObject("mcZip", selectedClient.getMainContact().getZipcode());
			mav.addObject("ocFirstName", selectedClient.getOtherContact().getFirstName());
			mav.addObject("ocLastName", selectedClient.getOtherContact().getLastName());
			mav.addObject("ocEmail", selectedClient.getOtherContact().getEmail());
			mav.addObject("ocWorkPhone", selectedClient.getOtherContact().getPhoneNumWork());
			mav.addObject("ocMobilePhone", selectedClient.getOtherContact().getPhoneNumMobile());
			mav.addObject("ocStreet", selectedClient.getOtherContact().getStreet());
			mav.addObject("ocCity", selectedClient.getOtherContact().getCity());
			mav.addObject("ocState", selectedClient.getOtherContact().getState());
			mav.addObject("ocZip", selectedClient.getOtherContact().getZipcode());

		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
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


	  