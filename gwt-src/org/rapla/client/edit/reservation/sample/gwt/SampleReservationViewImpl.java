package org.rapla.client.edit.reservation.sample.gwt;

import java.util.Locale;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.SampleReservationView;
import org.rapla.client.edit.reservation.sample.SampleReservationView.Presenter;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
 
public class SampleReservationViewImpl extends AbstractView<Presenter> implements SampleReservationView<IsWidget> {
    
    FlowPanel content;

    FlowPanel contentRes;
    
    FlowPanel subView = new FlowPanel();
    
    TextBox tb;
    
    Panel popup;
    
    public void show(Reservation event)
    {
        content = new FlowPanel();
        contentRes = new FlowPanel();
        tb = new TextBox();
        popup = RootPanel.get("raplaPopup");
        popup.setVisible(true);
        popup.clear();
        content.clear();
        popup.add(content);
        content.add(new Label("Veranstaltung bearbeiten/anlegen"));
        content.add(tb);

        content.add( contentRes );
        content.add( subView );

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

        if (getPresenter().isDeleteButtonEnabled())
        {
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
        
        {
        	Button button2 = new Button("ANLEGEN");
        	content.add(button2);
        }
        mapFromReservation( event );
        tb.addChangeHandler( new ChangeHandler() {
            
            @Override
            public void onChange(ChangeEvent event) {
                getPresenter().changeEventName( tb.getText() );
            }
        });
    }

    public void mapFromReservation(Reservation event) {
        Locale locale = getRaplaLocale().getLocale();
        tb.setText( event.getName( locale));
        contentRes.clear();
        Allocatable[] resources = event.getAllocatables();
        {
            StringBuilder builder = new StringBuilder();
            for ( Allocatable res:resources)
            {
                builder.append( res.getName( locale));
            }
            contentRes.add(new Label("Ressourcen: " +builder.toString() ));

        }
    }
    
    public void hide()
    {
        popup.setVisible(false);
        content.clear();
    }


    @Override
    public void addSubView(ReservationEditSubView<IsWidget> view) {
        IsWidget provideContent = view.provideContent();
        subView.add( provideContent.asWidget());
    }
    
}
