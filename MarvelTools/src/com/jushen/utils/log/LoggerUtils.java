package com.jushen.utils.log;

import android.util.Log;

public class LoggerUtils {
	public static String TAG = "com.jushencompany.marveltools";
	public static void setTag(String vTag) {
		TAG = vTag;
	}
	
	static LogListener _LogListener;
	public static void setListener(LogListener vLogListener){
		_LogListener = vLogListener;
	}
	
	public static void i(String vMsg){
		i(TAG, vMsg);
	}
	public static void w(String vMsg){
		w(TAG, vMsg);
	}
	public static void e(String vMsg){
		e(TAG, vMsg);
	}
	
	public static void i(String vTag, String vMsg){
		Log.i(vTag, vMsg);
		if(_LogListener != null)
			_LogListener.iListener(vMsg);
	}
	public static void w(String vTag, String vMsg){
		Log.w(vTag, vMsg);
		if(_LogListener != null)
			_LogListener.wListener(vMsg);
	}
	public static void e(String vTag, String vMsg){
		Log.e(vTag, vMsg);
		
		StackTraceElement[] printStackTraceElements = null;
		try {
			throw new Exception("");
		} catch (Exception e) {
			// TODO: handle exception
			//e.printStackTrace();
			printStackTraceElements = e.getStackTrace();
		}
		
		if(_LogListener != null)
			_LogListener.eListener(vMsg + LogController.S_NewLiner + LoggerUtilsHelper.getMsgByStackTraceElementArray(printStackTraceElements));
	}
	
	public static void iConsleOnly(String vMsg){
		iConsleOnly(TAG, vMsg);
	}
	public static void wConsleOnly(String vMsg){
		wConsleOnly(TAG, vMsg);
	}
	public static void eConsleOnly(String vMsg){
		eConsleOnly(TAG, vMsg);
	}
	
	public static void iConsleOnly(String vTag, String vMsg){
		Log.i(vTag, vMsg);
	}
	public static void wConsleOnly(String vTag, String vMsg){
		Log.w(vTag, vMsg);
	}
	public static void eConsleOnly(String vTag, String vMsg){
		Log.e(vTag, vMsg);
	}
	
	static class LoggerUtilsHelper{
		public static String getMsgByStackTraceElement(StackTraceElement vStackTraceElement) {
			if(LoggerUtils.class.toString().contains(vStackTraceElement.getClassName())){
				return "";
			}
			return vStackTraceElement.getClassName() + " [" + vStackTraceElement.getMethodName() + "] line:" + vStackTraceElement.getLineNumber();
		}
		
		static String tmpStr;
		public static String getMsgByStackTraceElementArray(StackTraceElement[] vStackTraceElements) {
			String retString = "";
			if(vStackTraceElements == null)
				return retString;
			for (int i = 0; i < vStackTraceElements.length; i++) {
				tmpStr =  getMsgByStackTraceElement(vStackTraceElements[i]);
				if(tmpStr.isEmpty()) continue;
				retString += tmpStr + LogController.S_NewLiner;
			}
			return retString;
		}
	}
}