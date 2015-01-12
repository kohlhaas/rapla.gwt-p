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
    

    TextBox tb;

    

    String chosenEventType = "";
    String chosenLanguage = "";


    public void show(Reservation event) {
    	
    	/*Structuring GU*/
    	
    	//Popup for the whole eventplanning
        popup = RootPanel.get("raplaPopup");
        popup.setVisible(true);
        popup.addStyleName("popup");

        //Tabs for structuring eventplanning in general information - tab and appointment- and ressourceplanning 
    	tabPanel = new TabPanel();
        tabPanel.addStyleName("tabPanel");
        
        //Content tab1
        content = new FlowPanel();
        content.addStyleName("content");
        
        generalInformation = new FlowPanel();
        generalInformation.setStyleName("generalInformation");
        
        contentRes = new FlowPanel();
        
        
        

        tb = new TextBox();

        //Clear Panels
        popup.clear();
        content.clear();
        generalInformation.clear();
        
        //popup.add(content);
        popup.add(tabPanel);
        tabPanel.add(content, "Allgemeine Informationen");
        content.add(generalInformation);
        
        
        /* Filling structure */
        
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

    //Method to insert the AppointmentView as SubView to the ReservationView
    @Override
    public void addSubView(ReservationEditSubView<IsWidget> view) {
        IsWidget provideContent = view.provideContent();
        subView.add(provideContent.asWidget());
    }

}
