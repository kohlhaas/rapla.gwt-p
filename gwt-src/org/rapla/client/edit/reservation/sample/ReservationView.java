package org.rapla.client.edit.reservation.sample;

import org.rapla.client.base.View;
import org.rapla.client.edit.reservation.AttributeValue.AttributeValues;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.Category;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.facade.Conflict;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface ReservationView<W> extends View<Presenter> {

    public interface Presenter {
        void onSaveButtonClicked();

        void onDeleteButtonClicked();

        void changeReservationName(String newEvent);

        boolean isDeleteButtonEnabled();

        void onCancelButtonClicked();

        DynamicType[] getAllEventTypes();

        Category[] getCategory(Locale locale, String searchCategory);

        String getCurrentReservationName(Locale locale);

        String getEventType(Locale locale);

        void changeAttributesOfCLassification(Map<String, Object> attributeNames, Locale locale);

        List<String> getAllCurrentAttributesAsStrings(Locale locale);

        boolean getIsNew();

        void setAttributesOfReservation(Map<Attribute, Object> valuesToSave, Map<Attribute, Collection<Object>> attributeCollectionMap);

        Attribute[] getAllCurrentAttributes();

		Category[] getCategories();

		AttributeValues getCurrentAttributesOfReservationWithValues();

		void eventTypeChanged(DynamicType dynamicType);

    }

    void show(Reservation event);
    
    

    void hide();

    void addSubView(String tabName, ReservationEditSubView<W> view);
}
