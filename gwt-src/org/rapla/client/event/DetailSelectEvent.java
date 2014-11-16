package org.rapla.client.event;

import org.rapla.client.event.DetailSelectEvent.DetailSelectEventHandler;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class DetailSelectEvent extends GwtEvent<DetailSelectEventHandler> {
	public static interface DetailSelectEventHandler extends EventHandler {
		void detailsRequested(DetailSelectEvent event);
	}

	public static final Type<DetailSelectEventHandler> TYPE = new Type<DetailSelectEventHandler>();

	private Object selectedObject;

	public DetailSelectEvent(Object selectedObject) {
		super();
		this.selectedObject = selectedObject;
	}

	public Object getSelectedObject() {
		return selectedObject;
	}

	@Override
	public Type<DetailSelectEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(DetailSelectEventHandler handler) {
		handler.detailsRequested(this);
	}
}
