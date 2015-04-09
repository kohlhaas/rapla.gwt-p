package org.rapla.client.plugin.weekview.gwt;

import java.util.Collection;

import org.rapla.client.base.AbstractView;
import org.rapla.client.plugin.weekview.CalendarWeekView;
import org.rapla.entities.domain.Reservation;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;

public class CalendarWeekViewImpl extends AbstractView<org.rapla.client.plugin.weekview.CalendarWeekView.Presenter> implements CalendarWeekView<IsWidget> {

    private HTML html;

    public CalendarWeekViewImpl() {
        html = new HTML();
    }

    @Override
    public void update(Collection<Reservation> result) {
        
    }

    @Override
    public IsWidget provideContent() {
        return html;
    }
}
