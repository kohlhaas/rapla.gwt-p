package org.rapla.client.edit.reservation.sample.gwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.client.mwi14_1.view.infos.InfoHorizontalPanel;
import org.rapla.client.mwi14_1.view.resoursedates.TerminList;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;

import com.blogspot.ctasada.gwt.eureka.client.ui.SmallTimeBox;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.datepicker.client.DateBox;
 
public class ReservationViewImpl extends AbstractView<Presenter> implements ReservationView<IsWidget> {
    
	//for general popup
	private FlowPanel mainPanel;
	private VerticalPanel layout;
	private FlowPanel tabBarPanel;
	private Panel contentPanel;
	private FlowPanel buttonsPanel;
	public Panel popup;
	private TabBar bar;
	private Button cancel, save, delete;
	private int height, width;
	
	//for info tab 
	private VerticalPanel contentLeft;
	private VerticalPanel contentRight;
	private HorizontalPanel infoTab; 
	private ListBox eventTypesListBox;
	private Tree resources;
	private TextBox titelInput;
	private TextBox vorlesungsStundenInput;
	private ListBox studiengangListBox;
	private Collection<String> studiengangListBoxAuswahl;
	
	//for resources and dates tab
	private ArrayList<List<String>> toBeReservedResources = new ArrayList<List<String>>();
	
	private ArrayList<List<String>> reservedResources = new ArrayList<List<String>>();
		
	
	FlowPanel mainContent;
	TerminList dateList;
	FlowPanel buttonBar;

	Label buttonNextGap;
	Label buttonGarbageCan;
	Label buttonPlus;
	// TO-DO: Is this really a Label? Or should it be a Button? Can a Label be
	// used, too?

	DateBox dateBegin;
	DateBox dateEnd;

//	SmallTimeBox timeBegin;
//	SmallTimeBox timeEnd;

	Tree resourceTree;
	
	CheckBox cbWholeDay;

	FlowPanel chosenResources;
	DisclosurePanel dateDisclosurePanel;
	DisclosurePanel cbRepeatType;
	
	Label addDateInfo;
	Button addDate;
	Button rewriteDate;
	
	HorizontalPanel repeat;
	RadioButton daily;
	RadioButton weekly;
	RadioButton monthly;
	RadioButton year;
	RadioButton noReccuring;
    
    public void show(Reservation event)
    {
       
        popup = RootPanel.get("raplaPopup");
        popup.setVisible(true);
      
        //popup.setGlassEnabled(true);
		//popup.setAnimationEnabled(true);
        //	popup.setAnimationType(PopupPanel.AnimationType.ROLL_DOWN);
        
        
		height = (int) (Window.getClientHeight() * 0.90);
		width = (int) (Window.getClientWidth() * 0.90);
		popup.setHeight(height + "px");
		popup.setWidth(width + "px");
	
     	popup.clear();
        
        bar = new TabBar();
		bar.addTab("Veranstaltungsinfos");
		bar.addTab("Termine und Resourcen");
	
		cancel = new Button("abbrechen");
		cancel.setStyleName("cancelButton");
		cancel.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent e) {
                getPresenter().onCancelButtonClicked();
            }});

		save = new Button("speichern");
		save.setStyleName("saveButton");
		save.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent e) {
                getPresenter().onSaveButtonClicked();
            }});

		delete = new Button("l\u00F6schen");
		delete.setStyleName("deleteButton");
		delete.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent e) {
                getPresenter().onDeleteButtonClicked();
            }});

		layout = new VerticalPanel();
		tabBarPanel = new FlowPanel();
		contentPanel = new SimplePanel();
		buttonsPanel = new FlowPanel();

		tabBarPanel.add(bar);
        
		bar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				getPresenter().onTabChanged(bar.getSelectedTab());
			}
		});
		
		contentPanel.clear();
	
		
		bar.selectTab(0);
		update(bar.getSelectedTab());

		buttonsPanel.add(cancel);
		buttonsPanel.add(save);
		buttonsPanel.add(delete);

		layout.add(buttonsPanel);
		layout.add(tabBarPanel);
		layout.add(contentPanel);

		layout.setCellHeight(buttonsPanel, "40px");
		layout.setCellHeight(tabBarPanel, "50px");

		popup.add(layout);
		//popup.center();

	
		
		
        }
    
       
     public void update(int selectedTab){
    	 
    	 if(selectedTab == 0){
    		 
    		 contentPanel.clear(); 
    		 
    		 infoTab = new HorizontalPanel();
    			contentLeft = new VerticalPanel();
    			contentRight = new VerticalPanel();
    			contentLeft.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    			contentRight
    					.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
    			contentLeft.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
    			contentRight.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

    			eventTypesListBox = new ListBox();
    			resources = new Tree();

    			// add the Event Types from data.xml here

    			eventTypesListBox.addChangeHandler(new ChangeHandler() {

    				@Override
    				public void onChange(ChangeEvent event) {
    					getPresenter().onEventTypesChanged();

    				}
    			});

    			final FlowPanel listPanel = new FlowPanel();
    			listPanel.add(eventTypesListBox);
    			listPanel.add(resources);
    			listPanel.setWidth(width + "px");
    			contentLeft.add(listPanel);

    			final InfoHorizontalPanel titelPanel = new InfoHorizontalPanel(width
    					+ "px");
    			final Label title = new Label("Vorlesungstitel");
    			titelInput = new TextBox();
    			titelPanel.add(title, (width / 3) + "px");
    			titelPanel.add(titelInput, (width / 3) + "px");
    			 titelPanel.setWidth(width + "px");

    			final Label vorlesungsStunden = new Label("Vorlesungsstunden");
    			final Label vorlesungsStundenMessage = new Label("");
    			vorlesungsStundenInput = new TextBox();
    			vorlesungsStundenInput.addKeyUpHandler(new KeyUpHandler() {
    				@Override
    				public void onKeyUp(KeyUpEvent event) {
    					//to be implemented

    				}
    			});

    			final InfoHorizontalPanel vorlesungsStundenPanel = new InfoHorizontalPanel(
    					width + "px");
    			vorlesungsStundenPanel.add(vorlesungsStunden, (width / 3) + "px");
    			vorlesungsStundenPanel.add(vorlesungsStundenInput, (width / 3) + "px");
    			vorlesungsStundenPanel
    					.add(vorlesungsStundenMessage, (width / 3) + "px");

    			final InfoHorizontalPanel studiengangPanel = new InfoHorizontalPanel(
    					width + "px");
    			Label studiengang = new Label("Studiengang");
    			studiengangListBox = new ListBox();
    			studiengangListBox.addItem("WWI12B1");
    			studiengangListBox.addItem("WWI12B2");
    			studiengangListBox.addItem("WWI12B3");
    			studiengangListBoxAuswahl = new ArrayList();
    			studiengangListBox.addChangeHandler(new ChangeHandler() {
    				// Hier den Handler / Listener für die Listbox einbinden ~ done
    				@Override
    				public void onChange(ChangeEvent event) {
    					getPresenter().onStudiengangChanged();

    				}

    			});

    			studiengangPanel.add(studiengang, (width / 3) + "px");
    			studiengangPanel.add(studiengangListBox, (width / 3) + "px");

    			studiengangPanel.finalize(3);
    			vorlesungsStundenPanel.finalize(3);
    			titelPanel.finalize(3);

    			contentRight.add(titelPanel);
    			contentRight.add(vorlesungsStundenPanel);
    			contentRight.add(studiengangPanel);

    			
    			
    			infoTab.add(contentLeft);
    			infoTab.add(contentRight);
    			
    			infoTab.setCellWidth(contentLeft, width + "px");
    			infoTab.setCellWidth(contentRight, width + "px");
    			contentPanel.add(infoTab);
    		 
    	 }else if(selectedTab == 1){
    		 
 	
    		 	contentPanel.clear();
    		 	
    		 	mainContent = new FlowPanel();
    		 	dateList = new TerminList();
        		dateList.setHeight(height + "px");
        		dateList.setStyleName("dateList");

        			
        			FlowPanel firstDateListWidget = new FlowPanel();
        			firstDateListWidget.setStyleName("wildcardPanel");
        			Label explainer = new Label("Durch das Dr\u00FCcken des roten Plus-Buttons \u00F6ffnet sich das Fenster zur Erstellung von Terminen");
        			explainer.setStyleName("wildcard");
        			firstDateListWidget.add(explainer);	
        			dateList.add(firstDateListWidget);
        			
        			buttonBar = new FlowPanel();
        			buttonBar.setHeight(height + "px");
        			buttonBar.setStyleName("datesButtonBar");
        			

        			// Image buttonNextGap = new Image("button_luecke.png");
        			buttonNextGap = new Label(">>");
        			buttonNextGap.setStyleName("buttonsResourceDates");

        			// Image buttonGarbageCan = new Image("button_eimer.png");
        			buttonGarbageCan = new Label("X");
        			buttonGarbageCan.setStyleName("buttonsResourceDates");
        			buttonGarbageCan.setTitle("Termin l\u00F6schen");
        			buttonGarbageCan.addClickHandler(new ClickHandler(){
        					public void onClick(ClickEvent e) {
        		                getPresenter().onGarbageCanButtonClicked();
        		            }});
        				
        			
        			// Image buttonPlus = new Image("button_plus.png");
        			buttonPlus = new Label("+");
        			buttonPlus.setTitle("Termin erstellen");
        			buttonPlus.setStyleName("buttonsResourceDatesClickable");

        			// TO-DO: Is this really a Label? Or should it be a Button? Can a Label
        			// be used, too?

        			buttonBar.add(buttonPlus);
        			buttonBar.add(buttonGarbageCan);
        			buttonBar.add(buttonNextGap);

        			FlowPanel dateInfos = new FlowPanel();
        			dateInfos.setHeight(height + "px");
        			dateInfos.setStyleName("dateInfos");

        			DateTimeFormat dateFormat = DateTimeFormat
        					.getFormat(PredefinedFormat.DATE_MEDIUM);

        			// Datum und Uhzreit BEGIN
        			HorizontalPanel begin = new HorizontalPanel();
        			begin.setSpacing(5);
        			begin.setStyleName("dateInfoLineComplete");

        			Label beginText = new Label("Begin:");
        			beginText.setStyleName("beschriftung");
        			begin.add(beginText);
        			begin.setCellVerticalAlignment(beginText,
        					HasVerticalAlignment.ALIGN_MIDDLE);

        			dateBegin = new DateBox();
        			dateBegin.setStyleName("dateInput");
        			dateBegin.setFormat(new DateBox.DefaultFormat(dateFormat));
        			begin.add(dateBegin);

        		//	timeBegin = new SmallTimeBox(new Date(-3600000));
        			//begin.add(timeBegin);
        			final Label beginTimeText = new Label("Uhr");
        			beginTimeText.setStyleName("beschriftung");
        			begin.add(beginTimeText);
        			begin.setCellVerticalAlignment(beginTimeText,
        					HasVerticalAlignment.ALIGN_MIDDLE);

        			
        			// Datum und Uhrzeit ENDE
        			HorizontalPanel end = new HorizontalPanel();
        			end.setSpacing(4);
        			end.setStyleName("dateInfoLineComplete");
        			
        			Label endText = new Label("Ende: ");
        			endText.setStyleName("beschriftung");
        			end.add(endText);
        			end.setCellVerticalAlignment(endText, HasVerticalAlignment.ALIGN_MIDDLE);

        			dateEnd = new DateBox();
        			dateEnd.setStyleName("dateInput");
        			dateEnd.setFormat(new DateBox.DefaultFormat(dateFormat));
        			end.add(dateEnd);

        			//timeEnd = new SmallTimeBox(new Date(-3600000));
        			//timeEnd.setTitle("endTime");
        			//end.add(timeEnd.asWidget());

        			final Label endTimeText = new Label("Uhr");
        			endTimeText.setStyleName("beschriftung");
        			end.add(endTimeText);
        			end.setCellVerticalAlignment(endTimeText,
        					HasVerticalAlignment.ALIGN_MIDDLE);

        			end.setCellWidth(endText, "50px");
        			
        			
        			cbWholeDay = new CheckBox("ganzt\u00E4gig");
        			begin.add(cbWholeDay);
        			cbWholeDay.addClickHandler(new ClickHandler(){

        				@Override
        				public void onClick(ClickEvent event) {

        					getPresenter().onWholeDaySelected();
        				}
        				
        			});
        			
        			begin.setCellVerticalAlignment(cbWholeDay,
        					HasVerticalAlignment.ALIGN_MIDDLE);

        			begin.setCellWidth(beginText, "50px");

        			// Add termin

        			buttonPlus.addClickHandler(new ClickHandler() {

        				@Override
        				public void onClick(ClickEvent event) {
        					getPresenter().onButtonPlusClicked();

        				}
        			});
        			
        			// Checkbox WIEDERHOLEN
        			repeat = new HorizontalPanel();
        			cbRepeatType = new DisclosurePanel("Wiederholen");
        			cbRepeatType.setStyleName("dateInfoLineLeft");

        			daily = new RadioButton("repeat","t\u00E4glich");
        			weekly = new RadioButton("repeat", "w\u00F6chentlich");
        			monthly = new RadioButton("repeat", "monatlich");
        			year = new RadioButton("repeat", "j\u00E4hrlich");
        			noReccuring = new RadioButton("repeat", "keine Wiederholung");

        			repeat.add(daily);
        			repeat.add(weekly);
        			repeat.add(monthly);
        			repeat.add(year);
        			repeat.add(noReccuring);

        			cbRepeatType.add(repeat);
        			
        			HorizontalPanel addDateWithLabel = new HorizontalPanel();
        			addDate = new Button("Termin hinzuf\u00FCgen");
        			rewriteDate = new Button("Termin \u00FCberschreiben");
        			rewriteDate.setVisible(false);
        			addDateInfo = new Label();
        			addDateWithLabel.add(addDate);
        			addDateWithLabel.add(rewriteDate);
        			addDateWithLabel.add(addDateInfo);
        			addDateWithLabel.setCellVerticalAlignment(addDate, HasVerticalAlignment.ALIGN_MIDDLE);
        			addDateWithLabel.setCellVerticalAlignment(rewriteDate, HasVerticalAlignment.ALIGN_MIDDLE);
        			addDateWithLabel.setCellVerticalAlignment(addDateInfo, HasVerticalAlignment.ALIGN_MIDDLE);
        			
        			rewriteDate.addClickHandler(new ClickHandler(){

        				@Override
        				public void onClick(ClickEvent event) {
        					getPresenter().onRewriteDateClicked();
        				}
        				
        			});
        			
        			addDate.addClickHandler(new ClickHandler(){

        				@Override
        				public void onClick(ClickEvent event) {
        					getPresenter().onAddDateClicked();
        				}
        				
        			});
        		
		
        			// Ausgewählte Ressourcen laden
        			chosenResources = new FlowPanel();
        			chosenResources.setStyleName("dateInfoLineComplete");

        			// Ausgewählte Resourcen laden
        		//	loadChosenResources();
        			

        			Label headerChosenRes  = new Label("Ausgewaehlte Ressourcen:");
        			headerChosenRes.setStyleName("beschriftung");
        			
        			chosenResources.setStyleName("dateInfoLineComplete");
        			chosenResources.add(headerChosenRes);
        			
        			//-------
        			
        			
//        			for(FlowPanel helpList: getPanelResourceContainer()){
//        				chosenResources.add(helpList);
//        			}

        			DisclosurePanel addResources = new DisclosurePanel("Resourcen Hinzufuegen");
        			addResources.addOpenHandler(new OpenHandler<DisclosurePanel>() {
        					
        				  @Override
        				  public void onOpen(OpenEvent<DisclosurePanel> event) {
        				    dateDisclosurePanel.setOpen(false);
        					buttonPlus.setStyleName("buttonsResourceDatesClickable");
        				  }
        			});
        			
        			addResources.setStyleName("dateInfoLineComplete");

        			FlowPanel chooseContainer = new FlowPanel();
        			chooseContainer.setStyleName("chooseContainer");

        			// Baumstruktur für verfügbare Resourcen
        			resourceTree = new Tree();

        			// Auswählbare Ressourcen laden
        		//	loadResourcesToChoose();
        			
        			 //createResourceTree();
        			
        				
        		    chooseContainer.add(resourceTree);
        		 //   chooseContainer.setWidth(width  * 0.85 + "px");
        			
        		    addResources.setContent(chooseContainer);
        			

        		    VerticalPanel dateContentWrapper = new VerticalPanel();
        		    dateContentWrapper.add(begin);	
        		    dateContentWrapper.add(end);
        		    dateContentWrapper.add(cbRepeatType);
        		    dateContentWrapper.add(addDateWithLabel);
        		    dateDisclosurePanel = new DisclosurePanel();
        		    dateDisclosurePanel.add(dateContentWrapper);
        			dateDisclosurePanel.setOpen(false);
        			dateInfos.add(dateDisclosurePanel);
        			
        			dateInfos.add(new HTML("<hr  style=\"width:90%;\" />"));
        			dateInfos.add(chosenResources);
        			dateInfos.add(addResources);

        			mainContent.add(dateList);
        			mainContent.add(buttonBar);
        			mainContent.add(dateInfos);

    		 
    		    contentPanel.add(mainContent);
    		 
    		 
    	 } else{
    		//not possible, error handling should be added though
    	 }
    	 
     }
    

    public void mapFromReservation(Reservation event) {
        Locale locale = getRaplaLocale().getLocale();
      // tb.setText( event.getName( locale));
     //   contentRes.clear();
        Allocatable[] resources = event.getAllocatables();
        {
            StringBuilder builder = new StringBuilder();
            for ( Allocatable res:resources)
            {
                builder.append( res.getName( locale));
            }
          //  contentRes.add(new Label("Ressourcen: " +builder.toString() ));

        }
    }
    
    public void hide()
    {
        popup.setVisible(false);
        contentPanel.clear();
    }


    @Override
    public void addSubView(ReservationEditSubView<IsWidget> view) {
        IsWidget provideContent = view.provideContent();
     //   subView.add( provideContent.asWidget());
    }
    
    
    
    
}
