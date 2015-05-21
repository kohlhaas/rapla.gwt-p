package org.rapla.client.edit.reservation.sample.gwt;

import com.google.gwt.cell.client.CompositeCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.HasCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
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
import org.rapla.facade.Conflict;

import javax.inject.Inject;

import java.util.*;
import java.util.logging.Logger;

public class AppointmentViewImpl extends AbstractView<Presenter> implements AppointmentView<IsWidget> {

    @Inject
    AppointmentFormater formatter;

    FlowPanel content = new FlowPanel();
    RadioButton[] selectRepeat = new RadioButton[5];
    FlowPanel selectRepeatPanel;

    FlowPanel appointmentFP;
    FlowPanel resourceFP;
    FlowPanel appointmentOptionsFP;
    Button convertToSingleEventsBtn;
    FlowPanel appointmentDatesFP;
    TextBox startHourTB, startMinuteTB, endHourTB, endMinuteTB;
    DateBox startDateDB, endDateDB;
    FlowPanel startFieldsFP, endFieldsFP;
    Label startTimeColonL, endTimeColonL;
    CellList<Appointment> appointmentCL;
    ScrollPanel appointmentListSP;
    ListBox dynamicTypeLB = new ListBox();
    ListBox allocatableLB = new ListBox();
    Button nextFreeAppBtn = new Button();
    TextBox nextFreeAppTB = new TextBox();

    DateTimeFormat dateTimeFormat = DateTimeFormat.getFormat("dd.MM.yyyy");
    DateTimeFormat hoursFormat = DateTimeFormat.getFormat("HH");
    DateTimeFormat minutesFormat = DateTimeFormat.getFormat("mm");

    private FlowPanel resourceListsFP;
    private ListBox resourceTypesLB;
    private Map<String, ListBox> resourceListsM;

    ListDataProvider<Appointment> appointmentDataProvider = new ListDataProvider<>();

    SingleSelectionModel<Appointment> selectionModel;

    ListBox bookedResourcesLB = new ListBox();

    /**
     * TODO: change a current appointent, now its only possible to add one, but not to change the existing one.
     * TODO. In Case of usage, f.e. use a button "changed current appointment" and i can create a "change appointment" method
     */

    public void show(Reservation reservation) {
        List<Appointment> appointments = Arrays.asList(reservation.getAppointments());
        List<Allocatable> resources = Arrays.asList(reservation.getAllocatables());

        content.clear();
        appointmentFP = new FlowPanel();
        appointmentFP.addStyleName("appointment-panel");
        content.add(appointmentFP);

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
        appointmentFP.add(addAppointment);

        // Appointment List
        appointmentListSP = new ScrollPanel();
        updateAppointmentList(appointments, appointments.size() - 1);
        appointmentListSP.addStyleName("appointment-list-scroll");
        appointmentCL.addStyleName("appointment-list");
        appointmentFP.add(appointmentListSP);
        appointmentListSP.add(appointmentCL);

        appointmentOptionsFP = new FlowPanel();

        // Resources Panel
        resourceFP = new FlowPanel();
        resourceFP.addStyleName("resource-panel");
        content.add(resourceFP);

        FlowPanel resourceToolbar = new FlowPanel();
        resourceToolbar.addStyleName("resource-toolbar");
        resourceTypesLB = new ListBox();
        resourceTypesLB.addStyleName("resources-types");
        resourceToolbar.add(resourceTypesLB);
        resourceFP.add(resourceToolbar);
        
        VerticalPanel addRemoveResourceVp = new VerticalPanel(); 
        addRemoveResourceVp.addStyleName("resource-add-remove");
        Button addResource = new Button(">>");
        addResource.addStyleName("add-resource");
        addRemoveResourceVp.add(addResource);
        Button removeResourceBtn = new Button("<<");
        removeResourceBtn.addStyleName("remove-resource");
        addRemoveResourceVp.add(removeResourceBtn);

        resourceListsFP = new FlowPanel();
        resourceListsFP.addStyleName("resources-lists");
        resourceFP.add(resourceListsFP);
        resourceFP.add(addRemoveResourceVp);
        resourceListsM = new HashMap<String, ListBox>();
        bookedResourcesLB.addStyleName("booked-resources");
        bookedResourcesLB.setVisibleItemCount(7);
        resourceFP.add(bookedResourcesLB);
        updateBookedResources(Arrays.asList(reservation.getResources()));
        addResource.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String resourceType = resourceTypesLB.getSelectedItemText();
                getPresenter().addResourceButtonPressed(resourceListsM.get(resourceType).getSelectedIndex(), resourceType, getRaplaLocale().getLocale());
            }
        });
        removeResourceBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                getPresenter().removeResourceButtonPressed(bookedResourcesLB.getSelectedIndex());
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
        return "NONE";
    }

    private Date getStartDate() {
        Date startDate = startDateDB.getValue();
        startDate.setHours(Integer.parseInt(startHourTB.getValue()));
        startDate.setMinutes(Integer.parseInt(startMinuteTB.getValue()));
        return startDate;
    }

    private Date getEndDate() {
        Date endDate = endDateDB.getValue();
        endDate.setHours(Integer.parseInt(endHourTB.getValue()));
        endDate.setMinutes(Integer.parseInt(endMinuteTB.getValue()));
        return endDate;
    }

    private void setStartDate(Date date) {
        startDateDB.setValue(date);
        startHourTB.setValue(addZero(date.getHours()));
        startMinuteTB.setValue(addZero(date.getMinutes()));
    }

    private void setEndDate(Date date) {
        endDateDB.setValue(date);
        endHourTB.setValue(addZero(date.getHours()));
        endMinuteTB.setValue(addZero(date.getMinutes()));
    }

    private String addZero(int n) {
        if (n < 10)
            return "0" + n;
        else
            return "" + n;
    }

    public void updateAppointmentOptionsPanel(Appointment selectedAppointment) {
        // Create Panels and Widgets
        appointmentOptionsFP.clear();
        appointmentOptionsFP.addStyleName("appointment-options");
        appointmentFP.add(appointmentOptionsFP);

        if (selectedAppointment == null) {
            return;
        }

        // Repeat Radio Buttons
        initRadioButtonRepeat();

        // Einzeltermine Button
        convertToSingleEventsBtn = new Button("In Einzeltermine umwandeln");
        appointmentOptionsFP.add(convertToSingleEventsBtn);

        // Formular zur Zeit- & Datumswahl 
        appointmentDatesFP = new FlowPanel();
        appointmentDatesFP.addStyleName("appointment-date-form");
        appointmentOptionsFP.add(appointmentDatesFP);

        initStartDateFields();
        initEndDateFields();
        // "next free appointment" Button
        nextFreeAppBtn.setText("nächste freie Veranstaltung");
        nextFreeAppBtn.addStyleName("next-free-appointment");
        appointmentDatesFP.add(nextFreeAppBtn);
        nextFreeAppBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Date[] dates = getPresenter().nextFreeDateButtonPressed(getStartDate(), getEndDate());
                if (dates != null) {
                    setStartDate(dates[0]);
                    setEndDate(dates[1]);
                }
            }
        });

        // Fill in data from appointment object
        // Check the box according to selected appointment
        Repeating repeat = selectedAppointment.getRepeating();
        RadioButton checked = getRadioButtonToCheck(repeat);

        checked.setValue(true);
        // Fill text fields
        startDateDB.setValue(selectedAppointment.getStart());
        startHourTB.setText(hoursFormat.format(selectedAppointment.getStart()));
        startMinuteTB.setText(minutesFormat.format(selectedAppointment.getStart()));
        endDateDB.setValue(selectedAppointment.getEnd());
        endHourTB.setText(hoursFormat.format(selectedAppointment.getEnd()));
        endMinuteTB.setText(minutesFormat.format(selectedAppointment.getEnd()));

        HistoryManager.getInstance().trackWidget(startDateDB);
        HistoryManager.getInstance().trackWidget(endDateDB);
        HistoryManager.getInstance().trackWidget(startHourTB);
        HistoryManager.getInstance().trackWidget(startMinuteTB);
        HistoryManager.getInstance().trackWidget(endHourTB);
        HistoryManager.getInstance().trackWidget(endMinuteTB);
        
        startDateDB.addValueChangeHandler(new ValueChangeHandler<Date>() {
			@Override
			public void onValueChange(ValueChangeEvent<Date> event) {
				DateBox startDateField = (DateBox) event.getSource();
				endDateDB.setValue(startDateField.getValue());
			}
        });
        startHourTB.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				TextBox startHourField = (TextBox) event.getSource();
				endHourTB.setText( "" + (Integer.parseInt(startHourField.getValue()) + 1) );
			}
        });
        
        for(TextBox textBox : new TextBox[]{startHourTB, startMinuteTB, endHourTB, endMinuteTB}) {
        	textBox.addChangeHandler(new ChangeHandler() {
    			@Override
    			public void onChange(ChangeEvent event) {
    				appointmentChanged(event);
    			}
    		});
        }
        
        for(DateBox dateBox : new DateBox[]{startDateDB, endDateDB}) {
        	dateBox.addValueChangeHandler(new ValueChangeHandler<Date>() {
    			@Override
    			public void onValueChange(ValueChangeEvent<Date> event) {
    				appointmentChanged(event);
    			}
    		});
        }
        
        for(RadioButton radioButton : selectRepeat) {
        	radioButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
    			@Override
    			public void onValueChange(ValueChangeEvent<Boolean> event) {
    				appointmentChanged(event);
    			}
    		});
        }
        
        
    }
    
    private void appointmentChanged(GwtEvent event) {
    	Conflict[] conflicts = getPresenter().saveAppointment(
				((SingleSelectionModel<Appointment>) appointmentCL.getSelectionModel()).getSelectedObject(),
				getStartDate(),
				getEndDate(),
				getSelectedRepeating()
		);
		if(conflicts.length<1) {
			//success
		}
		else {
			//show conflicts
		}
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
                return dateTimeFormat.format(appointment.getStart());
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
        appointmentCL = new CellList<>(appointmentCell);
        appointmentCL.setRowCount(appointments.size());
        appointmentDataProvider.addDataDisplay(appointmentCL);

        if (selectionModel == null) {
            selectionModel = new SingleSelectionModel<Appointment>(keyProvider);
            selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
                @Override
                public void onSelectionChange(SelectionChangeEvent event) {
                    updateAppointmentOptionsPanel(selectionModel.getSelectedObject());
                }
            });
        }
        appointmentCL.setSelectionModel(selectionModel);

        if (focus >= 0) {
            selectionModel.setSelected(appointments.get(focus), true);
        } else {
            selectionModel.setSelected(null, true);
        }
        appointmentListSP.onResize();
    }

    @Override
    public void updateResources(List<Allocatable> resources) {
        resourceListsM.clear();
        resourceTypesLB.clear();
        resourceTypesLB.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                resourceListsFP.clear();
                resourceListsFP.add(
                        resourceListsM.get(
                                resourceTypesLB.getSelectedValue()
                        )
                );
            }
        });

        Locale locale = getRaplaLocale().getLocale();
        Map<DynamicType, List<Allocatable>> sortedResources = getPresenter().getSortedAllocatables();
        for (DynamicType resourceTypes : sortedResources.keySet()) {
            String resourceTypeName = resourceTypes.getName(locale);
            resourceTypesLB.addItem(resourceTypeName);
            ListBox resourceList = new ListBox();
            resourceList.setVisibleItemCount(7);
            resourceList.addStyleName("resources-list");
            resourceListsM.put(resourceTypeName, resourceList);
            for (Allocatable resource : sortedResources.get(resourceTypes)) {
                resourceList.addItem(resource.getName(locale));
            }
        }
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), resourceTypesLB);
    }

    private void initStartDateFields() {
        startFieldsFP = new FlowPanel();
        startFieldsFP.addStyleName("start-datetime");
        appointmentDatesFP.add(startFieldsFP);

        startDateDB = new DateBox();
        startDateDB.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
        startDateDB.addStyleName("date-field");
        startFieldsFP.add(startDateDB);

        startHourTB = new TextBox();
        startHourTB.addStyleName("time-field");
        startHourTB.setMaxLength(2);
        startHourTB.setVisibleLength(2);
        startFieldsFP.add(startHourTB);

        startTimeColonL = new Label(" : ");
        startTimeColonL.addStyleName("time-field");
        startFieldsFP.add(startTimeColonL);

        startMinuteTB = new TextBox();
        startMinuteTB.addStyleName("time-field");
        startMinuteTB.setMaxLength(2);
        startMinuteTB.setVisibleLength(2);
        startFieldsFP.add(startMinuteTB);
    }

    private void initEndDateFields() {
        endFieldsFP = new FlowPanel();
        endFieldsFP.addStyleName("end-datetime");
        appointmentDatesFP.add(endFieldsFP);

        endDateDB = new DateBox();
        endDateDB.setFormat(new DateBox.DefaultFormat(dateTimeFormat));
        endDateDB.addStyleName("date-field");

        endFieldsFP.add(endDateDB);

        endHourTB = new TextBox();
        endHourTB.addStyleName("time-field");
        endHourTB.setMaxLength(2);
        endHourTB.setVisibleLength(2);
        endFieldsFP.add(endHourTB);

        endTimeColonL = new Label(" : ");
        endTimeColonL.addStyleName("time-field");
        endFieldsFP.add(endTimeColonL);

        endMinuteTB = new TextBox();
        endMinuteTB.addStyleName("time-field");
        endMinuteTB.setMaxLength(2);
        endMinuteTB.setVisibleLength(2);
        endFieldsFP.add(endMinuteTB);
    }

    private RadioButton getRadioButtonToCheck(Repeating repeat) {
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
        selectRepeat[0].setFormValue("NONE");
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
        appointmentOptionsFP.add(selectRepeatPanel);
        for (RadioButton repeatButton : selectRepeat) {
            selectRepeatPanel.add(repeatButton);
        }
        HistoryManager.getInstance().trackWidget(selectRepeatPanel);
    }
    
    private RepeatingType getSelectedRepeating() {
    	RepeatingType r = null;
    	int checked = 0;
    	for(int i = 1; i < selectRepeat.length; i++) {
    		if( selectRepeat[i].getValue() ) {
    			checked = i;
    		}
    	}
    	switch(checked) {
    		case 1:
    			r = Repeating.DAILY;
    			break;
    		case 2:
    			r = Repeating.WEEKLY;
    			break;
    		case 3:
    			r = Repeating.MONTHLY;
    			break;
    		case 4:
    			r = Repeating.YEARLY;
    			break;
    	}
    	return r;
    }

    @Override
    public IsWidget provideContent() {
        return content;
    }

    @Override
    public void updateBookedResources(List<Allocatable> resources) {
        bookedResourcesLB.clear();
        for (Allocatable resource : resources) {
            bookedResourcesLB.addItem(resource.getName(getRaplaLocale().getLocale()));
        }
    }

}
