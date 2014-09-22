package org.rapla.client;

import org.rapla.client.content.DetailSelectEvent;
import org.rapla.client.content.DetailSelectEvent.DetailSelectEventHandler;
import org.rapla.client.data.MainInjector;
import org.rapla.client.event.RaplaEventBus;
import org.rapla.client.module.GUI;
import org.rapla.client.plugin.view.ViewController;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent;
import org.rapla.client.plugin.view.ViewSelectionChangedEvent.ViewSelectionChangedHandler;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootPanel;

public class Application implements ViewSelectionChangedHandler,DetailSelectEventHandler {
    private final MainInjector injector = GWT.create(MainInjector.class);
    FlowPanel drawingContent = new FlowPanel();
    ViewController viewController = new ViewController();
    
    public Application() {
    drawingContent.setStyleName("raplaDrawingContent");
    RaplaEventBus.getInstance().addHandler(ViewSelectionChangedEvent.TYPE,
            this);
    RaplaEventBus.getInstance().addHandler(DetailSelectEvent.TYPE, this);
    }

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

