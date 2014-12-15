package org.rapla.client.plugin.view.infos;

import org.rapla.client.plugin.view.ContentDrawer;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class InfoDrawer implements ContentDrawer {

	private HorizontalPanel content;
	private VerticalPanel contentLeft;
	private VerticalPanel contentRight;
	
	private ListBox listBox;	
  
	
	@Override
	public Widget createContent() {
		
		Integer width = (int) (Window.getClientWidth() * 0.9) / 2 ;
		
		content = new HorizontalPanel();
		//content.addStyleName("infoHorizontalPanel");
		//content.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contentLeft = new VerticalPanel();
		contentRight = new VerticalPanel();
		contentLeft.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		contentRight.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
	    
	    
		listBox = new ListBox();
	        
	     //add the Event Types from data.xml here
	    listBox.addItem("Lehrveranstaltung");
	    listBox.addItem("Pr\u00FCfung");
	    listBox.addItem("Sonstige Veranstaltung");
		
	    listBox.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				
			}
		});
	    
	    contentLeft.add(listBox);
	    
	    final HorizontalPanel titelPanel = new HorizontalPanel();
	    final Label titel = new Label("Vorlesungstitel");
	    final TextBox titelInput = new TextBox();
	    titelPanel.add(titel);
	    titelPanel.add(titelInput);
	   
	    final Label vorlesungsStunden = new Label("Vorlesungsstunden");
	    final Label vorlesungsStundenMessage = new Label("");
	    final TextBox vorlesungsStundenInput = new TextBox();
	    vorlesungsStundenInput.addKeyPressHandler(new KeyPressHandler() {
	        @Override
	        public void onKeyPress(KeyPressEvent event) {
	            String input = vorlesungsStundenInput.getText();
	            if (input.matches("[0-9]*")) {
	            	double message = Integer.parseInt(input);
	            	message = (message * 45) / 60;
	            	vorlesungsStundenMessage.setText("= " + message + " Stunden");
	            }else{
	            	vorlesungsStundenMessage.setText("Keine korrekter Eingabe");
	            }
	            // do your thang
	        }
	    });
	    final HorizontalPanel vorlesungsStundenPanel= new HorizontalPanel();
	    vorlesungsStundenPanel.add(vorlesungsStunden);
	    vorlesungsStundenPanel.add(vorlesungsStundenInput);
	    vorlesungsStundenPanel.add(vorlesungsStundenMessage);
	    
	    contentRight.add(titelPanel);
	    contentRight.add(vorlesungsStundenPanel);
	    
		content.add(contentLeft);
		content.add(contentRight);
		content.setCellWidth(contentLeft, width + "px");
		content.setCellWidth(contentRight, width + "px");
	    
	    return content;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
