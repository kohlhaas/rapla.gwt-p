package org.rapla.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rapla.entities.domain.Allocatable;
import org.rapla.facade.internal.FacadeImpl;
import org.rapla.framework.RaplaException;
import org.rapla.rest.gwtjsonrpc.client.impl.AbstractJsonProxy;
import org.rapla.rest.gwtjsonrpc.client.impl.EntryPointFactory;
import org.rapla.rest.gwtjsonrpc.common.AsyncCallback;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;
import org.rapla.storage.dbrm.LoginTokens;
import org.rapla.storage.dbrm.RemoteOperator;
import org.rapla.storage.dbrm.RemoteServer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rapla implements EntryPoint {
    private final MainInjector injector = GWT.create(MainInjector.class);
    final Logger logger = Logger.getLogger("componentClass");
    public static final String LOGIN_COOKIE = "raplaLoginToken";

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
    	StyleInjector.inject("Rapla.css", true);
    	//logger.info("Heute ist " + new GWTRaplaLocale().formatDateLong( new Date()));
        AbstractJsonProxy.setServiceEntryPointFactory( new EntryPointFactory() {
            
            @Override
            public String getEntryPoint(Class serviceClass) {
                String name = serviceClass.getName().replaceAll("_JsonProxy", "");
                
                logger.info("Setting entry point to " + name);
                return  GWT.getModuleBaseURL() + "../rapla/json/" + name;
            }
        });
        LoginTokens token = getValidToken();
        if (token != null)
        {
            
            startApplication(token);
        } 
        else
        {
            showLogin();
        }
    }

    private void showLogin() {
        final Button sendButton = new Button("login");
        final TextBox nameField = new TextBox();
        nameField.setText("admin");
        final TextBox passwordField = new PasswordTextBox();
        passwordField.setText("");

        final Label errorLabel = new Label();
        // We can add style names to widgets
        sendButton.addStyleName("sendButton");

        // Add the nameField and sendButton to the RootPanel
        // Use RootPanel.get() to get the entire body element
        RootPanel panel = RootPanel.get("raplaRoot");
        panel.add(nameField);
        panel.add(passwordField);
        panel.add(sendButton);
        panel.add(errorLabel);
        sendButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                errorLabel.setText("");
                final String username = nameField.getText();
                String password = passwordField.getText();
                // Then, we send the input to the server.
                sendButton.setEnabled(false);
                final RemoteServer loginService = injector.getLoginService();
                FutureResult<LoginTokens> login = loginService.login(username, password, null);
                login.get(new
                        AsyncCallback<LoginTokens>() {

                            public void onSuccess(LoginTokens token) {
                                logger.info("Login successfull  ");
                                // we store the token in a cookie for future login
                                setValidToken(username, token);
                                // then we set the token for the remote proxies
                                startApplication( token);
                            }

                            @Override
                            public void onFailure(Throwable caught) {
                                logger.log(Level.SEVERE, caught.getMessage(), caught);
                                sendButton.setEnabled(true);
                            }
                        });

            }
        });
    }
    private void setValidToken(String username, LoginTokens token)
    {
        Cookies.setCookie(LOGIN_COOKIE, username+ ":" +token.toString());
        
    }

    private LoginTokens getValidToken() {
        logger.log(Level.INFO, "Looking for cookie");
        String cookie = Cookies.getCookie(LOGIN_COOKIE);
        if (cookie != null)
        {
            // re request the server for refresh token
            int indexOf = cookie.indexOf(":");
            String username = cookie.substring(0,indexOf);
            String tokenString = cookie.substring(indexOf +1);
            logger.log(Level.INFO, "found cookie: " +cookie +" parsed " + username +":"+ tokenString);
            
            LoginTokens token = LoginTokens.fromString(tokenString);
            boolean valid = token.isValid();
            logger.log(Level.INFO, "found cookie: " + valid);
            if ( !valid)
            {
                return null;
            }
            return token;
        }
        return null;
    }

    private void startApplication(LoginTokens token) {
        AbstractJsonProxy.setAuthThoken(token.getAccessToken());
        
        logger.log(Level.INFO, "GWT Applet started BLUBS1");
        RemoteOperator operator = (RemoteOperator)injector.getOperator();
        operator.getRemoteConnectionInfo().setServerURL( "bla");
        try {
            String cookie = Cookies.getCookie(LOGIN_COOKIE);
            int indexOf = cookie.indexOf(":");
            String username = cookie.substring(0,indexOf);
            operator.loadData(null);
            operator.initRefresh();
            FacadeImpl facadeImpl = (FacadeImpl)injector.getFacade();
            facadeImpl.setUsernameInternal(null, username);
            facadeImpl.setCachingEnabled( false );
            // Test for the resources
            Collection<Allocatable> allocatables = Arrays.asList(facadeImpl.getAllocatables());
            //Collection<Allocatable> allocatables = operator.getAllocatables(null);
            logger.info("loaded " + allocatables.size() + " resources.");
            
        } catch (RaplaException e) {
            logger.log( Level.SEVERE, e.getMessage(), e);
            return;
        }
        Application app = injector.getApplication();
        app.createApplication();

    }

//    static class RaplaLoginCookie {
//        final Logger logger = Logger.getLogger("RaplaLoginCookie");
//        public static final String LOGIN_COOKIE = "raplaLoginToken";
//
//        private static final RaplaLoginCookie INSTANCE = new RaplaLoginCookie();
//
//        public static RaplaLoginCookie getCookie() {
//            return INSTANCE;
//        }
//
//        public void updateLogin(LoginTokens result) {
//            Cookies.setCookie(LOGIN_COOKIE, result.toString());
//        }
//
//        public boolean isAlreadyLoggedIn() {
//            logger.log(Level.INFO, "Looking for cookie");
//            String cookie = Cookies.getCookie(LOGIN_COOKIE);
//            if (cookie != null) {
//                LoginTokens token = LoginTokens.fromString(cookie);
//                boolean valid = token.isValid();
//                logger.log(Level.INFO, "found cookie: " + valid);
//                return valid;
//            }
//            return false;
//        }
//
//    }

}