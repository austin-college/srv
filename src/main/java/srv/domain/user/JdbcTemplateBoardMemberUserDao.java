package srv.domain.user;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;

import srv.AppConstants;
import srv.domain.JdbcTemplateAbstractDao;

public class JdbcTemplateBoardMemberUserDao extends JdbcTemplateAbstractDao implements BoardMemberUserDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateServantUserDao.class);
	
	@Autowired
	private JdbcTemplateServantUserDao srvUserDao;
	
	public JdbcTemplateBoardMemberUserDao() {
		super();
	}
	
	/*
	 * Returns a list of all current board member users
	 */
	@Override
	public List<BoardMemberUser> listAllBoardMemberUsers() throws Exception {
		
		List<BoardMemberUser> currentBmUsers = getJdbcTemplate().query("SELECT * FROM boardMemberUsers", new BoardMemberUserRowMapper());
		
		return currentBmUsers;
	}
	
	/**
	 * This class maps a BoardMemberUser database record to the BoardMemberUser model object by using
	 * a RowMapper interface to fetch the records for a BoardMember user from the data store.
	 */
	class BoardMemberUserRowMapper implements RowMapper<BoardMemberUser> {
		
		/*
		 * Returns the BoardMemberUser in the given row
		 */
		@Override
		public BoardMemberUser mapRow(ResultSet rs, int rowNum) throws SQLException {
			
			BoardMemberUser bmUser = new BoardMemberUser();
			
			try {
				
				ServantUser srvUser = srvUserDao.fetchServantUserById(rs.getInt("userId"));
				
				/*
				 * If the given object is null, then the current BoardMemberUser's variable
				 * will be set to null since it is not required. Otherwise, we set the variable
				 * using daos when needed.
				 */
				Boolean currentIsCoChair = rs.getObject("isCoChair") != null ? rs.getBoolean("isCoChair") : null;
				
				
				bmUser.setIsCoChair(currentIsCoChair)
					.setExpectedGradYear(srvUser.getExpectedGradYear())
					.setAffiliation(srvUser.getAffiliation())
					.setHasCar(srvUser.getHasCar())
					.setCarCapacity(srvUser.getCarCapacity())
					.setUid(srvUser.getUid())
					.setUsername(srvUser.getUsername())
					.setContactInfo(srvUser.getContactInfo())
					.setRoll(AppConstants.ROLE_BOARDMEMBER);
					;
			
				
				 
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return bmUser;
		}
	}
}
