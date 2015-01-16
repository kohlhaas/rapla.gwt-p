package org.rapla.client.edit.reservation.sample.gwt;


import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.HasWidgets.ForIsWidget;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationEditSubView;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;

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
    FlowPanel contentRes;
    FlowPanel subView = new FlowPanel();
    FlowPanel generalInformation;
    FlowPanel zeile1;
    FlowPanel part2;
    FlowPanel coursePanel;
    
    Grid grid;
    
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
        
        //zeile1
        zeile1 = new FlowPanel();
        zeile1.setStyleName("zeile1");
        
        coursePanel = new FlowPanel();
        coursePanel.setStyleName("coursePanel");
        
        //Second part of the structure
        part2 = new FlowPanel();
        grid = new Grid(2, 2);
        grid.setStyleName("grid");
        
        //Structuring up and down button
        updown = new VerticalPanel(); 
        updown.setStyleName("updown");
       
        //FlowPanel for resources-part
        contentRes = new FlowPanel();

        
        //Clear Panels
        popup.clear();
        tabPanel.clear();
        content.clear();
        generalInformation.clear();
        zeile1.clear();
        part2.clear();
        grid.clear();
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
        generalInformation.add(zeile1);
        generalInformation.add(coursePanel);
        generalInformation.add(part2);
        part2.add(grid);
        
        
        /* Filling structure */
        
        // Eventtype
        ListBox eventType = new ListBox();
        eventType.setStyleName("eventtype");
        eventType.addItem("Lehrveranstaltung");
        eventType.addItem("Klausur");
        chosenEventType = eventType.getSelectedValue();
        zeile1.add(eventType);
        
        //DOPPELT initEvenTypeLB();
        
       
        // Language selection
        ListBox language = new ListBox();
        language.addItem("Deutsch");
        language.addItem("Englisch");
        chosenLanguage = eventType.getSelectedValue();
        language.setStyleName("language");
        zeile1.add(language);
        
        //DOPPELT initLanguageLB();
        
        //Study course
        {
            Button course = new Button("Studiengang");
            course.setStyleName("course");
            course.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent e) {
                	coursePanel.setVisible(true);
                	
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
                	ausblenden.addClickHandler(new ClickHandler(){
                		@Override
                		public void onClick(ClickEvent e) {
                			coursePanel.setVisible(false);
                		}
                	});
                    
                	new MyDialog().show();
                	
                    getPresenter().onCourseButtonClicked();
                }
            });
            
            zeile1.add(course);
        }
        
        
        

    	
    	
        
        //DOPPELT initCourseButton();


        //Label eventname
        Label eventname = new Label("Veranstaltungsname");
        eventname.setStyleName("eventname");
        grid.setWidget(0,0, eventname);
        
        //TextBox for insert eventname 
        {
            tb = new TextBox();
            tb.setStyleName("textbox");
            tb.addChangeHandler(new ChangeHandler(){

                @Override
                public void onChange(ChangeEvent event) {
                    getPresenter().changeEventName(tb.getText());
                }
            });
            grid.setWidget(0,1, tb);
        }

        
        //Label planned lesson hours
        Label planhour = new Label("geplante Vorlesungsstunden");
        planhour.setStyleName("planhour");
        grid.setWidget(1,0, planhour);
        
        //TextBox for insert planned hours 
        TextBox tbPlanhour = new TextBox();
        tbPlanhour.setStyleName("tbPlanhour");
        grid.setWidget(1,1, tbPlanhour);
        
        
        
        //Buttons for count up planned hours
        Button up = new Button();
        up.setStyleName("up");
        //String html = "<div><center><img src = '/images/TriangleUp.png' height = '7px' width = '7px'></img></center><label>Text</label></br></div>";
        //String html = "<div><center><img src = '/images/TriangleUp.png' height = '7px' width = '7px'></img></center></div>";
        //up.setHTML(html);
        updown.add(up);
        
/*        Button down = new Button();
        down.setSize("15px", "15px");
        down.setStyleName("down");
        String html2 = "<div class = 'img' ><img class= 'img_1' src = '/images/triangledown_klein.png'></img></div>";
        down.setHTML(html2);
        updown.add(down);  */
        
        part2.add(updown);
        
        Label info = new Label("Sonstige Veranstaltungsinformationen:");
        part2.add(info);
        
        TextArea taInfo = new TextArea();
        taInfo.setStyleName("taInfo");
        part2.add(taInfo);
        

        
        
        
        
        

        
        
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
