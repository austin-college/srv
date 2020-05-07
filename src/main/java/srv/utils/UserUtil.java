package srv.utils;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import srv.AppConstants;
import srv.domain.user.JdbcTemplateUserDao;
import srv.domain.user.User;
import srv.domain.user.UserDao;

/**
 * This utility class is the home for several convenient
 * authenticated user oriented methods.    
 * 
 * @author mahiggs
 *
 */
public class UserUtil {
	
	/*
	 * TODO 
	 * make changes to HoursController.java, HoursControllerTests.java
	 * 
	 * need to properly use the JdbcTemplateUserDao, before the dao was not autowired and thus
	 * we had a null pointer exception when we tried to fetch the user's username. In order to bypass
	 * this error we are passing in the template from the HoursController.java (where it is autowired)
	 * but this will change 
	 */
	public static User currentUser(JdbcTemplateUserDao jdbcTUD) throws Exception {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		String auth_user_id = auth.getName();
		return fetchUserByUserName(auth_user_id, jdbcTUD);
		
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

	//TODO see the method above currentUser
	private static User fetchUserByUserName(String auth_user_id, JdbcTemplateUserDao jdbcTUD) throws Exception {

		return jdbcTUD.fetchUserByUserName(auth_user_id);
		
		
		
	}



}
