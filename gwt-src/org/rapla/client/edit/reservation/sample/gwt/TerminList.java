package org.rapla.client.edit.reservation.sample.gwt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlowPanel;

public class TerminList extends FlowPanel {
	
	private List<RaplaDate> dates = new ArrayList<RaplaDate>();
	private int active = -1;
	private boolean firstWidget = true;
	

	public TerminList() {
		// TODO Auto-generated constructor stub
	}

	public TerminList(String tag) {
		super(tag);
		// TODO Auto-generated constructor stub
	}
	
	public void add(final RaplaDate s){
		
		if(firstWidget){
			this.remove(0);
			firstWidget = false;
		}
			
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
		super.remove(index+1);
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
	public int getLastPosition(){
		return dates.size()-1;
	}
	public RaplaDate getLastRaplaDate(){
		return dates.get(getLastPosition());
	}

	public void setStyle(int RaplaDatePosition, String styleName) {
		// TODO Auto-generated method stub
		dates.get(RaplaDatePosition).setStyleName(styleName);
		
	}
	public void removeStyle(int RaplaDatePosition, String styleName) {
		// TODO Auto-generated method stub
		dates.get(RaplaDatePosition).removeStyleName(styleName);
		
	}
	
	//Assume the resources of the given RaplaDate to all created resources
	public void setResources(RaplaDate Date) {
		// TODO Auto-generated method stub
		
	}

	public List<RaplaDate> checkConflict() {
		List<RaplaDate> conflictingDates = new ArrayList<>();
		for (int j = 0; j < dates.size(); j++){
			for (int i = j+1; i <= dates.size()-1; i++){
						if(
							    Math.min(dates.get(j).getEndTime().getTime(), dates.get(i).getEndTime().getTime())
							    >= Math.max(dates.get(j).getBeginTime().getTime(), dates.get(i).getBeginTime().getTime())
							){
							    //interval of dates overlap							
							if(!dates.get(j).getResources().containsAll(dates.get(i).getResources())  && !dates.get(i).getResources().containsAll(dates.get(j).getResources())){
								//Resources overlap
								conflictingDates.add(dates.get(j));
								conflictingDates.add(dates.get(i));
							}
							}
			}
		}
		return conflictingDates;
	}

	public List<RaplaDate> getDates() {
		return dates;
	}

	public void setDates(List<RaplaDate> dates) {
		if(firstWidget){
			this.remove(0);
			firstWidget = false;
		}
		
		this.dates = dates;
		for(RaplaDate date: dates){
			super.add(date);
		}
	}
	
	

}
