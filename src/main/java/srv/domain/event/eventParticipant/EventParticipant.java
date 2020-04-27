package srv.domain.event.eventParticipant;

import java.io.Serializable;

import srv.domain.event.Event;
import srv.domain.user.User;

public class EventParticipant implements Serializable {

	private int eventParticipantId;
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
}