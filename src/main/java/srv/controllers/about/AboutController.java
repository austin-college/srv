
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
	
	@GetMapping("/about/MHiggs")
	public ModelAndView aboutMhiggsAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutMHiggs");

		return mav;
	}

	@GetMapping("/about/Segun")
	public ModelAndView aboutSegun(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutSegun");

		return mav;
	}

	@GetMapping("/about/Nadia")
	public ModelAndView aboutNadia2(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutNadia2");
		
		//free marker variables
		mav.addObject("name", "Nadia");
		mav.addObject("class", "junior");
		mav.addObject("majorMinor", "I am majoring in physics and art with minors in computer science and math");
		mav.addObject("hobby1", "woodworking");
		mav.addObject("hobby2", "video games");
		mav.addObject("hobby3", "stained glass");
		mav.addObject("futureGoal", "mechanical engineering");
		
		return mav;
	}
	
	@GetMapping("/about/Sameeha")
	public ModelAndView aboutSameehaAction(HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView("about/aboutSameeha");
		
		/* free-marker variables */
		mav.addObject("name", "Sameeha Khaled");
		mav.addObject("age", "19");
		mav.addObject("major", "Computer Science & Gender Studies");
		mav.addObject("color", "Yellow");
		mav.addObject("zodiac", "Capricorn");
		
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
	
	@GetMapping("/about/Conor")
	public ModelAndView aboutConorAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutConor");
		mav.addObject("name", "Conor Mackey");
		mav.addObject("major", "Computer Science");
		mav.addObject("minor", "Film");
		
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


	@GetMapping("/about/AJ")
	public ModelAndView aboutAJAction(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("/about/aboutAJ");
		mav.addObject("name", "AJ Pritchard");
		mav.addObject("class", "Senior");
		mav.addObject("age", 20);
		mav.addObject("state", "Washington");
		mav.addObject("major", "Computer Science");
		mav.addObject("minor", "Math");
		mav.addObject("sports", "Baseball and Water Polo");
		
		return mav;
	}
	
	@GetMapping("/about/Min")
	public ModelAndView aboutMinAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutMin");
		
		mav.addObject("name", "MinSeob Shim");
		mav.addObject("country", "South Korea");
		mav.addObject("major", "Computer Science & Business Administration");
		mav.addObject("minor", "EALC: Japanese");
		mav.addObject("hobby", "Basketball");
		mav.addObject("music", "Clarinet and Piano");

		return mav;
	}
	
	
	
	@GetMapping("/about/Hunter")
	public ModelAndView aboutHunterAction(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView mav = new ModelAndView("about/aboutHunter");

		return mav;
	}
}
