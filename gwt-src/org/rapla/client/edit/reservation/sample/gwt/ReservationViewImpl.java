package org.rapla.client.edit.reservation.sample.gwt;


import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;

import java.util.Locale;

public class ReservationViewImpl extends AbstractView<Presenter> implements ReservationView<IsWidget> {

    Panel popup;

    TabPanel tabPanel; //Tabs for General Information and Appointment+Ressources-Planning

    FlowPanel content;
    FlowPanel contentRes;
    FlowPanel subView = new FlowPanel();
    FlowPanel generalInformation;
    
    HorizontalPanel ho;
    HorizontalPanel horizontal2;
    
    VerticalPanel updown;
    
    TextBox tb;


    String chosenEventType = "";
    String chosenLanguage = "";


    public void show(Reservation event) {

    	/*Structuring GUI*/

        //Popup for the whole event planning
        popup = RootPanel.get("raplaPopup");
        popup.setVisible(true);
        popup.setStyleName("popup");

        //Tabs for structuring event-planning in general information - tab and appointment- and resource planning 
        tabPanel = new TabPanel();
        tabPanel.addStyleName("tabPanel");

        //Content tab 1 (Content generalInformation und ressourcen) --> fällt nachdem Ressourcen bei AppointmenView sind raus
        content = new FlowPanel();
        content.setStyleName("content");

        //includes content of generalInformation
        generalInformation = new FlowPanel();
        generalInformation.setStyleName("generalInformation");
        
        //Horizontal Panels for structuring Labels
        ho = new HorizontalPanel();
        ho.setStylePrimaryName("horizontal");
        
        horizontal2 = new HorizontalPanel();
        horizontal2.setStyleName("horizontal2");
        
        //Structuring up and down button
        updown = new VerticalPanel(); 
       
        //FlowPanel for resources-part
        contentRes = new FlowPanel();

        
        //Clear Panels
        popup.clear();
        tabPanel.clear();
        content.clear();
        generalInformation.clear();
        ho.clear();
        horizontal2.clear();
        updown.clear();
        contentRes.clear();
        

        //Building structure through panels
        popup.add(tabPanel);
        tabPanel.add(content, "Allgemeine Informationen");
        tabPanel.add(subView, "Termin- und Ressourcenplanung");
        tabPanel.selectTab(0);
        content.add(generalInformation);
        content.add(contentRes);// Notiz Yvonne: Ressourcen - Implementierung (siehe mapfromReservation-Methode)
        // content.add(subView); //Notiz Yvonne: Inhalt von SampleAppointmentViewImpl.java wird hier hinzugef�gt
        generalInformation.add(ho);
        generalInformation.add(horizontal2);
        horizontal2.add(updown);
        
        
        /* Filling structure */
        
        // Eventtype
        ListBox eventType = new ListBox();
        eventType.setStyleName("eventtype");
        eventType.addItem("Lehrveranstaltung");
        eventType.addItem("Klausur");
        chosenEventType = eventType.getSelectedValue();
        generalInformation.add(eventType);
        
        //DOPPELT initEvenTypeLB();
        
       
        // Language selection
        ListBox language = new ListBox();
        language.addItem("Deutsch");
        language.addItem("Englisch");
        chosenLanguage = eventType.getSelectedValue();
        language.setStyleName("language");
        generalInformation.add(language);
        
        //DOPPELT initLanguageLB();
        
        //Study course
        {
            Button course = new Button("Studiengang");
            course.setStyleName("course");
            course.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent e) {
                    getPresenter().onCourseButtonClicked();
                }
            });
            
            generalInformation.add(course);
        }
        
        //DOPPELT initCourseButton();

        //Label eventname (ho)
        Label eventname = new Label("Veranstaltungsname");
        eventname.setStyleName("eventname");
        ho.add(eventname);
        
        //TextBox for insert eventname (ho)
        {
            tb = new TextBox();
            tb.setStyleName("textbox");
            tb.addChangeHandler(new ChangeHandler(){

                @Override
                public void onChange(ChangeEvent event) {
                    getPresenter().changeEventName(tb.getText());
                }
            });
            ho.add(tb);
        }

        
        //Label planned lesson hours (horizontal2)
        Label planhour = new Label("geplante Vorlesungsstunden");
        planhour.setStyleName("planhour");
        horizontal2.add(planhour);
        
        //TextBox for insert planned hours (horizontal2)
        TextBox tbPlanhour = new TextBox();
        horizontal2.add(tbPlanhour);
        
        
        //Buttons for count up planned hours
        Button up = new Button();
        up.setStyleName("up");
        //String html = "<div><center><img src = '/images/TriangleUp.png' height = '7px' width = '7px'></img></center><label>Text</label></br></div>";
        String html = "<div><center><img src = '/images/TriangleUp.png' height = '7px' width = '7px'></img></center></div>";
        up.setHTML(html);
        updown.add(up);
        
        Button down = new Button();
        down.setStyleName("down");
        String html2 = "<div><center><img src = '/images/TriangleDown.png' height = '7px' width = '7px'></img></center></div>";
        down.setHTML(html2);
        updown.add(down);
        
        
        //DOPPELT initHorizontalPanel(eventname);

        initSaveDeleteCancelButtons();



    }

    private void initHorizontalPanel(Label eventname) {
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        horizontalPanel.add(eventname);
        horizontalPanel.add(tb);
        horizontalPanel.addStyleName("horizontal");
        generalInformation.add(horizontalPanel);
    }

    private void initLanguageLB() {
        ListBox language = new ListBox();
        language.addItem("Deutsch");
        language.addItem("Englisch");
        chosenLanguage = language.getSelectedValue();
        language.addStyleName("language");
        generalInformation.add(language);
    }

    private void initEvenTypeLB() {
        ListBox eventType = new ListBox();
        eventType.addItem("Lehrveranstaltung");
        eventType.addItem("Klausur");
        eventType.addStyleName("Eventtype");
        chosenEventType = eventType.getSelectedValue();
        generalInformation.add(eventType);
    }

    private void initCourseButton() {
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

    private void initSaveDeleteCancelButtons() {
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
            content.add(button);}
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
