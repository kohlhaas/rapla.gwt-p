package org.rapla.client.edit.reservation.sample.gwt;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.user.datepicker.client.DatePicker;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.AppointmentView;
import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.entities.domain.Repeating;

import javax.inject.Inject;

import java.util.Date;
import java.util.List;

public class AppointmentViewImpl extends AbstractView<Presenter> implements AppointmentView<IsWidget> {

    @Inject
    AppointmentFormater formatter;

    FlowPanel content = new FlowPanel();
    
    FlowPanel appointmentOptionsPanel;
        
    DateTimeFormat df = DateTimeFormat.getFormat("dd.MM.yyyy");
    DateTimeFormat hoursFormat = DateTimeFormat.getFormat("HH");
    DateTimeFormat minutesFormat = DateTimeFormat.getFormat("mm");


    public void show(List<Appointment> appointments) {
        content.clear();

        // Set the default value

//        // Set the value in the text box when the user selects a date
//        initStartDatePicker();
//        initEndDatePicker();
////TODO: ADD an extended DatePicker or smth similiar, to add the concrete time "08:00"...
//        content.add(datePicker);
//        content.add(datePicker2);
//        content.add(dateStartText);
//        content.add(dateEndText);
//        Button newApp = new Button("new Termin");
//
//        newApp.addClickHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                getPresenter().newAppButtonPressed(dateStart, dateEnd);
//            }
//        });
//        content.add(newApp);
//        content.add(appointmentContent);
//        update(appointments);
//        
        
        // Terminplanung View
        // Appointment List
        ListBox appointmentList = new ListBox();
        appointmentList.setStyleName("appointment-list");
        for(Appointment a : appointments) {
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


//    public void update(List<Appointment> appointments) {
//        appointmentContent.clear();
//        StringBuilder builder = new StringBuilder();
//        boolean isFirst = true;
//        for (Appointment app : appointments) {
//            String shortSummary = formatter.getShortSummary(app);
//            if (!isFirst) {
//                builder.append(", ");
//            } else {
//                isFirst = false;
//            }
//            builder.append(shortSummary);
//
//        }
//        appointmentContent.add(new Label("Termine: " + builder.toString()));
//    }
//
//    private void initEndDatePicker() {
//        datePicker2.addValueChangeHandler(new ValueChangeHandler<Date>() {
//            public void onValueChange(ValueChangeEvent<Date> event) {
//                dateEnd = event.getValue();
//                String dateString = DateTimeFormat.getMediumDateFormat().format(dateEnd);
//                dateEndText.setText("ausgewaehlt Ende: " + dateString);
//            }
//        });
//        datePicker2.setValue(new Date(), true);
//
//    }
//
//    private void initStartDatePicker() {
//        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
//            public void onValueChange(ValueChangeEvent<Date> event) {
//                dateStart = event.getValue();
//                String dateString = DateTimeFormat.getMediumDateFormat().format(dateStart);
//                dateStartText.setText("ausgewaehlt Start: " + dateString);
//            }
//        });
//        datePicker.setValue(new Date(), true);
//    }

    @Override
    public IsWidget provideContent() {
        return content;
    }


}
