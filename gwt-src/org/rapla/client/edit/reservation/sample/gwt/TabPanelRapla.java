package org.rapla.client.edit.reservation.sample.gwt;

import com.google.gwt.user.client.ui.FlowPanel;

/**
 * Created by miniNam on 19.01.2015.
 */
public class TabPanelRapla {

    String name;

    public TabPanelRapla(String name, FlowPanel tab) {
        this.name = name;
        this.tab = tab;
    }

    public FlowPanel getTab() {
        return tab;
    }

    public void setTab(FlowPanel tab) {
        this.tab = tab;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    FlowPanel tab;

}
