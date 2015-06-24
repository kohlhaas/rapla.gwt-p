package org.rapla.client.edit.reservation.sample.gwt.subviews;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.client.gwt.components.BooleanInputField;
import org.rapla.client.gwt.components.BooleanInputField.BooleanValueChange;
import org.rapla.client.gwt.components.DateComponent;
import org.rapla.client.gwt.components.DateComponent.DateValueChanged;
import org.rapla.client.gwt.components.DropDownInputField;
import org.rapla.client.gwt.components.DropDownInputField.DropDownItem;
import org.rapla.client.gwt.components.DropDownInputField.DropDownValueChanged;
import org.rapla.client.gwt.components.InputUtils;
import org.rapla.client.gwt.components.LongInputField;
import org.rapla.client.gwt.components.LongInputField.LongValueChange;
import org.rapla.client.gwt.components.TextInputField;
import org.rapla.client.gwt.components.TextInputField.TextValueChanged;
import org.rapla.entities.Category;
import org.rapla.entities.User;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.ConstraintIds;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.framework.RaplaLocale;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class InfoView
{

    private final Panel contentPanel;
    private final Presenter presenter;
    private final RaplaLocale raplaLocale;
    private final User user;

    public InfoView(Presenter presenter, RaplaLocale raplaLocale, User user)
    {
        this.presenter = presenter;
        this.raplaLocale = raplaLocale;
        this.user = user;
        contentPanel = new FlowPanel();
        contentPanel.setStyleName("appointmentInfo");
    }

    private Presenter getPresenter()
    {
        return presenter;
    }

    public Widget provideContent()
    {
        return contentPanel;
    }

    public void createContent(final Reservation reservation)
    {
        contentPanel.clear();

        final FlowPanel contentLeft = new FlowPanel();
        contentLeft.setStyleName("content left");
        final FlowPanel contentRight = new FlowPanel();
        contentRight.setStyleName("content right");

        final Locale locale = raplaLocale.getLocale();
        {
            final Collection<DynamicType> dynamicTypes = getPresenter().getChangeableReservationDynamicTypes();
            final Map<String, DynamicType> idToClassification = new HashMap<String, DynamicType>();
            final Collection<DropDownItem> values = new ArrayList<DropDownInputField.DropDownItem>(dynamicTypes.size());
            for (final DynamicType dynamicType : dynamicTypes)
            {
                values.add(new DropDownItem(dynamicType.getName(locale), dynamicType.getId()));
                idToClassification.put(dynamicType.getId(), dynamicType);
            }
            contentLeft.add(new DropDownInputField("Veranstaltungsart", new DropDownValueChanged()
            {
                @Override
                public void valueChanged(String newValue)
                {
                    DynamicType newDynamicType = idToClassification.get(newValue);
                    getPresenter().changeClassification(reservation, newDynamicType);
                }
            }, values));
        }
        boolean fillLeft = false;
        final Classification classification = reservation.getClassification();
        final Attribute[] attributes = classification.getAttributes();
        for (final Attribute attribute : attributes)
        {
            final String attributeName = attribute.getName(locale);
            final Object value = classification.getValue(attribute);
            final FlowPanel toAdd = fillLeft ? contentLeft : contentRight;
            fillLeft = !fillLeft;
            if (InputUtils.isAttributeInt(attribute))
            {
                toAdd.add(new LongInputField(attributeName, (Long) value, new LongValueChange()
                {
                    @Override
                    public void valueChanged(Long newValue)
                    {
                        getPresenter().changeAttribute(attribute, newValue);
                    }
                }));
            }
            else if (InputUtils.isAttributeString(attribute))
            {
                toAdd.add(new TextInputField(attributeName, (String) value, new TextValueChanged()
                {
                    @Override
                    public void valueChanged(String newValue)
                    {
                        getPresenter().changeAttribute(attribute, newValue);
                    }
                }));
            }
            else if (InputUtils.isAttributeDate(attribute))
            {
                toAdd.add(new DateComponent((Date) value, raplaLocale, new DateValueChanged()
                {
                    @Override
                    public void valueChanged(Date newValue)
                    {
                        getPresenter().changeAttribute(attribute, newValue);
                    }
                }));
            }
            else if (InputUtils.isAttributeBoolean(attribute))
            {
                toAdd.add(new BooleanInputField(attributeName, (Boolean) value, new BooleanValueChange()
                {
                    @Override
                    public void valueChanged(Boolean newValue)
                    {
                        getPresenter().changeAttribute(attribute, newValue);
                    }
                }));
            }
            else if (InputUtils.isCategory(attribute))
            {
                Category rootCategory = (Category) attribute.getConstraint(ConstraintIds.KEY_ROOT_CATEGORY);
                Boolean multipleSelectionPossible = (Boolean) attribute.getConstraint(ConstraintIds.KEY_MULTI_SELECT);
                final Map<String, Category> idToCategory = InputUtils.createIdMap(rootCategory);
                final Collection<DropDownItem> values = InputUtils.createDropDownItems(idToCategory, locale);
                toAdd.add(new DropDownInputField(attributeName, new DropDownValueChanged()
                {
                    @Override
                    public void valueChanged(String newValue)
                    {
                        final Category newCategory = idToCategory.get(newValue);
                        getPresenter().changeAttribute(attribute, newCategory);
                    }
                }, values, multipleSelectionPossible));
            }
            else if (InputUtils.isAllocatable(attribute))
            {

            }
        }

        contentPanel.add(contentLeft);
        contentPanel.add(contentRight);
    }

    public void clearContent()
    {
        contentPanel.clear();
    }
}