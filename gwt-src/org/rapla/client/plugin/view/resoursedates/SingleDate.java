package org.rapla.client.plugin.view.resoursedates;

import java.text.ParseException;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.Date;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;


public class SingleDate implements Comparable<SingleDate>{
	
	private String date, begin, end;
	private double vorlesungsStunden;
	private FlowPanel main = new FlowPanel();
	private DateTimeFormat sdfToDate = DateTimeFormat.getFormat("dd.MM.yyyy");
	private DateTimeFormat sdfToTime = DateTimeFormat.getFormat( "HH:mm" );
	private Date dateObject, beginTime, endTime;
	
	public SingleDate(){
		
	}


	@Deprecated
	public SingleDate(String date, String begin, String end,
			double vorlesungsStunden) throws ParseException {
		this.date = date;
		this.begin = begin;
		this.end = end;
		this.vorlesungsStunden = vorlesungsStunden;
	
		dateObject = sdfToDate.parse(date);
		beginTime = sdfToTime.parse(begin);
		endTime = sdfToTime.parse(begin);
	}
	
	public SingleDate(Date date, Date begin, Date end) throws ParseException {
		this.dateObject = date;
		this.beginTime = begin;
		this.endTime = end;
		
		this.date = sdfToDate.format(date);
		this.begin = sdfToTime.format(begin);
		this.end = sdfToTime.format(end);
		
		this.vorlesungsStunden = (((end.getTime() - begin.getTime()) / 60000 ) / 45);		
	}
	
	private void createSingleDate() {
		
		main.setStyleName("singleDate");
			
		Label element = new Label("\u25CF");
		element.setStyleName("singleDateSign");
		
		FlowPanel times = new FlowPanel();
		times.setStyleName("singleDateTimes");
		times.add(new Label(begin));
		times.add(new Label(end));
		
		FlowPanel infos = new FlowPanel();
		Label dateLabel = new Label(date);
		dateLabel.setStyleName("singleDateDateLabel");
		Label infoLabel = new Label(vorlesungsStunden + " Vorlesungsstunden");
		
		
		infos.add(dateLabel);
		infos.add(infoLabel);
		
		main.add(element);
		main.add(times);
		main.add(infos);
	}

	public String getBegin() {
		return begin;
	}

	public String getEnd() {
		return end;
	}

	public double getVorlesungsstunden() {
		return vorlesungsStunden;
	}
	public String getDate() {
		return date;
	}
	public FlowPanel getSingleDate() {
		this.createSingleDate();
		return main;
	}



	@Override
	public int compareTo(SingleDate o) {
		int back = 0;
		
		if (this.dateObject.compareTo(o.dateObject) == -1){
			back = -1;
		}else if (this.dateObject.compareTo(o.dateObject) == 0){
			if(this.beginTime.compareTo(o.beginTime) == -1){
				back = -1;
			}else if(this.beginTime.compareTo(o.beginTime) == 0){
				back = 0;
			}else{
				back = 1;
			}
		}else{
		back = 1;
		}
		return back;
	}
	
}
