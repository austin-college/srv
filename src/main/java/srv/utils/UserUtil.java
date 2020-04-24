package srv.utils;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import srv.AppConstants;
import srv.domain.user.User;

/**
 * This utility class is the home for several convenient
 * authenticated user oriented methods.    
 * 
 * @author mahiggs
 *
 */
public class UserUtil {
	
	public static User currentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String auth_user_id = auth.getName();
		
		
		// TODO  Now use our daos to find a user in our system 
		
		return null;
	}
	
	
	/**
	 * Returns a collection of authorities/roles for the current authenticated user.
	 * In our app, the user can have multiple roles {ADMIN  
	 * 
	 * @return
	 */
	public static Collection<GrantedAuthority> userAuthorities() {
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
	public static boolean hasAnyRole(String ... roles) throws Exception {
		
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
	public static boolean userIsAdmin() throws Exception {
		return hasAnyRole(AppConstants.ROLE_ADMIN);
	}

	
	/**
	 * @return true if user has the BOARDMEMBER authority/role.
	 * @throws Exception
	 */
	public static boolean userIsBoardMember() throws Exception {
		return hasAnyRole(AppConstants.ROLE_BOARDMEMBER);
	}
	
	/**
	 * @return true if user has the SERVANT authority/role.
	 * @throws Exception
	 */
	public static boolean userIsServant() throws Exception {
		return hasAnyRole(AppConstants.ROLE_SERVANT);
	}

}
