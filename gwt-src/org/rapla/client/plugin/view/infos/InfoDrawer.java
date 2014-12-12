package org.rapla.client.plugin.view.infos;

import org.rapla.client.plugin.view.ContentDrawer;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class InfoDrawer implements ContentDrawer {

	private FlowPanel content;
	private FlowPanel contentLeft;
	private FlowPanel contentRight;
	
	private ListBox listBox;	
  
	
	@Override
	public Widget createContent() {
		
		content = new FlowPanel();
		contentLeft = new FlowPanel();
		contentRight = new FlowPanel();
		
		content.add(contentLeft);
		content.add(contentRight);
	    
	    
		listBox = new ListBox();
	        
	     //add the Event Types from data.xml here
	    listBox.addItem("Lehrveranstaltung");
	    listBox.addItem("Pr\u00FCfung");
	    listBox.addItem("Sonstige Veranstaltung");
		
	    
	   // content.setWidgetLeftWidth(contentLeft, 0, Style.Unit.PCT, 30, Style.Unit.PCT);
	   // content.setWidgetRightWidth(contentRight, 0, Style.Unit.PCT, 70, Style.Unit.PCT); 
	    
	    contentLeft.add(listBox);
	    return content;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
