package srv.domain.user;
import java.util.List;

import srv.domain.contact.Contact;

public class JdbcTemplateUserDao implements UserDao {

	@Override
	public List<User> listAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User create(String userID, String password, double totalHoursServed, int cid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int uid) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(int uid, String newVal) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public User fetchUserById(int uid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Contact fetchUserContactById(int cid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	

}
