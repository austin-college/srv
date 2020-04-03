
package srv.controllers.about;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * This is the algorithm that prepares the response.
 * 
 * @author mahiggs
 *
 */

@Controller
public class AboutController {

	/**
	 * Splash action displays the splash page. See splash.html template
	 * 
	 * @param request
	 * @param response
	 * @return
	 * 
	 * @author lahouse
	 */
	@GetMapping("/about")
	public ModelAndView aboutAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutBase");

		return mav;
	}

	@GetMapping("/about/Segun")
	public ModelAndView aboutSegun(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/Segun");

		return mav;
	}

	@GetMapping("/about/Nadia")
	public ModelAndView aboutNadia2(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutNadia2.html");

		return mav;
	}

	@GetMapping("/about/Lydia")
	public ModelAndView aboutLydiaAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutLydia");

		/* freemarker variables */
		mav.addObject("name", "Lydia House");
		mav.addObject("major", "Computer Science");
		mav.addObject("minor", "Mathematics");
		mav.addObject("hobby", "Taekwondo");
		mav.addObject("workday", "Wednesday");

		return mav;
	}

	@GetMapping("/about/Emma")
	public ModelAndView aboutEmmaAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutEmma");

		/* freemarker variables */
		mav.addObject("name", "Emma Drisoll");
		mav.addObject("class", "Junior");
		mav.addObject("major", "Computer Science");
		mav.addObject("minor", "Data Science and Analytics");
		mav.addObject("animal", "dolphins");
		mav.addObject("color", "blue");
		mav.addObject("music", "alternative");

		return mav;
	}

	@GetMapping("/about/Kevin")
	public ModelAndView aboutKevinAction(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/about/aboutKevin");
		mav.addObject("name", "Kevin Miranda");
		mav.addObject("class", "Senior");
		mav.addObject("age", "21");
		return mav;
	}
	
	@GetMapping("/about/Catalina")
	public ModelAndView aboutCatalinaAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutCatalina");

		/* freemarker variables */
		mav.addObject("name", "Catalina Canizalez");
		mav.addObject("class", "Sophomore");
		mav.addObject("major", "Computer Science");
		mav.addObject("minor", "Spanish");
		mav.addObject("animal", "Capybara");
		mav.addObject("color", "Green");

		return mav;
	}


}
