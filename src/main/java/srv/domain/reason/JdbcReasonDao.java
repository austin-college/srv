package srv.domain.reason;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import srv.config.H2JpaConfig;



public class JdbcReasonDao  implements ReasonDao {
	
	private static Logger log = LoggerFactory.getLogger(JdbcReasonDao.class);
	

    Connection getConnection() throws Exception {
    	return  DriverManager.getConnection("jdbc:h2:mem:testdb","sa","");
    }
    
    
	@Override
	public Reason getReasonById(int rid) throws Exception {

		Reason r = null;
		Connection conn = null;
		
		try {
			conn = getConnection(); 
			
			Statement s = conn.createStatement();
			
			String selectSql = String.format("select rid, reason from reasons where rid=%d",rid);
			log.debug(selectSql);
			
			ResultSet rs = s.executeQuery(selectSql);
					
			if (rs.next()) {
				r = new Reason().setRid(rs.getInt("rid")).setReason(rs.getString(2));
			}
			
			rs.close();
			s.close();
			
		} 
		finally {
			if (conn != null)
				conn.close();
		}
		
		return r;
	}
	
	
	@Override
	public List<Reason> listAll() throws Exception {

		List<Reason> list = new ArrayList<>();
		Connection conn = null;
		
		try {
			conn = getConnection();
			
			Statement s = conn.createStatement();
			
			String selectSql = "select rid, reason from reasons order by reason";
			log.debug(selectSql);
			ResultSet rs = s.executeQuery(selectSql);
					
			while (rs.next()) {
				Reason r = new Reason().setRid(rs.getInt(1)).setReason(rs.getString(2));
				list.add(r);
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
		public Reason create(String r) throws Exception {
	
			String insertSql = "insert into reasons (reason) values (?)";
			log.debug(insertSql);
	
			int rid = -1;
	
			try (Connection connection = getConnection();
					PreparedStatement statement = connection.prepareStatement(insertSql,
							Statement.RETURN_GENERATED_KEYS);) {
				
				statement.setString(1, r);
	
				int affectedRows = statement.executeUpdate();
	
				if (affectedRows == 0) {
					throw new SQLException("Creating reason failed, no rows affected.");
				}
	
				try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
					if (generatedKeys.next()) {
						rid = generatedKeys.getInt(1);
					} else {
						throw new SQLException("Creating reason failed, no ID obtained.");
					}
				}
				
			}
			
			return getReasonById(rid);
		}
	
	
	@Override
	public void delete(int r) throws Exception {

		Connection conn = null;

			try {
				conn = getConnection();
				
				Statement s = conn.createStatement();
				
				String deleteSql = String.format("delete from reasons where rid = %d",r);
				log.debug(deleteSql);
				
				int rc = s.executeUpdate(deleteSql);

				if (rc == 0) throw new Exception(String.format("unable to delete reason. [%s]",r));
				
			} 
			finally {
				conn.close();
			}
			
	}

	@Override
	public void update(int rid, String newVal) throws Exception {
		Connection conn = null;

		try {
			conn = getConnection();
			
			Statement s = conn.createStatement();
			
			String updateSql = String.format("update reasons set reason = '%s' where rid = %d",newVal, rid);
			log.debug(updateSql);
			
			int rc = s.executeUpdate(updateSql);

			if (rc == 0) throw new Exception(String.format("unable to update reason. [%d] to [%s]",rid, newVal));
			
		} 
		finally {
			conn.close();
		}
		
	}



}
