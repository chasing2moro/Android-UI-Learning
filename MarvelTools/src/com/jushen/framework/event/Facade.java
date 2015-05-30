package com.jushen.framework.event;

public class Facade {

	// Use this for initialization
	EventHandler _EventHandler = new EventHandler();
	
	volatile static Facade _Interface;
	public static Facade singleton()
	{
		if (_Interface == null) 
		{
			_Interface = new Facade();	
		}
		return _Interface;
	}

	public void registEvent(String vEventName, Object vObject, String vMethodName)
	{	
		_EventHandler.addEvent(vEventName, vObject, vMethodName);
	}
	
	public void unRegistEvent(String vEventName, Object vObject, String vMethodName)
	{	
		_EventHandler.removeEvent(vEventName, vObject, vMethodName);
	}
	
	public Object sendEvent(String vEventName, EventArg vEventArg)
	{
		return _EventHandler.fireEvent(vEventName, vEventArg);
	}
}
