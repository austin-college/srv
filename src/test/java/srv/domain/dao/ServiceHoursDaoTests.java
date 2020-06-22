package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.services.ServiceHoursService;

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

	// @Autowired
	// ServiceHoursService hrSvc;

	@Autowired
	ServiceHoursDao dao;

	/**
	 * Tests the FetchById method in ServiceHoursDao, should return the information
	 * for the first serviceHour entered.
	 * 
	 * @throws Exception
	 */

	@Test
	void testFetchById() throws Exception {

		log.warn("\n\n\n");

		ServiceHours sh1 = dao.fetchHoursById(1);

		assertEquals(1, sh1.getShid());
		assertEquals(1, sh1.getServedPet().getScid());
		assertEquals(1, sh1.getServant().getUid());
		assertEquals(1, sh1.getEvent().getEid());
		assertEquals(3.0, sh1.getHours());
		assertEquals("Approved", sh1.getStatus());
		assertEquals("I hated it", sh1.getReflection());
		assertEquals("House building", sh1.getDescription());
	}

	/**
	 * Tests the listAll method in the ServiceHoursDao.
	 */
	@Test
	void testListAll() throws Exception {

		List<ServiceHours> serviceHours = dao.listAll();

		assertEquals(5, serviceHours.size());

		ServiceHours sHour1 = serviceHours.get(0);
		ServiceHours sHour2 = serviceHours.get(1);
		ServiceHours sHour4 = serviceHours.get(3);

		// Verifying service hour id's
		assertEquals(1, sHour1.getShid());
		assertEquals(2, sHour2.getShid());
		assertEquals(4, sHour4.getShid());

		// testing contents of sHour1 assertEquals(1,

		assertEquals(1, sHour1.getServedPet().getScid());
		assertEquals(1, sHour1.getServant().getUid());
		assertEquals(1, sHour1.getEvent().getEid());
		assertEquals(3.0, sHour1.getHours());
		assertEquals("Approved", sHour1.getStatus());
		assertEquals("I hated it", sHour1.getReflection());
		assertEquals("House building", sHour1.getDescription());

		// testing contents of sHour2
		assertEquals(2, sHour2.getServedPet().getScid());
		assertEquals(2, sHour2.getServant().getUid());
		assertEquals(1, sHour2.getEvent().getEid());
		assertEquals(2.0, sHour2.getHours());
		assertEquals("Pending", sHour2.getStatus());
		assertEquals("Made food", sHour2.getReflection());
		assertEquals("Crisis Center", sHour2.getDescription());

		// testing contents of sHour4
		assertEquals(1, sHour4.getServedPet().getScid());
		assertEquals(4, sHour4.getServant().getUid());
		assertEquals(1, sHour4.getEvent().getEid());
		assertEquals(2.3, sHour4.getHours());
		assertEquals("Approved", sHour4.getStatus());
		assertEquals("Met a guy named Randy", sHour4.getReflection());
		assertEquals("Landscaping", sHour4.getDescription());

	}

	@Test
	void testCreate() throws Exception {

		List<ServiceHours> hoursBefore = dao.listAll();
		int numBeforeInsert = hoursBefore.size();
		for (ServiceHours sc : hoursBefore) {
			System.err.println(sc.getShid());
		}

		ServiceHours nsh = dao.create(2, 1, 1, 3.0, "Approved", "Painted", "House Painting");

		assertNotNull(nsh);

		List<ServiceHours> hoursAfter = dao.listAll();
		int numAfterInsert = hoursAfter.size();

		System.err.println("\n\nAfter Insert " + numAfterInsert);
		for (ServiceHours sc : hoursBefore) {
			System.err.println(sc.getShid());
		}

		/*
		 * The next assigned id on successful insert should be numBeforeInsert + 1.
		 */
		assertEquals(numBeforeInsert + 1, nsh.getShid());

		/*
		 * Now we will examine the newly inserted record.
		 */
		ServiceHours sh5 = nsh;

		// assertEquals(5, sh5.getShid());
		assertEquals(2, sh5.getServedPet().getScid());
		assertEquals(1, sh5.getServant().getUid());
		assertEquals(1, sh5.getEvent().getEid());
		assertEquals(3.0, sh5.getHours());
		assertEquals("Approved", sh5.getStatus());
		assertEquals("Painted", sh5.getReflection());
		assertEquals("House Painting", sh5.getDescription());

		/*
		 * log.warn("\n\n\n");
		 * 
		 * ServiceHours s = dao.create(5, 2, 3, 2, 2.0, "Approved", "fun",
		 * "Crisis Center");
		 * 
		 * ServiceHours sh5 = dao.fetchHoursById(5);
		 * 
		 * //Verifying that contact was stored in database assertEquals(5,
		 * sh5.getShid()); assertEquals(2, sh5.getServedPet().getScid());
		 * assertEquals(3, sh5.getServant().getUid()); assertEquals(2,
		 * sh5.getEvent().getEid()); assertEquals(2.0, sh5.getHours());
		 * assertEquals("Approved", sh5.getStatus()); assertEquals("fun",
		 * sh5.getReflection()); assertEquals("Crisis Center", sh5.getDescription());
		 * 
		 * //Testing ServiceHour returned from create
		 * 
		 * assertEquals(5, s.getShid()); assertEquals(2, s.getServedPet().getScid());
		 * assertEquals(3, s.getServant().getUid()); assertEquals(2,
		 * s.getEvent().getEid()); assertEquals(2.0, s.getHours());
		 * assertEquals("Approved", s.getStatus()); assertEquals("fun",
		 * s.getReflection()); assertEquals("Crisis Center", s.getDescription());
		 */

	}

	/**
	 * Tests the delete method. Should remove the query with the Specific ID, in
	 * this case 1.
	 * 
	 */
	@Test
	void testDelete() throws Exception {

		log.warn("\n\n\n");

		dao.delete(1);

		List<ServiceHours> serviceHours = dao.listAll();

		assertEquals(4, serviceHours.size());

		ServiceHours sh1 = serviceHours.get(0);
		ServiceHours sh2 = serviceHours.get(1);
		ServiceHours sh3 = serviceHours.get(2);

		assertEquals(2, sh1.getShid());
		assertEquals(3, sh2.getShid());
		assertEquals(4, sh3.getShid());

		// Verifying first service hour info

		assertEquals(2, sh1.getServedPet().getScid());
		assertEquals(2, sh1.getServant().getUid());
		assertEquals(1, sh1.getEvent().getEid());
		assertEquals(2.0, sh1.getHours());
		assertEquals("Pending", sh1.getStatus());
		assertEquals("Made food", sh1.getReflection());
		assertEquals("Crisis Center", sh1.getDescription());

		// Verifying second service hour info

		assertEquals(2, sh2.getServedPet().getScid());
		assertEquals(3, sh2.getServant().getUid());
		assertEquals(2, sh2.getEvent().getEid());
		assertEquals(1.5, sh2.getHours());
		assertEquals("Approved", sh2.getStatus());
		assertEquals("Made friends", sh2.getReflection());
		assertEquals("Crisis Center", sh2.getDescription());

		// Verifying third service hour info

		assertEquals(1, sh3.getServedPet().getScid());
		assertEquals(4, sh3.getServant().getUid());
		assertEquals(1, sh3.getEvent().getEid());
		assertEquals(2.3, sh3.getHours());
		assertEquals("Approved", sh3.getStatus());
		assertEquals("Met a guy named Randy", sh3.getReflection());
		assertEquals("Landscaping", sh3.getDescription());
	}

	/**
	 * Tests the update method, should update the query with specified id.
	 */
	@Test
	void testUpdate() throws Exception {

		log.warn("\n\n\n");

		dao.update(1, 1, 1, 3, 2.0, "Pending", "Painted a lot", "House Painting");

		ServiceHours sh1 = dao.fetchHoursById(1);

		assertEquals(1, sh1.getShid());
		assertEquals(1, sh1.getServedPet().getScid());
		assertEquals(1, sh1.getServant().getUid());
		assertEquals(3, sh1.getEvent().getEid());
		assertEquals(2, sh1.getHours());
		assertEquals("Pending", sh1.getStatus());
		assertEquals("Painted a lot", sh1.getReflection());
		assertEquals("House Painting", sh1.getDescription());
	}
	
	/**
	 * Tests the fetch hours by the current user's id.
	 */
	@Test
	void testFetchByUserId() throws Exception {
		
		List<ServiceHours> userHours = dao.fetchHoursByUserId(2);
		
		assertEquals(1, userHours.size());
	}
	
	/**
	 * Filters the list of hours by user id.
	 */
	@Test
	void listByFilter_byUserId() throws Exception {
		
		// fetches the list of hours with user id of 4
		List <ServiceHours> hoursByUid4 = dao.listByFilter(4, null, null, null);
		
		assertEquals(2, hoursByUid4.size());
		assertEquals(4, hoursByUid4.get(0).getShid());
		assertEquals(5, hoursByUid4.get(1).getShid());		
	}
	/**
	 * Filters the list of hours by service client/sponsor id.
	 */
	@Test
	void listByFilter_byServiceClient() throws Exception {
		
		// fetches the list of hours with service client id 1
		List <ServiceHours> hoursByScid1 = dao.listByFilter(null, 1, null, null);
		
		assertEquals(3, hoursByScid1.size());
		assertEquals(1, hoursByScid1.get(0).getShid());
		assertEquals(4, hoursByScid1.get(1).getShid());
		assertEquals(5, hoursByScid1.get(2).getShid());		
	}
	
	/**
	 * Verifies that the entire list of events is return when all parameters are null
	 */
	@Test
	void listByFilter_allNullParam() throws Exception {
		
		// Fetches the list of hours with all nulls
		List <ServiceHours> allHours = dao.listByFilter(null, null, null, null);
		
		assertEquals(5, allHours.size());
	}
	
	/**
	 * Filters the list of hours by month name.
	 */
	@Test
	void listByFilter_byMonth() throws Exception {
		
		// fetches the list of hours for March
		List <ServiceHours> allMarchHours = dao.listByFilter(null, null, "March", null);
		
		assertEquals(1, allMarchHours.size());
		assertEquals(2, allMarchHours.get(0).getShid());
	}
	
	/**
	 * Filters the list of hours by status.
	 */
	@Test
	void listByFilter_byStatus() throws Exception {
		
		// fetches the list of hours that are pending
		List <ServiceHours> allPendingHours = dao.listByFilter(null, null, null, "Pending");
		
		assertEquals(2, allPendingHours.size());
		assertEquals(2, allPendingHours.get(0).getShid());
		assertEquals(5, allPendingHours.get(1).getShid());
	}
}
