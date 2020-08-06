package srv.services;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import srv.utils.SemesterUtil;
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

	@Mock
	private SemesterUtil semUtil; 

	
	@InjectMocks
	private ServiceHoursService shs; 
	
	@Rule 
	public ExpectedException exceptionRule = ExpectedException.none();
	
	/*
	 * Test Fixture Handles...see before/setup method 
	 */
	private ServiceHours sh1; 
	private ServiceHours sh2; 
	private ServiceHours sh3; 
	private ServiceHours sh4; 
	
	private EventType et1;
	private EventType et2;

	private Event e1;
	private Event e2;
	private Event e3;
	private Event e4; 

	private ServiceClient sc1; 
	private ServiceClient sc2; 
	
	private User user;
	private ServiceClient sc3;
	private EventType et3;
	
	
	@Before 
	public void setUp() throws Exception	{
		
		
		sc1 = new ServiceClient()
				.setScid(1)
				.setName("Habitat for Humanity")
				.setMainContact(new Contact()
						.setContactId(1)
						.setEmail("1800@nohelp.org")
						.setCity("Sherman"))
				.setCurrentBoardMember(null)
				.setCategory("HELP");
		
		sc2 = new ServiceClient()
				.setScid(2)
				.setName("Sherman Animal Shelter")
				.setMainContact(new Contact()
						.setContactId(3)
						.setEmail("shermananimal@helpful.org")
						.setCity("Sherman"))
				.setCurrentBoardMember(null)
				.setCategory("NOHELP");
						
		sc3 = new ServiceClient()
				.setScid(3)
				.setName("yaml")
				.setMainContact(new Contact()
						.setContactId(3)
						.setEmail("yaml@helpful.org")
						.setCity("Sherman"))

				.setCurrentBoardMember(null)
				.setCategory("NOHELP");
		
		user = new User()
				.setUid(1);
				
	
		et1 = new EventType()
		.setEtid(1)
		.setName("gds")
		.setDescription("great day of service for test")
		.setDefHours(0.0)
		.setDefClient(sc1)
		.setPinHours(false);
		
		et2 = new EventType()
		.setEtid(2)
		.setName("fws")
		.setDescription("first we serve for test")
		.setDefHours(1.0)
		.setDefClient(sc2)
		.setPinHours(false);

		et3 = new EventType()
		.setEtid(3)
		.setName("test et")
		.setDescription("only for testing")
		.setDefHours(1.0)
		.setDefClient(sc3)
		.setPinHours(false);
		
		e1 = new Event()
		.setEid(1)
		.setTitle("gds 2020")
		.setDate(new SimpleDateFormat("MM/dd/yyyy").parse("05/01/2020"))
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
		.setDate(new SimpleDateFormat("MM/dd/yyyy").parse("05/05/2020"))
		.setAddress("900 N. Grand Ave")
		.setType(et2)
		.setContact(null);


		e3 = new Event()
		.setEid(3)
		.setTitle("really old event")
		.setDate(new SimpleDateFormat("MM/dd/yyyy").parse("11/2/1998"))
		.setAddress("900 N. Grand Ave")
		.setType(et3)
		.setContact(null);

		e4 = new Event()
		.setEid(4)
		.setTitle("old event")
		.setDate(new SimpleDateFormat("MM/dd/yyyy").parse("12/5/2000"))
		.setAddress("900 N. Grand Ave")
		.setType(et3)
		.setContact(null);
		
		
		sh1 = new ServiceHours()
			.setShid(1)
			.setServedPet(sc1)
			.setServant(user)
			.setEvent(e1)
			.setHours(2.0)
			.setStatus("Approved")
			.setReflection("test reflection")
			.setDate(e1.getDate())
			.setDescription(e1.getType().getDescription())
			.setContactName("Billy Joe")
			.setContactContact("111-222-3333 dummyEmail@gmail.com")
			;
		

		sh2 = new ServiceHours()
			.setShid(2)
			.setServedPet(sc2)
			.setServant(user)
			.setEvent(e2)
			.setDate(e2.getDate())
			.setHours(3.5)
			.setStatus("Pending")
			.setReflection("test 2 reflection")
			.setDescription("test 2 description")
			.setFeedback("")
			.setContactName("Rusty Buckle")
			.setContactContact("222-333-4444")
			;
		
		
		sh3 = new ServiceHours()
			.setShid(3)
			.setServedPet(sc3)
			.setServant(user)
			.setEvent(e3)
			.setDate(e3.getDate())
			.setHours(2.0)
			.setStatus("Approved")
			.setReflection("test reflection")
			.setDescription(e1.getType().getDescription())
			.setContactName("Rita Jones")
			.setContactContact("rJones@yahoo.com")			
			;

		sh4 = new ServiceHours()
			.setShid(4)
			.setServedPet(sc3)
			.setServant(user)
			.setEvent(e4)
			.setDate(e4.getDate())
			.setHours(3.5)
			.setStatus("Pending")
			.setReflection("test 2 reflection")
			.setDescription("test 2 description")
			.setContactName("Lucy")
			.setContactContact("444-555-6666")
			;
		
		
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
				Mockito.any(String.class),
				Mockito.any(String.class),
				Mockito.any(String.class)
				)).thenReturn(sh1);
		
		/*
		 * Training mock objects 
		 */
		Mockito.when(userUtil.currentUser()).thenReturn(user);
		
		// param are Integer service client id, Integer event id, Double hours, String reflection, String description, String contact name and contact's email
		ServiceHours ns = shs.createServiceHour(1, 1, 4.0, "Was fun.", "a description", "Bruce Lee", "bLee@gmail.com");
		
		assertEquals(ns, sh1);
		
		/*
		 * Make sure dao was created with expected parameters. 
		 */
		Mockito.verify(dao).create(
				Mockito.eq(1),
				Mockito.eq(1),
				Mockito.eq(1),
				Mockito.eq(4.0),
				Mockito.eq("Pending"),
				Mockito.eq("Was fun."),
				Mockito.eq("a description"),
				Mockito.eq("Bruce Lee"),
				Mockito.eq("bLee@gmail.com"));
		
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
		
		// param are Integer service client id, Integer event id, Double hours, String reflection, String description, contact name and contact's phone number
		ServiceHours s = shs.createServiceHour(
				-1, 
				-1, 
				Mockito.anyDouble(),
				Mockito.any(String.class),
				Mockito.any(String.class),
				Mockito.any(String.class),
				Mockito.any(String.class));
		
	}
	
	/**
	 * Tests the updateHour method in ServiceHourService. 
	 * @throws Exception
	 */
	@Test
	public void test_updateHour() throws Exception {
		
		/*
		 * Training mock objects 
		 */
		Mockito.when(userUtil.currentUser()).thenReturn(user);
		Mockito.doNothing().when(dao).update(1, 1, 1, 1, 2.5, "Pending", "a reflection", "a description", "Ron Steward", "800-777-9090");

		// tell service to update the service hour
		// after setting event to e2
		// param are Integer service hour id, Integer service client id, Integer event id, Double hours, String reflection, String description
		shs.updateHour(1, 1, 1, 2.5, "a reflection", "a description", "Ron Steward", "800-777-9090");
		
		// make assertions to make sure fields updated 
		//TODO
//		
//		assertEquals(1, sh1.getShid());
//		assertEquals(1, sh1.getServedPet().getScid());
//		assertEquals(1, sh1.getServant().getUid());
//		assertEquals(1, sh1.getEvent().getEid());
//		assertEquals(2.5, sh1.getHours());
//		assertEquals("Pending", sh1.getStatus());
//		assertEquals("a reflection", sh1.getReflection());
//		assertEquals("a description", sh1.getDescription());
		
		//verify that the dao was involved
		
		Mockito.verify(dao).update(
				Mockito.eq(1),

				Mockito.eq(1),
				Mockito.eq(1), 
				Mockito.eq(1),
				Mockito.eq(2.5),
				Mockito.eq("Pending"),
				Mockito.eq("a reflection"),
				Mockito.eq("a description"),
				Mockito.eq("Ron Steward"), 
				Mockito.eq("800-777-9090")
				);

		
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
	
	/**
	 * Initial test for filteredHours. Should delegate to the dao and
	 * return whatever the dao provided.
	 */
	@Test
	public void test_filter_noFilters() throws Exception {
		
		List<ServiceHours> list = new ArrayList<ServiceHours>();
		list.add(sh1);	list.add(sh2);
		
		Mockito.when(dao.listByFilter(null, null, null, null, null)).thenReturn(list);
		
		List<ServiceHours> newList = shs.filteredHours(null, null, null, null, null);
		assertEquals(2, newList.size());
		
		Mockito.verify(dao).listByFilter(null, null, null, null, null);
	}
	/**
	 * Test to make sure that the service returns a list of hours based on a
	 * valid service client/sponsor id.
	 */
	@Test
	public void test_filter_byServiceClient_whenIdValid() throws Exception {
		
		List<ServiceHours> list = new ArrayList<ServiceHours>();
		list.add(sh2);
		
		Mockito.when(dao.listByFilter(null, 2, null, null, null)).thenReturn(list);
		
		List<ServiceHours> newList = shs.filteredHours(null, 2, null, null, null);
	
		assertSame(list,newList);
		
		assertEquals(1, newList.size());
		assertEquals(sh2.getShid().intValue(), newList.get(0).getShid().intValue());
		
		Mockito.verify(dao).listByFilter(null, 2, null, null, null);
	}
	
	/**
	 * Test to make sure that the service checks for valid id for service
	 * client/sponsor and throws exception when not valid.
	 */
	@Test(expected=Exception.class)
	public void test_filter_byServiceClient_whenIdInvalid() throws Exception {
		
		shs.filteredHours(null, -1, null, null, null);
	}
	
	/**
	 * Test to make sure that the service returns a list of hours based
	 * on a valid user id.
	 */
	@Test
	public void test_filter_byUser_whenIdValid() throws Exception {
		
		List<ServiceHours> list = new ArrayList<ServiceHours>();
		list.add(sh1);
		
		Mockito.when(dao.listByFilter(1, null, null, null, null)).thenReturn(list);
		
		List<ServiceHours> newList = shs.filteredHours(1, null, null, null, null);
	
		assertSame(list,newList);
		assertEquals(1, newList.size());
		assertEquals(sh1.getShid(), newList.get(0).getShid());
		
		Mockito.verify(dao).listByFilter(1, null, null, null, null);
		
	}
	
	/**
	 * Test to make sure that the service checks for valid id for
	 * user and throws an exception when not valid.
	 */
	@Test(expected=Exception.class)
	public void test_filter_byUser_whenIdInvalid() throws Exception {
		
		shs.filteredHours(-1, null, null, null, null);
	}
	
	/** 
	 * Test to make sure that the service returns a list of hours based
	 * on a valid month name.
	 */
	@Test
	public void test_filter_byMonth_whenNameValid() throws Exception {
		
		// Creating new Date object
		String sDate = "2020-06-12 00:00:00";
		String pattern = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = sdf.parse(sDate);
		
		// to avoid future tests failing, sets the date.
		sh1.setDate(date);	sh2.setDate(date);
		
		List<ServiceHours> list = new ArrayList<ServiceHours>();
		list.add(sh1);	list.add(sh2);
		
		Mockito.when(dao.listByFilter(null, null, "June", null, null)).thenReturn(list);
		
		List<ServiceHours> newList = shs.filteredHours(null, null, "June", null, null);
		assertEquals(2, newList.size());
		
		Mockito.verify(dao).listByFilter(null, null, "June", null, null);
	}
	
	/** 
	 * Test to make sure that the service returns a list of hours based
	 * on an invalid month name.
	 */
	@Test(expected=Exception.class)
	public void test_filter_byMonth_whenNameInvalid() throws Exception {
		
		shs.filteredHours(null, null, "", null, null);
	}
	
	/** 
	 * Test to make sure that the service returns a list of hours based
	 * on when 'List All'. Should return the entire list.
	 */
	@Test
	public void test_filter_byMonth_whenNameListAll() throws Exception {
			
		List<ServiceHours> list = new ArrayList<ServiceHours>();
		list.add(sh1);	list.add(sh2);
		
		Mockito.when(dao.listByFilter(null, null, null, null, null)).thenReturn(list);
		
		List<ServiceHours> newList = shs.filteredHours(null, null, "List All", null, null);
		assertEquals(2, newList.size());
		
		Mockito.verify(dao).listByFilter(null, null, null, null, null);
	}
	
	/** 
	 * Test to make sure that the service returns a list of hours based
	 * on a valid status.
	 */
	@Test
	public void test_filter_byStatus_whenStatusValid() throws Exception {
		
		List<ServiceHours> list = new ArrayList<ServiceHours>();
		list.add(sh1);	
		
		Mockito.when(dao.listByFilter(null, null, null, "Approved", null)).thenReturn(list);
		
		List<ServiceHours> newList = shs.filteredHours(null, null, null, "Approved", null);
		assertEquals(1, newList.size());
		
		Mockito.verify(dao).listByFilter(null, null, null, "Approved", null);
	}
	
	
	/**
	 * Test our helper algorithm for filtering down to just 
	 * approved hours.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_approvedHours_whenAllApproved() throws Exception {
		
		Mockito.when(semUtil.currentSemester()).thenReturn("2020SP");
		
		sh1.setStatus(ServiceHours.STATUS_APPROVED);
		sh2.setStatus(ServiceHours.STATUS_APPROVED);
		sh3.setStatus(ServiceHours.STATUS_APPROVED);
		
		sh4.setStatus(ServiceHours.STATUS_APPROVED);
		
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		hours.add(sh1);  // should match
		hours.add(sh2);  // should match
		hours.add(sh3);  // should not match due to semester id
		hours.add(sh4);  // should not match due to rejection status
		
		List<ServiceHours> results = shs.approvedHours(hours);
		
		// make sure all hours are present
		assertEquals(4, results.size());
		for (ServiceHours h : hours) {
			assertTrue(results.contains(h));
			results.remove(h);
		}
		
		// and no more 
		assertTrue(results.isEmpty());
		
	}
	
	@Test
	public void test_approvedHours_whenNoneApproved() throws Exception {
		
		Mockito.when(semUtil.currentSemester()).thenReturn("2020SP");
		
		sh1.setStatus(ServiceHours.STATUS_REJECTED);
		sh2.setStatus(ServiceHours.STATUS_REJECTED);
		sh3.setStatus(ServiceHours.STATUS_PENDING);
		
		sh4.setStatus(ServiceHours.STATUS_REJECTED);
		
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		hours.add(sh1);  // should match
		hours.add(sh2);  // should match
		hours.add(sh3);  // should not match due to semester id
		hours.add(sh4);  // should not match due to rejection status
		
		List<ServiceHours> results = shs.approvedHours(hours);

		// should be an empty list
		assertTrue(results.isEmpty());
		
	}

	
	@Test
	public void test_approvedHours_whenSomeApproved() throws Exception {
		
		Mockito.when(semUtil.currentSemester()).thenReturn("2020SP");
		
		sh1.setStatus(ServiceHours.STATUS_APPROVED);
		sh2.setStatus(ServiceHours.STATUS_REJECTED);
		sh3.setStatus(ServiceHours.STATUS_APPROVED);
		
		sh4.setStatus(ServiceHours.STATUS_PENDING);
		
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		hours.add(sh1);  // should match
		hours.add(sh2);  // should match
		hours.add(sh3);  // should not match due to semester id
		hours.add(sh4);  // should not match due to rejection status
		
		List<ServiceHours> results = shs.approvedHours(hours);
		
		// make sure all approved hours are present  sh1, sh3 only.
		assertEquals(2, results.size());
		assertTrue(results.contains(sh1));
		assertTrue(results.contains(sh3));

		results.remove(sh1);
		results.remove(sh3);
		
		// and no more 
		assertTrue(results.isEmpty());
		
	}
	
	@Test
	public void test_totalSemesterHours() throws Exception {
		
		Mockito.when(semUtil.currentSemester()).thenReturn("2020SP");
		
		sh1.setStatus(ServiceHours.STATUS_APPROVED);
		sh2.setStatus(ServiceHours.STATUS_APPROVED);
		sh3.setStatus(ServiceHours.STATUS_APPROVED);
		
		sh4.setStatus(ServiceHours.STATUS_REJECTED);
		
		Mockito.when(semUtil.semesterID(Mockito.any(Date.class)))
			.thenReturn("2020SP")
			.thenReturn("2020SP")
			.thenReturn("2020FA")
			.thenReturn("2020SP");
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		hours.add(sh1);  // should match
		hours.add(sh2);  // should match
		hours.add(sh3);  // should not match due to semester id
		hours.add(sh4);  // should not match due to rejection status
		
		
		assertEquals(5.5, shs.totalSemesterHours(hours),0.000001);
		
	}
	
	
	@Test
	public void test_totalSemesterHours_NoneApproved() throws Exception {
		
		Mockito.when(semUtil.currentSemester()).thenReturn("2020SP");
		
		sh1.setStatus(ServiceHours.STATUS_REJECTED);
		sh2.setStatus(ServiceHours.STATUS_REJECTED);
		sh3.setStatus(ServiceHours.STATUS_REJECTED);
		
		sh4.setStatus(ServiceHours.STATUS_REJECTED);
		
		Mockito.when(semUtil.semesterID(Mockito.any(Date.class)))
			.thenReturn("2020SP")
			.thenReturn("2020SP")
			.thenReturn("2020FA")
			.thenReturn("2020SP");
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		hours.add(sh1);  // should match
		hours.add(sh2);  // should match
		hours.add(sh3);  // should not match due to semester id
		hours.add(sh4);  // should not match due to rejection status
		
		
		assertEquals(0.0, shs.totalSemesterHours(hours),0.000001);
		
	}
	
	
	@Test
	public void test_totalAcadYearHours_AllApproved() throws Exception {
		
		Mockito.when(semUtil.currentAcadYear()).thenReturn("AY2020/2021");
		
		sh1.setStatus(ServiceHours.STATUS_APPROVED);
		sh2.setStatus(ServiceHours.STATUS_APPROVED);
		sh3.setStatus(ServiceHours.STATUS_APPROVED);
		
		sh4.setStatus(ServiceHours.STATUS_APPROVED);
		
		Mockito.when(semUtil.acadYear(Mockito.any(Date.class)))
			.thenReturn("AY2020/2021")
			.thenReturn("AY2020/2021")
			.thenReturn("AY2020/2021")
			.thenReturn("XXXXXX");
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		hours.add(sh1);  // should match (2.0)
		hours.add(sh2);  // should match (3.5)
		hours.add(sh3);  // should match (2.0)
		hours.add(sh4);  // should not match due to acad year  (3.5)
		
		assertEquals(7.5, shs.totalAcademicYearHours(hours),0.000001);
		
	}
	
	
	@Test
	public void test_totalSponsorCount_WithDuplicates() throws Exception {
		
				
		sh1.setStatus(ServiceHours.STATUS_APPROVED);
		sh2.setStatus(ServiceHours.STATUS_APPROVED);
		sh3.setStatus(ServiceHours.STATUS_APPROVED);
		sh4.setStatus(ServiceHours.STATUS_APPROVED);
		
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		hours.add(sh1);  // should match scid=1
		hours.add(sh2);  // should match scid=2
		hours.add(sh3);  // should not scid=3
		hours.add(sh4);  // should match scid=3; duplicate; not counted
		
		assertEquals(3, shs.totalSponsorsCount(hours));
		
	}
	
	
	/**
	 * Tests average per month when a couple of the hours are
	 * in the same month.
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_averageHoursPerMonth() throws Exception {
		
				
		sh1.setStatus(ServiceHours.STATUS_APPROVED);
		sh2.setStatus(ServiceHours.STATUS_APPROVED);
		sh3.setStatus(ServiceHours.STATUS_APPROVED);
		sh4.setStatus(ServiceHours.STATUS_APPROVED);
		
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		hours.add(sh1); // 2.0 on 2020-05
		hours.add(sh2); // 3.5 on 2020-05 
		hours.add(sh3); // 2.0 on 1998-11
		hours.add(sh4);  // 3.5 on 2000-12 
		
		// 5.5 + 2.0 + 3.5 / 3 == 3.66666666
		
		assertEquals(3.666666, shs.averageHoursPerMonth(hours), 0.00001);
		
		
	}
	
	
	/**
	 * Make sure we handle the empty list case.
	 * @throws Exception
	 */
	@Test
	public void test_averageHoursPerMonth_whenEmpty() throws Exception {
		
				
		sh1.setStatus(ServiceHours.STATUS_APPROVED);
		sh2.setStatus(ServiceHours.STATUS_APPROVED);
		sh3.setStatus(ServiceHours.STATUS_APPROVED);
		sh4.setStatus(ServiceHours.STATUS_APPROVED);
		
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		
		
		assertEquals(0.0, shs.averageHoursPerMonth(hours), 0.00001);
		
		
	}
	
	/**
	 * Strictly not needed, but what if all hours are in the same
	 * month?
	 * 
	 * @throws Exception
	 */
	@Test
	public void test_averageHoursPerMonth_whenAllInSameMonth() throws Exception {
		
				
		sh1.setStatus(ServiceHours.STATUS_APPROVED);
		sh2.setStatus(ServiceHours.STATUS_APPROVED);
		sh3.setStatus(ServiceHours.STATUS_APPROVED);
		sh4.setStatus(ServiceHours.STATUS_APPROVED);
		
		sh3.setDate(sh1.getDate());
		sh4.setDate(sh2.getDate());  // now should all be in 2020-05
		
		List<ServiceHours> hours = new ArrayList<ServiceHours>();
		hours.add(sh1); // 2.0 on 2020-05
		hours.add(sh2); // 3.5 on 2020-05 
		hours.add(sh3); // 2.0 on 2020-05 
		hours.add(sh4);  // 3.5 on 2020-05  
		
		assertEquals(11.0, shs.averageHoursPerMonth(hours), 0.00001);
		
		
	}
	

	/** 
	 * Test to make sure that the service returns a list of hours based
	 * on an invalid status.
	 */
	@Test(expected=Exception.class)
	public void test_filter_byStatus_whenStatusInvalid() throws Exception {
		
		shs.filteredHours(null, null, null, "", null);
	}
	
	/** 
	 * Test to make sure that the service returns a list of hours based
	 * on when 'List All'. Should return the entire list.
	 */
	@Test
	public void test_filter_byStatus_whenStatusListAll() throws Exception {
			
		List<ServiceHours> list = new ArrayList<ServiceHours>();
		list.add(sh1);	list.add(sh2);
		
		Mockito.when(dao.listByFilter(null, null, null, null, null)).thenReturn(list);
		
		List<ServiceHours> newList = shs.filteredHours(null, null, null, "List All", null);
		assertEquals(2, newList.size());
		
		Mockito.verify(dao).listByFilter(null, null, null, null, null);
	}
	
	/** 
	 * Test to make sure that the service returns a list of hours based
	 * on a valid year.
	 */
	@Test
	public void test_filter_byYear_whenYearValid() throws Exception {
		
		// Creating new Date object
		String sDate = "2020-06-12 00:00:00";
		String pattern = "yyyy-MM-dd HH:mm:ss";

		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		Date date = sdf.parse(sDate);
		
		// to avoid future tests failing, sets the date.
		sh1.setDate(date);	sh2.setDate(date);
		
		List<ServiceHours> list = new ArrayList<ServiceHours>();
		list.add(sh1);	list.add(sh2);
		
		Mockito.when(dao.listByFilter(null, null, null, null, "2020")).thenReturn(list);
		
		List<ServiceHours> newList = shs.filteredHours(null, null, null, null, "2020");
		assertEquals(2, newList.size());
		
		Mockito.verify(dao).listByFilter(null, null, null, null, "2020");
	}
	
	/** 
	 * Test to make sure that the service returns a list of hours based
	 * on an invalid year.
	 */
	@Test(expected=Exception.class)
	public void test_filter_byYear_whenYearInvalid() throws Exception {
		
		shs.filteredHours(null, null, null, null, "");
	}
	
	/** 
	 * Test to make sure that the service returns a list of hours based
	 * on when 'List All'. Should return the entire list.
	 */
	@Test
	public void test_filter_byYear_whenYearListAll() throws Exception {
			
		List<ServiceHours> list = new ArrayList<ServiceHours>();
		list.add(sh1);	list.add(sh2);
		
		Mockito.when(dao.listByFilter(null, null, null, null, null)).thenReturn(list);
		
		List<ServiceHours> newList = shs.filteredHours(null, null, null, null, "List All");
		assertEquals(2, newList.size());
		
		Mockito.verify(dao).listByFilter(null, null, null, null, null);
	}
	
	/**
	 * Test to make sure that the service returns a list of hours when
	 * all the parameters are valid. Should return an empty list since no
	 * hours satisfy all the parameters.
	 */
	@Test
	public void test_filter_byAllParam() throws Exception {
		
		List <ServiceHours> dummyList = new ArrayList<ServiceHours>();
		
		Mockito.when(dao.listByFilter(1, 2, "May", "Approved", "2020")).thenReturn(dummyList);
		
		List<ServiceHours> newList = shs.filteredHours(1, 2, "May", "Approved", "2020");
		assertEquals(0, newList.size());
		
		Mockito.verify(dao).listByFilter(1, 2, "May", "Approved", "2020");
	}
	
	/** 
	 * Test to make sure that service returns an updated service hour with 
	 * status change and feedback.
	 */
	@Test
	public void test_changeStatus() throws Exception {
		
		// updated values
		String newStatus = "Approved";
		String feedbackMsg = "Good job";
		int shid = 2;
		
		// before the change
		assertEquals("Pending", sh2.getStatus());
		assertEquals("", sh2.getFeedback());
		
		// change it for our mock
		sh2.setStatus(newStatus)
			.setFeedback(feedbackMsg);
		
		// Mock dependencies
		Mockito.doNothing().when(dao).changeHourStatusWithFeedback(shid, newStatus, feedbackMsg);
		Mockito.when(dao.fetchHoursById(shid)).thenReturn(sh2);
		ServiceHours updatedHr = shs.changeStatus(shid, newStatus, feedbackMsg);

		assertEquals(newStatus, updatedHr.getStatus());
		assertEquals(feedbackMsg, updatedHr.getFeedback());
		assertEquals(2, updatedHr.getShid());
		
		// verify that the dao got involved
		Mockito.verify(dao).changeHourStatusWithFeedback(shid, newStatus, feedbackMsg);
	}
	
	@Test
	public void test_get_ServiceClients_wating_on_BoardMember() throws Exception {
		int i = dao.getServiceHoursWaitingOnSignedInBoardMember(1);
		System.out.println(i);
		
	}
	
}
