package srv.services;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.serviceclient.ServiceClient;

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
	private ServiceHoursDao dao;
	
	@InjectMocks
	private ServiceHoursService shs; 
	
	@Rule 
	public ExpectedException exceptionRule = ExpectedException.none();
	
	private ServiceHours sh1; 
	private ServiceHours sh2; 
	private ServiceHours sh3; 
	
	@Before 
	public void setUp() throws Exception	{
		
		MockitoAnnotations.initMocks(this);
		
		sh1 = new ServiceHours()
				.setShid(1)
				.setServedPet(null)
				.setServant(null)
				.setEvent(null)
				.setHours(2.0)
				.setStatus("Approved")
				.setReflection("test reflection")
				.setDescription("test description");
		
		sh2 = new ServiceHours()
				.setShid(2)
				.setServedPet(null)
				.setServant(null)
				.setEvent(null)
				.setHours(3.5)
				.setStatus("Pending")
				.setReflection("test 2 reflection")
				.setDescription("test 2 description");
	}
	
	
	
	
	@Test

	public void testListHours() throws Exception {

	
		
		ServiceHours sh1 = new ServiceHours().setShid(1).setHours(3.0);
		ServiceHours sh2 = new ServiceHours().setShid(2).setHours(2.0);
		
		List<ServiceHours> dummyList = new ArrayList<ServiceHours>();
		dummyList.add(sh1);
		dummyList.add(sh2);
		
		Mockito.when(dao.listAll()).thenReturn(dummyList);
		
		List<ServiceHours> testList = shs.listHours();

		assertEquals(2, testList.size());
		
		Mockito.verify(dao.listAll());
		
	}

}
