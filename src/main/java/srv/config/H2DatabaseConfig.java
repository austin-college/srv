package srv.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import srv.domain.contact.JdbcTemplateContactDao;
import srv.domain.event.JdbcTemplateEventDao;
import srv.domain.event.JdbcTemplateEventTypeDao;
import srv.domain.event.eventParticipant.JdbcTemplateEventParticipantDao;
import srv.domain.reason.JdbcTemplateReasonDao;
import srv.domain.serviceClient.JdbcTemplateServiceClientDao;
//<<<<<<< HEAD
import srv.domain.serviceHours.JdbcTemplateServiceHoursDao;
//=======
import srv.domain.serviceGroup.JdbcTemplateServiceGroupDao;
//>>>>>>> 377854a68d82e5e1026c3807c225b8f07ad3181e
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

	@Bean
	public JdbcTemplateServiceClientDao serviceClientDao() {
		return new JdbcTemplateServiceClientDao();
	}

	@Bean
	public JdbcTemplateReasonDao reasonDao() {
		return new JdbcTemplateReasonDao();
	}

	@Bean
	public JdbcTemplateContactDao contactDao() {
		return new JdbcTemplateContactDao();
	}

	@Bean
	public JdbcTemplateUserDao UserDao() {
		return new JdbcTemplateUserDao();
	}

	@Bean
	public JdbcTemplateEventDao EventDao() {
		return new JdbcTemplateEventDao();
	}
	
	@Bean
	public JdbcTemplateEventParticipantDao EventParticipantDao() {
		return new JdbcTemplateEventParticipantDao();
	}
	
	@Bean
	public JdbcTemplateEventTypeDao EventTypeDao() {
		return new JdbcTemplateEventTypeDao();
	}
	
//<<<<<<< HEAD
	/*
	 * Added ServiceHourDao bean for autowired dependency 
	 */
	@Bean
	public JdbcTemplateServiceHoursDao ServiceHoursDao() {
		
		return new JdbcTemplateServiceHoursDao();
	}
	
	/*
	 * Added ServiceHoursService Bean for autowired dependency in HoursController
	 */
	@Bean
	public ServiceHoursService serviceHoursService() {
		
		return new ServiceHoursService();
		
	}
	
	/*
	 * Add BoardMemberHoursListService Bean for autowired dependency in BoardMemberController
	 */
	@Bean
	public BoardMemberHoursListService bmHoursListService() {
		
		return new BoardMemberHoursListService();
		
	}
//=======
//	@Bean
//	public JdbcTemplateServiceGroupDao ServiceGroupDao() {
//		return new JdbcTemplateServiceGroupDao();
//>>>>>>> 377854a68d82e5e1026c3807c225b8f07ad3181e
//	}

//    @Bean
//    public DataSource dataSource() {
//    
//        return new EmbeddedDatabaseBuilder()
//                .setType(EmbeddedDatabaseType.H2)
//                .addScript("schema.sql")//script to create person table
//                //.addScript("data.sql")//script to create person table
//                .build();
//    }

}
