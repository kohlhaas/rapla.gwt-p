package org.rapla.client.event;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.Event;
import com.google.web.bindery.event.shared.Event.Type;
import com.google.web.bindery.event.shared.HandlerRegistration;
import com.google.web.bindery.event.shared.SimpleEventBus;

public class RaplaEventBus {

	private final SimpleEventBus eventBus;

	@Inject
	private RaplaEventBus(SimpleEventBus eventBus) {
		this.eventBus = eventBus;
	}

	public <H> HandlerRegistration addHandler(Type<H> type, H handler) {
		HandlerRegistration registration = eventBus.addHandler(type, handler);
		return registration;
	}

	public void fireEvent(Event<?> event) {
		eventBus.fireEvent(event);
	}
}
