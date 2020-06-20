package srv.domain.spotlight;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import srv.domain.JdbcTemplateAbstractDao;


public class JdbcTemplateSpotLightDao extends JdbcTemplateAbstractDao implements SpotLightDao {

	private static Logger log = LoggerFactory.getLogger(JdbcTemplateSpotLightDao.class);
	
	



	@Override
	public void update(SpotLight s) throws Exception {
		
		int rc = getJdbcTemplate().update("UPDATE spotlight SET imgtype = ?, imgsize = ?, spottxt = ?, img = ? "
				+ " WHERE sid = ?", 
				new Object[] {s.getImgType(), s.getImgSize(), s.getSpotText(), s.getImg(), s.getSid()});

		if (rc < 1) {
			log.error("Unable to spotlight contact [{}]",s.getSid());
		}
		
	}

	@Override
	public SpotLight spotLightById(int sid) throws Exception {
		
		if (sid == 0) return null;
		
		String sqlStr = String.format("SELECT sid, img, imgtype, imgsize, spottxt "
				+ "FROM spotlight WHERE sid = %d", sid);
		
		List<SpotLight> results = getJdbcTemplate().query(sqlStr, new SpotLightRowMapper());
		
		if (results.size() != 1) {
			log.error("Unable to fetch spotlight[{}]", sid);
			throw new Exception("Unable to fetch spotlight "+sid);
		}
		
		return results.get(0);
	}
	

	private class SpotLightRowMapper implements RowMapper <SpotLight> {

		@Override
		public SpotLight mapRow(ResultSet rs, int rowNum) throws SQLException {

			SpotLight s = new SpotLight()
					.setSid(rs.getInt("sid"))
					.setImgType(rs.getString("imgtype"))
					.setImgSize(rs.getObject("imgsize") == null ? null : rs.getInt("imgsize"))
					.setImg(rs.getObject("img")==null ? null: rs.getBytes("img"))
					.setSpotText(rs.getString("spottxt"));
			
			return s;
		}
	    
	}

}
