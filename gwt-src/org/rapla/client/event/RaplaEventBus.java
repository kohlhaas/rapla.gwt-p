package org.rapla.client.event;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

public class RaplaEventBus {

	private final SimpleEventBus eventBus;
	private static final RaplaEventBus INSTANCE = new RaplaEventBus();

	private RaplaEventBus() {
		eventBus = GWT.create(SimpleEventBus.class);
	}

	public static RaplaEventBus getInstance() {
		return INSTANCE;
	}

	public <H extends EventHandler> HandlerRegistration addHandler(
			Type<H> type, H handler) {
		HandlerRegistration registration = eventBus.addHandler(type, handler);
		return registration;
	}

	public void fireEvent(GwtEvent<?> event) {
		eventBus.fireEvent(event);
	}
}
