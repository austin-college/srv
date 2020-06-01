package srv.services.reason;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import srv.domain.reason.Reason;
import srv.domain.reason.ReasonDao;
import srv.services.ReasonService;


@RunWith(MockitoJUnitRunner.class)
public class ReasonServiceTests {

	@Mock
	ReasonDao dao;
	
	@InjectMocks
	ReasonService rs;
	
	
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testReasonCount_with2Reasons() throws Exception {
		Reason r1 = new Reason().setRid(1).setReason("reason one");
		Reason r2 = new Reason().setRid(2).setReason("reason two");
		
		List<Reason> dummyList = new ArrayList<Reason>();
		dummyList.add(r1);
		dummyList.add(r2);
		
		Mockito.when(dao.listAll()).thenReturn(dummyList);
		
		
		int count = rs.reasonCount();
		
		assertEquals(2,count);
		
		Mockito.verify(dao);
		
	}

}
