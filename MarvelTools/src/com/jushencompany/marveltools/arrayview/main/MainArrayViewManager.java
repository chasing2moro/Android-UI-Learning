package com.jushencompany.marveltools.arrayview.main;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.jushen.utils.log.LoggerUtils;

public class MainArrayViewManager {
	private static MainArrayViewManager _Instance;
	public static MainArrayViewManager singleton(){
		if(_Instance == null)
			_Instance = new MainArrayViewManager();
		return _Instance;
	}
	
	private Map<MainArrayViewType, MainArrayViewBase> _viewType2View;
	public MainArrayViewBase getMainArrayView(MainArrayViewType vMainArrayViewType) {
		if(_viewType2View == null){
			_viewType2View = new HashMap<MainArrayViewType, MainArrayViewBase>();
		}
		
		MainArrayViewBase mainArrayView = _viewType2View.get(vMainArrayViewType);
		if(mainArrayView == null){
			switch (vMainArrayViewType) {
			case community:
				mainArrayView = new MainArraySubViewCommunity();
				break;
			case marveltools:
				mainArrayView = new MainArraySubViewMarvelTools();
				break;
			case discovery:
				mainArrayView = new MainArraySubViewDiscovery();
				break;
			case about_me:
				mainArrayView = new MainArraySubViewAboutMe();
				break;
			default:
				LoggerUtils.e("unhandle type:" + vMainArrayViewType);
				break;
			}
			_viewType2View.put(vMainArrayViewType, mainArrayView);
		}
		
		return mainArrayView;
	}
	
	
	public MainArrayViewBase getRawMainArrayView(MainArrayViewType vMainArrayViewType) {
		if(_viewType2View == null){
			_viewType2View = new HashMap<MainArrayViewType, MainArrayViewBase>();
		}
		
		MainArrayViewBase mainArrayView = _viewType2View.get(vMainArrayViewType);
		if(mainArrayView == null){
			return null;
		}
		
		return mainArrayView;
	}
}
