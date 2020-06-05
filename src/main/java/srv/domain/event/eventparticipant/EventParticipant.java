package srv.domain.event.eventparticipant;

import java.io.Serializable;

import srv.domain.event.Event;
import srv.domain.user.User;

/**
 * An instance of this class represents an event and a person (user)
 * who has claimed (RSVP) they are going to the event. 
 * 
 * @author Lydia House
 *
 */
public class EventParticipant implements Serializable {

	private int eventParticipantId;	// unique id, given by database
	private Event event;
	private User user;
	
	public EventParticipant() {
		super();
	}
	
	public EventParticipant(Event event, User user) {
		this.event = event;
		this.user = user;
	}

	public int getEventParticipantId() {
		return eventParticipantId;
	}

	public EventParticipant setEventParticipantId(int eventParticipantId) {
		this.eventParticipantId = eventParticipantId;
		return this;
	}

	public Event getEvent() {
		return event;
	}

	public EventParticipant setEvent(Event event) {
		this.event = event;
		return this;
	}

	public User getUser() {
		return user;
	}

	public EventParticipant setUser(User user) {
		this.user = user;
		return this;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + eventParticipantId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventParticipant other = (EventParticipant) obj;
		if (eventParticipantId != other.eventParticipantId)
			return false;
		return true;
	}
	
	
}
