package org.rapla.client.edit.reservation.sample.gwt;


import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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

    FlowPanel generalInformation = new FlowPanel();


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
        initEvenTypeLB();
        // Language selection
        initLanguageLB();
        //Study course
        initCourseButton();

        //Label eventname
        Label eventname = new Label("Veranstaltungsname");
        //String name = "Veranstaltungsname";
        eventname.addStyleName("eventname");
        //tabelle.setWidget(1, 1, eventname);
        //tabelle.setWidget(1, 2 ,tb);
        initHorizontalPanel(eventname);
        eventname.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        //generalInformation.add(tb);

        content.add(contentRes);// Notiz Yvonne: Ressourcen - Implementierung (siehe mapfromReservation-Methode)
        // content.add(subView); //Notiz Yvonne: Inhalt von SampleAppointmentViewImpl.java wird hier hinzugef�gt
        tabPanel.add(subView, "Termin- und Ressourcenplanung");
        tabPanel.selectTab(0);

        initSaveDeleteCancelButtons();

        mapFromReservation(event);
        tb.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                getPresenter().changeEventName(tb.getText());
            }
        });


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
            content.add(button);
        }
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
