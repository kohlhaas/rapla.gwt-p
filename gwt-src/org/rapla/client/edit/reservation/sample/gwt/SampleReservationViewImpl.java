package org.rapla.client.edit.reservation.sample.gwt;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.SampleReservationView;
import org.rapla.client.edit.reservation.sample.SampleReservationView.Presenter;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;

import java.util.Locale;

public class SampleReservationViewImpl extends AbstractView<Presenter> implements SampleReservationView<IsWidget> {

    FlowPanel content;

    FlowPanel contentRes;

    FlowPanel subView = new FlowPanel();

    TextBox tb;

    Panel popup;

    String chosenEventType = "";
    String chosenLanguage = "";


    public void show(Reservation event) {


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

        content.add(contentRes);
        content.add(subView);

        // Veranstaltungstyp
        ListBox eventType = new ListBox();
        eventType.addItem("Vorlesung");
        chosenEventType = eventType.getSelectedValue();
        content.add(eventType);

        // Sprachauswahl
        ListBox language = new ListBox();
        language.addItem("Deutsch");
        language.addItem("Englisch");
        chosenLanguage = eventType.getSelectedValue();
        content.add(language);

        //Studiengang
        {
            Button course = new Button("Studiengang");
            course.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent e) {
                    getPresenter().onCourseButtonClicked();
                }
            });
            content.add(course);
        }

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


    @Override
    public void addSubView(ReservationEditSubView<IsWidget> view) {
        IsWidget provideContent = view.provideContent();
        subView.add(provideContent.asWidget());
    }

}
