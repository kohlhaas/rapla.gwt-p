package org.rapla.client.plugin.view.resoursedates;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.FlowPanel;

public class TerminList extends FlowPanel {
	
	private List<DateParent> dates = new ArrayList<>();
	private int active = -1;

	public TerminList() {
		// TODO Auto-generated constructor stub
	}

	public TerminList(String tag) {
		super(tag);
		// TODO Auto-generated constructor stub
	}
	
	public void add(SingleDate s){
		super.add(s);
		dates.add(s);
	}
	
	public void add(MultiDate md){
		super.add(md.getMultiDateLabel());
		dates.add(md);
	}
	public DateParent getDate(int index){
		return dates.get(index);
		
	}
	public void removeDate(int index){
		super.remove(index);
		dates.remove(index);	
	}

	public void setActive(DateParent date) {
		active = getDateParentIndex(date);		
	}
	public int getActive(){
		return active;
	}
	
	public int getDateParentIndex(DateParent date){
		return dates.indexOf(date);
	}

}
