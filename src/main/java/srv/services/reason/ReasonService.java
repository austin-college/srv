package srv.services.reason;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import srv.domain.reason.Reason;
import srv.domain.reason.ReasonDao;

public class ReasonService {
	
	@Autowired 
	ReasonDao dao;
	
	/**
	 * @return the integer count of all reasons we have in our system. Or throws
	 * exception.
	 * 
	 */
	public int reasonCount() throws Exception {
		
		List<Reason> rList = dao.listAll();
		
		int count = 0;
		for (Reason r : rList) {
			count = count + 1;
		}
		
		return count;
	}

}
