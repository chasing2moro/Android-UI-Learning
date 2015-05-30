package com.jushencompany.marveltools;

import com.jushen.utils.log.LogController;
import com.jushen.utils.log.LoggerUtils;

import android.content.Context;

public class MainEntrance {
	private static MainEntrance _instance;
	public static MainEntrance singleton() {
		if(_instance == null){
			_instance = new MainEntrance();
		}
		return _instance;
	}
	
	private Context _context;
	public void Main(Context vContext) {
		_context = vContext;
		
		LogController.singleton().Init(_context.getApplicationContext());
		LoggerUtils.setListener(LogController.singleton());
	}
}
