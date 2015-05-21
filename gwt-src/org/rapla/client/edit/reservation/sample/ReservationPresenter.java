package org.rapla.client.edit.reservation.sample;

import org.rapla.client.edit.reservation.ReservationController;
import org.rapla.client.edit.reservation.AttributeValue.AttributeValues;
import org.rapla.client.edit.reservation.sample.ReservationView.Presenter;
import org.rapla.entities.Category;
import org.rapla.entities.domain.Reservation;
import org.rapla.entities.dynamictype.Attribute;
import org.rapla.entities.dynamictype.AttributeType;
import org.rapla.entities.dynamictype.Classification;
import org.rapla.entities.dynamictype.ConstraintIds;
import org.rapla.entities.dynamictype.DynamicType;
import org.rapla.entities.dynamictype.DynamicTypeAnnotations;
import org.rapla.facade.ClientFacade;
import org.rapla.facade.Conflict;
import org.rapla.framework.RaplaException;
import org.rapla.framework.RaplaLocale;
import org.rapla.framework.logger.Logger;

import com.gargoylesoftware.htmlunit.javascript.host.Event;
import com.google.gwt.user.client.ui.CheckBox;

import de.vksi.c4j.ContractReference;

import javax.inject.Inject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Each Reservation has a classification, some resources(allocatable) and appointments
 * Classifications kinds are : Reservation, Resource, Person.
 * Each Classification has a type : f.e. a resource can be type: course
 * A Classification for a reservation is classified as a reservation
 * A Appointment is a kind of timeline (from x to y)
 * A Allocatable has to be classified as resource and hold some attributes
 */
@ContractReference(ReservationPresenterContract.class)
public class ReservationPresenter implements ReservationController, Presenter {

    @Inject
    Logger logger;
    @Inject
    RaplaLocale raplaLocale;
    @Inject
    ClientFacade facade;

    private ReservationView view;
    private AppointmentPresenter appointmentPresenter;
    private Reservation reservation;
    boolean isNew;

    String tabName = "Termin- und Ressourcenplanung";
    

    @Inject
    public ReservationPresenter(ReservationView view, AppointmentPresenter appointmentPresenter) {
        this.view = view;
        view.setPresenter(this);
        this.appointmentPresenter = appointmentPresenter;
        view.addSubView(tabName, appointmentPresenter.getView());
    }

    @Override
    public void edit(final Reservation event, boolean isNew) {
        this.reservation = event;
        this.isNew = isNew;
        appointmentPresenter.setReservation(event);
        view.show(event);
    }
    

    @Override
    public void onSaveButtonClicked() {
        logger.info("save clicked");
        try {
            facade.store(reservation);
        } catch (RaplaException e1) {
            logger.error(e1.getMessage(), e1);
        }
        view.hide();
    }

    public String getCurrentReservationName(Locale locale) {
        return reservation.getName(locale);
    }

    @Override
    public void onDeleteButtonClicked() {
        logger.info("delete clicked");
        try {
            facade.remove(reservation);
        } catch (RaplaException e1) {
            logger.error(e1.getMessage(), e1);
        }
        view.hide();
    }

    @Override
    public void onCancelButtonClicked() {
        logger.info("cancel clicked");
        view.hide();
    }

    /**
     * @return all "Veranstaltungstypen" eventTypes, null if error
     */
    @Override
    public DynamicType[] getAllEventTypes() {
        try {
            return facade.getDynamicTypes("reservation");
        } catch (RaplaException e) {
            logger.error("error while using facade: ", e);
        }
        return null;
    }

    /**
     * @param neededCategory Studiengaenge, Benutzergruppen....
     * @return null if error
     */
    public Category[] getCategory(Locale locale, String neededCategory) {
        Category courseCategory = null;

        Category superCategory = facade.getSuperCategory();
        Category[] categories = superCategory.getCategories();
        for (Category category : categories) {
            if (category.getName(locale).equals(neededCategory)) {
                courseCategory = category;
            }
        }
        if (courseCategory == null) {
            logger.error("there is no : " + neededCategory);
        }

        if (courseCategory != null) {
            return courseCategory.getCategories();
        } else return null;
    }
    
    //von Yvonne//
    
    public Category[] getCategories(){
    	Category superCategory = facade.getSuperCategory();
    	Category[] categories = superCategory.getCategories();
    	
    	if(categories == null){
    		logger.error("there are no categories");	
    	}
    	
		return categories;
    }


    @Override
    public void changeReservationName(String newName) {
        logger.info("Name changed to " + newName);
        Classification classification = reservation.getClassification();
        Attribute attribute = classification.getType().getAttributes()[0];
        classification.setValue(attribute, newName);
    }
    


    /**
     * for now you have to save the original type and not as string or smth similiar
     * TODO: need a way to get the type and only save the specific type etc.., for now its only objects and that's not save
     * @param attributeNames input a map of string and the corresponding object --> searching for the object in the attributes and change the value
     *
     */
    public void changeAttributesOfCLassification(Map<String, Object> attributeNames, Locale locale) {
        logger.info("adding number of attributes: " + attributeNames.size());

        Classification classification = reservation.getClassification();
        DynamicType type = classification.getType();
        Attribute[] attributes = type.getAttributes();
        for (Attribute attribute : attributes) {
            for (Map.Entry<String, Object> entry : attributeNames.entrySet()) {
                if (entry.getKey().equalsIgnoreCase((attribute.getName(locale)))) {
                    classification.setValue(attribute, entry.getValue());
                }
            }
        }

    }

    public Attribute[] getAllCurrentAttributes() {
        return reservation.getClassification().getAttributes();
    }

    /**
     * @return all current Values as a String
     */
    public List<String> getAllCurrentAttributesAsStrings(Locale locale) {
        List<String> list = new ArrayList<>();
        Classification classification = reservation.getClassification();
        DynamicType type = classification.getType();
        for (Attribute attribute : type.getAttributes()) {
            String valueAsString = classification.getValueAsString(attribute, locale);
            if (valueAsString == null || valueAsString.isEmpty()) {
                valueAsString= "not defined yet";
            }

            list.add(attribute.getName() + " : " + valueAsString + " Typ: " + attribute.getType().name());
        }
        list.add("");
        for (Attribute attribute : type.getAttributes()) {
            Object constraint = attribute.getConstraint(ConstraintIds.KEY_ROOT_CATEGORY);
            list.add(String.valueOf(constraint));
        }

        list.add("");
        Category superCategory = facade.getSuperCategory();
        Category[] categories = superCategory.getCategories();
        for (Category category : categories) {
            list.add(category.getKey());
        }
        logger.info("all attributes length: " + list.size());
        return list;
    }
    
    

    public AttributeValues getCurrentAttributesOfReservationWithValues() {
    			
               
                Map<Attribute, Object> valuesToGet = new HashMap<Attribute, Object>();
                Map<Attribute, Collection<Object>> attributeCollectionMap = new HashMap<Attribute, Collection<Object>>();
                List<Object> objectList = new LinkedList<Object>();
                
    			DynamicType [] eventTypes = getAllEventTypes();
            	final Category[] allCategories = getCategories();
            	Category categoryForAttribute = null;
            	
            	for(DynamicType eventType : eventTypes){
            		
            			
            			for(Attribute attribute : eventType.getAttributes()){
            				
            				if (attribute.getType().equals(AttributeType.CATEGORY)) {
            				
            					categoryForAttribute = (Category) attribute.getConstraint(ConstraintIds.KEY_ROOT_CATEGORY);
            				
            					for(Category mainCategory : allCategories){
            					
            					if(mainCategory.getName().equals(categoryForAttribute.getName())){
            						
            						if(mainCategory.getCategories() != null){
            							
            							for(Category subCategory : mainCategory.getCategories()){
                								
                								valuesToGet.put(attribute, subCategory);
            							
                								if(!subCategory.getCategories().equals(null)){
            								                							
                									for(Category subSubCategory : subCategory.getCategories()){
            								
            												objectList.add(subSubCategory);
            											
            										}
            									
                									attributeCollectionMap.put(attribute, objectList);
            									
            									}
            								}
            								
            							}
            							
            						}
            						
            						
            					}
            					
            				}
            					
            			}
            			
            		}		
            	AttributeValues attributeValues = new AttributeValues(valuesToGet, attributeCollectionMap);
            	return attributeValues;
            				
    }

    /**
     * Each DynamicType (Lehrveranstaltung, Prüfung etc) has N Attributes (Titel, Sprache etc)
     * With the Map u can give each attribute a new value
     * overwrites current values
     *
     * @param valuesToSave           a Map with the attribute and a value, IT OVERWRITES ALL CURRENT ATTRIBUTES, SO SAVE NAME TOO
     * @param attributeCollectionMap a Map with the attribute and a ValueCollection (f.e. attribute: Studiengang has values Arztassitent, Wirtschaftsinformatij, Informatik)
     * @param attributeCollectionMap give NULL if not needed, wont be saved then
     */
    public void setAttributesOfReservation(Map<Attribute, Object> valuesToSave, Map<Attribute, Collection<Object>> attributeCollectionMap) {

        Classification classification = reservation.getClassification();
        DynamicType type = classification.getType();
        Classification newClassification = type.newClassification();
        logger.info("saving Map:" + valuesToSave.toString() + "and size:" + valuesToSave.size());
        for (Map.Entry<Attribute, Object> stringObjectEntry : valuesToSave.entrySet()) {
            newClassification.setValue(stringObjectEntry.getKey(), stringObjectEntry.getValue());
        }
        if (attributeCollectionMap != null) {
            for (Map.Entry<Attribute, Collection<Object>> attributeCollectionEntry : attributeCollectionMap.entrySet()) {
                newClassification.setValues(attributeCollectionEntry.getKey(), attributeCollectionEntry.getValue());
            }
        }
        reservation.setClassification(newClassification);
        logger.info("new Classification" + Arrays.toString(newClassification.getAttributes()));
    }

    /**
     * change String, but it will be added as parameter!
     */
    public String getEventType(Locale locale) {
        if (!isNew) {
            Classification classification = reservation.getClassification();
            logger.info("returning Eventtype: " + classification.getType().getName());
            return "current Event Type: " + classification.getType().getName(locale);
        }
        return "is new";

    }
    
    public void eventTypeChanged(DynamicType eventType) {
    	try{
    		Classification oldClassification = reservation.getClassification();
    		Classification newClassification;
    		DynamicType[] eventTypes = facade.getDynamicTypes(DynamicTypeAnnotations.VALUE_CLASSIFICATION_TYPE_RESERVATION);
    		
    		for(DynamicType eventType_it : eventTypes){
    			if(eventType_it.getKey().equalsIgnoreCase(eventType.getKey())){
    				newClassification = eventType.newClassification(oldClassification);
    				reservation.setClassification(newClassification);
    			}
    		}
    		
    		
    	}
    	catch (RaplaException e1) {
    		logger.error( e1.getMessage(), e1);
    	}
    }
    
    
    


    /**
     * returns true if the user clicked on the "+" button
     * @return if the event is new or not
     */

    public boolean getIsNew(){
        return isNew;
    }

    @Override
    public boolean isDeleteButtonEnabled() {
        return !isNew;
    }


	/*@Override
	public Category[] getCategoryAttributes(Locale locale, String neededCategory) {
		// TODO Auto-generated method stub
		return null;
	}*/

	public Object getCategoryAttributes(Locale locale, String neededCategory) {
		// TODO Auto-generated method stub
		return null;
	}

}
