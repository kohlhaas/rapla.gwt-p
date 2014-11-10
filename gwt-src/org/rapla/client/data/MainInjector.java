package org.rapla.client.data;

import org.rapla.client.Application;
import org.rapla.client.plugin.view.list.ListViewPluginGinModule;
import org.rapla.client.plugin.view.month.MonthViewPluginGinModule;
import org.rapla.storage.dbrm.RemoteServer;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ MonthViewPluginGinModule.class, ListViewPluginGinModule.class })
public interface MainInjector extends Ginjector {
    public Application getApplication();

    public RemoteServer getLoginService();
}
