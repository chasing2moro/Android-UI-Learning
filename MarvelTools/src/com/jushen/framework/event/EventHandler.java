package com.jushen.framework.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;







import com.jushen.utils.Incubate;
import com.jushen.utils.log.LoggerUtils;


public class EventHandler {
    //����һ��List   
    private Map<String, List<Event>> _id2EventNameList;
    Incubate<Event> _EventIncubater = new Incubate<Event>();
       
    public EventHandler(){   
    	 _id2EventNameList = new HashMap<String, List<Event>>();
      //  objects=new ArrayList<Event>();   
    }   

    
    public void addEvent(String vEventName, Object vObject,String vMethodName){   
    	Event aEvent = _EventIncubater.dequeue();
    	if(aEvent == null) aEvent = new Event(vObject, vMethodName);
    	else aEvent.Init(vObject, vMethodName);
    	
    	List<Event> aList = findEventList(vEventName);
    	if(aList == null){
    		aList = new ArrayList<Event>();
    		_id2EventNameList.put(vEventName, aList);
    	}
    	
    	for (Event event : aList) {
			if(event.isTheSameEvent(vObject, vMethodName)){
				LoggerUtils.e(LoggerUtils.TAG, vEventName + " for " + vObject.toString()
						+ " aleady Added(" + vMethodName + ")");
				return;
			}
		}
    	
    	//LoggerUtils.i("add :" + vEventName);
    	aList.add(aEvent);
    }   
    
    public boolean removeEvent(String vEventName, Object vObject, String vMethodName){
    	List<Event> findList = findEventList(vEventName);
    	if(findList == null){
    		LoggerUtils.e(LoggerUtils.TAG, "removeEvent " + vEventName + " from " + vObject.toString() + " but Eventlist is empty");
    		return false;
    	}
    	Event removeEvent = findEvent(vEventName, vObject, vMethodName);
    	if(removeEvent == null){
    		LoggerUtils.e(LoggerUtils.TAG, "removeEvent " + vEventName + " from " + vObject.toString() + " but it has been removed");
    		return false;
    	}else {
    		_EventIncubater.enqueue(removeEvent);
    		findList.remove(removeEvent);
			return true;
		}
    }
    
    Event findEvent(String vEventName, Object vObject, String vMethodName){
    	List<Event> findList = findEventList(vEventName);
    	if(findList == null){
    		return null;
    	}
    	Event findEVent = null;
    	for (Event event : findList) {
    		//LoggerUtils.i("EObject:" + event.getObjet() + " EMethodName:" + event.getmethodName());
    		//LoggerUtils.i("vObject:" + vObject + " vMethodName:" + vMethodName);
    		if(event.isTheSameEvent(vObject, vMethodName)){
    			//LoggerUtils.i("Set vObject:" + vObject);
    			findEVent = event;
    			break;
    		}
		}
    	return findEVent;
    }
    
    List<Event> findEventList(String vEventName){
    	List<Event> findList = _id2EventNameList.get(vEventName);
    	return findList;
    }
    
    //֪ͨ���еĶ���ִ��ָ�����¼�   
    public Object fireEvent(String vEventName, EventArg vEventArg){ 
    	Object returnObject = null;
    	List<Event> findEventList = findEventList(vEventName);
    	if(findEventList != null){
    		for (Event event : findEventList) {
    			Object aObject = event.invoke(vEventArg);
    			if(returnObject == null){
    				returnObject = aObject;
    			} 
			}
    		if(findEventList.size() == 0){
    			LoggerUtils.i(LoggerUtils.TAG, vEventName + " count is 0, when fireEvent");
    		}
    	}else {
			LoggerUtils.i(LoggerUtils.TAG, "Your havn't register the " + vEventName + ", when fireEvent");
		}
    	return returnObject;
    }  
}
