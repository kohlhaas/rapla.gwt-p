package org.rapla.client.edit.reservation.sample.gwt;


import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.AttributeValue.AttributeValues;
import org.rapla.client.edit.reservation.history.HistoryManager;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.Category;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.ConstraintIds;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.facade.Conflict;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReservationViewImpl extends AbstractView<Presenter> implements ReservationView<IsWidget> {

    Panel popupPanel;
    FlowPanel headerPanel;

    TabPanel tabPanel; //Tabs for General Information and Appointment+Ressources-Planning

    FlowPanel contentPanel;
    FlowPanel contentRessourcesPanel = new FlowPanel();
    FlowPanel generalInformationPanel;
    FlowPanel row1Panel;
    FlowPanel part2Panel;
    FlowPanel coursePanel;

    HorizontalPanel saveDeleteCancelHPanel;
    FlowPanel historyMgmtPanel;

    List<TabPanelRapla> tabs = new ArrayList<>();


    Grid grid;

    VerticalPanel upDown; //aktuell nicht in Benutzung

    TextBox eventNameTB;

    final ListBox eventTypeLB = new ListBox();
    ListBox language = new ListBox();
    ListBox examinationTypeLB;
    ListBox allLanguageLB = new ListBox();
    ListBox allKeys = new ListBox();
    ListBox allKeys2 = new ListBox();
    Button course = new Button("Studiengang auswählen");
    TextBox testSaving = new TextBox(); //TEST!!!
    TextBox tbPlanhour;
    TextBox lectureCourseTextBox;
    TextArea taInfo;

    Button hideButton = null;
    Button deselectButton = null;
    CheckBox[] cb;
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

    Map<String, Object> attributeNames = new HashMap<String, Object>();
    Map<Attribute, Object> valuesToSave = new HashMap<Attribute, Object>();
    Map<Attribute, Collection<Object>> attributeCollectionMap = new HashMap<Attribute, Collection<Object>>();
    List<Object> objectList = new LinkedList<Object>();
    //Map<Category, Category> categoriesToSave = new HashMap<Category, Category>();
    AttributeValues attributeValues;
    Map<Attribute, Object> valuesToGet = new HashMap<Attribute, Object>();
    Map<Attribute, Collection<Object>> attributeCollectionMapToGet = new HashMap<Attribute, Collection<Object>>();


    public void show(Reservation event) {
        /*getSavedValues*/
        attributeValues = getPresenter().getCurrentAttributesOfReservationWithValues();
        valuesToGet = attributeValues.getvaluesToGet();
        attributeCollectionMapToGet = attributeValues.getattributeCollectionMap();

    	

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
        initLabelEventNameInGrid();
        initEventNameTextBox();
        initLabelPlannedHoursInGrid();
        initTextBoxPlannedHoursInGrid();
        // initUpButton();
        // initDownButton();
        initLabelInfo();
        initTextAreaInfo();

        //initTestButton(); //TEST!!!
        
        
    

        

        
/*        Button down = new Button();
        down.setSize("15px", "15px");
        down.setStyleName("down");
        String html2 = "<div class = 'img' ><img class= 'img_1' src = '/images/triangledown_klein.png'></img></div>";
        down.setHTML(html2);
        upDown.add(down);  */

        initSaveDeleteCancelButtons(event);
        final Locale locale = getRaplaLocale().getLocale();

    }

   
    
    /*Init Panels*/

    private void initRaplaPopupPanel() {
        popupPanel = RootPanel.get("raplaPopup");
        popupPanel.setVisible(true);
        popupPanel.setStyleName("popup");
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
        contentPanel = new FlowPanel();
        contentPanel.setStyleName("content");
    }

    private void initGeneralInformationPanel() {
        generalInformationPanel = new FlowPanel();
        generalInformationPanel.setStyleName("generalInformation");
    }

    private void initRow1() {
        row1Panel = new FlowPanel();
        row1Panel.setStyleName("zeile1");
    }

    private void initCoursePanel() {
        coursePanel = new FlowPanel();
        coursePanel.setStyleName("coursePanel");

    }

    private void initPart2Panel() {
        //Second part of the structure
        part2Panel = new FlowPanel();
        part2Panel.setStyleName("part2");

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
        popupPanel.clear();
        headerPanel.clear();
        saveDeleteCancelHPanel.clear();
        tabPanel.clear();
        contentPanel.clear();
        generalInformationPanel.clear();
        row1Panel.clear();
        coursePanel.clear();
        part2Panel.clear();
        grid.clear();
        //upDown.clear();
        contentRessourcesPanel.clear();
        allKeys.clear();
        allLanguageLB.clear();

    }

    private void structuringPanels() {
        popupPanel.add(headerPanel);
        popupPanel.add(tabPanel);
        popupPanel.add(saveDeleteCancelHPanel);
        initTabs();
        contentPanel.add(generalInformationPanel);
        contentPanel.add(contentRessourcesPanel);// Notiz Yvonne: Ressourcen - Implementierung (siehe mapfromReservation-Methode)

        generalInformationPanel.add(row1Panel);
        generalInformationPanel.add(coursePanel);
        generalInformationPanel.add(part2Panel);
        part2Panel.add(grid);
        //part2.add(upDown);
    }
    

    /*Defining two Tabs for the two Views*/

    private void initTabs() {
        tabPanel.add(contentPanel, "Allgemeine Informationen");
        tabPanel.getTabBar().setStyleName("tabBar");

        for (TabPanelRapla tab : tabs) {
            tabPanel.add(tab.getTab(), tab.getName());
        }

        tabPanel.selectTab(0);
    }
    
    
    
    /* Filling Panels */

    private void initCaptionLabel() {

    	/*Header for Creating Reservations*/
        if (getPresenter().getIsNew()) {
            captionLabel = new Label("Veranstaltung anlegen");
            captionLabel.setStyleName("captionCreationLabel");
        }

    	/*Header for Editing Reservations*/
        else {
            captionLabel = new Label("Veranstaltung bearbeiten");
            captionLabel.setStyleName("captionEditingLabel");

        }

        headerPanel.add(captionLabel);
    }


    private void initEventTypeListBoxes() {
        // Eventtype
        final DynamicType[] eventTypes = getPresenter().getAllEventTypes();
        final Locale locale = getRaplaLocale().getLocale();

        //initLabelCurrentEventType(locale);

        eventTypeLB.setStyleName("eventType");

        /**
         * SEHR UNSAUBER, nur grob, bitte ggf. verbessern --> ggf. in eigene Klasse oder Presenter
         */
        for (DynamicType dynamicType : eventTypes) {
            eventTypeLB.addItem(dynamicType.getName(locale));
        }
        row1Panel.add(eventTypeLB);

        String SavedEventType = getPresenter().getEventType(locale);

        for (int i = 0; i < eventTypeLB.getItemCount(); i++) {
            if (eventTypeLB.getItemText(i).equalsIgnoreCase(SavedEventType)) {

                eventTypeLB.setSelectedIndex(i);
            }
        }
        
        
      
/*        if (getPresenter().getIsNew()) {
        	
        	getPresenter().getAllCurrentAttributesAsStrings(locale);
        	eventTypeLB.setItemSelected(index, selected);
        }*/


        chosenEventType = eventTypeLB.getSelectedValue();
        if (chosenEventType.equalsIgnoreCase(null)) {
            eventTypeLB.setItemSelected(0, true);
            chosenEventType = eventTypeLB.getSelectedValue();

        }


        if (chosenEventType.equalsIgnoreCase("Lehrveranstaltung")) {
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


    }

    private void mapDynamicTypesToChangeHandler(final DynamicType[] eventTypes, final Locale locale, final ListBox eventTypeLB) {
        eventTypeLB.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                //changedLehrveranstaltung = true;

                language.clear();

                if (chosenEventType.equalsIgnoreCase("Lehrveranstaltung")) {

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
                        getPresenter().eventTypeChanged(dynamicType);

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

                if (chosenEventType.equalsIgnoreCase("Prüfung")) {
                    initExaminationTypeLB();
                    grid.resizeRows(2);
                    initLectureCourseLabelInGrid();
                    initLectureCourseTextBoxInGrid();
                }

            }
        });
    }

    private void initExaminationTypeLB() {
        examinationTypeLB = new ListBox();
        //examinationTypeLB.setStyleName("examinationTypeLB");
        final Locale locale = getRaplaLocale().getLocale();
        final Category[] examinationTypesCategory = getPresenter().getCategory(locale, "Prüfungsart");

        for (Category art : examinationTypesCategory) {
            examinationTypeLB.addItem(art.getName(locale));
        }
        row1Panel.add(examinationTypeLB);
    }

    private void initAllLanguageLB() {
        //examinationTypeLB.setStyleName("allLanguage");
        final Locale locale = getRaplaLocale().getLocale();
        final Category[] languagesCategory = getPresenter().getCategory(locale, "Sprachen");

        if (!changedToLehrveranstaltung) {

            allLanguageLB.addItem("Veranstaltungssprache auswählen");

            for (Category language : languagesCategory) {
                allLanguageLB.addItem(language.getName(locale));
            }

        }
        row1Panel.add(allLanguageLB);


        allLanguageLB.addChangeHandler(new ChangeHandler() {

            public void onChange(ChangeEvent event) {
                if (firstChange) {
                    allLanguageLB.removeItem(0);
                    ;
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

        //Study course

        course.setStyleName("course");
        row1Panel.add(course);

        course.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {
                if (buttonRemoved) {
                    row1Panel.add(course);
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
        int i = 0;
        int f = 0;
        int anzahl = 0;

        for (Category category : allCourses) {
            for (Category underCategory : category.getCategories()) {
                anzahl = anzahl + 1;
            }
        }

        cb = new CheckBox[anzahl];

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

    private void initDeselectButton() {

        deselectButton = new Button("Alle Selektionen löschen");
        deselectButton.setStyleName("deselectButton");
        coursePanel.add(deselectButton);

        final Locale locale = getRaplaLocale().getLocale();
        final Category[] allCourses = getPresenter().getCategory(locale, "Studiengänge");

        deselectButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent e) {

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


    private void removeAllLanguageLB() {
        row1Panel.remove(allLanguageLB);
    }


    private void removeCourseButton() {
        row1Panel.remove(course);
        coursePanel.remove(tree);
        coursePanel.remove(hideButton);
        coursePanel.remove(deselectButton);

    }

    private void removeExaminationTypeLB() {
        row1Panel.remove(examinationTypeLB);
    }

    //könnte ich als eine Methode schreiben
    private void removePlannedHoursLabelTB() {
        grid.removeRow(1);
    }

    private void removeLectureCourseLabelTB() {
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
        tbPlanhour = new TextBox();
        tbPlanhour.setStyleName("tbPlanhour");
        for (Attribute attribute : getPresenter().getAllCurrentAttributes()) {
            if (attribute.getName(getRaplaLocale().getLocale()).equalsIgnoreCase("Gepl. Vorlesungsstunden")) {
                tbPlanhour.setValue(getPresenter().getCurrentClassification().getValueAsString(attribute, getRaplaLocale().getLocale()));
            }
        }
        grid.setWidget(1,1,tbPlanhour);
    }

    private void initLectureCourseLabelInGrid() {
        lectureCourseLabel = new Label("Vorlesung");
        lectureCourseLabel.setStyleName("lectureCourse");
        grid.setWidget(1, 0, lectureCourseLabel);


    }

    private void initLectureCourseTextBoxInGrid() {
        lectureCourseTextBox = new TextBox();
        lectureCourseTextBox.setStyleName("textbox");
        grid.setWidget(1, 1, lectureCourseTextBox);

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
        part2Panel.add(info);

    }

    private void initTextAreaInfo() {
        taInfo = new TextArea();
        taInfo.setStyleName("taInfo");
        for (Attribute attribute : getPresenter().getAllCurrentAttributes()) {
            if (attribute.getName(getRaplaLocale().getLocale()).equalsIgnoreCase("Planungsnotiz")) {
                taInfo.setValue(getPresenter().getCurrentClassification().getValueAsString(attribute, getRaplaLocale().getLocale()));
            }
        }
        part2Panel.add(taInfo);
        part2Panel.add(testSaving); //TEST!!!!!!


    }


    private void initSaveDeleteCancelButtons(Reservation event) {
        //Standard Buttons
        {
            Button button = new Button("Abbrechen");
            button.setStyleName("cancel");
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
            button.setStyleName("delete");
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
            button.setStyleName("save");
            final Locale locale = getRaplaLocale().getLocale();
            button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent e) {


                    DynamicType[] eventTypes = getPresenter().getAllEventTypes();
                    final Category[] allCategories = getPresenter().getCategories();
                    Category categoryForAttribute = null;

                    for (DynamicType eventType : eventTypes) {

                        if (eventType.getName(locale).equalsIgnoreCase(eventTypeLB.getSelectedValue())) {

                            for (Attribute attribut : eventType.getAttributes()) {

                                if (attribut.getName(locale).equalsIgnoreCase("Titel")) {
                                    valuesToSave.put(attribut, eventNameTB.getText());
                                }

                                if (attribut.getName(locale).equalsIgnoreCase("Gepl. Vorlesungsstunden")) {
                                    valuesToSave.put(attribut, tbPlanhour.getText());
                                }

                                if (attribut.getName(locale).equalsIgnoreCase("Planungsnotiz")) {
                                    valuesToSave.put(attribut, taInfo.getText());
                                }

                                if (attribut.getName(locale).equalsIgnoreCase("Vorlesung")) {
                                    valuesToSave.put(attribut, lectureCourseTextBox.getText());
                                }


                                categoryForAttribute = (Category) attribut.getConstraint(ConstraintIds.KEY_ROOT_CATEGORY);


                                for (Category mainCategory : allCategories) {

                                    if (!hasNull(examinationTypeLB, allLanguageLB, categoryForAttribute)) {
                                        if (mainCategory.getName(locale).equalsIgnoreCase(categoryForAttribute.getName(locale))) {

                                            for (Category subCategory : mainCategory.getCategories()) {

                                                if (subCategory.getName(locale).equalsIgnoreCase(examinationTypeLB.getSelectedValue())) {

                                                    valuesToSave.put(attribut, subCategory);
                                                }

                                                if (subCategory.getName(locale).equalsIgnoreCase(allLanguageLB.getSelectedValue())) {
                                                    valuesToSave.put(attribut, subCategory);
                                                }

                                                if (!subCategory.getCategories().equals(null)) {

                                                    for (Category subSubCategory : subCategory.getCategories()) {

                                                        for (CheckBox categoryCB : cb) {
                                                            if (categoryCB.getValue()) {

                                                                if (categoryCB.getName().equalsIgnoreCase(subSubCategory.getName(locale))) {
                                                                    objectList.add(subSubCategory);

                                                                }
                                                            }

                                                        }
                                                    }

                                                    valuesToSave.put(attribut, subCategory);
                                                    attributeCollectionMap.put(attribut, objectList);

                                                }

                                            }

                                        }

                                    }
                                }

                            }

                        }

                    }


                    getPresenter().setAttributesOfReservation(valuesToSave, attributeCollectionMap);
                    getPresenter().onSaveButtonClicked();
                }
            });
            saveDeleteCancelHPanel.add(button);
        }

        {
            historyMgmtPanel = new FlowPanel();
            historyMgmtPanel.addStyleName("history-mgmt");
            popupPanel.add(historyMgmtPanel);
            Button button = new Button("Rückgängig");
            button.setStyleName("back");
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
            button.setStyleName("further");
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

    private boolean hasNull(ListBox eins, ListBox zwei, Category categoryForAttribute) {
        boolean isBad = false;
        if (eins == null || zwei == null || categoryForAttribute == null) {
            isBad = true;
        }

        return isBad;
    }

    //TEST!!!
   /* private void initTestButton() {
    	
    	Button testButton = new Button("Show Saving");
    	saveDeleteCancelHPanel.add(testButton);
    	 final Locale locale = getRaplaLocale().getLocale();
    	testButton.addClickHandler(new ClickHandler() {
    		
    		@Override
    		public void onClick(ClickEvent event) {
    			DynamicType [] eventTypes = getPresenter().getAllEventTypes();
            	final Category[] examinationTypesCategory = getPresenter().getCategory(locale, "Prüfungsart");
            	//getPresenter().getCategoryAttributes(locale, "Prüfungsart");
            	final Category[] allCategories = getPresenter().getCategories();
            	
            	for(DynamicType eventType : eventTypes){
            		
            		if(eventType.getName(locale).equalsIgnoreCase(eventTypeLB.getSelectedValue())){
            			
            			for(Attribute attribut : eventType.getAttributes()){
            				
            				
                        	for(Category mainCategory : allCategories){
                        		
                        		//testSaving.setText(attribut.getType().name());
                        		
                        		//if(attribut.getType().name().equalsIgnoreCase(mainCategory.getName(locale))){
                        		
                					//testSaving.setText(attribut.getName(locale));
                        			
                        			for(Category subCategory : mainCategory.getCategories()){
                        				
                        				if(attribut.getName(locale).equalsIgnoreCase("Art")){
                        			
                        					if(subCategory.getName(locale).equalsIgnoreCase(examinationTypeLB.getSelectedValue())){
                        						//testSaving.setText(attribut.getKey());
                        						
                        						Category neu = (Category) attribut.getConstraint(ConstraintIds.KEY_ROOT_CATEGORY);
                        						testSaving.setText(neu.getName(locale));
                        						//testSaving.setText(mainCategory.getKey());
                        						
                        						//testSaving.setText((String)attribut.getConstraint(mainCategory.getKey()));
                        						valuesToSave.put(attribut, subCategory);
                        					}
                        				
                        				}
                        				
                        				
                        			}
                        		
                        	}
                				
                		
            			
            		
            				
            				
            				
            				//attribut.getName(locale);
            			
            			//attributeNames.put(examinationTypeLB.getSelectedValue(),eventType.getAttribute("Prüfungsart"));
            				//testSaving.setText(attributeNames.toString());
            				if(attribut.getName(locale).equalsIgnoreCase("Art")){
            					//testSaving.setText(attribut.getName(locale));
            				}
            			
            			valuesToSave.put(eventType.getAttribute("Titel"), examinationTypeLB.getSelectedValue());
            				//testSaving.setText(valuesToSave.toString());
            				
            			//testSaving.setText(eventType.getAttribute("Prüfungsart").getName(locale));
            				//testSaving.setText(attributeNames.values().toString());
            				
            				//testSaving.setText(attribut.getName(locale));
            			

                    	

            			
            		}
            			
            		
            	}
            	

            		
            		
            	
            	
            	for(Category art : examinationTypesCategory){
            		
            		if(art.getName(locale).equalsIgnoreCase(examinationTypeLB.getSelectedValue())){
        
            			//art.
            			//valuesToSave.put(art, value)
            			
            		}
            		
            		
            	}
    		}
    		}
    	});
    	
    	
    	
    }*/


    public void hide() {
        popupPanel.setVisible(false);
        contentPanel.clear();
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

    public void showConflicts(Conflict[] conflicts) {
        for (Conflict c : conflicts) {
            Logger.getGlobal().log(Level.INFO, c.getReservation1Name() + " konfligiert mit " + c.getReservation2Name());
        }
    }

}
