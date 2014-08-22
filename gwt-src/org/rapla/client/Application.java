package org.rapla.client;

import org.rapla.client.plugin.view.ViewController;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Application {
	public void createApplication() {
		RootPanel root = RootPanel.get("raplaRoot");
		root.clear();
		root.add(new HTML("Here i am"));
		root.add(new ViewController());

	}
}
