package srv.domain.reason;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import srv.config.H2DatabaseConfig;
import srv.domain.JdbcTemplateAbstractDao;

@Primary
@Repository
@ComponentScan("srv.config")
public class JdbcTemplateReasonDao extends JdbcTemplateAbstractDao implements  ReasonDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateReasonDao.class);
	
	

	public JdbcTemplateReasonDao() {
		super();
	}

	@Override
	public List<Reason> listAll() throws Exception {
		
		List<Reason> results = getJdbcTemplate().query("select rid,reason from reasons", new ReasonRowMapper());
		 
	   return results;
		
	}

	
	/**
	 * 
	 * Given a new reason, we issue the proper SQL update command to add another 
	 * row in our table.   Since the table has auto-numbered keys, we will discover
	 * the assigned number and use it to fetch the newly created object from the database
	 * table as positive proof that the insert was successful.  Otherwise, this method
	 * throws an exception.
	 * 
	 */
	@Override
	public Reason create(String anyReasonStr) throws Exception {
		
		  final String sql = "INSERT INTO reasons (reason) VALUES(?)";
		  
		  final KeyHolder keyHolder = new GeneratedKeyHolder();

		  /* 
		   * in the following code we are using java8's closure (lambda expression) feature .  It's like an anonymous inline 
		   * class definition of a listener.  JdbcTemplate update allows us to pass a snippet of code, given an 
		   * untyped parameter (connection).   Inside our code snippet, we can refer to the parameter by name. 
		   * Our snippet returns a prepared statement (which is what the JdbcTemplate.update method requires as 
		   * the first parameter.   The second parameter of the update method is a keyholder object that we can ask 
		   * for the database assigned auto number key value (a number).
		   * 
		   * Note: The preparedStatement's string array names the columns that are auto-number keys.   For the
		   * contact table, the auto number key is contactId.
		   * 
		   */
	      getJdbcTemplate().update(
	              connection -> {
	                  PreparedStatement ps = connection.prepareStatement(sql, new String[]{"rid"});
	                  ps.setString(1, anyReasonStr);
	                  return ps;
	              }, keyHolder);
		
	      /*
	       * Now....what was the DBMS assigned auto number key?
	       */
	     Number num = keyHolder.getKey();
	     
	     /*
	      * Be careful, if it failed, we have none.   Avoid null pointer.
	      */
		if (num == null ) {
			String msg = String.format("Unable to insert new reason [%s]", anyReasonStr);
			log.warn(msg);
			throw new Exception(msg);
		}
	   
		
	   log.debug("generated reason id is {}", num);
		
	   /*
	    *  return a handle on the newly created object as a courtesy
	    */
	   return this.getReasonById((int)num); 
		
	}
	
	

	@Override
	public void delete(int rid) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from Reasons where rid= ?", new Object[] { rid });
		
		if (rc != 1) {
			String msg = String.format("unable to delete reason [%s]",rid);
			log.warn(msg);
			throw new Exception(msg);
		}
	}
	

	@Override
	public void update(int rid, String newVal) throws Exception {
		int rc = getJdbcTemplate().update("update reasons set reason = ? where rid = ?", new Object[] { newVal, rid });

		if (rc < 1) {
			log.error("unable to update reason [{}]",rid);
		}

	}

	@Override
	public Reason getReasonById(int rid) throws Exception {
		
		String sqlStr = String.format("select rid,reason from reasons where rid = %d",rid);
		log.debug(sqlStr);
		
		List<Reason> results = getJdbcTemplate().query(sqlStr, new ReasonRowMapper());
		
		if (results.size() != 1) {
			log.error("unable to fetch reason [{}]",rid);
		}
		return results.get(0);
	}

	
	
	private class ReasonRowMapper implements RowMapper < Reason > {
	    @Override
	    public Reason mapRow(ResultSet rs, int rowNum) throws SQLException {

	        Reason reason = new Reason()
	        		.setReason(rs.getString("reason"))
	        		.setRid(rs.getInt("rid"));
	        
	        return reason;
	    }
	}
}
