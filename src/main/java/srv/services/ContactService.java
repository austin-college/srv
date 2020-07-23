package srv.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import freemarker.template.utility.StringUtil;
import srv.domain.contact.Contact;
import srv.domain.contact.ContactDao;

@Service
public class ContactService {
	
	@Autowired ContactDao dao;
	
	private static Logger log = LoggerFactory.getLogger(ContactService.class);

	/**
	 * Returns  the specified contact object from our back end data base or 
	 * throws exception if id is not valid or not found.
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public Contact contactById(Integer id) throws Exception {

		if (id <= 0) {
			throw new Exception(String.format("Invalid contact id [%d]", id));
		}
		
		return dao.fetchContactById(id);
		
	}

	public void updateContact(Integer id, String fname, String lname, String pphone, String sphone, String email,
			String street, String city, String state, String zip) throws Exception {

		dao.update(id, StringUtil.emptyToNull(fname), 
				StringUtil.emptyToNull(lname), 
				StringUtil.emptyToNull(email), 
				StringUtil.emptyToNull(pphone), 
				StringUtil.emptyToNull(sphone), 
				StringUtil.emptyToNull(street), 
				StringUtil.emptyToNull(city), 
				StringUtil.emptyToNull(state), 
				StringUtil.emptyToNull(zip));
		
	}

	/**
	 * @return a list of all contacts known to system...unfiltered.
	 * @throws Exception
	 */
	public  List<Contact> allContacts() throws Exception {

		return dao.listAll();
	}
	
	
	

}
