package org.rapla.client;

import org.rapla.client.content.DetailSelectEvent;
import org.rapla.client.content.DetailSelectEvent.DetailSelectEventHandler;
import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.plugin.view.ViewController;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent.ViewSelectionChangedHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Application implements ViewSelectionChangedHandler,
		DetailSelectEventHandler {
	FlowPanel drawingContent = new FlowPanel();
	ViewController viewController = new ViewController();

	public Application() {
		drawingContent.setStyleName("raplaDrawingContent");
		RaplaEventBus.getInstance().addHandler(ViewSelectionChangedEvent.TYPE,
				this);
		RaplaEventBus.getInstance().addHandler(DetailSelectEvent.TYPE, this);
	}

	public void createApplication() {
		RootPanel root = RootPanel.get("raplaRoot");
		root.clear();
		root.add(new HTML("Here i am"));
		root.add(viewController.createContent());
		root.add(drawingContent);
		viewChanged();

	}

	@Override
	public void viewChanged() {
		drawingContent.clear();
		drawingContent.add(viewController.getSelectedContentDrawer()
				.createContent());
	}

	@Override
	public void detailsRequested(DetailSelectEvent event) {
		Window.alert("Selected object is: " + event.getSelectedObject());
	}
}
