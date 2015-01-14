package org.rapla.client.gwt;

import org.rapla.client.Application;
import org.rapla.client.plugin.tableview.gwt.ListViewPluginModule;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value= { RaplaGWTModule.class, ListViewPluginModule.class},properties="extra.ginModules")
public interface MainInjector extends Ginjector {
    public Application getApplication();
}
