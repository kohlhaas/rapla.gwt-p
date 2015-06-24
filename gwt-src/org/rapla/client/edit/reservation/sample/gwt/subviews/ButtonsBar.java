package org.rapla.client.edit.reservation.sample.gwt.subviews;

import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.client.edit.reservation.sample.gwt.gfx.ImageImport;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;

public class ButtonsBar extends FlowPanel
{
    private static final ImageResource IMG_ICON_SAVE = ImageImport.INSTANCE.saveIcon();
    private static final ImageResource IMG_ICON_CANCEL = ImageImport.INSTANCE.cancelIcon();
    private static final ImageResource IMG_ICON_DELETE = ImageImport.INSTANCE.deleteIcon();
    private static final ImageResource IMG_ICON_UNDO = ImageImport.INSTANCE.undoIcon();
    private static final ImageResource IMG_ICON_REDO = ImageImport.INSTANCE.redoIcon();

    private Presenter presenter;

    public ButtonsBar()
    {
        final Image cancel = new Image(IMG_ICON_CANCEL);
        cancel.setStyleName("cancelButton");
        cancel.setTitle("Abbrechen");
        cancel.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent e)
            {
                presenter.onCancelButtonClicked();
            }
        });

        final Image save = new Image(IMG_ICON_SAVE);
        //save = new Button("speichern");
        save.setStyleName("saveButton");
        save.setTitle("Veranstaltung speichern");
        save.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent e)
            {
                presenter.onSaveButtonClicked();
            }
        });

        final Image delete = new Image(IMG_ICON_DELETE);
        //delete = new Button("l\u00F6schen");
        delete.setStyleName("deleteButton");
        delete.setTitle("Veranstaltung l\u00F6schen");
        delete.addClickHandler(new ClickHandler()
        {
            @Override
            public void onClick(ClickEvent e)
            {
                presenter.onDeleteButtonClicked();
            }
        });

        final Image undo = new Image(IMG_ICON_UNDO);
        undo.setStyleName("undoButton");
        undo.setTitle("R\u00FCckg\u00E4ngig");

        final Image redo = new Image(IMG_ICON_REDO);
        redo.setStyleName("redoButton");
        redo.setTitle("Wiederholen");

        add(save);
        add(cancel);
        add(delete);
        add(undo);
        add(redo);
        //      buttonsPanel.add(plus);
        setStyleName("mainButtonsBar");
    }

    public void setPresenter(Presenter presenter)
    {
        this.presenter = presenter;
    }

}
