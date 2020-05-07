package srv.controllers.events;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.event.EventDao;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceClient.ServiceClientDao;
import srv.utils.UserUtil;

/**
 * 
 * this algorithm prepares the response 
 * @author Conor Mackey
 * 
 *
 */

@Controller
@EnableWebSecurity
public class EventController {

	private static Logger log = LoggerFactory.getLogger(EventController.class);

	@Autowired
	EventDao dao;


	/**
	 * displays the admin manage events page
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("home/admin/manageEvents")
	public ModelAndView adminManageEventsAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("events/adminManageEvents");

		try {

			// Lists the current events in the event database in a table
			List<Event> myEvents = dao.listAll();
			mav.addObject("events", myEvents);


		} catch (Exception e) {

			System.err.println("\n\n ERROR ");
			System.err.println(e.getMessage());

			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mav;
	}

}
