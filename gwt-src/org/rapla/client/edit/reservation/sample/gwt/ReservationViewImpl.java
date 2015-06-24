package org.rapla.client.edit.reservation.sample.gwt;

import java.util.Locale;

import javafx.scene.control.Tab;

import javax.inject.Inject;

import org.rapla.client.base.AbstractView;
import org.rapla.client.edit.reservation.sample.ReservationView;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.client.edit.reservation.sample.gwt.subviews.ButtonsBar;
import org.rapla.client.edit.reservation.sample.gwt.subviews.InfoView;
import org.rapla.client.edit.reservation.sample.gwt.subviews.ResourceDatesView;
import org.rapla.client.gwt.view.RaplaPopups;
import org.rapla.entities.User;
import org.rapla.entities.domain.Allocatable;
import org.rapla.entities.domain.Reservation;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabBar;

public class ReservationViewImpl extends AbstractView<Presenter> implements ReservationView<IsWidget>
{

    private final Logger logger;

    private final RaplaLocale raplaLocale;
    private final FlowPanel content = new FlowPanel();
    private final FlowPanel tabBarPanel;
    private final ButtonsBar buttonsPanel;
    private final TabBar bar;
    private HandlerRegistration selectionHandler;

    @Inject
    public ReservationViewImpl(Logger logger, RaplaLocale raplaLocale)
    {
        super();
        this.logger = logger;
        this.raplaLocale = raplaLocale;
        tabBarPanel = new FlowPanel();
        bar = new TabBar();
        bar.addStyleName("tabbar");
        bar.addTab("Veranstaltungsinformationen");
        bar.addTab("Termine und Ressourcen");
        bar.addTab("Rechte");
        tabBarPanel.add(bar);
        buttonsPanel = new ButtonsBar();
        bar.selectTab(0);
    }

    @Override
    public void setPresenter(org.rapla.client.edit.reservation.sample.ReservationView.Presenter presenter)
    {
        super.setPresenter(presenter);
        buttonsPanel.setPresenter(presenter);
    }

    @Override
    public void showWarning(String title, String warning)
    {
        RaplaPopups.showWarning(title, warning);
    }

    public void show(final Reservation reservation, final User user)
    {
        final Panel popup = RaplaPopups.getPopupPanel();
        popup.clear();
        popup.setVisible(true);
        popup.setHeight("90%");
        popup.setWidth("90%");
        final InfoView infoView = new InfoView(getPresenter(), raplaLocale, user);
        final ResourceDatesView resourcesView = new ResourceDatesView(getPresenter(), raplaLocale);
        if (selectionHandler != null)
        {
            selectionHandler.removeHandler();
        }
        selectionHandler = bar.addSelectionHandler(new SelectionHandler<Integer>()
        {
            public void onSelection(SelectionEvent<Integer> event)
            {
                final Integer index = event.getSelectedItem();
                updateContent(reservation, infoView, resourcesView, index);
            }
        });
        updateContent(reservation, infoView, resourcesView, bar.getSelectedTab());
        final FlowPanel layout = new FlowPanel();
        layout.add(buttonsPanel);
        layout.add(tabBarPanel);
        layout.add(content);
        popup.add(layout);
    }

    private void updateContent(final Reservation reservation, final InfoView infoView, final ResourceDatesView resourcesView, final Integer index)
    {
        for (int i = 0; i < bar.getTabCount(); i++)
        {
            bar.setTabEnabled(i, true);
        }
        bar.setTabEnabled(index, false);
        content.clear();
        infoView.clearContent();
        resourcesView.clearContent();
        if (index == 0)
        {
            infoView.createContent(reservation);
            content.add(infoView.provideContent());
        }
        else if (index == 1)
        {
            resourcesView.createContent(reservation);
            content.add(resourcesView.provideContent());
        }
        else
        {
            RaplaPopups.showWarning("Navigation error", "Unknown View at index " + index);
        }
    }

    public void mapFromReservation(Reservation event)
    {
        Locale locale = getRaplaLocale().getLocale();
        Allocatable[] resources = event.getAllocatables();
        {
            StringBuilder builder = new StringBuilder();
            for (Allocatable res : resources)
            {
                builder.append(res.getName(locale));
            }
        }
    }

    public void hide()
    {
        final RootPanel popup = RaplaPopups.getPopupPanel();
        popup.setVisible(false);
        popup.clear();
    }
}
