package srv.domain.event.eventParticipants;

import java.io.Serializable;

import srv.domain.event.Event;
import srv.domain.user.User;

public class EventParticipants implements Serializable {

	private int epid;
	private Event event;
	private User user;
	
	public EventParticipants() {
		super();
	}
	
	public EventParticipants(Event event, User user) {
		this.event = event;
		this.user = user;
	}

	public int getEpid() {
		return epid;
	}

	public EventParticipants setEpid(int epid) {
		this.epid = epid;
		return this;
	}

	public Event getEvent() {
		return event;
	}

	public EventParticipants setEvent(Event event) {
		this.event = event;
		return this;
	}

	public User getUser() {
		return user;
	}

	public EventParticipants setUser(User user) {
		this.user = user;
		return this;
	}
}
