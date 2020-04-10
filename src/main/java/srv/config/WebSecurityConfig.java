package srv.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author aj_pr http://localhost:8080/
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
     
    @Override
    protected void configure(HttpSecurity http) throws Exception {
 
        //http.csrf().disable();
 
    	http.authorizeRequests()
    		.antMatchers("/", "/splash",  "/login",  "/logout", "/css_style/**", "/js/**", "/images/**", "**/favicon.ico" ).permitAll()
    		.antMatchers("/**").hasRole("USER");
    		
    	http.authorizeRequests().and().formLogin();
    	
     
// examples we can use eventually....
//
//        // The pages does not require login
//        http.authorizeRequests()
//        	.antMatchers("/", "/splash",  "/login",  "/logout", "/css_style/**", "/js/**", "/images/**", "**/favicon.ico" ).permitAll()
//        	.antMatchers("/about/**", "/admin/**").hasRole("USER")
//        	.antMatchers("/snum", "/snum/**", "/items", "/items/**", "/drawings/**").hasAnyRole(AppUserRoles.ROLE_ADMIN, AppUserRoles.ROLE_FULL)
//        	.antMatchers("/home", "/home/**", "/quik", "/quik/**", "/drawing/item/**").hasAnyRole(AppUserRoles.ROLE_BASIC, AppUserRoles.ROLE_ADMIN, AppUserRoles.ROLE_FULL)	        		        	
//        	.anyRequest().authenticated();
//
//        
//        // Config for Login Form
//        http.authorizeRequests()
//        	.and().formLogin()
//                .loginPage("/srv/login")
//                .loginProcessingUrl("/srv/login")
//                .defaultSuccessUrl("/quik")
//                .failureUrl("/srv/login?error=true")
//                .usernameParameter("username")
//                .passwordParameter("password");
//                
//                
//                // Config for Logout Page
//            .and().logout()
//            	.logoutUrl("/logout")
//            	.logoutSuccessUrl("/login")
//            	.deleteCookies("JSESSIONID")
//            	.invalidateHttpSession(true)
//            	;
//        
//        http.authorizeRequests()
//        .and().exceptionHandling()
//        .accessDeniedPage("/noaccess");
 
    }	


    /**
     * <p>
     * The following method is called automagically by the Spring Framework
     * during configuration.  It sets up a dummy in-memory database store of
     * users to use during authentication.   We use it during development
     * and testing. 
     * <p/>
     * <p>
     * We setup 4 dummy users representing the 4 ROLES identified by our system:
     * <ul>
     * <li> id = "user" : pw = "user"
     * <li> id = "admin" : pw = "admin"
     * </ul> 
     * 
     * </p>
     * 
     * Use the following URL to derive the BCrypt version of user's password. We
     * never, NEVER include clear text passwords in our code.   Always encrypted
     * if it has to be embedded.   Also, the default basic authentication HTML form
     * uses javascript on the client side to encrypt the users password in the 
     * POST request back to out server.   
     * 
     * https://passwordhashing.com/BCrypt
     * 
     * @param auth
     * @throws Exception
     */
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
      throws Exception {
		
        auth.inMemoryAuthentication().withUser("user")
          .password("$2b$10$TOwIs32GeV.OZNdQBW5XpOCj1fsfGydm5ytY5YhVeuUj88djiXp0e")  // "user"
          .roles("USER");
        
        auth.inMemoryAuthentication().withUser("admin")
        .password("$2b$10$21ITM86vZOBISgzdP9KgjuKuuURsa4OlqH7GbrMVjJ07r867Fn91m")  // "admin"
        .roles("USER","ADMIN");


    }


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}