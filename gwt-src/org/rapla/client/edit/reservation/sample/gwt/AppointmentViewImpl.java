package org.rapla.client.edit.reservation.sample.gwt;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.AppointmentView;
import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.RaplaType;
import org.rapla.entities.domain.*;
import org.rapla.entities.dynamictype.DynamicType;

import javax.inject.Inject;
import java.util.*;

public class AppointmentViewImpl extends AbstractView<Presenter> implements AppointmentView<IsWidget> {

    @Inject
    AppointmentFormater formatter;

    FlowPanel content = new FlowPanel();
    RadioButton[] selectRepeat = new RadioButton[5];
    FlowPanel selectRepeatPanel;

    FlowPanel appointmentPanel;
    FlowPanel resourcePanel;
    FlowPanel appointmentOptionsPanel;
    Button convertToSingleEventsButton;
    FlowPanel appointmentDatesForm;
    IntegerBox startHourField, startMinuteField, endHourField, endMinuteField;
    DateBox startDateField, endDateField;
    FlowPanel startFields, endFields;
    Label startTimeColon, endTimeColon;
    ListBox appointmentList;
    ListBox dynamicTypeList = new ListBox();
    ListBox allocatableList = new ListBox();
    Button nextFreeApp = new Button();
    TextBox nextFreeAppTB = new TextBox();

    DateTimeFormat df = DateTimeFormat.getFormat("dd.MM.yyyy");
    DateTimeFormat hoursFormat = DateTimeFormat.getFormat("HH");
    DateTimeFormat minutesFormat = DateTimeFormat.getFormat("mm");

    private FlowPanel resourceListsPanel;
    private ListBox resourceTypesList;
    private Map<String, ListBox> resourceLists;


    /**
     * save an appointment by calling : "getPresenter().newAppointmentButtonPressed(dateStart, dateEnd)"
     */

    public void show(Reservation reservation) {
        List<Appointment> appointments = Arrays.asList(reservation.getAppointments());
        List<Allocatable> resources = Arrays.asList(reservation.getAllocatables());

        content.clear();
        appointmentPanel = new FlowPanel();
        appointmentPanel.addStyleName("appointment-panel");
        content.add(appointmentPanel);

        // "Add appointment" Button
        Button addAppointment = new Button("Termin hinzufügen");
        addAppointment.addStyleName("add-appointment");
        addAppointment.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getPresenter().newAppointmentButtonPressed();
            }
        });
        appointmentPanel.add(addAppointment);

        // Appointment List
        appointmentList = new ListBox();
        updateAppointmentList(appointments, appointments.size() - 1);
        appointmentList.addStyleName("appointment-list");
        appointmentList.setVisibleItemCount(7);
        appointmentPanel.add(appointmentList);
        appointmentOptionsPanel = new FlowPanel();
        appointmentList.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent change) {
                ListBox appointmentList = (ListBox) change.getSource();
                getPresenter().appointmentSelected(appointmentList.getSelectedIndex());
            }
        });
        //fire change event to update appointment options panel
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), appointmentList);


        // Resources Panel
        resourcePanel = new FlowPanel();
        resourcePanel.addStyleName("resource-panel");
        content.add(resourcePanel);

        resourceTypesList = new ListBox();
        resourceTypesList.addStyleName("resources-types");
        resourcePanel.add(resourceTypesList);
        resourceListsPanel = new FlowPanel();
        resourceListsPanel.addStyleName("resources-lists");
        resourcePanel.add(resourceListsPanel);
        resourceLists = new HashMap<String, ListBox>();
        updateResources(resources);
    }

    public void updateAppointmentOptionsPanel(Appointment selectedAppointment) {
        // Rechte Seite des Termin Panels

        // Create Panels and Widgets
        appointmentOptionsPanel.clear();
        appointmentOptionsPanel.addStyleName("appointment-options");
        appointmentPanel.add(appointmentOptionsPanel);

        // Repeat Radio Buttons
        initRadioButtonRepeat();

        // Einzeltermine Button
        convertToSingleEventsButton = new Button("In Einzeltermine umwandeln");
        appointmentOptionsPanel.add(convertToSingleEventsButton);

        // Formular zur Zeit- & Datumswahl 
        appointmentDatesForm = new FlowPanel();
        appointmentDatesForm.addStyleName("appointment-date-form");
        appointmentOptionsPanel.add(appointmentDatesForm);

        initStartDateFields();
        initEndDateFields();

        Button removeAppointment = new Button("Termin löschen");
        removeAppointment.addStyleName("remove-appointment");
        removeAppointment.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getPresenter().removeAppointmentButtonPressed(appointmentList.getSelectedIndex());
            }
        });
        appointmentOptionsPanel.add(removeAppointment);

        // Fill in data from appointment object
        // Check the box according to selected appointment
        Repeating repeat = selectedAppointment.getRepeating();
        RadioButton checked = getCheckedRadioButton(repeat);

        checked.setValue(true);
        // Fill text fields
        startDateField.setValue(selectedAppointment.getStart());
        startHourField.setText(hoursFormat.format(selectedAppointment.getStart()));
        startMinuteField.setText(minutesFormat.format(selectedAppointment.getStart()));
        endDateField.setValue(selectedAppointment.getEnd());
        endHourField.setText(hoursFormat.format(selectedAppointment.getEnd()));
        endMinuteField.setText(minutesFormat.format(selectedAppointment.getEnd()));

    }

    public void updateAppointmentList(List<Appointment> appointments, int focus) {
        appointmentList.clear();
        for (Appointment a : appointments) {
            String appointmentLabel = df.format(a.getStart()) + " "; // + " - " + df.format(a.getEnd());
            appointmentList.addItem(appointmentLabel);
        }
        appointmentList.setSelectedIndex(focus);
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), appointmentList);
    }

    @Override
    public void updateResources(List<Allocatable> resources) {
        resourceLists.clear();
        resourceTypesList.clear();
        resourceTypesList.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                resourceListsPanel.clear();
                resourceListsPanel.add(
                        resourceLists.get(
                                resourceTypesList.getSelectedValue()
                        )
                );
            }
        });

        Locale locale = getRaplaLocale().getLocale();
        Map<DynamicType, List<Allocatable>> sortedResources = getPresenter().getSortedAllocatables();
        for (DynamicType resourceTypes : sortedResources.keySet()) {
            String resourceTypeName = resourceTypes.getName(locale);
            resourceTypesList.addItem(resourceTypeName);
            ListBox resourceList = new ListBox();
            resourceList.setVisibleItemCount(7);
            resourceList.addStyleName("resources-list");
            resourceLists.put(resourceTypeName, resourceList);
            for (Allocatable resource : sortedResources.get(resourceTypes)) {
                resourceList.addItem(resource.getName(locale));
            }
        }

        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), resourceTypesList);
    }

//    public void mapFromReservation(Reservation event) {
//        Locale locale = getRaplaLocale().getLocale();
//        tb.setText(event.getName(locale));
//        contentRes.clear();
//        
//        {
//            StringBuilder builder = new StringBuilder();
//            for (Allocatable res : resources) {
//                builder.append(res.getName(locale));
//            }
//            contentRes.add(new Label("Ressourcen: " + builder.toString()));
//
//        }
//    }

    //TODO: startDate and EndDate is kind of redundant, maybe using a method for both ?
    private void initStartDateFields() {
        startFields = new FlowPanel();
        startFields.addStyleName("start-datetime");
        appointmentDatesForm.add(startFields);

        startDateField = new DateBox();
        startDateField.setFormat(new DateBox.DefaultFormat(df));
        startDateField.addStyleName("date-field");
        startFields.add(startDateField);

        startHourField = new IntegerBox();
        startHourField.addStyleName("time-field");
        startHourField.setMaxLength(2);
        startHourField.setVisibleLength(2);
        startFields.add(startHourField);

        startTimeColon = new Label(" : ");
        startTimeColon.addStyleName("time-field");
        startFields.add(startTimeColon);

        startMinuteField = new IntegerBox();
        startMinuteField.addStyleName("time-field");
        startMinuteField.setMaxLength(2);
        startMinuteField.setVisibleLength(2);
        startFields.add(startMinuteField);
    }

    private void initEndDateFields() {
        endFields = new FlowPanel();
        endFields.addStyleName("end-datetime");
        appointmentDatesForm.add(endFields);

        //endDateField = new TextBox();
        endDateField = new DateBox();
        endDateField.setFormat(new DateBox.DefaultFormat(df));
        endDateField.addStyleName("date-field");

        endFields.add(endDateField);

        endHourField = new IntegerBox();
        endHourField.addStyleName("time-field");
        endHourField.setMaxLength(2);
        endHourField.setVisibleLength(2);
        endFields.add(endHourField);

        endTimeColon = new Label(" : ");
        endTimeColon.addStyleName("time-field");
        endFields.add(endTimeColon);

        endMinuteField = new IntegerBox();
        endMinuteField.addStyleName("time-field");
        endMinuteField.setMaxLength(2);
        endMinuteField.setVisibleLength(2);
        endFields.add(endMinuteField);
    }

    private RadioButton getCheckedRadioButton(Repeating repeat) {
        RadioButton checked = selectRepeat[0];
        if (repeat != null) {
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
        return checked;
    }

    private void initRadioButtonRepeat() {
        selectRepeat[0] = new RadioButton("select-repeat", "Nicht wiederholen");
        selectRepeat[1] = new RadioButton("select-repeat", "Täglich");
        selectRepeat[2] = new RadioButton("select-repeat", "Wöchentlich");
        selectRepeat[3] = new RadioButton("select-repeat", "Monatlich");
        selectRepeat[4] = new RadioButton("select-repeat", "Jährlich");
        selectRepeat[0].setValue(true);
        selectRepeatPanel = new FlowPanel();
        appointmentOptionsPanel.add(selectRepeatPanel);
        for (RadioButton repeatButton : selectRepeat) {
            selectRepeatPanel.add(repeatButton);
        }
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

    @Override
    public IsWidget provideContent() {
        return content;
    }

    @Override
    public void updateBookedResources(List<Allocatable> resources) {
        // TODO Auto-generated method stub

    }


}
