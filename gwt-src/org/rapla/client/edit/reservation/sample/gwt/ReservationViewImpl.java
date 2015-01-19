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
import org.rapla.entities.Category;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.DynamicType;

import java.util.Locale;

public class ReservationViewImpl extends AbstractView<Presenter> implements ReservationView<IsWidget> {

    private static class MyDialog extends DialogBox {

        public MyDialog() {
            // Set the dialog box's caption.
            setText("My First Dialog");

            // Enable animation.
            setAnimationEnabled(true);

            // Enable glass background.
            setGlassEnabled(true);

            // DialogBox is a SimplePanel, so you have to set its widget property to
            // whatever you want its contents to be.


            Button ok = new Button("OK");
            ok.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    MyDialog.this.hide();
                }
            });
            setWidget(ok);
        }
    }


    Panel popup;

    TabPanel tabPanel; //Tabs for General Information and Appointment+Ressources-Planning

    FlowPanel content;
    FlowPanel contentRes = new FlowPanel();
    FlowPanel subView = new FlowPanel();
    FlowPanel generalInformation;
    FlowPanel row1;
    FlowPanel part2;
    FlowPanel coursePanel;


    Grid grid;

    VerticalPanel upDown;

    TextBox tb;

    ListBox language = new ListBox();
    ListBox allCoursesLB = new ListBox();
    ListBox allCoursesValuesLB = new ListBox();
    Button course = new Button("Studiengang");
    String chosenEventType = "";
    String chosenLanguage = "";
    Boolean activateCourseButton = false;


    public void show(Reservation event) {

//        TODO: using the type in the name of the method or variable ... YES OR NO? Example:  initEventTypeListBox or initEventTypeLB or initEventType || content or contentPanel or PContent --> s. Hungarian Notation

    	/*Structuring GUI*/

        initRaplaPopupPanel();
        initTabPanel();
        initContentPanel();
        initGeneralInformationPanel();
        initRow1();
        initCoursePanel();
        initPart2Panel();
        initSecondGrid();
        initUpDownPanel();

        clearPanels();
        structuringPanels();

        /* Filling structure */
        initEventTypeListBox();
        initCourseButton();
        initLabelEventNameInGrid();
        initEventNameTextBox();
        initLabelPlannedHoursInGrid();
        initTextBoxPlannedHoursInGrid();


        initUpButton();
        initLabelInfo();
        initTextAreaInfo();
    

        

        
/*        Button down = new Button();
        down.setSize("15px", "15px");
        down.setStyleName("down");
        String html2 = "<div class = 'img' ><img class= 'img_1' src = '/images/triangledown_klein.png'></img></div>";
        down.setHTML(html2);
        upDown.add(down);  */

        initSaveDeleteCancelButtons();


    }

    private void initRaplaPopupPanel() {
        popup = RootPanel.get("raplaPopup");
        popup.setVisible(true);
        popup.setStyleName("popup");
    }

    private void initTabPanel() {
        tabPanel = new TabPanel();
        tabPanel.addStyleName("tabPanel");
    }

    private void initContentPanel() {
        content = new FlowPanel();
        content.setStyleName("content");
    }

    private void initGeneralInformationPanel() {
        generalInformation = new FlowPanel();
        generalInformation.setStyleName("generalInformation");
    }

    private void initRow1() {
        row1 = new FlowPanel();
        row1.setStyleName("zeile1");
    }

    private void initCoursePanel() {
        coursePanel = new FlowPanel();
        coursePanel.setStyleName("coursePanel");

    }

    private void initPart2Panel() {
        //Second part of the structure
        part2 = new FlowPanel();

    }

    private void initSecondGrid() {
        grid = new Grid(2, 2);
        grid.setStyleName("grid");
    }

    private void initUpDownPanel() {
        upDown = new VerticalPanel();
        upDown.setStyleName("upDown");

    }

    private void clearPanels() {
        popup.clear();
        tabPanel.clear();
        content.clear();
        generalInformation.clear();
        row1.clear();
        coursePanel.clear();
        part2.clear();
        grid.clear();
        upDown.clear();
        contentRes.clear();
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
        generalInformation.add(coursePanel);
        generalInformation.add(part2);
        part2.add(grid);
        part2.add(upDown);
    }

    private void initEventTypeListBox() {
        // Eventtype
        final ListBox eventTypeLB = new ListBox();
        eventTypeLB.setStyleName("eventtype");
        final DynamicType[] eventTypes = getPresenter().getAllEventTypes();
        final Locale locale = getRaplaLocale().getLocale();
        final Category[] allCourses = getPresenter().getCategory(locale, "Studiengänge");
        /**
         * here you got all categories like Allgemein, Wirtschaft, Technik etc.
         */
        for (Category category : allCourses) {
            allCoursesLB.addItem(category.getName(locale));
        }

        /**
         * here you got all underCategories "Technik --> Elektrotechnik,Informatik...| Wirtschaft --> BWl ..."
         * depending on the study category u chosed
         */

        allCoursesLB.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                allCoursesValuesLB.clear();
                String selectedValue = allCoursesLB.getSelectedValue();
                for (Category category : allCourses) {
                    if (selectedValue.equals(category.getName(locale))) {
                        for (Category underCategory : category.getCategories()) {
                            allCoursesValuesLB.addItem(underCategory.getName(locale));
                        }

                    }
                }
            }

        });

        /**
         *
         */
        row1.add(allCoursesValuesLB);
        row1.add(allCoursesLB);

        /**
         * SEHR UNSAUBER, nur grob, bitte ggf. verbessern --> ggf. in eigene Klasse oder Presenter
         */
        for (DynamicType dynamicType : eventTypes) {
            eventTypeLB.addItem(dynamicType.getName(locale));
        }
        chosenEventType = eventTypeLB.getSelectedValue();
        if (chosenEventType.equalsIgnoreCase(null)) {
            eventTypeLB.setItemSelected(0, true);
            chosenEventType = eventTypeLB.getSelectedValue();
        }
        if (chosenEventType.equalsIgnoreCase("Lehrveranstaltung")) {
            activateCourseButton = true;
        }

        eventTypeLB.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                language.clear();
                if (chosenEventType.equalsIgnoreCase("Lehrveranstaltung")) {
                    removeCourseButton();

                }
                if (chosenEventType.equalsIgnoreCase("Prüfung")) {
                    initLanguageListBox();

                }
                for (DynamicType dynamicType : eventTypes) {
                    chosenEventType = eventTypeLB.getSelectedValue();
                    if (dynamicType.getName(locale).equals(chosenEventType)) {

                        for (Attribute attribute : dynamicType.getAttributes()) {
                            language.addItem(attribute.getName(locale));
                        }
                    }
                }
                if (chosenEventType.equalsIgnoreCase("Lehrveranstaltung")) {
                    activateCourseButton = true;
                    initCourseButton();
                }

            }
        });

        row1.add(eventTypeLB);

    }

    private void initLanguageListBox() {
        // Language selection
        chosenLanguage = language.getSelectedValue();
        language.setStyleName("language");
        row1.add(language);
    }

    private void initCourseButton() {


        //Study course
        if (activateCourseButton) {

            course.setStyleName("course");
            course.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent e) {
                    coursePanel.setVisible(true);


                    new MyDialog().show();
                    /**
                     * this method returns all "Veranstaltungstypen" in a DynamicType array, ask if you want only Veranstaltungstyp with name "Studiengang"
                     */
                }
            });

            row1.add(course);


            TreeItem root = new TreeItem();
            root.setText("root");
            root.addTextItem("item0");
            root.addTextItem("item1");
            root.addTextItem("item2");

            // Add a CheckBox to the tree
            TreeItem item = new TreeItem(new CheckBox("item3"));
            root.addItem(item);

            Tree t = new Tree();
            t.addItem(root);

            coursePanel.add(t);


            Button ausblenden = new Button("ausblenden");
            coursePanel.add(ausblenden);
            ausblenden.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent e) {
                    coursePanel.setVisible(false);
                }
            });

            coursePanel.setVisible(false);
            //activateCourseButton = false;
        }
    }

    private void removeCourseButton() {
        row1.remove(course);
        generalInformation.remove(coursePanel);
    }

    private void initLabelEventNameInGrid() {
        //Label eventname
        Label eventname = new Label("Veranstaltungsname");
        eventname.setStyleName("eventname");
        grid.setWidget(0, 0, eventname);
    }

    private void initEventNameTextBox() {
        //TextBox for insert eventname
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

    private void initLabelPlannedHoursInGrid() {
        Label planhour = new Label("geplante Vorlesungsstunden");
        planhour.setStyleName("planhour");
        grid.setWidget(1, 0, planhour);
    }

    private void initTextBoxPlannedHoursInGrid() {
        TextBox tbPlanhour = new TextBox();
        tbPlanhour.setStyleName("tbPlanhour");
        grid.setWidget(1, 1, tbPlanhour);
    }

    private void initUpButton() {
        //Button for count up planned hours
        Button upButton = new Button();
        upButton.setStyleName("up");
        //String html = "<div><center><img src = '/images/TriangleUp.png' height = '7px' width = '7px'></img></center><label>Text</label></br></div>";
        //String html = "<div><center><img src = '/images/TriangleUp.png' height = '7px' width = '7px'></img></center></div>";
        //up.setHTML(html);
        upDown.add(upButton);

    }

    private void initLabelInfo() {
        Label info = new Label("Sonstige Veranstaltungsinformationen:");
        part2.add(info);

    }

    private void initTextAreaInfo() {
        TextArea taInfo = new TextArea();
        taInfo.setStyleName("taInfo");
        part2.add(taInfo);

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
