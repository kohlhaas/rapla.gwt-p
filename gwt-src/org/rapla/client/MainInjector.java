package org.rapla.client;

import org.rapla.client.internal.RaplaGWTModule;
import org.rapla.client.plugin.view.list.ListViewPluginGinModule;
import org.rapla.facade.ClientFacade;
import org.rapla.storage.StorageOperator;
import org.rapla.storage.dbrm.RemoteServer;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;

@GinModules({ RaplaGWTModule.class, ListViewPluginGinModule.class })
public interface MainInjector extends Ginjector {
    public Application getApplication();

    public RemoteServer getLoginService();
    
    public ClientFacade getFacade();

    public StorageOperator getOperator();
    
    
}
