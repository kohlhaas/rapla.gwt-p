package org.rapla.client.edit.reservation.sample.gwt;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.client.edit.reservation.sample.ResourceDatesView;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;

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

import org.rapla.client.edit.reservation.sample.InfoView; 


public class ReservationViewImpl extends AbstractView<Presenter> implements ReservationView<IsWidget> {
    
	//for general popup
	private FlowPanel mainPanel;
	private VerticalPanel layout;
	private FlowPanel tabBarPanel;
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
	
	ReservationEditSubView currentView; 
	Panel currentTabContent;
    
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
		buttonsPanel = new FlowPanel();

		tabBarPanel.add(bar);
        
		bar.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				getPresenter().onTabChanged(bar.getSelectedTab());
			}
		});
		
		

		buttonsPanel.add(cancel);
		buttonsPanel.add(save);
		buttonsPanel.add(delete);

		layout.add(buttonsPanel);
		layout.add(tabBarPanel);

		layout.setCellHeight(buttonsPanel, "40px");
		layout.setCellHeight(tabBarPanel, "50px");

		popup.add(layout);
		//popup.center();

	
		bar.selectTab(0);

		
        }
    
       
     public void update(ReservationEditSubView tempView ){
    	
    	 if(currentTabContent != null){
    		 currentTabContent.clear();
    	 }
    	 
    	 this.currentView =  tempView; 
    	 
    	 if(currentView instanceof InfoView ){
    		 ((InfoView) currentView).show(); 
        	currentTabContent = (Panel) ((InfoView) currentView).provideContent();
        	 
    	 }else if(currentView instanceof ResourceDatesView){
    		 ((ResourceDatesView) currentView).show(); 
    		 
    		 currentTabContent = (Panel) ((ResourceDatesView) currentView).provideContent();
    	   
        	 
    	 }
    	 
    	 
    	 popup.add(currentTabContent);
    	 
    	
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
    }


    @Override
    public void addSubView(ReservationEditSubView<IsWidget> view) {
        IsWidget provideContent = view.provideContent();
     //   subView.add( provideContent.asWidget());
    }


	

	public void setEventTypes(List<String> eventTypes) {

		for (int i = 0; i < eventTypes.size(); i++) {
			eventTypesListBox.addItem((String) eventTypes.get(i).toString());
		}

	}

	


	public void setSelectedEventType(String select) {
		for (int i = 0; i < eventTypesListBox.getItemCount(); i++) {
			if (eventTypesListBox.getItemText(i).equals(select)) {
				eventTypesListBox.setItemSelected(i, true);
			}
		}
	}

	
	public void setDynamicFields(Attribute[] attributes) {

		for (int i = 0; i < attributes.length; i++) {

			String name = attributes[i].toString();

			eventTypesListBox.addItem(name);

		}

	}

	public void setConstraintKeys(String[] constraintKeys) {

	}

	//resources and dates elements
	
	public FlowPanel getMainContent() {
		return mainContent;
	}

	public void setMainContent(FlowPanel mainContent) {
		this.mainContent = mainContent;
	}

	public FlowPanel getDateList() {
		return dateList;
	}

	public FlowPanel getButtonBar() {
		return buttonBar;
	}

	public void setButtonBar(FlowPanel buttonBar) {
		this.buttonBar = buttonBar;
	}

	public DateBox getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(DateBox dateBegin) {
		this.dateBegin = dateBegin;
	}

	public DateBox getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(DateBox dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Tree getResourceTree() {
		return resourceTree;
	}

	public void setResourceTree(Tree resourceTree) {
		this.resourceTree = resourceTree;
	}

	public FlowPanel getChosenResources() {
		return chosenResources;
	}

	public void setChosenResources(FlowPanel chosenResources) {
		this.chosenResources = chosenResources;
	}

	public Label getButtonNextGap() {
		return buttonNextGap;
	}

	public void setButtonNextGap(Label buttonNextGap) {
		this.buttonNextGap = buttonNextGap;
	}

	public Label getButtonGarbageCan() {
		return buttonGarbageCan;
	}

	public void setButtonGarbageCan(Label buttonGarbageCan) {
		this.buttonGarbageCan = buttonGarbageCan;
	}

	public Label getButtonPlus() {
		return buttonPlus;
	}

	public void setButtonPlus(Label buttonPlus) {
		this.buttonPlus = buttonPlus;
	}

	public ArrayList<List<String>> getToBeReservedResources() {
		return toBeReservedResources;
	}

	public void setToBeReservedResources(ArrayList<List<String>> toBeReservedResources) {
		this.toBeReservedResources = toBeReservedResources;
	}

	private void addDateWidget() {

	//	RaplaDate addTermin = new RaplaDate();
			
//		Date beginTmp = new Date(dateBegin.getValue().getTime() + timeBegin.getTime() + 3600000);
//		Date endTmp = new Date(dateEnd.getValue().getTime() + timeEnd.getTime() + 3600000);
		
		
//		if(beginTmp.after(endTmp)){
//		addDateInfo.setStyleName("error");	
//		addDateInfo.setText("Begin- nach Endtermin!");
//		}else{
//			addDateInfo.setStyleName("");
//			addDateInfo.setText("");
//			if(daily.getValue() || weekly.getValue() || monthly.getValue() || year.getValue()){
//				List<RaplaDate> tmp = new ArrayList<>();
//				int type;
//				if(daily.getValue()){
//					type = 1;
//				}else if (weekly.getValue()){
//					type = 2;
//				}else if( monthly.getValue()){
//					type = 3;
//				}else{
//					type = 4;
//				}
////				tmp = RaplaDate.recurringDates(dateBegin.getValue(), dateEnd.getValue(), timeBegin.getTime() + 3600000,timeEnd.getTime() + 3600000, type);
//				try {
//			//		tmp.add(new RaplaDate(beginTmp, new Date(dateBegin.getValue().getTime() + timeEnd.getTime() + 3600000), true));
//					addTermin = new RaplaDate(tmp);
//					dateList.add(addTermin);
//				//	addTermin.addClickHandler(new RaplaDateClickHandler());
//					clearDateTimeInputFields();
//				} catch (ParseException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}else{
//			try {	
//				addTermin = new RaplaDate(beginTmp, endTmp, true);
//			//	addTermin.addClickHandler(new RaplaDateClickHandler());
//				dateList.add(addTermin);
//				clearDateTimeInputFields();
//			} catch (ParseException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
	//		}
	//	}
	}
	private void clearDateTimeInputFields(){
		dateBegin.setValue(null);
		dateEnd.setValue(null);
	//	timeEnd.setValue((long) -3600000);
		//timeBegin.setValue((long) -3600000);
		cbWholeDay.setValue(false);
		rewriteDate.setVisible(false);
		buttonGarbageCan.setStyleName("buttonsResourceDates");
		noReccuring.setValue(true);
		cbRepeatType.setOpen(false);
	}
    
    
}
