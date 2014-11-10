package org.rapla.client.services;

import java.util.logging.Logger;

import javax.inject.Provider;

import org.rapla.storage.dbrm.RemoteServer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class LoginServiceProvider implements Provider<RemoteServer>{

    RemoteServer loginService;
    public LoginServiceProvider(){
        Logger.getLogger("componentClass").info("Start");
//        loginService = GWT.create(RemoteServer.class);
//        Logger.getLogger("componentClass").info("WEiter "+loginService);
//        String address = GWT.getModuleBaseURL() + "../rapla/json/" + RemoteServer.class.getName();
//        ((ServiceDefTarget) loginService).setServiceEntryPoint(address);
    }
    @Override
    public RemoteServer get() {
        return loginService;
    }
    
    
}
