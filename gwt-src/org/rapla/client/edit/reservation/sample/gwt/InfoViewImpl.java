package org.rapla.client.edit.reservation.sample.gwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.InfoView;
import org.rapla.client.edit.reservation.sample.InfoView.Presenter;
import org.rapla.entities.dynamictype.Attribute;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;

public class InfoViewImpl extends AbstractView<Presenter>  implements InfoView<IsWidget>{

	private Panel contentPanel;

	private VerticalPanel contentLeft;
	private VerticalPanel contentRight;
	private HorizontalPanel infoTab; 
	private ListBox eventTypesListBox;
	private Tree resources;
	private TextBox titelInput;
	private TextBox vorlesungsStundenInput;
	private ListBox studiengangListBox;
	private Collection<String> studiengangListBoxAuswahl;
	
	private int height, width; //might need a method to transfer values 
	

	@Override
	public IsWidget provideContent() {

		return contentPanel;
	}

	@Override
	public void show() {
		
		 
		height = (int) (Window.getClientHeight() * 0.90); //to be deleted
		width = (int) (Window.getClientWidth() * 0.90); //to be deleted

		contentPanel = new SimplePanel();
		    contentPanel.clear(); 
	 
	    infoTab = new HorizontalPanel();
		contentLeft = new VerticalPanel();
		contentRight = new VerticalPanel();
		contentLeft.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contentRight
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contentLeft.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		contentRight.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

		eventTypesListBox = new ListBox();
		resources = new Tree();

		// add the Event Types from data.xml here

		eventTypesListBox.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				getPresenter().onEventTypesChanged();

			}
		});

		final FlowPanel listPanel = new FlowPanel();
		listPanel.add(eventTypesListBox);
		listPanel.add(resources);
		listPanel.setWidth(width + "px");
		contentLeft.add(listPanel);

		final InfoHorizontalPanel titelPanel = new InfoHorizontalPanel(width
				+ "px");
		final Label title = new Label("Vorlesungstitel");
		titelInput = new TextBox();
		titelPanel.add(title, (width / 3) + "px");
		titelPanel.add(titelInput, (width / 3) + "px");
		 titelPanel.setWidth(width + "px");

		final Label vorlesungsStunden = new Label("Vorlesungsstunden");
		final Label vorlesungsStundenMessage = new Label("");
		vorlesungsStundenInput = new TextBox();
		vorlesungsStundenInput.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				//to be implemented

			}
		});

		final InfoHorizontalPanel vorlesungsStundenPanel = new InfoHorizontalPanel(
				width + "px");
		vorlesungsStundenPanel.add(vorlesungsStunden, (width / 3) + "px");
		vorlesungsStundenPanel.add(vorlesungsStundenInput, (width / 3) + "px");
		vorlesungsStundenPanel
				.add(vorlesungsStundenMessage, (width / 3) + "px");

		final InfoHorizontalPanel studiengangPanel = new InfoHorizontalPanel(
				width + "px");
		Label studiengang = new Label("Studiengang");
		studiengangListBox = new ListBox();
		studiengangListBox.addItem("WWI12B1");
		studiengangListBox.addItem("WWI12B2");
		studiengangListBox.addItem("WWI12B3");
		studiengangListBoxAuswahl = new ArrayList();
		studiengangListBox.addChangeHandler(new ChangeHandler() {
			// Hier den Handler / Listener für die Listbox einbinden ~ done
			@Override
			public void onChange(ChangeEvent event) {
				getPresenter().onStudiengangChanged();

			}

		});

		studiengangPanel.add(studiengang, (width / 3) + "px");
		studiengangPanel.add(studiengangListBox, (width / 3) + "px");

		studiengangPanel.finalize(3);
		vorlesungsStundenPanel.finalize(3);
		titelPanel.finalize(3);

		contentRight.add(titelPanel);
		contentRight.add(vorlesungsStundenPanel);
		contentRight.add(studiengangPanel);

		
		
		infoTab.add(contentLeft);
		infoTab.add(contentRight);
		
		infoTab.setCellWidth(contentLeft, width + "px");
		infoTab.setCellWidth(contentRight, width + "px");
		contentPanel.add(infoTab);
	
		
	}

	@Override
	public void update(List appointments) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setHeightAndWidth(int height, int width){
		this.height = height;
		this.width = width;
		
	}

	@Override
	public void hide() {
		contentPanel.setVisible(false);
		
	}
	
	@Override
	public String getTitelInput() {
		// TODO Auto-generated method stub
		return titelInput.getText();
	}


	@Override
	public Attribute getVorlesungsStundenInput() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public String getSelectedEventType() {
		return eventTypesListBox.getSelectedItemText();
	}
    
	public void setEventTypes(List<String> eventTypes) {

		for (int i = 0; i < eventTypes.size(); i++) {
			eventTypesListBox.addItem((String) eventTypes.get(i).toString());
		}

	}

	


	public void setSelectedEventType(String select) {
		for (int i = 0; i < eventTypesListBox.getItemCount(); i++) {
			if (eventTypesListBox.getItemText(i).equals(select)) {
				eventTypesListBox.setItemSelected(i, true);
			}
		}
	}

	
	public void setDynamicFields(Attribute[] attributes) {

		for (int i = 0; i < attributes.length; i++) {

			String name = attributes[i].toString();

			eventTypesListBox.addItem(name);

		}

	}

	public void setConstraintKeys(String[] constraintKeys) {
	}

	

}
