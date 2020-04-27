package srv.domain.event;

import java.io.Serializable;

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
public class EventType implements Serializable {
	
	private Integer etid;
	private String name;
	private String description;

	public EventType(Integer etid, String name, String description) {
		super();
		this.etid = etid;
		this.name = name;
		this.description = description;
	}
	
	public EventType() {
		super();
	}
	
	public Integer getEtid() {
		return etid;
	}

	public EventType setEtid(Integer etid) {
		this.etid = etid;
		return this;
	}

	public String getName() {
		return name;
	}

	public EventType setName(String name) {
		this.name = name;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public EventType setDescription(String description) {
		this.description = description;
		return this;
	}

}
