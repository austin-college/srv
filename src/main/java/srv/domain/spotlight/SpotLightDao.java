package srv.domain.spotlight;

import java.util.List;


public interface SpotLightDao {
	
	public void update(SpotLight spot) throws Exception;
	
	public SpotLight spotLightById(int sid) throws Exception;

}
