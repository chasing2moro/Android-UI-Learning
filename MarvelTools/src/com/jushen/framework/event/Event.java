package com.jushen.framework.event;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.jushen.framework.commondef.IncubateInterface;
import com.jushen.utils.log.LoggerUtils;

//reference from http://blog.csdn.net/yanshujun/article/details/6494447
public class Event implements IncubateInterface{   
 
    private Object object;   
 
    private String methodName;   

    public Event(){   

    }   
    public Event(Object object,String methodName){   
    	Init(object, methodName); 
    }   
    
    public void Init(Object vObject, String vMethodName) {
    	
		if (_ParamClass == null) {
			_ParamClass = com.jushen.framework.event.EventArg.class;
		}
    	
        this.object = vObject;   
        this.methodName = vMethodName;  
	}
    
    public Object getObjet(){
    	return object;
    }

    
    public String getmethodName() {
		return methodName;
	}
 
    public boolean isTheSameEvent(Object vObject, String vMethodName) {
		if(object == vObject && methodName == vMethodName){
			return true;
		}else {
			return false;
		}
	}
       
    static Class<?> _ParamClass;
    public Object invoke(EventArg vParam){   
        Method method = null;
		try {
			//
			method = object.getClass().getMethod(methodName, _ParamClass);//vParam.getClass());
		} catch (NoSuchMethodException e) {
			LoggerUtils.e(object.getClass().toString() + " can not find Method:" + methodName + "(" + _ParamClass.toString() + ") {function name is not match or is not public}");
			e.printStackTrace();
		}   
		
        if(null==method){   
        	LoggerUtils.e("Find method:" + methodName + " encounter error");
            return null;   
        }   
        
        try {
        	//
        	return method.invoke(object, vParam);
        } catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}   
        
        return null;
    }
	@Override
	public void clear() {
		// TODO Auto-generated method stub
		object = null;
		methodName = "";
	}   
}  