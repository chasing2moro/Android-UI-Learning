package com.jushen.framework.event;

public class Controller implements EventRegister{

	public Controller(){		

	}
	
	public void fireRegistEvent() {
		// TODO Auto-generated method stub
		onRegistEvent();
	}
	
	public void fireUnRegistEvent() {
		onUnRegistEvent();
	}

	@Override
	public void onRegistEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		
	}
	
	protected void registEvent(String vEventName, String vMethodName)
	{	
		Facade.singleton().registEvent(vEventName, this, vMethodName);
	}
	
	protected void unRegistEvent(String vEventName, String vMethodName)
	{	
		Facade.singleton().unRegistEvent(vEventName, this, vMethodName);
	}
	protected void sendEvent(String vEventName, EventArg vEventArg) {
		Facade.singleton().sendEvent(vEventName, vEventArg);
	}
}
