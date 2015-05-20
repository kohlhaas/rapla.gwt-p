package org.rapla.client.edit.reservation;

import static de.vksi.c4j.Condition.postCondition;
import static de.vksi.c4j.Condition.preCondition;

import org.rapla.client.edit.reservation.sample.InfoView;
import org.rapla.client.edit.reservation.sample.InfoViewPresenter;
import org.rapla.client.edit.reservation.sample.ReservationPresenter;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ResourceDatesViewPresenter;

import de.vksi.c4j.Target;

public class InfoViewContract extends InfoViewPresenter {



	@Target
	private ReservationPresenter target;

	public InfoViewContract(InfoView view) {
		super(view);

				 if (preCondition()) {
//			         assert hour >= HOUR_MIN : "hour >= HOUR_MIN";
//			         assert hour <= HOUR_MAX : "hour <= HOUR_MAX";
//			         assert minute >= MINUTE_MIN : "minute >= MINUTE_MIN";
//			         assert minute <= MINUTE_MAX : "minute <= MINUTE_MAX";
//			         assert second >= SECOND_MIN : "second >= SECOND_MIN";
//			         assert second <= SECOND_MAX : "second <= SECOND_MAX";
			      }
			      if (postCondition()) {
//			         assert target.getHour() == hour : "hour set";
//			         assert target.getMinute() == minute : "minute set";
//			         assert target.getSecond() == second : "second set";
			      }}
			
		     
		   

//		   @Override
//		   public void setHour(int hour) {
//		      if (preCondition()) {
//		         assert hour >= HOUR_MIN : "hour >= HOUR_MIN";
//		         assert hour <= HOUR_MAX : "hour <= HOUR_MAX";
//		      }
//		      if (postCondition()) {
//		         assert target.getHour() == hour : "hour set";
//		         assert unchanged(target.getMinute()) : "minute unchanged";
//		         assert unchanged(target.getSecond()) : "second unchanged";
//		      }
//		   }

}