package org.rapla.client.plugin.view.infos;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.impl.ReservationController;
import org.rapla.client.factory.InfoViewInterface;
import org.rapla.client.factory.ViewServiceProviderInterface;
import org.rapla.framework.RaplaException;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class InfoView implements ViewServiceProviderInterface, InfoViewInterface {

	private HorizontalPanel content;
	private VerticalPanel contentLeft;
	private VerticalPanel contentRight;

	private ListBox listBox;
	
	

	
	@Override
	public Widget createContent() {

		Integer width = (int) (Window.getClientWidth() * 0.9) / 2;

		content = new HorizontalPanel();
		// content.addStyleName("infoHorizontalPanel");
		// content.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contentLeft = new VerticalPanel();
		contentRight = new VerticalPanel();
		contentLeft.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contentRight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contentLeft.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		contentRight.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		listBox = new ListBox();
		
	
		// add the Event Types from data.xml here
		
				

		listBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub

			}
		});
		
		final FlowPanel listPanel = new FlowPanel();
		listPanel.add(listBox);
		listPanel.setWidth(width + "px");
		contentLeft.add(listPanel);

		final InfoHorizontalPanel titelPanel = new InfoHorizontalPanel(width + "px");
		final Label titel = new Label("Vorlesungstitel");
		final TextBox titelInput = new TextBox();
		titelPanel.add(titel, (width / 3) + "px" );
		titelPanel.add(titelInput, (width / 3) + "px");
		//titelPanel.setWidth(width + "px");

		final Label vorlesungsStunden = new Label("Vorlesungsstunden");
		final Label vorlesungsStundenMessage = new Label("");
		final TextBox vorlesungsStundenInput = new TextBox();
		vorlesungsStundenInput.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String input = vorlesungsStundenInput.getText();
				if (!input.isEmpty()) {
					if (input.matches("[0-9]*")) {
						double message = Integer.parseInt(input);
						message = (message * 45) / 60;
						vorlesungsStundenMessage.setText("= " + message
								+ " Zeitstunden");
					} else {
						vorlesungsStundenMessage
								.setText("Keine korrekter Eingabe");
					}
					// do your thang
				} else {
					vorlesungsStundenMessage.setText("");
				}

			}
		});


		final InfoHorizontalPanel vorlesungsStundenPanel = new InfoHorizontalPanel(width + "px");
		vorlesungsStundenPanel.add(vorlesungsStunden, (width / 3 ) + "px");
		vorlesungsStundenPanel.add(vorlesungsStundenInput, (width / 3 ) + "px");
		vorlesungsStundenPanel.add(vorlesungsStundenMessage, (width / 3 ) + "px");
		
		final InfoHorizontalPanel studiengangPanel = new InfoHorizontalPanel(width + "px");
		Label studiengang = new Label("Studiengang");
		ListBox studiengangListBox = new ListBox();
		studiengangListBox.addItem("WWI12B1");
		studiengangListBox.addItem("WWI12B2");
		studiengangListBox.addItem("WWI12B3");
		// Hier den Handler / Listener für die Listbox einbinden
		
		studiengangPanel.add(studiengang, (width / 3 ) + "px");
		studiengangPanel.add(studiengangListBox, (width / 3 ) + "px");

		studiengangPanel.finalize(3);
		vorlesungsStundenPanel.finalize(3);
		titelPanel.finalize(3);
		
		contentRight.add(titelPanel);
		contentRight.add(vorlesungsStundenPanel);
		contentRight.add(studiengangPanel);

		content.add(contentLeft);
		content.add(contentRight);
		content.setCellWidth(contentLeft, width + "px");
		content.setCellWidth(contentRight, width + "px");

		return content;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setEventTypes(List<String> eventTypes) {

		for(int i = 0; i < eventTypes.size(); i++){
			listBox.addItem((String) eventTypes.get(i).toString());
			}

	}

	@Override
	public String getSelectedEventType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setReservationController(
			ReservationController reservationController) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ReservationController getReservationController() {
		// TODO Auto-generated method stub
		return null;
	}

}
