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

import srv.domain.contact.Contact;
import srv.domain.serviceHours.ServiceHours;
import srv.domain.serviceHours.ServiceHoursDao;

//add listAll method to viewHours hours controller with try catch 
//check serviceClient controller

/**
 * Credit to AJ and Lydia 
 * 
 * Tests the ServiceHoursDao.
 * 
 * @author fancynine9
 *
 */
@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class ServiceHoursDaoTests {

	private static Logger log = LoggerFactory.getLogger(ServiceHoursDaoTests.class);
	
	@Autowired
	ServiceHoursDao dao;
	
	/**
	 * Tests the FetchById method in ServiceHoursDao, should return 
	 * the information for the first serviceHour entered.
	 * @throws Exception
	 */
	@Test
	void testFetchById_whenUsingJdbcTemplate() throws Exception {
		
		log.warn("\n\n\n");
		
		ServiceHours sh1 = dao.fetchHoursById(1);
		
		assertEquals(1, sh1.getShid());
		assertEquals(1, sh1.getServedPet());
		assertEquals(1, sh1.getServant());
		assertEquals(1, sh1.getEvent());
		assertEquals(3, sh1.getHours());
		assertEquals("Approved", sh1.getStatus());
	}
	
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
	
	@Test
	void testCreate_whenUsingJdbcTemplate() throws Exception {
		
		log.warn("\n\n\n");
		
		ServiceHours s = dao.create(2, 3, 2, 2.0, "Approved");
		
		ServiceHours sh5 = dao.fetchHoursById(5);
		
		//Verifying that contact was stored in database
		assertEquals(5, sh5.getShid());
		assertEquals(2, sh5.getServedPet());
		assertEquals(3, sh5.getServant());
		assertEquals(2, sh5.getEvent());
		assertEquals(2, sh5.getHours());
		assertEquals("Approved", sh5.getStatus());
		
		//Testing ServiceHour returned from create
		
		assertEquals(5, s.getShid());
		assertEquals(2, s.getServedPet());
		assertEquals(3, s.getServant());
		assertEquals(2, s.getEvent());
		assertEquals(2, s.getHours());
		assertEquals("Approved", s.getStatus());
		
	}
	
	
}

