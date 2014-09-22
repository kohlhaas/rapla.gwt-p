package org.rapla.client;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RaplaGin implements EntryPoint {
    final Logger logger = Logger.getLogger("componentClass");
    
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        logger.log(Level.INFO, "GWT Applet started BLUBS1");
        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onSuccess() {
                //Window.alert("Code downloaded BLUBS2");
                Application app = GWT.create(Application.class);
                app.createApplication();
            }

            @Override
            public void onFailure(Throwable reason) {

            }
        });
    }
}
