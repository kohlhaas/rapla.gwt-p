package org.rapla.client.plugin.view.infos;

import org.rapla.client.plugin.view.ContentDrawer;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;

public class InfoDrawer implements ContentDrawer {

	private LayoutPanel content;
	private FlowPanel contentLeft;
	private FlowPanel contentRight;
	
	private ListBox listBox;	
  
	
	@Override
	public Widget createContent() {
		//content = new LayoutPanel();
		//contentLeft = new FlowPanel();
		//contentRight = new FlowPanel();
		
		FlowPanel inhalt = new FlowPanel();
	    
	    
	  //  Label testText = new Label("halliahllo alles mal wieder nur ein test um zu schauen, ob auch wirklich alles funktioniert...");
		listBox = new ListBox();
	        
	     //add the Event Types from data.xml here
	    listBox.addItem("Lehrveranstaltung");
	    listBox.addItem("Püfung");
	    listBox.addItem("Sonstige Veranstaltung");
		
	  //  contentLeft.add(listBox);
	   // contentRight.add(testText);
	    	    
	   // content.add(contentLeft);	    
	   // content.add(contentRight);
	    
	   // content.setWidgetLeftWidth(contentLeft, 0, Style.Unit.PCT, 30, Style.Unit.PCT);  // Left panel
	  //  content.setWidgetRightWidth(contentRight, 0, Style.Unit.PCT, 70, Style.Unit.PCT);  // Left panel
	    
	    inhalt.add(inhalt);
	    return inhalt;
		//return content;
	}

	@Override
	public void updateContent() {
		// TODO Auto-generated method stub

	}

}
