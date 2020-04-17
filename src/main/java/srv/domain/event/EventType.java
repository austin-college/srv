package srv.domain.event;

/**
 * 
 * 
 * @author Min Shim
 *
 *			[AJ's comment] 
 *         We need to decide what to do here.
 *         I think this should be an enum, or something else if we don't want
 *         the choices to be hard coded. This is because I assume that this will
 *         be for purely for sorting means.
 *     	
 *     		[Min's comment]
 *     		This class is a data holder, mainly for sorting purpose.
 *     		Initially this class only have eventType input, and a method that
 *     		returns True if the event is AC and False if the event is non AC event.
 */
public class EventType {

	private String eventType; //eventType variable should be "AC" or "Adhoc"
	
	// constructor for EveentType
	public EventType(String eventType) {
		super();
		this.eventType = eventType;
	}
	
	/**
	 * This method will tell if the event type is AC or Adhoc(non-AC events)
	 * @param eventType
	 * @return if eventType is AC return True, if everntType is Adhoc return False
	 */
	public boolean isACEvent(String eventType) {
		if(this.eventType == "AC") {
			return true;
		} else {
			return false;
		}
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	} 
	
	
	
}
