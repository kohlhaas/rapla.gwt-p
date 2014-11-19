package org.rapla.client;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.rapla.client.event.AddEvent;
import org.rapla.client.event.DetailSelectEvent;
import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.event.AddEvent.AddEventHandler;
import org.rapla.client.event.DetailSelectEvent.DetailSelectEventHandler;
import org.rapla.client.plugin.view.ViewController;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent.ViewSelectionChangedHandler;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootPanel;
@Singleton
public class Application implements ViewSelectionChangedHandler,
		DetailSelectEventHandler, AddEventHandler {

	FlowPanel drawingContent = new FlowPanel();
	@Inject
	ViewController viewController;

	public Application() {
		drawingContent.setStyleName("raplaDrawingContent");
		RaplaEventBus.getInstance().addHandler(ViewSelectionChangedEvent.TYPE,
				this);
		RaplaEventBus.getInstance().addHandler(DetailSelectEvent.TYPE, this);
		RaplaEventBus.getInstance().addHandler(AddEvent.TYPE, this);
	}

	// private final DataInjector injector2 = GWT.create(DataInjector.class);
	public void createApplication() {
		RootPanel root = RootPanel.get("raplaRoot");
		root.clear();
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

	@Override
	public void addRequested(AddEvent event) {
		Window.alert("Add event received");
	}

}
