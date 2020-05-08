package srv.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * This class aids in the development of JdbcTemplate classes to provide an
 * easier, cleaner means of the following methods. 
 * 
 * @author Lydia House
 *
 */
@Repository
@ComponentScan("srv.config")
public abstract class JdbcTemplateAbstractDao {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
