package org.rapla.client.services;

import javax.inject.Provider;

import org.rapla.storage.dbrm.RemoteServer;
import org.rapla.storage.dbrm.RemoteStorage;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class RemoteStorageProvider implements Provider<RemoteStorage>{

    RemoteStorage service = GWT.create(RemoteStorage.class);
    {
        String address = GWT.getModuleBaseURL() + "../rapla/json/" + RemoteStorage.class.getName();
        ((ServiceDefTarget) service).setServiceEntryPoint(address);
    }
    
    @Override
    public RemoteStorage get() {
        return service;
    }
    
    
}
