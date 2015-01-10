package org.rapla.client.edit.reservation.sample.gwt;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.datepicker.client.DatePicker;
import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.AppointmentView;
import org.rapla.client.edit.reservation.sample.AppointmentView.Presenter;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class AppointmentViewImpl extends AbstractView<Presenter> implements AppointmentView<IsWidget> {

    @Inject
    AppointmentFormater formatter;

    FlowPanel content = new FlowPanel();
    FlowPanel appointmentContent = new FlowPanel();
    DatePicker datePicker = new DatePicker();
    DatePicker datePicker2 = new DatePicker();
    Date dateStart = new Date();
    Date dateEnd = new Date();
    Label dateStartText = new Label();
    Label dateEndText = new Label();


    public void show(List<Appointment> appointments) {
        content.clear();

        // Set the default value

        // Set the value in the text box when the user selects a date
        initStartDatePicker();
        initEndDatePicker();
//TODO: ADD an extended DatePicker or smth similiar, to add the concrete time "08:00"...
        content.add(datePicker);
        content.add(datePicker2);
        content.add(dateStartText);
        content.add(dateEndText);
        Button newApp = new Button("new Termin");

        newApp.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                getPresenter().newAppButtonPressed(dateStart, dateEnd);
            }
        });
        content.add(newApp);
        content.add(appointmentContent);
        update(appointments);
    }


    public void update(List<Appointment> appointments) {
        appointmentContent.clear();
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Appointment app : appointments) {
            String shortSummary = formatter.getShortSummary(app);
            if (!isFirst) {
                builder.append(", ");
            } else {
                isFirst = false;
            }
            builder.append(shortSummary);

        }
        appointmentContent.add(new Label("Termine: " + builder.toString()));
    }

    private void initEndDatePicker() {
        datePicker2.addValueChangeHandler(new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                dateEnd = event.getValue();
                String dateString = DateTimeFormat.getMediumDateFormat().format(dateEnd);
                dateEndText.setText("ausgewaehlt Ende: " + dateString);
            }
        });
        datePicker2.setValue(new Date(), true);

    }

    private void initStartDatePicker() {
        datePicker.addValueChangeHandler(new ValueChangeHandler<Date>() {
            public void onValueChange(ValueChangeEvent<Date> event) {
                dateStart = event.getValue();
                String dateString = DateTimeFormat.getMediumDateFormat().format(dateStart);
                dateStartText.setText("ausgewaehlt Start: " + dateString);
            }
        });
        datePicker.setValue(new Date(), true);
    }

    @Override
    public IsWidget provideContent() {
        return content;
    }


}
