package srv.domain.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import srv.domain.event.EventType;
import srv.domain.serviceGroup.ServiceGroup;
import srv.domain.serviceGroup.ServiceGroupDao;

@RunWith(SpringRunner.class)
@JdbcTest
@ComponentScan("srv.config")
public class ServiceGroupDaoTests {

	@Autowired
	ServiceGroupDao dao;
	
	/*
	 * Testing fetchServiceGroupById
	 * should return the ServiceGroup info for the Event with id i.
	 */
	@Test
	public void testFetchServiceGroupById_whenUsingJdbcTemplate() throws Exception{
		
		// Check existing ServieGroup in SQL 1~3
		ServiceGroup sg01 = dao.fetchServiceGroupById(1);
		
		assertEquals(1, sg01.getSgid());
		assertEquals("DummyName01", sg01.getShortName());
		assertEquals("DummyTitle01", sg01.getTitle());
		
		ServiceGroup sg02 = dao.fetchServiceGroupById(2);
		
		assertEquals(2, sg02.getSgid());
		assertEquals("DummyName02", sg02.getShortName());
		assertEquals("DummyTitle02", sg02.getTitle());
		
		ServiceGroup sg03 = dao.fetchServiceGroupById(3);
		assertEquals(3, sg03.getSgid());
		assertEquals("DummyName03", sg03.getShortName());
		assertEquals("DummyTitle03", sg03.getTitle());
	}
	
	/*
	 * Testing listAll(), should return the current 3 ServiceGroups
	 * that are in the data.sql database.
	 */
	@Test
	public void testListAll_whenUsingJdbcTemplate() throws Exception {

		List<ServiceGroup> sgs = dao.listAll();

		assertEquals(1, sgs.get(0).getSgid());
		assertEquals("DummyName01", sgs.get(0).getShortName());
		assertEquals("DummyTitle01", sgs.get(0).getTitle());
		
		assertEquals(2, sgs.get(1).getSgid());
		assertEquals("DummyName02", sgs.get(1).getShortName());
		assertEquals("DummyTitle02", sgs.get(1).getTitle());
		
		assertEquals(3, sgs.get(2).getSgid());
		assertEquals("DummyName03", sgs.get(2).getShortName());
		assertEquals("DummyTitle03", sgs.get(2).getTitle());
	}
	
	/* Testing the create(), should create a new ServiceGroup query 
	 * in the data.sql database.
	 */
	@Test
	public void testCreate_whenUsingJdbcTemplate() throws Exception{
		
		List<ServiceGroup> preCreate = dao.listAll();
		int numBeforeInsert = preCreate.size();
		System.err.println("\n\nBefore Insert " + numBeforeInsert);
		for(ServiceGroup sg : preCreate) {
			System.err.println(sg.getShortName());
		}
		
		ServiceGroup newSG = dao.create("DummyName04", "DummyTitle04", 4);
		
		assertNotNull(newSG);
		
		List<ServiceGroup> postCreate = dao.listAll();
		int numAfterInsert = postCreate.size();
		
		System.err.println("\n\nAfter Insert " + numAfterInsert);
		for(ServiceGroup sg : postCreate) {
			System.err.println(sg.getShortName());
		}
		
		// The next assigned id on successful insert should be numBeforeInsert + 1.
		assertEquals(numBeforeInsert+1, newSG.getSgid());
		
		// Checking the newly inserted record.
		ServiceGroup sg04 = newSG;
		
		assertEquals(4, sg04.getSgid());
		assertEquals("DummyName04", sg04.getShortName());
		assertEquals("DummyTitle04", sg04.getTitle());
	}
	
	/*
	 *  Testing the delete(), should remove the query with the specified ID (first one in this case). 
	 *  Should still be one query left in the database.
	 */
	@Test
	public void testDelete_whenUsingJdbcTemplate() throws Exception{
		
		dao.delete(1);
		
		List<ServiceGroup> sgs = dao.listAll();
		
		// Original Size, 3, minus 1 since we deleted = 2
		assertEquals(2, sgs.size());
		
		ServiceGroup sg01 = sgs.get(0);
		
		// SQL input for ServiceGroup id 2
		assertEquals("DummyName02", sg01.getShortName());
		assertEquals("DummyTitle02", sg01.getTitle());
	}
	
	/*
	 * Testing the update(), should update the query with the specified ID.
	 */
	@Test
	public void testUpdate_whenUsingJdbcTemplate() throws Exception{
		
		dao.update(1, "COOLShortName", "COOLTitleName", 1);
		
		ServiceGroup sg = dao.fetchServiceGroupById(1);
		
		assertEquals("COOLShortName", sg.getShortName());
		assertEquals("COOLTitleName", sg.getTitle());
	}
}
