package srv.services;

import static org.junit.Assert.*;
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
		
		MockitoAnnotations.initMocks(this);
		
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
		.setDefHours(0)
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
			.setDescription("test description");

		sh2 = new ServiceHours()
			.setShid(2)
			.setServedPet(sc2)
			.setServant(user)
			.setEvent(e2)
			.setHours(3.5)
			.setStatus("Pending")
			.setReflection("test 2 reflection")
			.setDescription("test 2 description");
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
		
		Mockito.verify(dao.create(
				Mockito.any(Integer.class),
				Mockito.any(Integer.class),
				Mockito.any(Integer.class),
				Mockito.anyDouble(),
				Mockito.eq("Pending"),
				Mockito.eq("Type your reflection here"),
				Mockito.any(String.class)));
		
	//	Mockito.verify(dao).create(
	//			null, null, null, null, null, null, null);
		
		
		/*
		 * Mockito.refEq(new ServiceClient() .getScid()), Mockito.refEq(new User()
		 * .getUid()), Mockito.refEq(new Event() .getEid()), Mockito.eq(0.0),
		 * Mockito.eq("Pending"), Mockito.eq(""), Mockito.eq(""));
		 */
	}

}
