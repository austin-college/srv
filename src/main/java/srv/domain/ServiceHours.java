package srv.domain;

/**
 * @author AJ Pritchard (For now)
 *
 *	TODO
 *
 *	What do we need from ServiceHours?
 *	Is this going to keep track of the hours served from a single service opportunity?
 *	Is this going to keep track of all service opportunities and thus has the total hours served through that?
 *	Is this going to just have an int of total hours served?
 */
public class ServiceHours {

	private int serviceHours; // TODO int or double? Hunter put double in User's serviceHours. 
	//That needs to be fixed too
	
	public ServiceHours() {
		serviceHours = 0;
	}
	
	public ServiceHours(int initialHours) {
		serviceHours = initialHours;
	}
	
	public void addHours(int hours) {
		serviceHours += hours;
	}
	
	public int getHours() {
		return serviceHours;
	}
}
