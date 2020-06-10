package srv.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;

/**
 * An instance of this class tests the ServiceHoursService class
 * using Mockito
 * 
 * @author fancynine9
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ServiceHoursServiceTests {

	@Mock
	ServiceHoursDao dao;
	
	@InjectMocks
	ServiceHoursService shs; 
	
	/**
	 * Tests the initialize method in the constructor. 
	 */
	@Test
	public void testListHours() throws Exception {
		
		ServiceHours sh1 = new ServiceHours().setShid(1).setHours(3.0);
		ServiceHours sh2 = new ServiceHours().setShid(2).setHours(2.0);
		
		List<ServiceHours> dummyList = new ArrayList<ServiceHours>();
		dummyList.add(sh1);
		dummyList.add(sh2);
		
		Mockito.when(dao.listAll()).thenReturn(dummyList);
		
		List<ServiceHours> testList = shs.listHours();
		
		assertEquals(dummyList, testList);
		
		Mockito.verify(dao);
	}

}
