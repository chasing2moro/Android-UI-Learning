package com.jushencompany.marveltools;

import com.jushen.sdk.weibo.utils.JasonParserUtils;
import com.jushen.utils.AsyncImageLoaderPlus;
import com.jushen.utils.CommonUtils;
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
	
	private AsyncImageLoaderPlus _asyncImageLoaderPlus;
	private CommonUtils _commonUtils;
	private JasonParserUtils _JasonParserUtils;
	
	private boolean _isInit = false;
	public void Main(Context vContext) {
		if(_isInit == true)
			return;
		
		_isInit = true;
		
		_context = vContext;
		
		LogController.singleton().Init(_context.getApplicationContext());
		LoggerUtils.setListener(LogController.singleton());
		
		_asyncImageLoaderPlus = new AsyncImageLoaderPlus(_context);
		_commonUtils = new CommonUtils();
		_JasonParserUtils = new JasonParserUtils();
		
		registEvent();
	}
	
	public void registEvent() {
		LogController.singleton().fireRegistEvent();
		
		_asyncImageLoaderPlus.fireRegistEvent();
		_commonUtils.fireRegistEvent();
		_JasonParserUtils.fireRegistEvent();
	}
	
	public void unRegistEvent() {
//		LogController.singleton().fireUnRegistEvent();
//		
//		_asyncImageLoaderPlus.fireUnRegistEvent();
//		_commonUtils.fireUnRegistEvent();
//		_JasonParserUtils.fireUnRegistEvent();
	}
}
