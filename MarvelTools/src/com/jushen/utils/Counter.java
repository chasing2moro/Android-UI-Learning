package com.jushen.utils;

import com.jushen.utils.log.LoggerUtils;

public class Counter {
	private int _counter = 0;
	
	public void retain() {
		++_counter;
	}
	
	public void release() {
		if(--_counter < 0){
			LoggerUtils.e("Error Use Coutner, _counter fall to:" + _counter);
		}
		
	}
	
	public boolean isFree() {
		//return _counter == 0;
		return _counter <= 0;
	}
}
