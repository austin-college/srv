package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import srv.domain.serviceHours.ServiceHours;
import srv.domain.serviceHours.ServiceHoursDao;

//add listAll method to viewHours hours controller with try catch 
//check serviceClient controller

/**
 * Tests the ServiceHoursDao.
 * 
 * @author fancynine9
 *
 */
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
public class ServiceHoursDaoTests {

	private static Logger log = LoggerFactory.getLogger(ServiceHoursDaoTests.class);
	
	@Autowired
	ServiceHoursDao dao;
	
	/**
	 * Tests the listAll method in the ServiceHoursDao. 
	 */
	@Test
	void testListAll_whenUsingJdbcTemplate() throws Exception {
		
		List<ServiceHours> serviceHours = dao.listAll();
		
		assertEquals(4, serviceHours.size());
		
		ServiceHours sHour1 = serviceHours.get(0);
		ServiceHours sHour2 = serviceHours.get(1);
		ServiceHours sHour4 = serviceHours.get(3);
		
		//Verifying service hour1
		assertEquals(1, sHour1.getShid());
		assertEquals(2, sHour2.getShid());
		assertEquals(4, sHour4.getShid());
		
		//testing contents of sHour1
		assertEquals(1, sHour1.getServedPet());
		assertEquals(1, sHour1.getServant());
		assertEquals(1, sHour1.getEvent());
		assertEquals(3, sHour1.getHours());
		assertEquals("Approved", sHour1.getStatus());
		
		//testing contents of sHour2
		assertEquals(2, sHour2.getServedPet());
		assertEquals(2, sHour2.getServant());
		assertEquals(2, sHour2.getEvent());
		assertEquals(2, sHour2.getHours());
		assertEquals("Pending", sHour2.getStatus());
		
		//testing contents of sHour4
		assertEquals(3, sHour4.getServedPet());
		assertEquals(4, sHour4.getServant());
		assertEquals(2, sHour4.getEvent());
		assertEquals(2, sHour4.getHours());
		assertEquals("Approved", sHour4.getStatus());
		
		
	}
	
	
}

