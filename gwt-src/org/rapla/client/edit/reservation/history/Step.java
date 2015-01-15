package org.rapla.client.edit.reservation.history;

import com.google.gwt.user.client.ui.Widget;

public interface Step {
	Object up();
	Object down();
	Widget getWidget();
}
