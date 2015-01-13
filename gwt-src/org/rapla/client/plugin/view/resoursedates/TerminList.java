package org.rapla.client.plugin.view.resoursedates;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;

public class TerminList extends FlowPanel {
	
	private List<RaplaDate> dates = new ArrayList<>();
	private int active = -1;

	public TerminList() {
		// TODO Auto-generated constructor stub
	}

	public TerminList(String tag) {
		super(tag);
		// TODO Auto-generated constructor stub
	}
	
	public void add(final RaplaDate s){
		super.add(s);
		dates.add(s);
		/*
		 * Needs GWTBus (fireEvent) to embed in ResourceDatesView
		s.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				int position = dates.indexOf(s);
				active = active == position ? -1 : position;				
			}
			
		});
		*/
	}
	
	public RaplaDate getDate(int index){
		return dates.get(index);
		
	}
	public void removeDate(int index){
		super.remove(index);
		dates.remove(index);	
	}

	public void setActive(RaplaDate date) {
		active = getRaplaDateIndex(date) == active ? -1 : getRaplaDateIndex(date);		
	}
	public int getActive(){
		return active;
	}
	
	public int getRaplaDateIndex(RaplaDate date){
		return dates.indexOf(date);
	}

}
