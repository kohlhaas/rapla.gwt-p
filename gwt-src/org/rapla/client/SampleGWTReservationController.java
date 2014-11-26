package org.rapla.client;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import org.rapla.client.internal.RaplaGWTClient;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Appointment;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.facade.ClientFacade;
import org.rapla.framework.RaplaException;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

public class SampleGWTReservationController implements GWTReservationController {

    RaplaGWTClient client;
    Logger logger = Logger.getLogger("reservationController");

    public @Inject SampleGWTReservationController(RaplaGWTClient client)
    {
        this.client = client;
    }
    @Override
    public void edit(final Reservation event, boolean isNew) {
        final TextBox tb = new TextBox();
        final PopupPanel popup = new PopupPanel();
        FlowPanel content = new FlowPanel();
        popup.add(content);
        content.add(new Label("Veranstaltung bearbeiten/anlegen"));
        content.add(tb);
        Locale locale = Locale.GERMANY;
        tb.setText( event.getName( locale));
        Allocatable[] resources = event.getAllocatables();
        Appointment[] appointments = event.getAppointments();
        {
            StringBuilder builder = new StringBuilder();
            for ( Allocatable res:resources)
            {
                builder.append( res.getName( locale));
            }
            content.add(new Label("Ressourcen: " +builder.toString() ));

        }
        {
            StringBuilder builder = new StringBuilder();
            for ( Appointment app:appointments)
            {
                String shortSummary = client.getAppointmentFormate().getShortSummary(app);
                builder.append( shortSummary);
            }
            content.add(new Label("Termine: " +builder.toString() ));
        }
        {
            Button button = new Button("Cancel");
            button.addClickHandler(new ClickHandler() {
                
                @Override
                public void onClick(ClickEvent e) {
                    popup.hide();
                }
            });
            content.add(button);
        }

        if (!isNew)
        {
            Button button = new Button("Loeschen");
            button.addClickHandler(new ClickHandler() {
                
                @Override
                public void onClick(ClickEvent e) {
                    try {
                        ClientFacade facade = client.getFacade();
                        facade.remove( event);
                    } catch (RaplaException e1) {
                        //TODO add exception handling
                        logger.log(Level.SEVERE, e1.getMessage(), e1);
                    }
                    popup.hide();
                }
            });
            content.add(button);
        }
        {
            Button button = new Button("Speichern");
            button.addClickHandler(new ClickHandler() {
                
                @Override
                public void onClick(ClickEvent e) {
                    Classification classification = event.getClassification();
                    Attribute first = classification.getType().getAttributes()[0];
                    String text = tb.getValue();
                    classification.setValue(first, text);
                    try {
                        ClientFacade facade = client.getFacade();
                        facade.store( event);
                    } catch (RaplaException e1) {
                        //TODO add exception handling
                        logger.log(Level.SEVERE, e1.getMessage(), e1);
                    }
                    popup.hide();
                }
            });
            content.add(button);
        }
        
        
        
        popup.center();
    }

}
