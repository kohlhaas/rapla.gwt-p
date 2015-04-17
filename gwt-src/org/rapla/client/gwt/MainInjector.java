package org.rapla.client.gwt;

import org.rapla.client.plugin.tableview.gwt.ListViewPluginModule;
import org.rapla.client.plugin.weekview.gwt.WeekViewPluginModule;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules(value= { RaplaGWTModule.class, ListViewPluginModule.class, WeekViewPluginModule.class},properties="extra.ginModules")
public interface MainInjector extends Ginjector {
//    public Application getApplication();
    public Bootstrap getBootstrap();
}
