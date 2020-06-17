package srv.services;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockitoSession;

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
import org.springframework.util.Assert;

import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.event.eventype.EventType;
import srv.domain.hours.ServiceHours;
import srv.domain.hours.ServiceHoursDao;
import srv.domain.serviceclient.ServiceClient;
import srv.domain.user.User;
import srv.utils.UserUtil;

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
	@Mock 
	private EventService eventService; 
	@Mock
	private UserUtil userUtil; 
	
	@InjectMocks
	private ServiceHoursService shs; 
	
	@Rule 
	public ExpectedException exceptionRule = ExpectedException.none();
	
	private ServiceHours sh1; 
	private ServiceHours sh2; 
	private ServiceHours sh3; 
	
	private EventType et1;
	private EventType et2;
	private Event e1;
	private Event e2;
	private ServiceClient sc1; 
	private ServiceClient sc2; 
	private User user; 
	
	
	@Before 
	public void setUp() throws Exception	{
		
		
		sc1 = new ServiceClient()
				.setClientId(1)
				.setName("Habitat for Humanity")
				.setMainContact(new Contact()
						.setContactId(1)
						.setEmail("1800@nohelp.org")
						.setCity("Sherman"))
				.setOtherContact(new Contact()
						.setContactId(2)
						.setEmail("rbuckle@helpful.org")
						.setCity("Sherman"))
				.setCurrentBoardMember(null)
				.setCategory("HELP");
		
		sc2 = new ServiceClient()
				.setClientId(2)
				.setName("Sherman Animal Shelter")
				.setMainContact(new Contact()
						.setContactId(3)
						.setEmail("shermananimal@helpful.org")
						.setCity("Sherman"))
				.setOtherContact(new Contact()
						.setContactId(4)
						.setEmail("rbuckle@helpful.org")
						.setCity("Sherman"))
				.setCurrentBoardMember(null)
				.setCategory("NOHELP");
						
		user = new User()
				.setUid(1);
				
	
		et1 = new EventType()
		.setEtid(1)
		.setName("gds")
		.setDescription("great day of service for test")
		.setDefHours(0)
		.setDefClient(sc1)
		.setPinHours(false);
		
		et2 = new EventType()
		.setEtid(2)
		.setName("fws")
		.setDescription("first we serve for test")
		.setDefHours(1)
		.setDefClient(sc2)
		.setPinHours(false);

		e1 = new Event()
		.setEid(1)
		.setTitle("gds 2020")
		.setDate(new java.util.Date())
		.setAddress("900 N. Grand Ave")
		.setType(et1)
		.setContact(new Contact()
				.setContactId(1)
				.setEmail("1800@nohelp.org")
				.setCity("Sherman")
				);

		e2 = new Event()
		.setEid(2)
		.setTitle("fws 2020")
		.setDate(new java.util.Date())
		.setAddress("900 N. Grand Ave")
		.setType(et2)
		.setContact(null);

		sh1 = new ServiceHours()
			.setShid(1)
			.setServedPet(sc1)
			.setServant(user)
			.setEvent(e1)
			.setHours(2.0)
			.setStatus("Approved")
			.setReflection("test reflection")
			.setDescription(e1.getType().getDescription());

		sh2 = new ServiceHours()
			.setShid(2)
			.setServedPet(sc2)
			.setServant(user)
			.setEvent(e2)
			.setHours(3.5)
			.setStatus("Pending")
			.setReflection("test 2 reflection")
			.setDescription("test 2 description");
		
		MockitoAnnotations.initMocks(this);
	}
	
	
	
	/**
	 * Tests the listHours() method in ServiceHoursService. Should return dummy list. 
	 * 
	 * @throws Exception
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

		assertEquals(2, testList.size());
		
		Mockito.verify(dao).listAll();
		
	}
	
	/**
	 * Tests the typical case for the createServiceHour method. Should create 
	 * dummy event based on eventId when it's valid (greater than 0). 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateServiceHour_whenEventIdIsValid() throws Exception {
		
		/*
		 * Training mock dao to return sh1 regardless of params. 
		 */
		Mockito.when(dao.create(
				Mockito.anyInt(),
				Mockito.anyInt(), 
				Mockito.anyInt(),
				Mockito.anyDouble(),
				Mockito.any(String.class),
				Mockito.any(String.class),
				Mockito.any(String.class)
				)).thenReturn(sh1);
		
		/*
		 * Training mock objects 
		 */
		Mockito.when(eventService.eventById(1)).thenReturn(e1);
		Mockito.when(userUtil.currentUser()).thenReturn(user);
		
		
		ServiceHours ns = shs.createServiceHour(1);
		
		assertEquals(ns, sh1);
		
	
		
		/*
		 * Make sure dao was created with expected parameters. 
		 */
		
		Mockito.verify(dao).create(
				Mockito.anyInt(),
				Mockito.eq(1),
				Mockito.eq(1),
				Mockito.eq(0.0),
				Mockito.eq("Pending"),
				Mockito.eq("Type your reflection here."),
				Mockito.eq(et1.getDescription()));
		
		Mockito.verify(eventService).eventById(1);
		Mockito.verify(userUtil).currentUser();
		
		
	}
	
	/**
	 * Tests to make sure the service checks for a valid eventId. Throws
	 * an exception if the id is not valid. 
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_createServiceHour_whenIdIsNotValid() throws Exception {
		
		//expect an exception to complain about eventId
		exceptionRule.expect(Exception.class);
		exceptionRule.expectMessage("Invalid event id");
		
		// test
		ServiceHours s = shs.createServiceHour(-1);
		
	}
	
	/**
	 * Tests the updateHour method in ServiceHourService. 
	 * @throws Exception
	 */
	@Test
	public void test_updateHour() throws Exception {
		
		// change some value for sh1
		sh1.setHours(3.0);
		sh1.setReflection("reflection");
		
		// tell service to update the service hour
		// after setting event to e2
		ServiceHours s = shs.updateHour(sh1);
		
		// make assertions to make sure fields updated
		
		assertEquals(1, s.getShid());
		assertEquals(1, s.getServedPet().getScid());
		assertEquals(1, s.getServant().getUid());
		assertEquals(1, s.getEvent().getEid());
		assertEquals(3.0, s.getHours());
		assertEquals("Approved", s.getStatus());
		assertEquals("reflection", s.getReflection());
		assertEquals("great day of service for test", s.getDescription());
		
		//verify that the dao was involved
		
		Mockito.verify(dao).update(
				Mockito.eq(1),
				Mockito.eq(sh1.getEvent().getType().getDefClient().getScid()),
				Mockito.eq(sh1.getServant().getUid()), 
				Mockito.eq(sh1.getEvent().getEid()),
				Mockito.anyDouble(),
				Mockito.anyString(),
				Mockito.eq(sh1.getReflection()),
				Mockito.eq(sh1.getEvent().getType().getDescription()));
		
	}
	
	/**
	 * Tests the userHours method...fetches the current
	 * user's service hours.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_hoursById() throws Exception {
		
		ServiceHours sh1 = new ServiceHours().setShid(1).setHours(3.0).setServant(user);
		ServiceHours sh2 = new ServiceHours().setShid(2).setHours(2.0).setServant(user);
		
		List<ServiceHours> dummyList = new ArrayList<ServiceHours>();
		dummyList.add(sh1);
		dummyList.add(sh2);
		
		Mockito.when(dao.fetchHoursByUserId(1)).thenReturn(dummyList);
		
		List<ServiceHours> testList = shs.userHours(1);

		assertEquals(2, testList.size());
		
		Mockito.verify(dao).fetchHoursByUserId(1);
	}

}
