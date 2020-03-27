
package srv.controllers.splash;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This is the algorithm that prepares the response. 
 * 
 * @author mahiggs
 *
 */

@Controller
public class SplashController {
	
	   @RequestMapping("/")
	   @ResponseBody
	   public String handleSplashRequest() {
	      return "Hello Roo College";
	   }
}

