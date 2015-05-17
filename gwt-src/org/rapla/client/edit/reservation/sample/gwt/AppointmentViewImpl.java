package org.rapla.client.edit.reservation.sample.gwt;

import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.history.HistoryManager;
import org.rapla.client.edit.reservation.sample.AppointmentView;
import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.domain.*;
import org.rapla.entities.dynamictype.DynamicType;

import javax.inject.Inject;
import java.util.*;
import java.util.logging.Logger;

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
    TextBox startHourField, startMinuteField, endHourField, endMinuteField;
    DateBox startDateField, endDateField;
    FlowPanel startFields, endFields;
    Label startTimeColon, endTimeColon;
    CellList<Appointment> appointmentList;
    ScrollPanel appointmentListScroll;
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

    ListDataProvider<Appointment> appointmentDataProvider = new ListDataProvider<>();

    SingleSelectionModel<Appointment> selectionModel;

    ListBox bookedResources = new ListBox();

    /**
     * TODO: change a current appointent, now its only possible to add one, but not to change the existing one.
     * TODO. In Case of usage, f.e. use a button "changed current appointment" and i can create a "change appointment" method
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
                String selectedRepeatRB = getSelectedRepeatRB(selectRepeat);
                Logger.getGlobal().info("selectedRepeat:" + selectedRepeatRB);
                getPresenter().newAppointmentButtonPressed(getStartDate(), getEndDate(),RepeatingType.findForString(selectedRepeatRB));
            }
        });
        appointmentPanel.add(addAppointment);

        // Appointment List
        //appointmentList = new CellList<>(null);
        appointmentListScroll = new ScrollPanel();
        updateAppointmentList(appointments, appointments.size() - 1);
        appointmentListScroll.addStyleName("appointment-list-scroll");
        appointmentList.addStyleName("appointment-list");
        appointmentPanel.add(appointmentListScroll);
        appointmentListScroll.add(appointmentList);

        appointmentOptionsPanel = new FlowPanel();

        // Resources Panel
        resourcePanel = new FlowPanel();
        resourcePanel.addStyleName("resource-panel");
        content.add(resourcePanel);

        FlowPanel resourceToolbar = new FlowPanel();
        resourceToolbar.addStyleName("resource-toolbar");
        resourceTypesList = new ListBox();
        resourceTypesList.addStyleName("resources-types");
        resourceToolbar.add(resourceTypesList);
        Button addResource = new Button("Ressource hinzufügen");
        addResource.addStyleName("add-resource");
        resourceToolbar.add(addResource);
        resourcePanel.add(resourceToolbar);

        resourceListsPanel = new FlowPanel();
        resourceListsPanel.addStyleName("resources-lists");
        resourcePanel.add(resourceListsPanel);
        resourceLists = new HashMap<String, ListBox>();
        bookedResources.addStyleName("booked-resources");
        bookedResources.setVisibleItemCount(7);
        resourcePanel.add(bookedResources);
        updateBookedResources(Arrays.asList(reservation.getResources()));
        addResource.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String resourceType = resourceTypesList.getSelectedItemText();
                getPresenter().addResourceButtonPressed(resourceLists.get(resourceType).getSelectedIndex(), resourceType, getRaplaLocale().getLocale());
            }
        });
        updateResources(resources);
    }

    private String getSelectedRepeatRB(RadioButton[] radioButtons) {
        for (RadioButton radioButton : radioButtons) {
            if (radioButton.getValue()) {
                return radioButton.getFormValue();
            }
        }
        return "NOTHING";
    }

    private Date getStartDate() {
        Date startDate = startDateField.getValue();
        startDate.setHours(Integer.parseInt(startHourField.getValue()));
        startDate.setMinutes(Integer.parseInt(startMinuteField.getValue()));
        return startDate;
    }

    private Date getEndDate() {
        Date endDate = endDateField.getValue();
        endDate.setHours(Integer.parseInt(endHourField.getValue()));
        endDate.setMinutes(Integer.parseInt(endMinuteField.getValue()));
        return endDate;
    }

    private void setStartDate(Date date) {
        startDateField.setValue(date);
        startHourField.setValue(addZero(date.getHours()));
        startMinuteField.setValue(addZero(date.getMinutes()));
    }

    private void setEndDate(Date date) {
        endDateField.setValue(date);
        endHourField.setValue(addZero(date.getHours()));
        endMinuteField.setValue(addZero(date.getMinutes()));
    }

    private String addZero(int n) {
        if (n < 10)
            return "0" + n;
        else
            return "" + n;
    }

    public void updateAppointmentOptionsPanel(Appointment selectedAppointment) {
        // Create Panels and Widgets
        appointmentOptionsPanel.clear();
        appointmentOptionsPanel.addStyleName("appointment-options");
        appointmentPanel.add(appointmentOptionsPanel);

        if (selectedAppointment == null) {
            Logger.getGlobal().info("### null appointment selected");
            return;
        } else {
            Logger.getGlobal().info("### appointment selected: " + selectedAppointment.getStart()+" with repeatingType: "+ selectedAppointment.getRepeating());

        }

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
        // "next free appointment" Button
        nextFreeApp.setText("nächste freie Veranstaltung");
        nextFreeApp.addStyleName("next-free-appointment");
        appointmentDatesForm.add(nextFreeApp);
        nextFreeApp.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Date[] dates = getPresenter().nextFreeDateButtonPressed(getStartDate(), getEndDate());
                if (dates != null) {
                    setStartDate(dates[0]);
                    setEndDate(dates[1]);
                } else {
                    // TODO: Show popup that no free appointment is available
                }
            }
        });

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

        HistoryManager.getInstance().trackWidget(startHourField);
        HistoryManager.getInstance().trackWidget(startMinuteField);
        HistoryManager.getInstance().trackWidget(endHourField);
        HistoryManager.getInstance().trackWidget(endMinuteField);
    }

    public void updateAppointmentList(List<Appointment> appointments, int focus) {
        appointmentDataProvider.setList(appointments);
        ProvidesKey<Appointment> keyProvider = new ProvidesKey<Appointment>() {
            @Override
            public String getKey(Appointment a) {
                return a.getId();
            }
        };
        List<HasCell<Appointment, ?>> row = new ArrayList<>();
        TextCell labelCell = new TextCell();
        ButtonImageCell iconCell = new ButtonImageCell();
        Column<Appointment, String> labelColumn = new Column<Appointment, String>(labelCell) {
            @Override
            public String getValue(Appointment appointment) {
                return df.format(appointment.getStart());
            }
        };
        Column<Appointment, String> iconColumn = new Column<Appointment, String>(iconCell) {
            @Override
            public String getValue(Appointment appointment) {
                return "images/black/080 Trash.png";
            }
        };
        iconColumn.setFieldUpdater(new FieldUpdater<Appointment, String>() {
            @Override
            public void update(int index, Appointment object, String value) {
                getPresenter().removeAppointmentButtonPressed(index);
            }
        });
        row.add(labelColumn);
        row.add(iconColumn);
        CompositeCell<Appointment> appointmentCell = new CompositeCell<Appointment>(row);
        appointmentList = new CellList<>(appointmentCell);
        appointmentList.setRowCount(appointments.size());
        appointmentDataProvider.addDataDisplay(appointmentList);

        if (selectionModel == null) {
            selectionModel = new SingleSelectionModel<Appointment>(keyProvider);
            selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                @Override
                public void onSelectionChange(SelectionChangeEvent event) {
                    updateAppointmentOptionsPanel(selectionModel.getSelectedObject());
                }
            });
        }
        appointmentList.setSelectionModel(selectionModel);

        if (focus >= 0) {
            selectionModel.setSelected(appointments.get(focus), true);
            //Logger.getGlobal().info("### focus: " + selectionModel.isSelected(appointments.get(focus)));
        } else {
            selectionModel.setSelected(null, true);
        }
        appointmentListScroll.onResize();
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
        // TODO: Remove after demo; selects the second in list because looks nicer
        if (sortedResources.keySet().size() > 1)
            resourceTypesList.setItemSelected(1, true);
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), resourceTypesList);
    }

//    public void mapFromReservation(Reservation event) {
//        Locale locale = getRaplaLocale().getLocale();
//        eventNameTB.setText(event.getName(locale));
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

        startHourField = new TextBox();
        startHourField.addStyleName("time-field");
        startHourField.setMaxLength(2);
        startHourField.setVisibleLength(2);
        startFields.add(startHourField);

        startTimeColon = new Label(" : ");
        startTimeColon.addStyleName("time-field");
        startFields.add(startTimeColon);

        startMinuteField = new TextBox();
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

        endHourField = new TextBox();
        endHourField.addStyleName("time-field");
        endHourField.setMaxLength(2);
        endHourField.setVisibleLength(2);
        endFields.add(endHourField);

        endTimeColon = new Label(" : ");
        endTimeColon.addStyleName("time-field");
        endFields.add(endTimeColon);

        endMinuteField = new TextBox();
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
                case WEEKLY:
                    checked = selectRepeat[2];
                    break;
                case MONTHLY:
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
        selectRepeat[0].setFormValue("NOTHING");
        selectRepeat[1] = new RadioButton("select-repeat", "Täglich");
        selectRepeat[1].setFormValue("daily");
        selectRepeat[2] = new RadioButton("select-repeat", "Wöchentlich");
        selectRepeat[2].setFormValue("weekly");
        selectRepeat[3] = new RadioButton("select-repeat", "Monatlich");
        selectRepeat[3].setFormValue("monthly");
        selectRepeat[4] = new RadioButton("select-repeat", "Jährlich");
        selectRepeat[4].setFormValue("yearly");
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
        bookedResources.clear();
        for (Allocatable resource : resources) {
            bookedResources.addItem(resource.getName(getRaplaLocale().getLocale()));
        }
    }


}
