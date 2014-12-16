package org.rapla.client.plugin.view.infos;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class InfoHorizontalPanel extends HorizontalPanel {

	public InfoHorizontalPanel(String width) {
		super();
		this.setWidth(width);
		this.setStylePrimaryName("InfoHorizontalPanel");
	}
	
	public void add(Widget w, String width){
		super.add(w);
		this.setCellHorizontalAlignment(w, HasHorizontalAlignment.ALIGN_CENTER);
		this.setCellVerticalAlignment(w, HasVerticalAlignment.ALIGN_MIDDLE);
		this.setCellWidth(w, width);
	}
	
	public void finalize(int anzahlZeilen){
		while(this.getWidgetCount() < anzahlZeilen){
			Label blanc = new Label("");
			this.add(blanc, this.getWidget(0).getOffsetWidth() + "px");
		}
	}

}

