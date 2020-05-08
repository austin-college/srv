package srv.utils;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import srv.AppConstants;
import srv.domain.user.JdbcTemplateUserDao;
import srv.domain.user.User;

/**
 * This utility class is the home for several convenient
 * authenticated user oriented methods.   An instance of this class can
 * inform us who is authenticated and what roles the authenticated has.
 * We use an instance of this class in your controllers and other places
 * as needed.
 * <p>
 * Can be autowired;   
 * 
 * @author mahiggs
 *
 */

@Component
public class UserUtil {
	

	@Autowired
	JdbcTemplateUserDao userDao;
	

	
	public UserUtil() {
		super();
	}

	
	
	public JdbcTemplateUserDao getUserDao() {
		return userDao;
	}



	public void setUserDao(JdbcTemplateUserDao userDao) {
		this.userDao = userDao;
	}



	/**
	 * Based on the authenticated user,  this method returns the associated
	 * domain user object.   This can be handy for finding information about
	 * the current user (including their roles).
	 *  
	 * @return
	 * @throws Exception
	 */
	public User currentUser() throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String auth_user_id = auth.getName();
		
		
		return null;
		
	}
	
	


	/**
	 * Returns a collection of authorities/roles for the current authenticated user.
	 * In our app, the user can have multiple roles {ADMIN  
	 * 
	 * @return
	 */
	public  Collection<GrantedAuthority> userAuthorities() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		/*
		 * current authenticated users.
		 */
		UserDetails usrDetails = (UserDetails) auth.getPrincipal();
		if (usrDetails == null) return null;
		
		/*
		 * what roles for current user?
		 */
		Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) usrDetails.getAuthorities();

		return authorities;
	}
	
	/**
	 * @param roles
	 * @return true if any of the specified string roles are granted to the
	 * principal user's authorities. 
	 * <br/><br/>
	 * <code>
	 * UserUtil.hasAnyRole("ADMIN");   // example
	 * </code>
	 * @throws Exception 
	 * @see srv.AppConstants for role constants.
	 * 
	 */
	public  boolean hasAnyRole(String ... roles) throws Exception {
		
		/*
		 * what roles for current user?
		 */
		Collection<GrantedAuthority> authorities = userAuthorities();
		
		if (authorities == null) throw new Exception("no user authorities");
		
		/**
		 * 
		 */
		for (String role : roles) {
			
			for (GrantedAuthority ga : authorities) {
				if (ga.getAuthority().equals("ROLE_"+role)) return true;
			}
				
		}
		
		return false;
	}
	
	
	
	/**
	 * @return true if user has the ADMIN authority/role.
	 * @throws Exception
	 */
	public  boolean userIsAdmin() throws Exception {
		return hasAnyRole(AppConstants.ROLE_ADMIN);
	}

	
	/**
	 * @return true if user has the BOARDMEMBER authority/role.
	 * @throws Exception
	 */
	public  boolean userIsBoardMember() throws Exception {
		return hasAnyRole(AppConstants.ROLE_BOARDMEMBER);
	}
	
	/**
	 * @return true if user has the SERVANT authority/role.
	 * @throws Exception
	 */
	public  boolean userIsServant() throws Exception {
		return hasAnyRole(AppConstants.ROLE_SERVANT);
	}





}
