package org.rapla.client.event;

import org.rapla.client.event.DetailSelectEvent.DetailSelectEventHandler;
import org.rapla.entities.Entity;
import org.rapla.entities.domain.Reservation;

import com.google.web.bindery.event.shared.Event;

public class DetailSelectEvent extends Event<DetailSelectEventHandler> {
	public static interface DetailSelectEventHandler {
		void detailsRequested(DetailSelectEvent event);
	}

	public static final Type<DetailSelectEventHandler> TYPE = new Type<DetailSelectEventHandler>();

	private Entity<?> selectedObject;

	public DetailSelectEvent(Entity<?> selectedObject) {
		super();
		this.selectedObject = selectedObject;
	}

	public Entity<?> getSelectedObject() {
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
