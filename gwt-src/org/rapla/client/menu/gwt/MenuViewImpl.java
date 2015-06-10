package org.rapla.client.menu.gwt;

import java.util.List;

import org.rapla.client.base.AbstractView;
import org.rapla.client.gwt.components.MenuPopup;
import org.rapla.client.menu.MenuView;
import org.rapla.client.menu.data.MenuCallback;
import org.rapla.client.menu.data.MenuEntry;
import org.rapla.client.menu.data.Point;
import org.rapla.framework.RaplaException;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;

public class MenuViewImpl extends AbstractView<org.rapla.client.menu.MenuView.Presenter> implements MenuView<IsWidget>
{

    @Override
    public void showException(RaplaException ex)
    {
        final PopupPanel popupPanel = new PopupPanel(true, true);
        popupPanel.add(new HTML(ex.getMessage()));
        popupPanel.center();
        popupPanel.show();
    }

    @Override
    public void showMenuPopup(List<MenuEntry> menu, Point p, MenuCallback menuCallback)
    {
        new MenuPopup(menu, menuCallback, p);
    }

}
