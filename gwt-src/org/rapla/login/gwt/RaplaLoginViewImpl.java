package org.rapla.login.gwt;

import org.rapla.login.LoginView;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RaplaLoginViewImpl implements LoginView 
{
    final Button sendButton = new Button("login");
    
    private Presenter presenter;

    public RaplaLoginViewImpl() {
        StyleInjector.inject("Rapla.css", true);
    }

    public void showLogin() {
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
                presenter.submit(username, password);
                
            }
        });
    }

    @Override
    public void showLoginError(String error) {
        sendButton.setEnabled(true);
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
        
    }

}