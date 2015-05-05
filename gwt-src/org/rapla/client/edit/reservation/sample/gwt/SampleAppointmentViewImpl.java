package org.rapla.client.edit.reservation.sample.gwt;

import java.util.List;

import javax.inject.Inject;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.SampleAppointmentView;
import org.rapla.client.edit.reservation.sample.SampleAppointmentView.Presenter;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
 
public class SampleAppointmentViewImpl extends AbstractView<Presenter> implements SampleAppointmentView<IsWidget> {
    
    @Inject
    AppointmentFormater formatter;

    FlowPanel content = new FlowPanel();
    FlowPanel appointmentContent = new FlowPanel();

    public void show(List<Appointment> appointments)
    {
        content.clear();
        Button newApp = new Button("Neuer Termin");
        newApp.addClickHandler( new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                getPresenter().newAppButtonPressed();
            }
        });
        content.add( newApp);
        content.add( appointmentContent);
        update(appointments);
    }

    public void update(List<Appointment> appointments)
    {
        appointmentContent.clear();
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for ( Appointment app:appointments)
        {
            String shortSummary = formatter.getShortSummary(app);
            if ( !isFirst)
            {
                builder.append( ", ");
            }
            else
            {
                isFirst = false;
            }
            builder.append( shortSummary);
            
        }
        appointmentContent.add(new Label("Termine: " +builder.toString() ));
    }
   
    @Override
    public IsWidget provideContent() {
        return content;
    }

    
   
}
