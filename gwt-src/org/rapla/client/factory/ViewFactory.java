package org.rapla.client.factory;

import org.rapla.client.edit.reservation.impl.ReservationController;
import org.rapla.client.plugin.view.infos.InfoView;
import org.rapla.client.plugin.view.resoursedates.ResourceDatesView;

public class ViewFactory {
	public static ViewServiceProviderInterface getInstance(ViewEnumTypes viewId){ //mit enum ersetzten
		ViewServiceProviderInterface view;
		
	if (viewId.equals(ViewEnumTypes.INFOVIEW_DESKTOP)){
		  view = new InfoView();	
	} 
	else {
		 view = new ResourceDatesView();	
	}
	
	 return view;
	}
}
