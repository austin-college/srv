package srv.controllers;

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
import org.springframework.web.servlet.ModelAndView;

import srv.domain.event.eventype.EventType;
import srv.domain.event.eventype.EventTypeDao;
import srv.utils.UserUtil;

@Controller
@EnableWebSecurity
public class EventTypeController {

	private static Logger log = LoggerFactory.getLogger(EventTypeController.class);
	
	@Autowired
	EventTypeDao etDao;
	
	/**
	 * Displays the admin manage event types page.
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("eventTypes")
	public ModelAndView basePageAction(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView("eventtypes/adminManageEventTypes");
		
		try {
			
			List<EventType> currentEvTypes = etDao.listAll();

			mav.addObject("evTypes", currentEvTypes);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return mav;		
	}
}