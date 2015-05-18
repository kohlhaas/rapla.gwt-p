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

public class InfoViewImpl extends AbstractView<Presenter> implements
		InfoView<IsWidget> {

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

	private int height, width; // might need a method to transfer values

	@Override
	public IsWidget provideContent() {

		return contentPanel;
	}

	@Override
	public void createContent() {
		width = (int) (Window.getClientWidth() * 0.9) / 2;

		height = (int) (Window.getClientHeight() * 0.90 * 0.80);

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
		Label eventType = new Label("Art der Veranstaltung");
		listPanel.add(eventType);
		listPanel.add(eventTypesListBox);
		listPanel.add(resources);
		listPanel.setWidth(width + "px");
		listPanel.setStyleName("listpanel");
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
				// to be implemented

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
		studiengangListBox.addItem("BWL-Bank");
		studiengangListBox.addItem("BWL-Handel");
		studiengangListBox.addItem("BWL-International Business");
		studiengangListBox.addItem("BWL-Industie");
		studiengangListBox.addItem("RSW-Steuern- und Pr\u00FCfungswesen");
		studiengangListBox.addItem("Unternehmertum");
		studiengangListBox.addItem("BWL-Versicherung");
		studiengangListBox.addItem("Wirtschaftsinformatik");
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
		
		final InfoHorizontalPanel planungsstatus = new InfoHorizontalPanel(width
				+ "px");
		final Label planungsstatusLabel = new Label("Planungsstatus");
		TextBox planungsstatusInput = new TextBox();
		planungsstatus.add(planungsstatusLabel, (width / 3) + "px");
		planungsstatus.add(planungsstatusInput, (width / 3) + "px");
		planungsstatus.setWidth(width + "px");
		
		final InfoHorizontalPanel erfassungsstatus = new InfoHorizontalPanel(width
				+ "px");
		final Label erfassungsstatusLabel = new Label("Erfassungsstatus");
		TextBox erfassungsstatusInput = new TextBox();
		erfassungsstatus.add(erfassungsstatusLabel, (width / 3) + "px");
		erfassungsstatus.add(erfassungsstatusInput, (width / 3) + "px");
		erfassungsstatus.setWidth(width + "px");


		studiengangPanel.finalize(3);
		vorlesungsStundenPanel.finalize(3);
		titelPanel.finalize(3);
		planungsstatus.finalize(3);
		erfassungsstatus.finalize(3);

		contentRight.add(titelPanel);
		contentRight.add(vorlesungsStundenPanel);
		contentRight.add(studiengangPanel);
		contentRight.add(planungsstatus);
		contentRight.add(erfassungsstatus);

		infoTab.add(contentLeft);
		infoTab.add(contentRight);

		infoTab.setCellWidth(contentLeft, width + "px");
		infoTab.setCellWidth(contentRight, width + "px");
		contentPanel.add(infoTab);

	}

	@Override
	public void update(List appointments) {

	}

	// @Override
	// public void update(List<Allocatable> persons) {
	// // TODO Auto-generated method stub
	//
	// }

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
	public String getVorlesungsStundenInput() {

		return this.vorlesungsStundenInput.getText();
	}

	@Override
	public String getSelectedEventType() {
		return eventTypesListBox.getSelectedItemText();
	}

	@Override
	public void setEventTypes(List<String> eventTypes) {
		eventTypesListBox.clear();
		for (int i = 0; i < eventTypes.size(); i++) {
			eventTypesListBox.addItem((String) eventTypes.get(i).toString());
		}

	}

	@Override
	public void setSelectedEventType(String select) {
		for (int i = 0; i < eventTypesListBox.getItemCount(); i++) {
			if (eventTypesListBox.getItemText(i).equals(select)) {
				eventTypesListBox.setItemSelected(i, true);
			}
		}
	}

	@Override
	public void setDynamicFields(Attribute[] attributes) {

		for (int i = 0; i < attributes.length; i++) {

			String name = attributes[i].toString();

			eventTypesListBox.addItem(name);

		}

	}

	public void setConstraintKeys(String[] constraintKeys) {
	}

	@Override
	public void setTitelInput(String reservationName) {
		this.titelInput.setText(reservationName);

	}

	@Override
	public void setVorlesungsStundenInput(String input) {
		this.vorlesungsStundenInput.setText(input);

	}

	@Override
	public int getItemCountOfListBoxStudiengang() {
		return this.studiengangListBox.getItemCount();
	}

	@Override
	public String getItemTextOfListBoxStudiengang(int index) {
		return this.studiengangListBox.getItemText(index);
	}

	@Override
	public void setSelectedIndexOfListBoxStudiengang(int index) {
		this.studiengangListBox.setSelectedIndex(index);
	}

	@Override
	public void setContentOfListBoxStudiengang(List<String> content) {
		for (String item : content) {
			this.studiengangListBox.addItem(item);
		}
	}

	@Override
	public void show() {
		contentPanel.setVisible(true);
		
	}

	
}
