package org.rapla.client.mwi14_1.view.resoursedates;

import java.text.ParseException;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class RaplaDate extends Composite implements Comparable<RaplaDate>, HasClickHandlers {

	private double vorlesungsStunden;
	private FlowPanel main = new FlowPanel();
	private DateTimeFormat sdfToTime = DateTimeFormat.getFormat("HH:mm");
	private DateTimeFormat sdfToDay = DateTimeFormat.getFormat("dd.MM.yyyy");
	private Date beginTime, endTime;
	private boolean calculateLectureHours;

	private List<RaplaDate> raplaDates = new ArrayList<>();
	private DisclosurePanel singleDatesContainer = new DisclosurePanel();

	public RaplaDate() {

	}

	public RaplaDate(Date begin, Date end, boolean calculateLectureHours)
			throws ParseException {
		this.beginTime = begin;
		this.endTime = end;
		this.calculateLectureHours = calculateLectureHours;

		this.vorlesungsStunden = (((end.getTime() - begin.getTime()) / 60000) / 45);
		createSingleDate();
		initWidget(main);
	}

	public RaplaDate(List<RaplaDate> list) {
		this.raplaDates = list;
		createMultiDateLabel();
		initWidget(main);
	}

	private void createSingleDate() {

		main.setStyleName("singleDate");

		Label element = new Label("\u25CF");
		element.setStyleName("singleDateSign");

		FlowPanel times = new FlowPanel();
		times.setStyleName("singleDateTimes");
		times.add(new Label(sdfToTime.format(beginTime)));
		times.add(new Label(sdfToTime.format(endTime)));

		FlowPanel infos = new FlowPanel();
		Label dateLabel = new Label(sdfToDay.format(beginTime));
		dateLabel.setStyleName("singleDateDateLabel");

		Label infoLabel = new Label();
		if (calculateLectureHours == true) {
			// call plugin to calculate lecture hours
			infoLabel.setText(vorlesungsStunden
					+ (vorlesungsStunden == 1 ? " Vorlesungsstunde"
							: " Vorlesungsstunden"));
		}

		infos.add(dateLabel);
		infos.add(infoLabel);

		main.add(element);
		main.add(times);
		main.add(infos);
	}

	public void createMultiDateLabel() {

		main.setStyleName("singleDate");
		Collections.sort(raplaDates);
		FlowPanel helper = new FlowPanel();

		for (Iterator<RaplaDate> iter = raplaDates.iterator(); iter.hasNext();) {
			RaplaDate sd = iter.next();
			if (sd.calculateLectureHours == true)
				vorlesungsStunden = vorlesungsStunden
						+ sd.getVorlesungsstunden();
			helper.add(sd);
		}
		this.beginTime = raplaDates.get(0).getBeginTime();
		this.endTime = raplaDates.get(raplaDates.size() - 1).getEndTime();

		// a DisclosurePanel can only contain two Widgets
		singleDatesContainer.add(helper);

		singleDatesContainer.setStyleName("singleDatesContainer");

		FlowPanel infos = new FlowPanel();
		infos.setStyleName("multiDateInfos");
		Label dateLabel = new Label(
				raplaDates.get(0).sdfToDay.format(beginTime)
						+ " - "
						+ raplaDates.get(raplaDates.size() - 1).sdfToDay
								.format(endTime));
		dateLabel.setStyleName("singleDateDateLabel");
		Label infoLabel = new Label(vorlesungsStunden + " Vorlesungsstunden");

		infos.add(dateLabel);
		infos.add(infoLabel);

		singleDatesContainer.setHeader(infos);
		main.add(singleDatesContainer);
	}


	public static List<RaplaDate> recurringDates(Date startDay, Date endDay, long startTime, long endTime,
			int repeatType) {
		List<RaplaDate> tmp = new ArrayList<>();
		RaplaDate next;
		long day = 86400000;
		while (startDay.before(endDay)) {
			switch (repeatType) {
			case 1:
				startDay = new Date(startDay.getTime() + day);
				break;
			case 2:
				startDay = new Date(startDay.getTime() + (day*7));
				break;
			case 3:
				startDay = new Date(startDay.getTime() + (day*7*4));
				break;
			case 4:
				startDay = new Date(startDay.getTime() + (day*365));
			default:
				break;

			}
			try {
				if((startDay.before(endDay))){
				next = new RaplaDate(new Date(startDay.getTime() + startTime), new Date(startDay.getTime() + endTime), true);
				tmp.add(next);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			

		}
		//tmp.remove(tmp.size()-1);
		return tmp;
	}

	@Override
	public int compareTo(RaplaDate o) {
		int back = 0;
		if (this.beginTime.compareTo(o.beginTime) == -1) {
			back = -1;
		} else if (this.beginTime.compareTo(o.beginTime) == 0) {
			back = 0;
		} else {
			back = 1;
		}
		return back;
	}

	public double getVorlesungsstunden() {
		return vorlesungsStunden;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public boolean isReccuringDate() {
		return this.raplaDates.isEmpty();
	}

	public List<RaplaDate> getRaplaDateList() {
		return this.raplaDates;
	}
	
	public long getStartHourMinute(){
		return (long) ((beginTime.getHours()*3600000) + (beginTime.getMinutes()*60000));
	}
	public long getEndHourMinute(){
		return (long) ((endTime.getHours()*3600000) + (endTime.getMinutes()*60000));
	}

	@Override
	public HandlerRegistration addClickHandler(ClickHandler handler) {
		// TODO Auto-generated method stub
		return addDomHandler(handler, ClickEvent.getType());
	}

}
