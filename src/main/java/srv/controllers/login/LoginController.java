package srv.controllers.login;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;




/**
 * An instance of this class is used to handle the requests related to user authentication.  
 * 
 *
 */

@Controller
@EnableWebSecurity
public class LoginController {


	private static final Logger log = LoggerFactory.getLogger(LoginController.class);
	
	
	/**
	 * <p>The basePage handler should remove any prior user logout or redirect path
	 * from session and place on the response model so the login form can encapsulate
	 * and possibly use downstream.
	 * </p>
	 * <p>
	 * When done, the model will contain a boolean to indicate if the user is in the process
	 * of exiting/logout (true) or entering/login (false).
	 * </p>
	 * <p>
	 * If the context is a redirection, the original redirectPath should be removed from 
	 * the session and added to the model.
	 * </p>
	 */
	@GetMapping("/login")
    public ModelAndView base(HttpServletRequest request, HttpServletResponse response, @RequestParam(value="error", required=false) String error) {

		log.debug("login controller");
		
		ModelAndView mav = new ModelAndView("security/login");
        
        return mav;
    };

    
 
    
    
    /**
     * The handleLogoutPost handler assumes we have an authenticated user.  We remove
     * the corresponding session component and redirect the user to our login page.
     * @throws IOException 
     */
    @GetMapping("/logout")
    @PostMapping("/logout")
    @Secured({ "ROLE_BOARDMEMBER", "ROLE_ADMIN", "ROLE_SERVANT"})
    public ModelAndView handleLogoutPost(HttpServletRequest request, HttpServletResponse response ) throws IOException {
    	
    	request.getSession().removeAttribute("user");
    	
        // request.getSession(true).getAttribute(MODEL_KEY_LOGGED_OUT);
        
        response.sendRedirect("/login");
        
        return null;
    };

    

}