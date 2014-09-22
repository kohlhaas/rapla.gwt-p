package org.rapla.client.plugin.view;

import org.rapla.client.plugin.view.ViewSelectionChangedEvent.ViewSelectionChangedHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class ViewSelectionChangedEvent extends
		GwtEvent<ViewSelectionChangedHandler> {

	public static interface ViewSelectionChangedHandler extends EventHandler {
		void viewChanged();
	}

	public static final Type<ViewSelectionChangedHandler> TYPE = new Type<>();

	@Override
	public com.google.gwt.event.shared.GwtEvent.Type<ViewSelectionChangedHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ViewSelectionChangedHandler handler) {
		handler.viewChanged();
	}
}
