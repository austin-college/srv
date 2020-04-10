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
    		.antMatchers("/about").hasRole("USER")
    		.antMatchers("/", "/splash",  "/login",  "/logout", "/css_style/**", "/js/**", "/images/**", "**/favicon.ico" ).permitAll();
    		
    	http.authorizeRequests().and().formLogin();
    	
     
// examples
//
//        // The pages does not require login
//        http.authorizeRequests()
//        	.antMatchers("/", "/splash",  "/login",  "/logout", "/css_style/**", "/js/**", "/images/**", "**/favicon.ico" ).permitAll()
//        	.antMatchers("/about/**", "/admin/**").hasRole("USER")
//        	.antMatchers("/snum", "/snum/**", "/items", "/items/**", "/drawings/**").hasAnyRole(AppUserRoles.ROLE_ADMIN, AppUserRoles.ROLE_FULL)
//        	.antMatchers("/home", "/home/**", "/quik", "/quik/**", "/drawing/item/**").hasAnyRole(AppUserRoles.ROLE_BASIC, AppUserRoles.ROLE_ADMIN, AppUserRoles.ROLE_FULL)	        		        	
//        	.anyRequest().authenticated();

        
//        // Config for Login Form
//        http.authorizeRequests()
//        	.and().formLogin()
//                .loginPage("/srv/login")
//                .loginProcessingUrl("/srv/login")
//                .defaultSuccessUrl("/quik")
//                .failureUrl("/srv/login?error=true")
//                .usernameParameter("username")
//                .passwordParameter("password");
                
                
//                // Config for Logout Page
//            .and().logout()
//            	.logoutUrl("/logout")
//            	.logoutSuccessUrl("/login")
//            	.deleteCookies("JSESSIONID")
//            	.invalidateHttpSession(true)
//            	;
        
//        http.authorizeRequests()
//        .and().exceptionHandling()
//        .accessDeniedPage("/noaccess");
 
    }	


    /**
     * Use the following URL to derive the BCrypt version of user's password 
     * 
     * https://passwordhashing.com/BCrypt
     * 
     * @param auth
     * @throws Exception
     */
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) 
      throws Exception {
		
		// password is 
        auth.inMemoryAuthentication().withUser("user")
          .password("$2b$10$TOwIs32GeV.OZNdQBW5XpOCj1fsfGydm5ytY5YhVeuUj88djiXp0e")  // "user"
          .roles("USER");
        
        auth.inMemoryAuthentication().withUser("admin")
        .password("$2b$10$21ITM86vZOBISgzdP9KgjuKuuURsa4OlqH7GbrMVjJ07r867Fn91m")  // "admin"
        .roles("USER","ADMIN");

        auth.inMemoryAuthentication().withUser("boardmember")
        .password("$2b$10$TOwIs32GeV.OZNdQBW5XpOCj1fsfGydm5ytY5YhVeuUj88djiXp0e").roles("USER","BM");

    }


	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}