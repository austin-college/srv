
package srv.controllers;

import java.io.ByteArrayInputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import srv.domain.event.Event;
import srv.domain.spotlight.SpotLight;
import srv.domain.spotlight.SpotLightDao;
import srv.services.EventService;

/**
 * A Controller object that renders responses for the splash (home) page of the webapp 
 * that is accessible to all public users.
 * 
 * @author mahiggs
 *
 */

@Controller
@EnableWebSecurity
public class SplashController {
	
	@Autowired
	EventService evSvc;

	
	@Autowired
	SpotLightDao spotLightDao;
	
	   /**
	    * Splash action displays the splash page. See splash.html template
	    * 
	    * @param request
	    * @param response
	    * @return
	    * 
	    * @author lahouse
	    */
	   @GetMapping("/splash")
	   public ModelAndView splashAction(HttpServletRequest request, HttpServletResponse response) {
		   
		   ModelAndView mav = new ModelAndView("splash/splash");
	
			try {
				
				List<Event> upcomingEvents = evSvc.filteredEvents(null, "now+1M", null, null, null);
				mav.addObject("events", upcomingEvents);

				// add spot light to our page model
				SpotLight s = spotLightDao.spotLightById(1);
				mav.addObject("spotTxt", s.getSpotText());
				mav.addObject("spotId", s.getSid());

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		   
		   
		   return mav;
	   }
	   
	   
	    @RequestMapping(value = "/splash/img/{id}", method = RequestMethod.GET)
	    public void getImage(HttpServletResponse response, @PathVariable Integer id) throws Exception {

	    	SpotLight s = spotLightDao.spotLightById(id);
	    	
	    	/*
	    	 * spotlight image missing;  use default 
	    	 */
	    	if (s.getImg() == null) {

		        ClassPathResource imgFile = new ClassPathResource("/static/images/defaultSpotlight.jpg", this.getClass().getClassLoader());
		        response.setContentType(MediaType.IMAGE_PNG_VALUE);
		        StreamUtils.copy(imgFile.getInputStream(), response.getOutputStream());
	    	}
	    	else {
	    		
	    		response.setContentLength(s.getImgSize());
	    		response.setContentType(s.getImgType());
	    		ByteArrayInputStream sin = new ByteArrayInputStream(s.getImg());
	    		StreamUtils.copy(sin, response.getOutputStream());
	    	}
	        
	    }

	   /**
	    * Provide the mapping if the user did not know to enter through the splash page.  In this
	    * case, we send back a redirect to the requester.
	    * 
	    * @param attributes
	    * @return
	    */
	    @GetMapping("/")
	    public RedirectView redirectAll (
	    		
	      RedirectAttributes attributes) {
	    	
	      return new RedirectView("/srv/splash");
	        
	    }
}

