package org.rapla.client.edit.reservation.impl.gwt;

import java.util.Locale;

import javax.inject.Inject;

import org.rapla.client.edit.reservation.impl.ReservationEditView;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.AppointmentFormater;
import org.rapla.entities.domain.Reservation;
import org.rapla.framework.RaplaLocale;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
 
public class ReservationEditViewImpl implements ReservationEditView {
    
    private Presenter presenter;
    @Inject
    RaplaLocale raplaLocale;
    @Inject
    AppointmentFormater formatter;

    FlowPanel content = new FlowPanel();

    FlowPanel contentRes = new FlowPanel();
    final TextBox tb = new TextBox();
    
    Panel popup;
    
    public void show(Reservation event)
    {
        popup = RootPanel.get("raplaPopup");
        popup.setVisible(true);
        popup.clear();
        content.clear();
        popup.add(content);
        content.add(new Label("Veranstaltung bearbeiten/anlegen"));
        content.add(tb);

        content.add( contentRes );

        {
            Button button = new Button("Cancel");
            button.addClickHandler(new ClickHandler() {
                
                @Override
                public void onClick(ClickEvent e) {
                    presenter.onCancelButtonClicked();
                }
            });
            content.add(button);
        }

        if (presenter.isDeleteButtonEnabled())
        {
            Button button = new Button("Loeschen");
            button.addClickHandler(new ClickHandler() {
                
                @Override
                public void onClick(ClickEvent e) {
                    presenter.onDeleteButtonClicked();
                }
            });
            content.add(button);
        }
        {
            Button button = new Button("Speichern");
            button.addClickHandler(new ClickHandler() {
                
                @Override
                public void onClick(ClickEvent e) {
                   presenter.onSaveButtonClicked();
                }
            });
            content.add(button);
        }
        mapFromReservation( event );
        tb.addChangeHandler( new ChangeHandler() {
            
            @Override
            public void onChange(ChangeEvent event) {
                presenter.changeEventName( tb.getText() );
            }
        });
    }

    public void mapFromReservation(Reservation event) {
        Locale locale = raplaLocale.getLocale();
        tb.setText( event.getName( locale));
        Allocatable[] resources = event.getAllocatables();
        Appointment[] appointments = event.getAppointments();
        {
            StringBuilder builder = new StringBuilder();
            for ( Allocatable res:resources)
            {
                builder.append( res.getName( locale));
            }
            contentRes.add(new Label("Ressourcen: " +builder.toString() ));

        }
        {
            StringBuilder builder = new StringBuilder();
            for ( Appointment app:appointments)
            {
                String shortSummary = formatter.getShortSummary(app);
                builder.append( shortSummary);
            }
            contentRes.add(new Label("Termine: " +builder.toString() ));
        }
    }
    
    public void hide()
    {
        popup.setVisible(false);
        content.clear();
    }
    
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
