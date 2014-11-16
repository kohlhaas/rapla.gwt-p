package org.rapla.client.event;

import org.rapla.client.event.AddEvent.AddEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class AddEvent extends GwtEvent<AddEventHandler> {
	public static interface AddEventHandler extends EventHandler {
		void addRequested(AddEvent event);
	}

	public static final Type<AddEventHandler> TYPE = new Type<AddEventHandler>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<AddEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(AddEventHandler handler) {
		handler.addRequested(this);
	}

}
