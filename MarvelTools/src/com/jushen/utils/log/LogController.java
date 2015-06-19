package com.jushen.utils.log;

import java.util.Calendar;

import com.jushen.framework.event.EventArg;
import com.jushen.framework.event.EventName;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.View;

//��־ϵͳ ��ʼ��
//_LogController = LogController.Singleton();
//_LogController.Init(getApplicationContext());
//LoggerUtils.setListener(_LogController);
//_LogController.showLog();

public class LogController extends LogConsole implements LogListener{
public static String S_NewLiner = "<br/>";
	public LogController() {
		_calendar = Calendar.getInstance();
	}
	public static LogController _interface;
	public static LogController singleton(){
		if(_interface == null){
			_interface = new LogController();
		}
		return _interface;
	}
  
	@Override
	public void onRegistEvent() {
		// TODO Auto-generated method stub
		registEvent(EventName.LogController_ChangeTop, "onHandleChageTop");
		registEvent(EventName.LogController_Show, "onHandleShow");
	}
	

	@Override
	public void onUnRegistEvent() {
		// TODO Auto-generated method stub
		unRegistEvent(EventName.LogController_ChangeTop, "onHandleChageTop");
		unRegistEvent(EventName.LogController_Show, "onHandleShow");
	}
	
	public Object onHandleChageTop(EventArg vEventArg){
		int top = vEventArg.getInt("top", -1);
		setTop(top);
		return null;
	}
	
	public Object onHandleShow(EventArg vEventArg){
		showLog();
		return null;
	}

	
	String _showMsg = "";
	Calendar _calendar;

	@Override
	public void showLog() {
		super.showLog();
		setText(_showMsg);
	}

	@Override
	public void iListener(String vMsg) {
		// TODO Auto-generated method stub
		showMsgAppendMsg("<font color='green'>(i)</font>" + vMsg);
		setText(_showMsg);
	}

	@Override
	public void wListener(String vMsg) {
		// TODO Auto-generated method stub
		showMsgAppendMsg("<font color='#FF7F00'>(w)</font>" + vMsg);
		setText(_showMsg);
	}

	@Override
	public void eListener(String vMsg) {
		// TODO Auto-generated method stub
		showMsgAppendMsg("<font color='red'>(e)</font>" + vMsg);
		setText(_showMsg);
	}
	

	int _tmpHour;
	int _tmpMin;
	int _tmpSec;
	final static String timeSeperator =  ":";
	void showMsgAppendMsg(String vMsg){
		_calendar.setTimeInMillis(System.currentTimeMillis());
		_tmpHour = _calendar.get(Calendar.HOUR_OF_DAY);
		_tmpMin = _calendar.get(Calendar.MINUTE);
		_tmpSec = _calendar.get(Calendar.SECOND);
		_showMsg = vMsg + "<font color='gray'>(" + _tmpHour + timeSeperator + _tmpMin + timeSeperator + _tmpSec + ")</font>" +
				S_NewLiner + S_NewLiner + _showMsg;
		if(_showMsg.length() > 5000){
			_showMsg = _showMsg.substring(0, 5000);
		}
	}
	
	@Override
	protected void ClearButtnClicked(){
		_showMsg = "";
		setText(_showMsg);
	}
}
