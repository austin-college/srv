package srv.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import srv.domain.contact.Contact;
import srv.domain.contact.ContactDao;
import srv.domain.event.eventype.EventType;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;
import srv.domain.user.User;
import srv.domain.user.UserDao;
import srv.utils.UserUtil;

/**
 * A Controller object that renders responses for the page of the site that contains the
 * list/table of service clients accessible to admin and board members. Also provides
 * the mappings of actions for adding, editing, selecting a row, and removing a ServiceClient.
 * 
 *  Note only board members are only allowed to view the page and the information for a selected
 *  ServiceClient and to edit the ServiceClient's contact information. They cannot edit other details
 *  about the ServiceClient (ex: name) nor add or remove ServieClients.
 * 
 * @author Lydia House
 */
@Controller
@EnableWebSecurity
@Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN"})
public class ServiceClientController {
	
	private static Logger log = LoggerFactory.getLogger(EventController.class);

	@Autowired
	ServiceClientDao srvClientDao;
	
	@Autowired
	ContactDao contactDao;
	
	@Autowired
	UserDao userDao;
	
	@Autowired
	UserUtil userUtil;

	/*
	 * Presents the current list of service clients in a table
	 */
	@GetMapping("/sc")
	public ModelAndView listAction(HttpServletRequest request, HttpServletResponse response) { 
		
		ModelAndView mav = new ModelAndView("serviceclients/manageServiceClients"); 

		try {
			
			// Lists the current service clients in the service client database in a table
			List<ServiceClient> myClients = srvClientDao.listAll();
			mav.addObject("clients", myClients);
			
			// Lists the current users in the user database in a drop down menu in the add and edit service client dialogs for selecting a current board member
			List<User> users = userDao.listAll();
			mav.addObject("users", users);
			
			// Lists the current contacts in the contact database in a drop down menu in the add and edit service client dialogs
			List<Contact> contacts = contactDao.listAll();			
			mav.addObject("contacts", contacts);
			
			// Checks to see if the current user is an admin, if so displays the add, edit, and delete buttons of the service client list
			// otherwise the buttons are gone.
			mav.addObject("userAdmin",userUtil.userIsAdmin());

		} catch (Exception e) {

			e.printStackTrace();
		}

		return mav;
	}
	
	/**
	 *  When the user needs to delete a service client, this controller action will
	 *  handle the request. 
	 *  
	 *  Note: we are using the DELETE HTTP method and embedding the item id as 
	 *  part of the URL (not a query parameter).
	 * 
	 * @param id
	 * @return 
	 */
	@PostMapping(value="/sc/ajax/del/{id}")
	public ResponseEntity<Integer> ajaxDeleteServiceClient(@PathVariable Integer id) {

		try {

			log.debug("deleting service client {}", id);

			srvClientDao.delete(id);   
		    return new ResponseEntity<>(id, HttpStatus.OK);		

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
	
	/**
	 * Ajax call to retrieve and return selected service client from the database.
	 */
	@ResponseBody
	@GetMapping(value="/sc/ajax/sc/{id}", produces="application/json")
	public ResponseEntity<ServiceClient> ajaxFetchServiceClient(@PathVariable Integer id) {
		
		try {
			log.debug("fetch service client " + id);
			
			ServiceClient srvClient = srvClientDao.fetchClientById(id);
			
			return new ResponseEntity<>(srvClient, HttpStatus.OK);
		
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Adding a new row to the service client for service client list. The parameters follow
	 * the parameters of the create method in the ServiceClientDao.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author Lydia House
	 */
	@PostMapping("/ajax/addSc")
	public ModelAndView ajaxServiceClientCreate(HttpServletRequest request, HttpServletResponse response) {
		
		response.setContentType("text/html");
		
		// Obtains the information from the JavaScript function
		String clientName = request.getParameter("clientName");
		String cid1Str = request.getParameter("mcID");
		String cid2Str = request.getParameter("ocID");
		String bmIdStr = request.getParameter("bmID");
		String category = request.getParameter("cat");
		
		Integer cid1 = null;
		Integer cid2 = null;
		Integer bmId = null;
		
		if (cid1Str != null && cid1Str.length()>0 )
			cid1 = Integer.valueOf(cid1Str); // main contact ID
		
		if (cid2Str != null && cid2Str.length()>0 )
			cid2 = Integer.valueOf(cid2Str);  // other/secondary ID
		
		if (bmIdStr != null && bmIdStr.length() > 0)
			bmId = Integer.valueOf(bmIdStr); // current board member's ID
		
		ModelAndView mav = new ModelAndView("/serviceclients/ajax_singleScRow");

		try {
			
			// Creates a new service client in the service client database. Then we hold onto a
			// handle of the newly created service client to aid with preparing the MAV response.
			ServiceClient newClient = srvClientDao.create(clientName, cid1, cid2, bmId, category); 
			
			//  Prepares and renders the response of the template's model for the HTTP response
			mav.addObject("scid", newClient.getScid());
			mav.addObject("name", newClient.getName());
			mav.addObject("category", newClient.getCategory());
			mav.addObject("firstName", newClient.getMainContact().getFirstName());
			mav.addObject("lastName", newClient.getMainContact().getLastName());
			mav.addObject("bmFirstName", newClient.getCurrentBoardMember().getContactInfo().getFirstName());
			mav.addObject("bmLastName", newClient.getCurrentBoardMember().getContactInfo().getLastName());
			

		} catch (Exception e) {
			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

		}

		return mav;
	}

	/**
	 * Updating an existing service client in the service client list.The parameters follow
	 * the parameters of the update method in the ServiceClientDao.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/ajax/editSc")
	public ModelAndView ajaxScUpdate(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("text/html");

		// Obtains the information from the JavaScript function
		int id = Integer.parseInt(request.getParameter("ID")); 
		String clientName = request.getParameter("clientName");
		int cid1 = Integer.parseInt(request.getParameter("mcID")); // main contact ID 
		int cid2 = Integer.parseInt(request.getParameter("ocID"));  // other/secondary ID
		int bmId = Integer.parseInt(request.getParameter("bmID"));
		
		String category = request.getParameter("cat");

		/*
		 * Prepare and render the response of the template's model for the HTTP response
		 */
		ModelAndView mav = new ModelAndView("/serviceclients/ajax_singleScRow");

		try {
			
			// Updates the service client in the service client database.
			srvClientDao.update(id, clientName, cid1, cid2, bmId, category);
			
			// Hold onto a handle of the updated service client to aid with preparing the MAV response.
			ServiceClient updatedClient = srvClientDao.fetchClientById(id);

			//  Prepares and renders the response of the template's model for the HTTP response
			mav.addObject("scid", updatedClient.getScid());
			mav.addObject("name", updatedClient.getName());
			mav.addObject("firstName", updatedClient.getMainContact().getFirstName());
			mav.addObject("lastName", updatedClient.getMainContact().getLastName());
			mav.addObject("bmFirstName", updatedClient.getCurrentBoardMember().getContactInfo().getFirstName());
			mav.addObject("bmLastName", updatedClient.getCurrentBoardMember().getContactInfo().getLastName());
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
	 * Note this function is also called for the editDlg for updating a service client
	 * Note there is most likely a better way to do this.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author lahouse
	 */
	@GetMapping("/ajax/infoSc")
	public ModelAndView ajaxScInfo(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("text/html");

		ModelAndView mav = new ModelAndView("/serviceclients/ajax_scInfo");

		int id = Integer.parseInt(request.getParameter("ID")); // Harvests the selected client's ID

		try {

			// Fetches the selected service client from the database
			ServiceClient selectedClient = srvClientDao.fetchClientById(id);

			// Adds the selected service client's information to an html snippet so that we can access it
			// in listClients.js in order to populate the fields in the dialog box in listClients.html
			mav.addObject("name", selectedClient.getName());
			mav.addObject("bmId", selectedClient.getCurrentBoardMember().getUid());
			mav.addObject("bmFirstName", selectedClient.getCurrentBoardMember().getContactInfo().getFirstName());
			mav.addObject("bmLastName", selectedClient.getCurrentBoardMember().getContactInfo().getLastName());
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
			mav.addObject("mcID", selectedClient.getMainContact().getContactId());
			mav.addObject("ocID", selectedClient.getOtherContact().getContactId());
		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mav;
	}
	
	/**
	 * When a user selects on an contact ID (or open opening) in the add service client dialog, we display that selected
	 * contact's information (such as name, address, email, etc.) in the main contact fields where the user is not allowed to make changes to. 
	 * 
	 * ajaxPopulateMCFields is called by the populateMCFields function in listClients.js in order to obtain the selected 
	 * main contact's information from the contact database and to return it back to the listClients.js so that the
	 * js file has access to the information in order to populate the main contact fields in the add service client dialog box. 
	 * 
	 * Note this is also used for the editDlg when editing/updating a service client.
	 * Note there is most likely a better way to do this.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author lahouse
	 */
	@GetMapping("/ajax/fillMCFields")
	public ModelAndView ajaxPopulateMCFields(HttpServletRequest request, HttpServletResponse response) { 

		response.setContentType("text/html");
		
		ModelAndView mav = new ModelAndView("/serviceclients/ajax_contactFields"); 
		
		int id = Integer.parseInt(request.getParameter("ID")); // Harvests the selected contact's ID

		try {
			
			// Fetches the selected contact from the contact database
			Contact selectedCon = contactDao.fetchContactById(id);
			
			// Adds the selected contact's information to an html snippet so that we can access it
			// in listClients.js in order to populate the main contact fields in the add dialog box in listClients.html
			mav.addObject("mcFirstName", selectedCon.getFirstName());
			mav.addObject("mcLastName", selectedCon.getLastName());
			mav.addObject("mcEmail", selectedCon.getEmail());
			mav.addObject("mcWorkPhone", selectedCon.getPhoneNumWork());
			mav.addObject("mcMobilePhone", selectedCon.getPhoneNumMobile());
			mav.addObject("mcStreet", selectedCon.getStreet());
			mav.addObject("mcCity", selectedCon.getCity());
			mav.addObject("mcState", selectedCon.getState());
			mav.addObject("mcZip", selectedCon.getZipcode());

		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mav;
	}
	
	/**
	 * When a user selects on an contact ID (or open opening) in the add service client dialog, we display that selected
	 * contact's information (such as name, address, email, etc.) in the other/secondary contact fields where the user is not allowed to make changes to. 
	 * 
	 * ajaxPopulateOCFields is called by the populateOCFields function in listClients.js in order to obtain the selected 
	 * other/secondary contact's information from the contact database and to return it back to the listClients.js so that the
	 * js file has access to the information in order to populate the other/secondary contact fields in the add service client dialog box. 
	 * 
	 * Note this is also used for the editDlg when editing/updating a service client.
	 * Note there is most likely a better way to do this.
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author lahouse
	 */
	@GetMapping("/ajax/fillOCFields")
	public ModelAndView ajaxPopulateOCFields(HttpServletRequest request, HttpServletResponse response) { 

		response.setContentType("text/html");
		
		ModelAndView mav = new ModelAndView("/serviceclients/ajax_contactFields"); 
		
		int id = Integer.parseInt(request.getParameter("ID")); // Harvests the selected contact's ID

		try {
			
			// Fetches the selected contact from the contact database
			Contact selectedCon = contactDao.fetchContactById(id);
			
			// Adds the selected contact's information to an html snippet so that we can access it
			// in listClients.js in order to populate the other/secondary contact fields in the add dialog box in listClients.html
			mav.addObject("ocFirstName", selectedCon.getFirstName());
			mav.addObject("ocLastName", selectedCon.getLastName());
			mav.addObject("ocEmail", selectedCon.getEmail());
			mav.addObject("ocWorkPhone", selectedCon.getPhoneNumWork());
			mav.addObject("ocMobilePhone", selectedCon.getPhoneNumMobile());
			mav.addObject("ocStreet", selectedCon.getStreet());
			mav.addObject("ocCity", selectedCon.getCity());
			mav.addObject("ocState", selectedCon.getState());
			mav.addObject("ocZip", selectedCon.getZipcode());

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
			   
			List<ServiceClient> myClients = srvClientDao.listAll();

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


	  