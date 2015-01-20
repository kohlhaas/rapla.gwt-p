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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ReservationViewImpl extends AbstractView<Presenter> implements ReservationView<IsWidget> {
    /**
     * please use smth like hungarian Notation or make the vars better readable
     */

    Panel popup;

    TabPanel tabPanel; //Tabs for General Information and Appointment+Ressources-Planning

    FlowPanel content;
    FlowPanel contentRes = new FlowPanel();
    FlowPanel generalInformation;
    FlowPanel row1;
    FlowPanel part2;
    FlowPanel coursePanel;

    List<TabPanelRapla> tabs =  new ArrayList<>();


    Grid grid;

    VerticalPanel upDown;

    TextBox eventNameTB;

    ListBox language = new ListBox();
    Button course = new Button("Studiengang");
    Button hideButton = null;
    String chosenEventType = "";
    String chosenLanguage = "";
    String chosenEventType1 = "";
    Tree tree;
    boolean buttonRemoved = false;
    



    public void show(Reservation event) {

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
        initEventTypeListBoxes();
        //initCourseButton();
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
        initTabs();
        content.add(generalInformation);
        content.add(contentRes);// Notiz Yvonne: Ressourcen - Implementierung (siehe mapfromReservation-Methode)

        generalInformation.add(row1);
        generalInformation.add(coursePanel);
        generalInformation.add(part2);
        part2.add(grid);
        part2.add(upDown);
    }

    private void initTabs(){
        tabPanel.add(content, "Allgemeine Informationen");

        for (TabPanelRapla tab : tabs) {
            tabPanel.add(tab.getTab(),tab.getName());
        }

        tabPanel.selectTab(0);
    }

    private void initEventTypeListBoxes() {
        // Eventtype
        final DynamicType[] eventTypes = getPresenter().getAllEventTypes();
        final Locale locale = getRaplaLocale().getLocale();
        final ListBox eventTypeLB = new ListBox();

        initLabelCurrentEventType(locale);

        eventTypeLB.setStyleName("eventType");

        /**
         * SEHR UNSAUBER, nur grob, bitte ggf. verbessern --> ggf. in eigene Klasse oder Presenter
         */
        for (DynamicType dynamicType : eventTypes) {
            eventTypeLB.addItem(dynamicType.getName(locale));
        }
        row1.add(eventTypeLB);

        chosenEventType = eventTypeLB.getSelectedValue();
        if (chosenEventType.equalsIgnoreCase(null)) {
            eventTypeLB.setItemSelected(0, true);
            chosenEventType = eventTypeLB.getSelectedValue();

        }

        if(chosenEventType.equalsIgnoreCase("Lehrveranstaltung")){
        	initCourseButton();
        	//boolean changedLehrveranstaltung = false;
        	//actviateCourseButton = true;

        }

        mapDynamicTypesToChangeHandler(eventTypes, locale, eventTypeLB);


        //row1.add(eventTypeLB);

    }

    private void mapDynamicTypesToChangeHandler(final DynamicType[] eventTypes, final Locale locale, final ListBox eventTypeLB) {
        eventTypeLB.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
            	//changedLehrveranstaltung = true;

                language.clear();

                if(chosenEventType.equalsIgnoreCase("Lehrveranstaltung")){
                	removeCourseButton();
                	buttonRemoved = true;

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
                    //activateCourseButton = true;
                    initCourseButton();

                }

            }
        });
    }

    private void initLabelCurrentEventType(Locale locale) {
        String name = getPresenter().getEventType(locale);
            Label label= new Label(name);
            row1.add(label);
        }

    private void initLanguageListBox() {
        // Language selection
        chosenLanguage = language.getSelectedValue();
        language.setStyleName("language");
        row1.add(language);
    }

    private void initCourseButton() {

/**
 * Make that method smaller please
 */
        //Study course
    	
        course.setStyleName("course");
        row1.add(course);
        
        course.addClickHandler(new ClickHandler() {
        @Override
        public void onClick(ClickEvent e) {
        	if(buttonRemoved){
        		row1.add(course);
        		buttonRemoved = false;
        	}
            coursePanel.setVisible(true);
        }
    });
        final Locale locale = getRaplaLocale().getLocale();
        final Category[] allCourses = getPresenter().getCategory(locale, "Studiengänge");
        tree = new Tree();
        TreeItem studiengangTreeItem = new TreeItem();
        studiengangTreeItem.setText("Studiengänge");
        tree.addItem(studiengangTreeItem);
        /**
         * here you got all categories like Allgemein, Wirtschaft, Technik etc.
         */
        int i = 0;
        for (Category category : allCourses) {
        	
            //allCoursesLB.addItem(category.getName(locale));
        	tree.addItem(new TreeItem());
        	tree.getItem(i).setText(category.getName(locale));
        	
        	for(Category underCategory : category.getCategories()){
        		tree.getItem(i).addItem(new TreeItem(new CheckBox(underCategory.getName(locale))));
        	}
        	i++;
        	
        }
        coursePanel.add(tree);

        /**
         * here you got all underCategories "Technik --> Elektrotechnik,Informatik...| Wirtschaft --> BWl ..."
         * depending on the study category u chosed
         * i dont know if its really smart to do it this way, but he has f.e very nested attributes.. and given so, a map could be wrong
         */

  
        hideButton = new Button("Eingabe bestätigen");
        coursePanel.add(hideButton);
    	
        hideButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                coursePanel.setVisible(false);
            }
        });

            coursePanel.setVisible(false);
            //activateCourseButton = false;
        

    }

    
    private void removeCourseButton(){
    	row1.remove(course);
    	coursePanel.remove(tree);
    	coursePanel.remove(hideButton);
    	
    }

    private void initLabelEventNameInGrid() {
        //Label eventname
        Label eventname = new Label("Veranstaltungsname");
        eventname.setStyleName("eventname");
        grid.setWidget(0, 0, eventname);
    }

    private void initEventNameTextBox() {
        //TextBox for insert eventname
        eventNameTB = new TextBox();
        Locale locale = getRaplaLocale().getLocale();
        eventNameTB.setText(this.getPresenter().getCurrentReservationName(locale));
        eventNameTB.setStyleName("textbox");
        eventNameTB.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                getPresenter().changeReservationName(eventNameTB.getText());
            }
        });
        grid.setWidget(0, 1, eventNameTB);

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
    public void addSubView(String tabName, ReservationEditSubView<IsWidget> view) {
        IsWidget providedContent = view.provideContent();
        FlowPanel flowPanel = new FlowPanel();
        flowPanel.add(providedContent);
        TabPanelRapla aTab= new TabPanelRapla(tabName,flowPanel);
        tabs.add(aTab);
    }

}
