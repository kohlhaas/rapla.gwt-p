package org.rapla.login.gwt;

import org.rapla.login.LoginView;

import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class RaplaLoginViewImpl implements LoginView {
	final Button sendButton = new Button("login");

	private Presenter presenter;

	public RaplaLoginViewImpl() {
		StyleInjector.inject("login.css", true);
	}

	public void showLogin() {
		final FlowPanel outerPanel = new FlowPanel();
		outerPanel.setStyleName("loginOuterPanel");
		final FlowPanel inputPanel = new FlowPanel();
		outerPanel.add(inputPanel);
		inputPanel.setStyleName("loginInputPanel");
		final FlowPanel commandPanel = new FlowPanel();
		commandPanel.setStyleName("loginCommandPanel");
		outerPanel.add(commandPanel);
		final TextBox nameField = new TextBox();
		nameField.setText("admin");
		nameField.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					event.stopPropagation();
					sendButton.click();
				}
			}
		});
		final TextBox passwordField = new PasswordTextBox();
		passwordField.setText("");
		passwordField.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					event.stopPropagation();
					sendButton.click();
				}
			}
		});
		inputPanel.add(nameField);
		inputPanel.add(passwordField);
		final Label errorLabel = new Label();
		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		commandPanel.add(sendButton);

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel panel = RootPanel.get("raplaRoot");
		panel.add(outerPanel);
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
		nameField.setFocus(true);
	}

	@Override
	public void showLoginError(String error) {
		sendButton.setEnabled(true);
		final ErrorPopup pp = new ErrorPopup(error);
		pp.center();
	}

	@Override
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;

	}

	private static class ErrorPopup extends PopupPanel {
		public ErrorPopup(String errorMessage) {
			super(true, false);
			setWidget(new HTML(errorMessage));
		}
	}

}