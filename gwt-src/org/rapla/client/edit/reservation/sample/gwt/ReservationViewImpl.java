package org.rapla.client.edit.reservation.sample.gwt;


import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Repeating;
import org.rapla.entities.domain.Reservation;

import java.util.Locale;

public class ReservationViewImpl extends AbstractView<Presenter> implements ReservationView<IsWidget> {

	Panel popup;
	
	TabPanel tabPanel; //Tabs for General Information and Appointment+Ressources-Planning
	
    FlowPanel content;

    FlowPanel contentRes;

    FlowPanel subView = new FlowPanel();
    
    FlowPanel generalInformation;
    
    FlowPanel appointmentOptionsPanel;

    TextBox tb;

    

    String chosenEventType = "";
    String chosenLanguage = "";
    
    DateTimeFormat df = DateTimeFormat.getFormat("dd.MM.yyyy");
    DateTimeFormat hoursFormat = DateTimeFormat.getFormat("HH");
    DateTimeFormat minutesFormat = DateTimeFormat.getFormat("mm");


    public void show(Reservation event) {

    	tabPanel = new TabPanel();
        content = new FlowPanel();
        generalInformation = new FlowPanel();
        contentRes = new FlowPanel();

        tb = new TextBox();
        popup = RootPanel.get("raplaPopup");
        popup.setVisible(true);
        
        popup.clear();
        content.clear();
        generalInformation.clear();
        
        //popup.add(content);
        popup.add(tabPanel);
        tabPanel.add(content, "Allgemeine Informationen");
        tabPanel.addStyleName("tabPanel");
        content.add(generalInformation);
        generalInformation.setStyleName("generalInformation");
        



        
//YS-Teil
        
        
        // Eventtype
        ListBox eventType = new ListBox();
        eventType.addItem("Lehrveranstaltung");
        eventType.addItem("Klausur");
        eventType.addStyleName("Eventtype");
        chosenEventType = eventType.getSelectedValue();
        generalInformation.add(eventType);
        
        // Language selection
        ListBox language = new ListBox();
        language.addItem("Deutsch");
        language.addItem("Englisch");
        chosenLanguage = eventType.getSelectedValue();
        language.addStyleName("language");
        generalInformation.add(language);
        
        //Study course
        {
            Button course = new Button("Studiengang");
            course.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent e) {
                    getPresenter().onCourseButtonClicked();
                }
            });
            course.addStyleName("Course");
            generalInformation.add(course);
        }
        
        //Tabelle Design
        HorizontalPanel ho = new HorizontalPanel();
        
        //Label eventname
        Label eventname = new Label("Veranstaltungsname");
        //String name = "Veranstaltungsname";
        eventname.addStyleName("eventname");
        //tabelle.setWidget(1, 1, eventname);
        //tabelle.setWidget(1, 2 ,tb);
        HTML name = new HTML("Veranstaltungsname");
        ho.add(eventname);
        ho.add(tb);
        ho.addStyleName("horizontal");
        generalInformation.add(ho);
        eventname.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        
        //generalInformation.add(tb);
        

        
        content.add(contentRes);// Notiz Yvonne: Ressourcen - Implementierung (siehe mapfromReservation-Methode)
       // content.add(subView); //Notiz Yvonne: Inhalt von SampleAppointmentViewImpl.java wird hier hinzugef�gt
        tabPanel.add(subView , "Temin- und Ressourcenplanung");
        tabPanel.selectTab(0);


               
        

        //Standard Buttons
        {
            Button button = new Button("Cancel");
            button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent e) {
                    getPresenter().onCancelButtonClicked();
                }
            });
            content.add(button);
        }

        if (getPresenter().isDeleteButtonEnabled()) {
            Button button = new Button("Loeschen");
            button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent e) {
                    getPresenter().onDeleteButtonClicked();
                }
            });
            content.add(button);
        }

        {
            Button button = new Button("Speichern");
            button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent e) {
                    getPresenter().onSaveButtonClicked();
                }
            });
            content.add(button);
        }
        mapFromReservation(event);
        tb.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                getPresenter().changeEventName(tb.getText());
            }
        });
        
        
        
        // Terminplanung View
        // Appointment List
        ListBox appointmentList = new ListBox();
        appointmentList.setStyleName("appointment-list");
        for(Appointment a : event.getAppointments()) {
        	String appointmentLabel = df.format(a.getStart()) + " "; // + " - " + df.format(a.getEnd());
        	appointmentList.addItem(appointmentLabel);
        }
        appointmentList.setVisibleItemCount(7);
        content.add(appointmentList);
        appointmentList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent change) {
				ListBox appointmentList = (ListBox) change.getSource();
				getPresenter().appointmentSelected(appointmentList.getSelectedIndex());
			}
        });     
        getPresenter().appointmentSelected(appointmentList.getSelectedIndex());
        
    }
    
    public void updateAppointmentOptionsPanel(Appointment selectedAppointment) {
        // Terminplanung View
        // Rechte Seite des Termin Panels
    	int widgetNo =content.getWidgetIndex(appointmentOptionsPanel);
    	if (widgetNo >= 0) {content.remove(widgetNo);} ;
        RadioButton[] selectRepeat = new RadioButton[5];
        FlowPanel selectRepeatPanel;
        Button convertToSingleEventsButton;
        FlowPanel appointmentDatesForm;
        IntegerBox startHourField, startMinuteField, endHourField, endMinuteField;
        DateBox startDateField, endDateField;
        FlowPanel startFields, endFields;
        Label startTimeColon,endTimeColon;

        // Create Panels and Widgets
    	appointmentOptionsPanel = new FlowPanel();
        appointmentOptionsPanel.setStyleName("appointment-options");
        content.add(appointmentOptionsPanel);
        
        // Repeat Radio Buttons
        selectRepeat[0] = new RadioButton("select-repeat", "Nicht wiederholen");
        selectRepeat[1] = new RadioButton("select-repeat", "Täglich");
        selectRepeat[2] = new RadioButton("select-repeat", "Wöchentlich");
        selectRepeat[3] = new RadioButton("select-repeat", "Monatlich");
        selectRepeat[4] = new RadioButton("select-repeat", "Jährlich");
        selectRepeat[0].setValue(true);
        selectRepeatPanel = new FlowPanel();
        appointmentOptionsPanel.add(selectRepeatPanel);
        for(RadioButton repeatButton : selectRepeat) {
        	selectRepeatPanel.add(repeatButton);
        }
        
        // Einzeltermine Button
        convertToSingleEventsButton = new Button("In Einzeltermine umwandeln");
        appointmentOptionsPanel.add(convertToSingleEventsButton);
        
        // Formular zur Zeit- & Datumswahl 
        appointmentDatesForm = new FlowPanel();
        appointmentDatesForm.setStyleName("appointment-date-form");
        appointmentOptionsPanel.add(appointmentDatesForm);
        
        
        startFields = new FlowPanel();
        startFields.setStyleName("start-datetime");
        appointmentDatesForm.add(startFields);
        
        startDateField = new DateBox();
        startDateField.setFormat(new DateBox.DefaultFormat(df));
        startDateField.setStyleName("date-field");
        startFields.add(startDateField);
        
        startHourField = new IntegerBox();
        startHourField.setStyleName("time-field");
        startHourField.setMaxLength(2);
        startHourField.setVisibleLength(2);
        startFields.add(startHourField);
        
        startTimeColon = new Label(" : ");
        startTimeColon.setStyleName("time-field");
        startFields.add(startTimeColon);
        
        startMinuteField = new IntegerBox();
        startMinuteField.setStyleName("time-field");
        startMinuteField.setMaxLength(2);
        startMinuteField.setVisibleLength(2);
        startFields.add(startMinuteField);
        
        endFields = new FlowPanel();
        endFields.setStyleName("end-datetime");
        appointmentDatesForm.add(endFields);
        
        //endDateField = new TextBox();
        endDateField = new DateBox();
        endDateField.setFormat(new DateBox.DefaultFormat(df));
        endDateField.setStyleName("date-field");
        
        endFields.add(endDateField);
        
        endHourField = new IntegerBox();
        endHourField.setStyleName("time-field");
        endHourField.setMaxLength(2);
        endHourField.setVisibleLength(2);
        endFields.add(endHourField);
        
        endTimeColon = new Label(" : ");
        endTimeColon.setStyleName("time-field");
        endFields.add(endTimeColon);
        
        endMinuteField = new IntegerBox();
        endMinuteField.setStyleName("time-field");
        endMinuteField.setMaxLength(2);
        endMinuteField.setVisibleLength(2);
        endFields.add(endMinuteField);
    	
        // Fill in data from appointment object
        // Check the box according to selected appointment
        Repeating repeat = selectedAppointment.getRepeating();
        RadioButton checked = selectRepeat[0]; 
        if(repeat != null) { 
        	switch (repeat.getType()) {
				case DAILY:
					checked = selectRepeat[1];
        			break;
				case MONTHLY:
					checked = selectRepeat[2];
					break;
				case WEEKLY:
					checked = selectRepeat[3];
					break;
				case YEARLY:
					checked = selectRepeat[4];
					break;
        	}
        	
        }
        checked.setValue(true);
        // Fill text fields
        startDateField.setValue(selectedAppointment.getStart());
        startHourField.setText(hoursFormat.format( selectedAppointment.getStart() ));
        startMinuteField.setText(minutesFormat.format( selectedAppointment.getStart() ));
        endDateField.setValue(selectedAppointment.getEnd());
        endHourField.setText(hoursFormat.format( selectedAppointment.getEnd() ));
        endMinuteField.setText(minutesFormat.format( selectedAppointment.getEnd() ));
		
	}

    public void mapFromReservation(Reservation event) {
        Locale locale = getRaplaLocale().getLocale();
        tb.setText(event.getName(locale));
        contentRes.clear();
        Allocatable[] resources = event.getAllocatables();
        {
            StringBuilder builder = new StringBuilder();
            for (Allocatable res : resources) {
                builder.append(res.getName(locale));
            }
            contentRes.add(new Label("Ressourcen: " + builder.toString()));

        }
    }

    public void hide() {
        popup.setVisible(false);
        content.clear();
    }


    @Override
    public void addSubView(ReservationEditSubView<IsWidget> view) {
        IsWidget provideContent = view.provideContent();
        subView.add(provideContent.asWidget());
    }

}
