package srv.domain.serviceClient;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import srv.domain.contact.Contact;
import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
import srv.domain.serviceClient.JdbcTemplateServiceClientDao.ServiceClientRowMapper;

public class JdbcTemplateServiceClientDao implements ServiceClientDao {
	
	private static Logger log = LoggerFactory.getLogger(JdbcTemplateServiceClientDao.class);

	private DataSource dataSource;    
	
	private JdbcTemplate jdbcTemplate;  
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
                .addScript("data.sql")
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

	public JdbcTemplateServiceClientDao() {
		super();
	}

	@Override
	public List<ServiceClient> listAll() throws Exception {
		
		List<ServiceClient> results = getJdbcTemplate().query("select serviceClientId, title, contactId, boardMem, category from serviceClients", new ServiceClientRowMapper());
		 
	   return results;
		
	}

	@Override
	public ServiceClient create(String sc) throws Exception {
		
			int rc = jdbcTemplate.update("INSERT INTO serviceClients (title) VALUES(?)", new Object[] { sc });

			if (rc != 1) {
				String msg = String.format("unable to insert new title [%s]", sc);
				log.warn(msg);
				throw new Exception("Unable insert new unique title. Maybe a duplicate?");
			}

			ServiceClient results = getJdbcTemplate().queryForObject(String.format("select serviceClientId, title, contactId, boardMem, category from serviceClients where title = '%s'",sc), new ServiceClientRowMapper());
	   
	   return results;
	}
	
	

	@Override
	public void delete(int scid) throws Exception {
		int rc = getJdbcTemplate().update("DELETE from serviceClients where serviceClientId= ?", new Object[] { scid });
		
		if (rc != 1) {
			String msg = String.format("unable to delete title [%s]", scid);
			log.warn(msg);
			throw new Exception(msg);
		}
	}
	

	@Override
	public void update(int scid, String newVal) throws Exception {
		int rc = getJdbcTemplate().update("update serviceClients set title = ? where serviceClientId = ?", new Object[] { newVal, scid });

		if (rc < 1) {
			log.error("unable to update title [{}]",scid);
		}

	}

	@Override
	public ServiceClient fetchClientId(int scid) throws Exception {
		
		String sqlStr = String.format("select serviceClientId, title, contactId, boardMem, category from serviceClients where serviceClientId = %d",scid);
		log.debug(sqlStr);
		
		List<ServiceClient> results = getJdbcTemplate().query(sqlStr, new ServiceClientRowMapper());
		
		if (results.size() != 1) {
			log.error("unable to fetch reason [{}]",scid);
		}
		return results.get(0);
	}

	
	
	class ServiceClientRowMapper implements RowMapper <ServiceClient> {
	    @Override
	    public ServiceClient mapRow(ResultSet rs, int rowNum) throws SQLException {
	    	
	    	JdbcTemplateContactDao dao = new JdbcTemplateContactDao();
	    	ServiceClient sc = new ServiceClient();
	    	
	    	try {
				Contact con = dao.fetchContactById(rs.getInt("contactId"));
				sc.setScid(rs.getInt("serviceClientId"))
		        	.setName(rs.getString("title"))
		        	.setContact(con)
		        	.setBoardMember(rs.getString("boardMem"))
		    		.setCategory(rs.getString("category"));
				
				  
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    			
	    	return sc;
	        
	      
	    }
	}
}
