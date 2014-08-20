package org.rapla.client;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.rapla.entities.domain.internal.ReservationImpl;
import org.rapla.rest.gwtjsonrpc.client.impl.AbstractJsonProxy;
import org.rapla.rest.gwtjsonrpc.common.AsyncCallback;
import org.rapla.rest.gwtjsonrpc.common.FutureResult;
import org.rapla.storage.dbrm.LoginTokens;
import org.rapla.storage.dbrm.RemoteServer;
import org.rapla.storage.dbrm.RemoteStorage;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Rapla implements EntryPoint {
    final Logger logger = Logger.getLogger("componentClass");
    public static final String LOGIN_COOKIE = "raplaLoginToken";

    RemoteStorage service = GWT.create(RemoteStorage.class);
    {
        String address = GWT.getModuleBaseURL() + "../rapla/json/" + RemoteStorage.class.getName();
        ((ServiceDefTarget) service).setServiceEntryPoint(address);
    }

    RemoteServer loginService = GWT.create(RemoteServer.class);
    {
        String address = GWT.getModuleBaseURL() + "../rapla/json/" + RemoteServer.class.getName();
        ((ServiceDefTarget) loginService).setServiceEntryPoint(address);
    }

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {

        if (alreadyLoggedIn())
        {
            goToWizard();
        } else
        {
            final Button sendButton = new Button("Send");
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
                    String textToServer = nameField.getText();
                    String password = passwordField.getText();

                    // Then, we send the input to the server.
                    sendButton.setEnabled(false);
                    FutureResult<LoginTokens> login = loginService.login(textToServer, password, null);
                    login.get(new
                            AsyncCallback<LoginTokens>() {

                                public void onSuccess(LoginTokens result) {
                                    logger.info("Login successfull  ");
                                    Cookies.setCookie(LOGIN_COOKIE, result.toString());
                                    ((AbstractJsonProxy) service).setAuthThoken(result.getAccessToken());
                                    goToWizard();
                                }

                                @Override
                                public void onFailure(Throwable caught) {
                                    sendButton.setEnabled(true);
                                }
                            });

                }
            });
        }
    }

    private boolean alreadyLoggedIn() {
        logger.log(Level.INFO, "Looking for cookie");
        String cookie = Cookies.getCookie(LOGIN_COOKIE);
        if (cookie != null)
        {
            LoginTokens token = LoginTokens.fromString(cookie);
            boolean valid = token.isValid();
            logger.log(Level.INFO, "found cookie: " + valid);
            return valid;
        }
        return false;
    }

    private void goToWizard() {
        logger.log(Level.INFO, "GWT Applet started BLUBS1");
        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onSuccess() {
                Window.alert("Code downloaded BLUBS2");
                Application app = GWT.create(Application.class);
                app.createApplication();
                FutureResult<List<ReservationImpl>> reservations =  service.getReservations(null, null, null, null);
                reservations.get(new AsyncCallback<List<ReservationImpl>>() {
                    
                     public void onFailure(Throwable caught) {
                         logger.log(Level.SEVERE, "get Reservation failed:" +
                                 caught.getMessage(),caught);
                     }
                    
                     @Override
                     public void onSuccess(List<ReservationImpl> result) {
                         logger.info( result.size()+ " Reservations loaded."                                 );
                    
                     }
                });

            }

            @Override
            public void onFailure(Throwable reason) {

            }
        });
    }
    // final Button sendButton = new Button("Send");
    // final TextBox nameField = new TextBox();
    // nameField.setText("admin");
    // final TextBox passwordField = new PasswordTextBox();
    // passwordField.setText("");
    //
    // final Label errorLabel = new Label();
    // // We can add style names to widgets
    // sendButton.addStyleName("sendButton");
    //
    // // Add the nameField and sendButton to the RootPanel
    // // Use RootPanel.get() to get the entire body element
    // RootPanel.get("nameFieldContainer").add(nameField);
    // RootPanel.get("passwordFieldContainer").add(passwordField);
    // RootPanel.get("sendButtonContainer").add(sendButton);
    // RootPanel.get("errorLabelContainer").add(errorLabel);
    //
    // // Focus the cursor on the name field when the app loads
    // nameField.setFocus(true);
    // nameField.selectAll();
    //
    // // Create the popup dialog box
    // final DialogBox dialogBox = new DialogBox();
    // dialogBox.setText("Remote Procedure Call");
    // dialogBox.setAnimationEnabled(true);
    // final Button closeButton = new Button("Close");
    // // We can set the id of a widget by accessing its Element
    // closeButton.getElement().setId("closeButton");
    // final Label textToServerLabel = new Label();
    // final HTML serverResponseLabel = new HTML();
    // VerticalPanel dialogVPanel = new VerticalPanel();
    // dialogVPanel.addStyleName("dialogVPanel");
    // dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
    // dialogVPanel.add(textToServerLabel);
    // dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
    // dialogVPanel.add(serverResponseLabel);
    // dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
    // dialogVPanel.add(closeButton);
    // dialogBox.setWidget(dialogVPanel);
    // final Logger logger = Logger.getLogger("componentClass");
    // logger.log(Level.INFO, "GWT Applet started");
    //
    // // Add a handler to close the DialogBox
    // closeButton.addClickHandler(new ClickHandler() {
    // public void onClick(ClickEvent event) {
    // dialogBox.hide();
    // sendButton.setEnabled(true);
    // sendButton.setFocus(true);
    // }
    // });
    //
    //
    // // Create a handler for the sendButton
    // class MyHandler implements ClickHandler {
    // /**
    // * Fired when the user clicks on the sendButton.
    // */
    // public void onClick(ClickEvent event) {
    // sendNameToServer();
    // //Logger logger = Logger.getLogger("componentClass");
    // //logger.log(Level.SEVERE, "hallo",new Exception("hallo"));
    //
    // }
    //
    // /**
    // * Send the name from the nameField to the server and wait for a response.
    // */
    // private void sendNameToServer() {
    // // First, we validate the input.
    // errorLabel.setText("");
    // String textToServer = nameField.getText();
    // String password = passwordField.getText();
    //
    //
    // // Then, we send the input to the server.
    // sendButton.setEnabled(false);
    // textToServerLabel.setText(textToServer);
    // serverResponseLabel.setText("");
    // dialogBox.setText("Remote Procedure Call");
    // serverResponseLabel.removeStyleName("serverResponseLabelError");
    // dialogBox.center();
    // closeButton.setFocus(true);
    // logger.info( "Calling login  " );
    // loginService.login(textToServer, password, null).get(new
    // AsyncCallback<LoginTokens>() {
    //
    // public void onSuccess(LoginTokens result) {
    // logger.info( "Login successfull  " );
    // ((AbstractJsonProxy)service).setAuthThoken(result.getAccessToken());
    // FutureResult<List<ReservationImpl>> reservations =
    // service.getReservations(null, null, null, null);
    // final long start = System.currentTimeMillis();
    // logger.info( "Calling getReservations  " );
    // reservations.get(new AsyncCallback<List<ReservationImpl>>() {
    //
    // public void onFailure(Throwable caught) {
    // logger.log(Level.SEVERE, "get Reservation failed:" +
    // caught.getMessage(),caught);
    // }
    //
    // @Override
    // public void onSuccess(List<ReservationImpl> result) {
    // logger.info( result.size()+ " Reservations loaded. It took " +
    // (System.currentTimeMillis()- start ) + "ms");
    //
    // }
    // });
    // }
    //
    // @Override
    // public void onFailure(Throwable caught) {
    // logger.log(Level.SEVERE, "Login failed " + caught.getMessage(),caught);
    // }
    // });
    // AsyncCallback<UserImpl> asyncCallback = new AsyncCallback<UserImpl>() {
    // public void onFailure(Throwable caught) {
    // // Show the RPC error message to the user
    // dialogBox
    // .setText("Remote Procedure Call - Failure");
    // serverResponseLabel
    // .addStyleName("serverResponseLabelError");
    // serverResponseLabel.setHTML(caught.getMessage());
    // dialogBox.center();
    // closeButton.setFocus(true);
    // }
    //
    // public void onSuccess(UserImpl user) {
    // dialogBox.setText("Remote Procedure Call");
    // serverResponseLabel
    // .removeStyleName("serverResponseLabelError");
    // StringBuilder builder = new StringBuilder();
    // builder.append( "<h2>Ressources</h2>");
    // builder.append( user.getName( Locale.GERMANY));
    // builder.append( "<br/>");
    // serverResponseLabel.setHTML(builder.toString());
    // user.setEmail("christopher.kohlhaas@googlemail.com");
    // AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
    //
    // public void onFailure(Throwable caught) {
    // // Show the RPC error message to the user
    // dialogBox
    // .setText("Remote Procedure Call - Failure");
    // serverResponseLabel
    // .addStyleName("serverResponseLabelError");
    // serverResponseLabel.setHTML(caught.getMessage());
    // dialogBox.center();
    // closeButton.setFocus(true);
    // }
    //
    // @Override
    // public void onSuccess(Boolean result) {
    //
    // }
    // };
    // service.storeUser(user, callback);
    //
    // }
    // };
    // service.getUser("admin",asyncCallback);

    // AsyncCallback<CategoryImpl> callback2 = new AsyncCallback<CategoryImpl>()
    // {
    //
    // public void onFailure(Throwable caught) {
    // // Show the RPC error message to the user
    // dialogBox
    // .setText("Remote Procedure Call - Failure");
    // serverResponseLabel
    // .addStyleName("serverResponseLabelError");
    // serverResponseLabel.setHTML(caught.getMessage());
    // dialogBox.center();
    // closeButton.setFocus(true);
    // }
    //
    // @Override
    // public void onSuccess(CategoryImpl result) {
    // System.out.println( result );
    //
    // }
    // };
    // service.getCategory( callback2);

    // AsyncCallback<List<AllocatableImpl>> callback2 = new
    // AsyncCallback<List<AllocatableImpl>>() {
    //
    // public void onFailure(Throwable caught) {
    // // Show the RPC error message to the user
    // dialogBox
    // .setText("Remote Procedure Call - Failure");
    // serverResponseLabel
    // .addStyleName("serverResponseLabelError");
    // serverResponseLabel.setHTML(caught.getMessage());
    // dialogBox.center();
    // closeButton.setFocus(true);
    // }
    //
    // @Override
    // public void onSuccess(List<AllocatableImpl> result) {
    //
    // }
    // };
    // service.getResources( callback2);
    // dialogBox.center();
    // closeButton.setFocus(true);
    //
    // }
    // }
    //
    // Add a handler to send the name to the server
    // MyHandler handler = new MyHandler();
    // sendButton.addClickHandler(handler);
    // nameField.addKeyUpHandler(handler);
    // }

    // RaplaGWTClient raplaGWTClient;
    // try {
    // raplaGWTClient = new RaplaGWTClient();
    // } catch (RaplaException e) {
    // e.printStackTrace();
    // return;
    // }
    // final RaplaContext context = raplaGWTClient.getContext();

    // Container c = new TestContainer();
    // provide(c);
    // Test hallo = c.getContext().get( Test.class);
    // hallo.message();

    // private void provide(Container c) {
    // Injector<Test> test = GWT.create(TestImpl.class);
    // c.provide(Test.class, test);
    // }
    //
    //
    // private Injector<Test> GWT_create(Class<TestImpl> class1) {
    // return new Injector<Test>()
    // {
    // public Test create(Container cont) {
    // return new TestImpl( cont.getContext());
    // }
    //
    // };
    // }
}
