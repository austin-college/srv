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
import srv.domain.serviceclient.ServiceClient;
import srv.domain.serviceclient.ServiceClientDao;
import srv.domain.user.User;
import srv.domain.user.UserDao;
import srv.utils.ParamUtil;
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

	/**
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
	 * Ajax call to create and return the new service client to the database.
	 */
	@PostMapping("/sc/ajax/addSc")
	public ModelAndView ajaxCreateServiceClient(HttpServletRequest request, HttpServletResponse response) {

		// Prepare and render the response of the template's model for the HTTP response
		ModelAndView mav = new ModelAndView("/serviceclients/ajax_singleScRow");

		response.setContentType("text/html");

		try {

			// fetch the data sent from the JavaScript function and verify the fields
			String name = request.getParameter("name");
			String cat = request.getParameter("cat");

			Integer cid1 = ParamUtil.requiredIntegerParam(request.getParameter("cid1"), "Main contact id is required.");
			Integer cid2 = ParamUtil.requiredIntegerParam(request.getParameter("cid2"), "Other/secondary contact id is required.");
			Integer bmId = ParamUtil.requiredIntegerParam(request.getParameter("bmId"), "board member id is required.");

			// Creates a new service client in the service client database.
			ServiceClient newClient = srvClientDao.create(name, cid1, bmId, cat);

			//  Prepares and renders the response of the template's model for the HTTP response
			mav.addObject("scid", newClient.getScid());
			mav.addObject("name", newClient.getName());
			mav.addObject("mainContactName", newClient.getMainContact().fullName());
			mav.addObject("boardMemberName", newClient.getCurrentBoardMember().getContactInfo().fullName());
			mav.addObject("category", newClient.getCategory());  

		} catch (Exception e) {
			log.error("\n\n ERROR ");
			log.error(e.getMessage());

			e.printStackTrace();

			response.setStatus(410);

			mav = new ModelAndView("/error");

			mav.addObject("errMsg", e.getMessage());
		}

		return mav;

	}

	/**
	 * Ajax call to update the specified service client in the database.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@PostMapping("/sc/ajax/editSc")
	public ModelAndView ajaxUpdateServiceClient(HttpServletRequest request, HttpServletResponse response) {

		response.setContentType("text/html");

		// Prepare and render the response of the template's model for the HTTP response
		ModelAndView mav = new ModelAndView("/serviceclients/ajax_singleScRow");

		try {

			// fetch the data sent from the JavaScript function and verify the fields
			String name = request.getParameter("name");
			String cat = request.getParameter("cat");

			Integer scid = ParamUtil.requiredIntegerParam(request.getParameter("scid"), "Service client id is required.");
			Integer cid1 = ParamUtil.requiredIntegerParam(request.getParameter("cid1"), "Main contact id is required.");
			Integer bmId = ParamUtil.requiredIntegerParam(request.getParameter("bmId"), "board member id is required.");

			// Updates the service client in the service client database.
			srvClientDao.update(scid, name, cid1, bmId, cat);

			// Hold onto a handle of the updated service client to aid with preparing the MAV response.
			ServiceClient updatedClient = srvClientDao.fetchClientById(scid);

			//  Prepares and renders the response of the template's model for the HTTP response
			mav.addObject("scid", updatedClient.getScid());
			mav.addObject("name", updatedClient.getName());
			mav.addObject("mainContactName", updatedClient.getMainContact().fullName());
			mav.addObject("boardMemberName", updatedClient.getCurrentBoardMember().getContactInfo().fullName());
			mav.addObject("category", updatedClient.getCategory());  

		} catch (Exception e) {
			log.error("\n\n ERROR ");
			log.error(e.getMessage());

			e.printStackTrace();

			response.setStatus(410);

			mav = new ModelAndView("/error");

			mav.addObject("errMsg", e.getMessage());
		}

		return mav;

	}

	/**
	 * Ajax call to retrieve and return selected contact from the database.
	 */
	@ResponseBody
	@GetMapping(value="/sc/ajax/contact/{id}", produces="application/json")
	public ResponseEntity<Contact> ajaxFetchContact(@PathVariable Integer id) {

		try {
			log.debug("fetch contact " + id);

			Contact con = contactDao.fetchContactById(id);

			return new ResponseEntity<>(con, HttpStatus.OK);

		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	/**
	 * Testing the ServiceClient dao
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping("/test/sc")
	public ModelAndView basicTest(HttpServletRequest request, HttpServletResponse response) {

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


	  