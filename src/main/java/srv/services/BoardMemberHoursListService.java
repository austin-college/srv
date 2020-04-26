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
		Contact srv1Con = new Contact();
		srv1Con.setFirstName("Kevin");
		Contact srv2Con = new Contact();
		srv2Con.setFirstName("AJ");
		Contact srv3Con = new Contact();
		srv3Con.setFirstName("Anyta");
		Contact srv4Con = new Contact();
		srv4Con.setFirstName("Lyfe");
		/*HardCoded blank contacts*/
		Contact clientContact = new Contact();
		Contact clientContact2 = new Contact();
		clientContact.setEmail("fakeEmail@temp.net");
		ServantUser srv1 = new ServantUser(1,"602232", srv1Con, 's');
		ServantUser srv2 = new ServantUser(2,"618826",srv2Con, 's');
		ServantUser srv3 = new ServantUser(3,"652286",srv3Con, 's');
		ServantUser srv4 = new ServantUser(4,"696632", srv4Con, 's');
		/*HardCoded pets to be replaced*/
		ServiceClient cl1 = new ServiceClient(1,"Billy Bob Thornton",clientContact,clientContact2,"Sameeha","CatRescue");
		ServiceClient cl2 = new ServiceClient(1,"Neena Simone",clientContact,clientContact2,"Sameeha","CatRescue");
		ServiceClient cl3 = new ServiceClient(1,"Johnny Cash",clientContact,clientContact2,"Sameeha","CatRescue");
		ServiceClient cl4 = new ServiceClient(1,"Denzel Curry",clientContact,clientContact2,"Sameeha","CatRescue");
		
		
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
