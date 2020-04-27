package srv.domain.serviceHours;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import srv.domain.JdbcTemplateAbstractDao;
import srv.domain.event.JdbcTemplateEventDao;
import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
import srv.domain.user.JdbcTemplateUserDao;

/** 
 * Credit to Lydia House for this code
 * 
 * An instance of this class is the JDBC Template that implements the ServiceHoursDao 
 * This class is responsible for retrieving data from the serviceHours table in the 
 * data.sql database. The methods implemented are to create a new service hour query, 
 * to update an existing service hours query, delete a service hour query and fetching
 * a service hour query by its primary id.
 * 
 * @author fancynine9
 *
 */
@ComponentScan("srv.config")
public class JdbcTemplateServiceHoursDao extends JdbcTemplateAbstractDao implements ServiceHoursDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateServiceHoursDao.class);
 
	@Autowired
	private JdbcTemplateServiceClientDao serviceClientDao;
	private JdbcTemplateUserDao userDao;
	private JdbcTemplateEventDao eventDao;
	
	/**
	 * Default constructor. 
	 */
	public JdbcTemplateServiceHoursDao() {
		
		super();
	}
	
	@Override
	public List<ServiceHours> listAll() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ServiceHours create(Integer shid, Integer scid, Integer uid, Integer eid, Double hours, String stat)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(int shid) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Integer shid, Integer scid, Integer uid, Integer eid, Double hours, String stat)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ServiceHours fetchHoursById(int shid) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	
}
