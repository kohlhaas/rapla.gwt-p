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
import org.rapla.entities.domain.Reservation;

public class ReservationViewImpl extends AbstractView<Presenter> implements ReservationView<IsWidget> {

    Panel popup;

    TabPanel tabPanel; //Tabs for General Information and Appointment+Ressources-Planning

    FlowPanel content;
    FlowPanel contentRes = new FlowPanel();
    FlowPanel subView = new FlowPanel();
    FlowPanel generalInformation;
    FlowPanel row1;
    FlowPanel part2 = new FlowPanel();

    Grid grid;

    VerticalPanel upDown;

    TextBox tb;

    String chosenEventType = "";
    String chosenLanguage = "";


    public void show(Reservation event) {

//        TODO: using the type in the name of the method or variable ... YES OR NO? Example:  initEventTypeListBox or initEventTypeLB or initEventType || content or contentPanel or PContent --> s. Hungarian Notation

    	/*Structuring GUI*/

        initRaplaPopupPanel();
        initTabPanel();
        initContentPanel();
        initGeneralInformationPanel();
        initRow1();
        initSecondGrid();
        initUpDownPanel();

        clearPanels();
        structuringPanels();

        /* Filling structure */
        initEventTypeListBox();
        initLanguageListBox();
        initCourseButton();
        initLabelEventNameInGrid();
        initEventNameTextBox();
        initLabelPlannedHoursInGrid();
        initTextBoxPlannedHoursInGrid();


        //Buttons for count up planned hours
        Button upButton = new Button();
        upButton.setStyleName("up");
        //String html = "<div><center><img src = '/images/TriangleUp.png' height = '7px' width = '7px'></img></center><label>Text</label></br></div>";
        //String html = "<div><center><img src = '/images/TriangleUp.png' height = '7px' width = '7px'></img></center></div>";
        //up.setHTML(html);
        upDown.add(upButton);
        
/*        Button down = new Button();
        down.setSize("15px", "15px");
        down.setStyleName("down");
        String html2 = "<div class = 'img' ><img class= 'img_1' src = '/images/triangledown_klein.png'></img></div>";
        down.setHTML(html2);
        upDown.add(down);  */

        part2.add(upDown);

        Label info = new Label("Sonstige Veranstaltungsinformationen:");
        part2.add(info);

        TextArea taInfo = new TextArea();
        part2.add(taInfo);


        initSaveDeleteCancelButtons();


    }

    private void initSecondGrid() {
        grid = new Grid(2, 2);
        grid.setStyleName("grid");
    }

    private void initUpDownPanel() {
        upDown = new VerticalPanel();
        upDown.setStyleName("upDown");
    }

    private void initTextBoxPlannedHoursInGrid() {
        TextBox tbPlanhour = new TextBox();
        tbPlanhour.setStyleName("tbPlanhour");
        grid.setWidget(1, 1, tbPlanhour);
    }

    private void initLabelPlannedHoursInGrid() {
        Label planhour = new Label("geplante Vorlesungsstunden");
        planhour.setStyleName("planhour");
        grid.setWidget(1, 0, planhour);
    }

    private void initLabelEventNameInGrid() {
        Label eventname = new Label("Veranstaltungsname");
        eventname.setStyleName("eventname");
        grid.setWidget(0, 0, eventname);
    }

    private void initRow1() {
        row1 = new FlowPanel();
        row1.setStyleName("zeile1");
    }

    private void structuringPanels() {
        popup.add(tabPanel);
        tabPanel.add(content, "Allgemeine Informationen");
        tabPanel.add(subView, "Termin- und Ressourcenplanung");
        tabPanel.selectTab(0);
        content.add(generalInformation);
        content.add(contentRes);// Notiz Yvonne: Ressourcen - Implementierung (siehe mapfromReservation-Methode)
        // content.add(subView); //Notiz Yvonne: Inhalt von SampleAppointmentViewImpl.java wird hier hinzugef�gt
        generalInformation.add(row1);
        generalInformation.add(part2);
        part2.add(grid);
    }

    private void initEventNameTextBox() {
        tb = new TextBox();
        tb.setStyleName("textbox");
        tb.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                getPresenter().changeEventName(tb.getText());
            }
        });
        grid.setWidget(0, 1, tb);
    }

    private void initCourseButton() {
        Button course = new Button("Studiengang");
        course.setStyleName("course");
        course.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                getPresenter().onCourseButtonClicked();
            }
        });

        row1.add(course);
    }

    private void initLanguageListBox() {
        ListBox language = new ListBox();
        language.addItem("Deutsch");
        language.addItem("Englisch");
        chosenLanguage = language.getSelectedValue();
        language.setStyleName("language");
        row1.add(language);
    }

    private void initEventTypeListBox() {
        ListBox eventType = new ListBox();
        eventType.setStyleName("eventtype");
        eventType.addItem("Lehrveranstaltung");
        eventType.addItem("Klausur");
        chosenEventType = eventType.getSelectedValue();
        row1.add(eventType);
    }

    private void initGeneralInformationPanel() {
        generalInformation = new FlowPanel();
        generalInformation.setStyleName("generalInformation");
    }

    private void initContentPanel() {
        content = new FlowPanel();
        content.setStyleName("content");
    }

    private void clearPanels() {
        popup.clear();
        tabPanel.clear();
        content.clear();
        generalInformation.clear();
        row1.clear();
        part2.clear();
        grid.clear();
        upDown.clear();
        contentRes.clear();
    }

    private void initTabPanel() {
        tabPanel = new TabPanel();
        tabPanel.addStyleName("tabPanel");
    }

    private void initRaplaPopupPanel() {
        popup = RootPanel.get("raplaPopup");
        popup.setVisible(true);
        popup.setStyleName("popup");
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
