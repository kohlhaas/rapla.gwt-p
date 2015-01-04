package org.rapla.login.gwt;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.rapla.login.LoginView;
import org.rapla.login.LoginView.Presenter;
import org.rapla.rest.gwtjsonrpc.client.impl.AbstractJsonProxy;
import org.rapla.rest.gwtjsonrpc.client.impl.EntryPointFactory;
import org.rapla.rest.gwtjsonrpc.common.AsyncCallback;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;
import org.rapla.storage.dbrm.LoginTokens;
import org.rapla.storage.dbrm.RemoteServer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RaplaLogin implements EntryPoint, Presenter {
    public static final String LOGIN_COOKIE = "raplaLoginToken";
    final Logger logger = Logger.getLogger("componentClass");
    LoginView view;
     
    public RaplaLogin()
    {
        this.view = new RaplaLoginViewImpl();
        this.view.setPresenter( this);
    }
  
    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        setProxy();
        showLogin();
    }
    
    private void setProxy() {
        AbstractJsonProxy.setServiceEntryPointFactory( new EntryPointFactory() {
            @Override
            public String getEntryPoint(Class serviceClass) {
                String name = serviceClass.getName().replaceAll("_JsonProxy", "");
                String url = GWT.getModuleBaseURL() + "../rapla/json/" + name;
                return  url;
            }
        });
    }
    
    @Override
    public void submit(final String username, final String password) {
        final RemoteServer loginService = GWT.create(RemoteServer.class);
        FutureResult<LoginTokens> login = loginService.login(username, password, null);
        login.get(new
                AsyncCallback<LoginTokens>() {

                    public void onSuccess(LoginTokens token) {
                        logger.info("Login successfull  ");
                        // we store the token in a cookie for future login
                        setValidToken( token);
                        // then we set the token for the remote proxies
                        startApplication( );
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        logger.log(Level.SEVERE, caught.getMessage(), caught);
                        view.showLoginError( caught.getMessage());
                    }
                });

        
    }

    private void setValidToken( LoginTokens token)
    {
        Cookies.setCookie(LOGIN_COOKIE, token.toString());
    }

   
    private void startApplication() {
        Window.Location.replace("rapla.html");
    }
    
    private void showLogin() {
        view.showLogin();
    }


}