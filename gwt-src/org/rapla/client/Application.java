package org.rapla.client;

import org.rapla.client.data.MainInjector;
import org.rapla.client.module.GUI;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Application {
    private final MainInjector injector = GWT.create(MainInjector.class);
    //private final DataInjector injector2 = GWT.create(DataInjector.class);
    public void createApplication(){
        RootPanel root = RootPanel.get("raplaRoot");
        root.clear();
//        {
//            Data data = injector.getData();
//            root.add(new HTML("Here i am at " + data.getLong()));
//        }
//        {
//            Data data = injector.getData();
//            root.add(new HTML("Here i am at " + data.getLong()));
//        }
//        {
//            ModuleA data = injector.getModule();
//            root.add(new HTML("Here i am at " + data.doSomething()));
//        }
//        {
//            ModuleB data = injector.getModuleB();
//            root.add(new HTML("Here i am at " + data.doSomething()));
//        }
        
        GUI gui = injector.getGUI();
        root.add(new HTML("Here i am at " + gui.getViews()));
        
    }
}
