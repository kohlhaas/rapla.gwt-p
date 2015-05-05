package org.rapla.client.edit.reservation.impl;

import javax.inject.Singleton;

import com.google.gwt.inject.client.GinModule;
import com.google.gwt.inject.client.binder.GinBinder;

public class ReservationControllerPluginGinModule implements GinModule {

    @Override
    public void configure(GinBinder binder) {
        binder.bind(org.rapla.client.edit.reservation.ReservationController.class).to(ReservationController.class).in( Singleton.class);
    }

}
