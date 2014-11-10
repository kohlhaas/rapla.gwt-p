package org.rapla.client.services;

import org.rapla.storage.dbrm.RemoteServer;
import org.rapla.storage.dbrm.RemoteStorage;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.binder.GinBinder;

public class ServerRequestModule implements GinModule {

    @Override
    public void configure(GinBinder binder) {
//        binder.bind(RemoteStorage.class).toProvider(RemoteStorageProvider.class);
        binder.bind(RemoteServer.class).toProvider(LoginServiceProvider.class);

    }

}
