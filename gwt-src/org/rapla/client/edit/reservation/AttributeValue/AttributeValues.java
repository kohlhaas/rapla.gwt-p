package org.rapla.client.edit.reservation.AttributeValue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.rapla.entities.dynamictype.Attribute;

public class AttributeValues {
  	 
    private Map<Attribute, Object> valuesToGet = new HashMap<Attribute, Object>();
    private Map<Attribute, Collection<Object>> attributeCollectionMap = new HashMap<Attribute, Collection<Object>>();
   
    public AttributeValues ( Map<Attribute, Object> valuesToGet, Map<Attribute, Collection<Object>> attributeCollectionMap){
       
        this.valuesToGet = valuesToGet;
        this.attributeCollectionMap = attributeCollectionMap;
       
    }
 
    public Map<Attribute, Object> getvaluesToGet() {
        return valuesToGet;
    }
 
    public Map<Attribute, Collection<Object>> getattributeCollectionMap() {
        return attributeCollectionMap;
    }
   
}
