package srv.domain;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public abstract class JdbcTemplateAbstractDao {
	private DataSource dataSource;    
	private JdbcTemplate jdbcTemplate; 
	
    public DataSource getDataSource() {
    	if (dataSource == null)
    		dataSource = h2DataSource();
    	
		return dataSource;
	}
    
    /** 
     * This is the method to be used to initialize
     * database resources ie. connection.
     */
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

}
