package org.rapla.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Application {
    public void createApplication(){
        RootPanel root = RootPanel.get("raplaRoot");
        root.clear();
        root.add(new HTML("Here i am"));

    }
}
