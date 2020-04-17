package srv.domain.event;

/**
 * 
 * 
 * @author Min Shim
 *
 *         [AJ's comment] We need to decide what to do here. I think this should
 *         be an enum, or something else if we don't want the choices to be hard
 *         coded. This is because I assume that this will be for purely for
 *         sorting means.
 * 
 *         [Min's comment] This class is a data holder, mainly for sorting
 *         purpose. Initially this class only have eventType input, and a method
 *         that returns True if the event is AC and False if the event is non AC
 *         event.
 */
public class EventType {

	private String name;
	private String description;

	public EventType(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
