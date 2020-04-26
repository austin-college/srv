package srv.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import srv.domain.serviceHours.ServiceHours;
import srv.domain.serviceHours.ServiceHoursDao;
import srv.domain.contact.Contact;
import srv.domain.event.Event;
import srv.domain.serviceClient.ServiceClient;
import srv.domain.serviceClient.ServiceClientDao;
import srv.domain.user.ServantUser;
import srv.domain.user.User;
import srv.domain.user.UserDao;
/*Need to check above imports and make sure they are all useful or removed. (Previously HardCoded)*/
/**
 * The methods in this class are just a temporary stand in until the ServiceHourDao is completed.
 */
public class BoardMemberHoursListService {

	@Autowired 
	UserDao userDao;
	ServiceHoursDao serviceDao;
	ServiceClientDao clientDao;
	
	public List<ServiceHours> hrs = new ArrayList<ServiceHours>();
	
	
	public BoardMemberHoursListService() {
		initialize();
	}
//TODO Make Live with finished ServiceHoursdao
	public void initialize() {
		//	public ServantUser(Integer uid, String userID,Contact contactInfo, char classification) {
		/*HardCoded users to be replaced*/
		Contact blankContact = new Contact();
		Contact clientContact = new Contact();
		clientContact.setEmail("fakeEmail@temp.net");
		ServantUser srv1 = new ServantUser(1,"602232", blankContact, 's');
		ServantUser srv2 = new ServantUser(2,"618826",blankContact, 's');
		ServantUser srv3 = new ServantUser(3,"652286",blankContact, 's');
		ServantUser srv4 = new ServantUser(4,"696632", blankContact, 's');
		/*HardCoded pets to be replaced*/
		ServiceClient cl1 = new ServiceClient(1,"Billy Bob Thornton",clientContact,blankContact,"Sameeha","CatRescue");
		ServiceClient cl2 = new ServiceClient(1,"Neena Simone",clientContact,blankContact,"Sameeha","CatRescue");
		ServiceClient cl3 = new ServiceClient(1,"Johnny Cash",clientContact,blankContact,"Sameeha","CatRescue");
		ServiceClient cl4 = new ServiceClient(1,"Denzel Curry",clientContact,blankContact,"Sameeha","CatRescue");
		
		
		/*Hard Coded serviceHour objects to be replaced*/
		ServiceHours a = new ServiceHours()
				.setShid(1)
				.setEventName(new Event().setTitle("Spending Time with Toys for Tots"))
				.setHours(6.0)
				.setStatus("Pending")
				.setServant(srv1)
				.setServedPet(cl1);
		ServiceHours b = new ServiceHours()
				.setShid(2)
				.setEventName(new Event().setTitle("Teaching Part Time"))
				.setHours(2.0)
				.setStatus("Pending")
				.setServant(srv2)
				.setServedPet(cl2);;
		ServiceHours c = new ServiceHours()
				.setShid(3)
				.setEventName(new Event().setTitle("Working with Food House"))
				.setHours(4.0)
				.setStatus("Pending")
				.setServant(srv3)
				.setServedPet(cl3);;
		ServiceHours d = new ServiceHours()
				.setShid(4)
				.setEventName(new Event().setTitle("Volunteering at Service Station"))
				.setHours(2.0)
				.setStatus("Pending")
				.setServant(srv4)
				.setServedPet(cl4);;
		
		hrs.add(a);
		hrs.add(b);
		hrs.add(c);
		hrs.add(d);
	}
	
	
}
