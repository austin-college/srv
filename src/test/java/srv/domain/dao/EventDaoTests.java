package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.event.EventDao;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
class EventDaoTests {

	@Autowired
	EventDao dao; 
	
	@Test
	void test() {
		fail("Not yet implemented");
	}

}
