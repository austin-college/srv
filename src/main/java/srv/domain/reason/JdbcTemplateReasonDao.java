package srv.domain.reason;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class JdbcTemplateReasonDao implements  ReasonDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateReasonDao.class);
	

	private DataSource dataSource;    // which database will we use?
	
	private JdbcTemplate jdbcTemplate;  // relational to object helper 
	
	
    public DataSource getDataSource() {
    	if (dataSource == null)
    		dataSource = h2DataSource();
    	
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}


	public DataSource h2DataSource() {
	    
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("data.sql")//script to create person table
                .build();
    }
	
    
	public JdbcTemplate getJdbcTemplate() {
		if (jdbcTemplate == null)
			jdbcTemplate = new JdbcTemplate(getDataSource());
		
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public JdbcTemplateReasonDao() {
		super();
	}

	@Override
	public List<Reason> listAll() throws Exception {
		
		List<Reason> results = getJdbcTemplate().query("select rid,reason from reasons", new ReasonRowMapper());
		 
	   return results;
		
	}

	@Override
	public Reason create(String r) throws Exception {
		
			int rc = jdbcTemplate.update("INSERT INTO Reasons (reason) VALUES(?)", new Object[] { r });

			if (rc != 1) {
				String msg = String.format("unable to insert new reason [%s]", r);
				log.warn(msg);
				throw new Exception("Unable insert new unique reason. Maybe a duplicate?");
			}

		   Reason results = getJdbcTemplate().queryForObject(String.format("select rid, reason from reasons where reason = '%s'",r), new ReasonRowMapper());
	   
	   return results;
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
