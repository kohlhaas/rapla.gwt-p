package org.rapla.client.edit.reservation.sample.gwt;


import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.history.HistoryManager;
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

    Panel popup;
    FlowPanel headerPanel;

    TabPanel tabPanel; //Tabs for General Information and Appointment+Ressources-Planning

    FlowPanel content;
    FlowPanel contentRes = new FlowPanel();
    FlowPanel generalInformation;
    FlowPanel row1;
    FlowPanel part2;
    FlowPanel coursePanel;

    HorizontalPanel saveDeleteCancelHPanel;
    FlowPanel historyMgmtPanel;

    List<TabPanelRapla> tabs = new ArrayList<>();


    Grid grid;

    VerticalPanel upDown;

    TextBox eventNameTB;

    ListBox language = new ListBox();
    ListBox examinationTypeLB;
    ListBox allLanguageLB = new ListBox();
    ListBox allKeys = new ListBox();
    ListBox allKeys2 = new ListBox();
    Button course = new Button("Studiengang auswählen");
    ListBox allCurrentAttributes = new ListBox();

    Button hideButton = null;
    Button deselectButton = null;
    CheckBox [] cb;
    Label planhour;
    Label lectureCourseLabel;
    Label captionLabel;
    //Label lectureCourseLabel;
    String html = "<div class = 'img' ><img class= 'img_1' src = '/images/081 Pen.png'></img></div>";
    String chosenEventType = "";
    String chosenLanguage = "";
    Tree tree;
    boolean buttonRemoved = false;
    boolean changedToLehrveranstaltung;
    boolean firstChange = true;
    

    public void show(Reservation event) {

    	/*Structuring GUI*/

        initRaplaPopupPanel();
        initHeaderPanel();
        initTabPanel();
        initSaveDeleteCancelHPanel();
        initContentPanel();
        initGeneralInformationPanel();
        initRow1();
        initCoursePanel();
        initPart2Panel();
        initSecondGrid();
        //initUpDownPanel();

        clearPanels();
        structuringPanels();

        /* Filling structure */
        initCaptionLabel();
        initEventTypeListBoxes();
        
        //initCourseButton();
        initLabelEventNameInGrid();
        initEventNameTextBox();
        initLabelPlannedHoursInGrid();
        initTextBoxPlannedHoursInGrid();


        // initUpButton();
        // initDownButton();
        initLabelInfo();
        initTextAreaInfo();
        
        
    

        

        
/*        Button down = new Button();
        down.setSize("15px", "15px");
        down.setStyleName("down");
        String html2 = "<div class = 'img' ><img class= 'img_1' src = '/images/triangledown_klein.png'></img></div>";
        down.setHTML(html2);
        upDown.add(down);  */

        initSaveDeleteCancelButtons();
        final Locale locale = getRaplaLocale().getLocale();

        /**
         * das sind die Namen für getCategory Werte (Methode von unten), Theorethisch koentest du die beiden listboxen voneinander abhaengig machen
         */
/*        List<String> keys = getPresenter().getCategoryNames(locale);
        for (String key : keys) {
            allKeys.addItem(key);
        }

       row1.add(allKeys);*/


        /**
         *
         */
        


/**
 * about current attributes
 */
      /*  //für Präsentation ausblenden
        
        for (String s : getPresenter().getAllCurrentAttributes(locale)) {
            allCurrentAttributes.addItem(s);
        }
        Label labelCurrentAttributes = new Label("Current Attributes");
        row1.add(labelCurrentAttributes);
        row1.add(allCurrentAttributes);*/
    } 
    /**
     *
     */


    private void initRaplaPopupPanel() {
        popup = RootPanel.get("raplaPopup");
        popup.setVisible(true);
        popup.setStyleName("popup");
    }

    private void initTabPanel() {
        tabPanel = new TabPanel();
        tabPanel.addStyleName("tabPanel");
    }

    private void initHeaderPanel() {
        headerPanel = new FlowPanel();
        headerPanel.setStyleName("headerPanel");
    }

    private void initSaveDeleteCancelHPanel() {
        saveDeleteCancelHPanel = new HorizontalPanel();
        saveDeleteCancelHPanel.addStyleName("saveDeleteCancelHPanel");
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
        part2.setStyleName("part2");

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
        headerPanel.clear();
        saveDeleteCancelHPanel.clear();
        tabPanel.clear();
        content.clear();
        generalInformation.clear();
        row1.clear();
        coursePanel.clear();
        part2.clear();
        grid.clear();
        //upDown.clear();
        contentRes.clear();
        allKeys.clear();
        allLanguageLB.clear();
        allCurrentAttributes.clear();

    }

    private void structuringPanels() {
        popup.add(headerPanel);
        popup.add(tabPanel);
        popup.add(saveDeleteCancelHPanel);
        initTabs();
        content.add(generalInformation);
        content.add(contentRes);// Notiz Yvonne: Ressourcen - Implementierung (siehe mapfromReservation-Methode)

        generalInformation.add(row1);
        generalInformation.add(coursePanel);
        generalInformation.add(part2);
        part2.add(grid);
        //part2.add(upDown);
    }
    


    private void initTabs() {
        tabPanel.add(content, "Allgemeine Informationen");
        tabPanel.getTabBar().setStyleName("tabBar");

        for (TabPanelRapla tab : tabs) {
            tabPanel.add(tab.getTab(), tab.getName());
        }

        tabPanel.selectTab(0);
    }

    private void initCaptionLabel() {
    	
    	/*Header for Creating Reservations*/
    	if(getPresenter().getIsNew()){
    		captionLabel = new Label("Veranstaltung anlegen");
    		captionLabel.setStyleName("captionCreationLabel");
    	}
    	
    	/*Header for Editing Reservations*/
    	else{
    		captionLabel = new Label("Veranstaltung bearbeiten");
    		captionLabel.setStyleName("captionEditingLabel");
    
    	}

		headerPanel.add(captionLabel);
    }

    

    
    
    private void initEventTypeListBoxes() {
        // Eventtype
        final DynamicType[] eventTypes = getPresenter().getAllEventTypes();
        final Locale locale = getRaplaLocale().getLocale();
        final ListBox eventTypeLB = new ListBox();

        //initLabelCurrentEventType(locale);

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
        	initAllLanguageLB();
        	initCourseButton();
        	initLabelPlannedHoursInGrid();
        	initTextBoxPlannedHoursInGrid();
        	
        	//boolean changedLehrveranstaltung = false;
        	//actviateCourseButton = true;

        }
        
/*        if(chosenEventType.equalsIgnoreCase("Prüfung")){
        	initLectureCourseLabel();
        	initLectureCourseTextBox();
        }*/

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
                	removeAllLanguageLB();
                	removePlannedHoursLabelTB();
                	//removeLectureCourseTB();
                	coursePanel.setVisible(false);
                	
                	buttonRemoved = true;

                }


                if (chosenEventType.equalsIgnoreCase("Prüfung")) {
                    //ausblenden für präsi: initLanguageListBox();
                	removeExaminationTypeLB();
                	removeLectureCourseLabelTB();

                }
                for (DynamicType dynamicType : eventTypes) {
                    chosenEventType = eventTypeLB.getSelectedValue();

                    if (dynamicType.getName(locale).equals(chosenEventType)) {

                        for (Attribute attribute : dynamicType.getAttributes()) {
                            //language.addItem(attribute.getName(locale));
                        }
                    }
                }

                if (chosenEventType.equalsIgnoreCase("Lehrveranstaltung")) {
                    //activateCourseButton = true;
                	coursePanel.setVisible(true);
                	changedToLehrveranstaltung = true;
                	initAllLanguageLB();
                    initCourseButton();
                    grid.resizeRows(2);
                    initLabelPlannedHoursInGrid();
                    initTextBoxPlannedHoursInGrid();
                    

                }
                
                if(chosenEventType.equalsIgnoreCase("Prüfung")){
                	initExaminationTypeLB();
                	grid.resizeRows(2);
                	initLectureCourseLabelInGrid();
                	initLectureCourseTextBoxInGrid();
                }

            }
        });
    }
    
    private void initExaminationTypeLB(){
    	examinationTypeLB = new ListBox();
    	//examinationTypeLB.setStyleName("examinationTypeLB");
    	final Locale locale = getRaplaLocale().getLocale();
    	final Category[] examinationTypesCategory = getPresenter().getCategory(locale, "Prüfungsart");
    	
    	for(Category art : examinationTypesCategory){
    		examinationTypeLB.addItem(art.getName(locale));
    	}
    	row1.add(examinationTypeLB);
    }
    
    private void initAllLanguageLB(){
    	//examinationTypeLB.setStyleName("allLanguage");
    	final Locale locale = getRaplaLocale().getLocale();
        final Category[] languagesCategory = getPresenter().getCategory(locale, "Sprachen");
        
        if(!changedToLehrveranstaltung){
        	
        	allLanguageLB.addItem("Veranstaltungssprache auswählen");
        
        	for (Category language : languagesCategory) {
        		allLanguageLB.addItem(language.getName(locale));
        	}
        	
        }
        row1.add(allLanguageLB);
        
        
        
        
        allLanguageLB.addChangeHandler(new ChangeHandler(){
        	
        	public void onChange(ChangeEvent event){
        		if(firstChange){
        			allLanguageLB.removeItem(0);;
        		}
        		
        		firstChange = false;
        	}
        });
    }

    //ausblenden für präsi
/*    private void initLabelCurrentEventType(Locale locale) {
        String name = getPresenter().getEventType(locale);
        Label label = new Label(name);
        row1.add(label);
    }*/

    //ausblenden für präsi
/*    private void initLanguageListBox() {
        // Language selection
        chosenLanguage = language.getSelectedValue();
        language.setStyleName("language");
        row1.add(language);
    }*/

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
                if (buttonRemoved) {
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
        int f = 0;
        int anzahl = 0;
        
        for (Category category : allCourses) {
        	for (Category underCategory : category.getCategories()) {
            	anzahl = anzahl + 1;
            }
        }
        
        cb = new CheckBox [anzahl]; 
        
        for (Category category : allCourses) {

            tree.addItem(new TreeItem());
            tree.getItem(i).setText(category.getName(locale));
            
            

            for (Category underCategory : category.getCategories()) {

            	cb[f] = new CheckBox(underCategory.getName(locale));

            	tree.getItem(i).addItem(new TreeItem(cb[f]));
            	f++;
            
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
        
        initDeselectButton();


        coursePanel.setVisible(false);
        //activateCourseButton = false;


    }
    
    private void initDeselectButton(){
    	
        deselectButton = new Button("Alle Selektionen löschen");
        deselectButton.setStyleName("deselectButton");
        coursePanel.add(deselectButton);
        
        final Locale locale = getRaplaLocale().getLocale();
        final Category[] allCourses = getPresenter().getCategory(locale, "Studiengänge");
        
        deselectButton.addClickHandler(new ClickHandler(){
        	@Override
        	public void onClick(ClickEvent e){
        		
        		int f = 0;
                for (Category category : allCourses) {
                	for (Category underCategory : category.getCategories()) {
                		//String name = "cb" + f;
                		cb[f].setValue(false);
                		f++;
                	
                	}
                
                }
        	}
        });
    }
    
    
    
    private void removeAllLanguageLB(){
    	row1.remove(allLanguageLB);
    }


    private void removeCourseButton() {
        row1.remove(course);
        coursePanel.remove(tree);
        coursePanel.remove(hideButton);
        coursePanel.remove(deselectButton);

    }
    
    private void removeExaminationTypeLB(){
    	row1.remove(examinationTypeLB);
    }
    
    //könnte ich als eine Methode schreiben
    private void removePlannedHoursLabelTB(){
    	grid.removeRow(1);
    }
    
    private void removeLectureCourseLabelTB(){
    	grid.removeRow(1);
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
        planhour = new Label("geplante Vorlesungsstunden");
        planhour.setStyleName("planhour");
        grid.setWidget(1, 0, planhour);
    }

    private void initTextBoxPlannedHoursInGrid() {
        TextBox tbPlanhour = new TextBox();
        tbPlanhour.setStyleName("tbPlanhour");
        grid.setWidget(1, 1, tbPlanhour);
    }
    
    private void initLectureCourseLabelInGrid() {
    	lectureCourseLabel = new Label("Vorlesung");
    	lectureCourseLabel.setStyleName("lectureCourse");
    	grid.setWidget(1,0, lectureCourseLabel);
      
      
     }
    
    private void initLectureCourseTextBoxInGrid() {
    	TextBox lectureCourseTextBox = new TextBox(); 
    	lectureCourseTextBox.setStyleName("textbox");
    	grid.setWidget(1,1, lectureCourseTextBox);
      
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

    private void initDownButton() {
        Button downButton = new Button("-");
        downButton.setStyleName("downButton");
        upDown.add(downButton);
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
            Button button = new Button("Abbrechen");
            button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent e) {
                    getPresenter().onCancelButtonClicked();
                }
            });
            saveDeleteCancelHPanel.add(button);
        }

        if (getPresenter().isDeleteButtonEnabled()) {
            Button button = new Button("Löschen");
            button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent e) {
                    getPresenter().onDeleteButtonClicked();
                }
            });
            saveDeleteCancelHPanel.add(button);
        }

        {
            Button button = new Button("Speichern");
            button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent e) {
                    //Attributes [] selectedAttributes = new Attributes();

                    //getPresenter().changeAttributes(attributes);
                    getPresenter().onSaveButtonClicked();
                }
            });
            saveDeleteCancelHPanel.add(button);
        }

        {
        	historyMgmtPanel = new FlowPanel();
        	historyMgmtPanel.addStyleName("history-mgmt");
        	popup.add(historyMgmtPanel);
            Button button = new Button("Rückgängig");
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (HistoryManager.getInstance().canUndo())
                        HistoryManager.getInstance().undo();
                }
            });
            saveDeleteCancelHPanel.add(button);
            historyMgmtPanel.add(button);
        }

        {
            Button button = new Button("Wiederholen");
            button.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    if (HistoryManager.getInstance().canRedo())
                        HistoryManager.getInstance().redo();
                }
            });
            saveDeleteCancelHPanel.add(button);
            historyMgmtPanel.add(button);
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
        TabPanelRapla aTab = new TabPanelRapla(tabName, flowPanel);
        tabs.add(aTab);
    }

}
