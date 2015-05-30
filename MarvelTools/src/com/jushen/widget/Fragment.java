package com.jushen.widget;

import java.util.EventListener;

import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventRegister;
import com.jushen.framework.event.Facade;
import com.jushen.utils.log.LoggerUtils;

public class Fragment extends android.app.Fragment implements EventRegister
{
	@Override
    public void onResume() {
        super.onResume();
        onRegistEvent();
    }
	
	@Override
	public void onPause() {
		super.onPause();
		onUnRegistEvent();
	};

	@Override
	public void onRegistEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		
	}
	
	protected void registEvent(String vEventName, String vMethodName){
		Facade.singleton().registEvent(vEventName, this, vMethodName);
	}

	protected void unRegistEvent(String vEventName, String vMethodName){
		Facade.singleton().unRegistEvent(vEventName, this, vMethodName);
	}
	
	protected void sendEvent(String vEventName, EventArg vEventArg) {
		Facade.singleton().sendEvent(vEventName, vEventArg);
	}
}
