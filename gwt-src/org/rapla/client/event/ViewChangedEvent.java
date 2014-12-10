package org.rapla.client.event;

import org.rapla.client.event.ViewChangedEvent.ViewChangedEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

public class ViewChangedEvent extends  GwtEvent<ViewChangedEventHandler> {

	public static interface ViewChangedEventHandler extends EventHandler {
		void addRequested(ViewChangedEvent event);
	}

	public static final Type<ViewChangedEventHandler> TYPE = new Type<ViewChangedEventHandler>();
	
	
	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ViewChangedEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	@Override
	protected void dispatch(ViewChangedEventHandler handler) {
		handler.addRequested(this);
		
	}


}
