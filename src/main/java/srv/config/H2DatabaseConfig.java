package srv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.event.JdbcTemplateEventDao;
import srv.domain.event.eventparticipant.JdbcTemplateEventParticipantDao;
import srv.domain.event.eventype.JdbcTemplateEventTypeDao;
import srv.domain.hours.JdbcTemplateServiceHoursDao;
import srv.domain.serviceclient.JdbcTemplateServiceClientDao;
import srv.domain.servicegroup.JdbcTemplateServiceGroupDao;
import srv.domain.user.JdbcTemplateUserDao;
import srv.services.BoardMemberHoursListService;
import srv.services.ServiceHoursService;

/**
 * This class is the home for runtime configuration for our database. It creates
 * new objects (beans) and remembers them (due to @Bean). Spring can then inject
 * them into other objects in our site that depend on these beans. An @Bean here
 * can be @Autowired as a dependant else where.
 * <p>
 * You can think of this as a mini-registry of important helper objects.
 * 
 * </p>
 * 
 * @author mahiggs
 *
 */
@Configuration
public class H2DatabaseConfig {

	
	/*
	 * DAO beans 
	 */
	

	@Bean
	public JdbcTemplateServiceClientDao serviceClientDao() {
		return new JdbcTemplateServiceClientDao();
	}

	
	@Bean
	public JdbcTemplateContactDao contactDao() {
		return new JdbcTemplateContactDao();
	}

	@Bean
	public JdbcTemplateUserDao userDao() {
		return new JdbcTemplateUserDao();
	}

	@Bean
	public JdbcTemplateEventDao eventDao() {
		return new JdbcTemplateEventDao();
	}
	
	@Bean
	public JdbcTemplateEventParticipantDao eventParticipantDao() {
		return new JdbcTemplateEventParticipantDao();
	}
	
	@Bean
	public JdbcTemplateEventTypeDao eventTypeDao() {
		return new JdbcTemplateEventTypeDao();
	}
	
	/*
	 * Added ServiceHourDao bean for autowired dependency 
	 */
	@Bean
	public JdbcTemplateServiceHoursDao serviceHoursDao() {
		
		return new JdbcTemplateServiceHoursDao();
	}
	
	/*
	 * Added ServiceHoursService Bean for autowired dependency in HoursController
	 */
	@Bean
	public ServiceHoursService serviceHoursService() {
		
		return new ServiceHoursService();
		
	}
	
	/*TODO Unsure if this Bean should be made*/
	@Bean
	public BoardMemberHoursListService bmHrListSrv() {
		return new BoardMemberHoursListService();
	}
	
	
	@Bean
	public JdbcTemplateServiceGroupDao serviceGroupDao() {
		return new JdbcTemplateServiceGroupDao();
	}

	
	

	


}
