package srv.domain.serviceclient;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import srv.config.H2JpaConfig;
import srv.domain.reason.Reason;
import srv.domain.serviceclient.JdbcServiceClientDao;

public class JdbcServiceClientDao implements ServiceClientDao{
	
	private static Logger log = LoggerFactory.getLogger(JdbcServiceClientDao.class);
	
    Connection getConnection() throws Exception {
    	return  DriverManager.getConnection("jdbc:h2:mem:testdb","sa","");
    }
    
    @Override
	public ServiceClient fetchClientId(int scid) throws Exception {
    	
    	ServiceClient sc = null;
		Connection conn = null;
		
		try {
			conn = getConnection(); 
			
			Statement s = conn.createStatement();
			
			String selectSql = String.format("select scid, title from serviceClients where scid=%d",scid);
			log.debug(selectSql);
			
			ResultSet rs = s.executeQuery(selectSql);
					
			if (rs.next()) {
				sc = new ServiceClient().setScid(rs.getInt("scid")).setTitle(rs.getString(2));
			}
			
			rs.close();
			s.close();
			
		} 
		finally {
			if (conn != null)
				conn.close();
		}
		
		return sc;
	}
    
	@Override
	public List<ServiceClient> listAll() throws Exception {
		List<ServiceClient> list = new ArrayList<>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			Statement s = conn.createStatement();
			
			String selectSql = "select scid, title from serviceClients order by title";
			log.debug(selectSql);
			ResultSet rs = s.executeQuery(selectSql);
					
			while (rs.next()) {
				ServiceClient sc = new ServiceClient().setScid(rs.getInt(1)).setTitle(rs.getString(2));
				list.add(sc);
			}
			
			rs.close();
			s.close();
			
		} 
		finally {
			conn.close();
		}
		
		return list;
	}

	@Override
	public ServiceClient create(String sc) throws Exception {
		
		String insertSql = "insert into serviceClients (title) values (?)";
		log.debug(insertSql);

		int scid = -1;

		try (Connection connection = getConnection();
				PreparedStatement statement = connection.prepareStatement(insertSql,
						Statement.RETURN_GENERATED_KEYS);) {
			
			statement.setString(1, sc);

			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating reason failed, no rows affected.");
			}

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					scid = generatedKeys.getInt(1);
				} else {
					throw new SQLException("Creating reason failed, no ID obtained.");
				}
			}
			
		}
		
		return fetchClientId(scid);
	}

	@Override
	public void delete(int scid) throws Exception {
		Connection conn = null;

		try {
			conn = getConnection();
			
			Statement s = conn.createStatement();
			
			String deleteSql = String.format("delete from serviceClients where scid = %d",scid);
			log.debug(deleteSql);
			
			int rc = s.executeUpdate(deleteSql);

			if (rc == 0) throw new Exception(String.format("unable to delete reason. [%s]",scid));
			
		} 
		finally {
			conn.close();
		}
		
	}

	@Override
	public void update(int scid, String newVal) throws Exception {
		Connection conn = null;

		try {
			conn = getConnection();
			
			Statement s = conn.createStatement();
			
			String updateSql = String.format("update serviceClients set title = '%s' where scid = %d",newVal, scid);
			log.debug(updateSql);
			
			int rc = s.executeUpdate(updateSql);

			if (rc == 0) throw new Exception(String.format("unable to update reason. [%d] to [%s]",scid, newVal));
			
		} 
		finally {
			conn.close();
		}
		
	}

	

}
