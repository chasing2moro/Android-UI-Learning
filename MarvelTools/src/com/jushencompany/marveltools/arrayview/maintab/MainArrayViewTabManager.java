package com.jushencompany.marveltools.arrayview.maintab;

import java.util.HashMap;
import java.util.Map;

import com.jushen.utils.log.LoggerUtils;
import com.jushencompany.marveltools.R;
import com.jushencompany.marveltools.arrayview.main.MainArrayViewType;

import android.widget.ImageButton;

public class MainArrayViewTabManager {
	private static MainArrayViewTabManager _Instance;
	public static MainArrayViewTabManager singleton() {
		if(_Instance == null)
			_Instance = new MainArrayViewTabManager();
		return _Instance;
	}
	
	
	private Map<MainArrayViewType, MainArrayViewTabBase> _viewType2ViewTab;
	public MainArrayViewTabBase getMainArrayViewTab(MainArrayViewType vMainArrayViewType) {
		if(_viewType2ViewTab == null){
			_viewType2ViewTab = new HashMap<MainArrayViewType, MainArrayViewTabBase>();
		}
		
		MainArrayViewTabBase mainArrayViewTab = _viewType2ViewTab.get(vMainArrayViewType);
		if(mainArrayViewTab == null){
			//#warning tmp
			mainArrayViewTab = new MainArrayViewTabBase();
			_viewType2ViewTab.put(vMainArrayViewType, mainArrayViewTab) ;
		}
		
		return mainArrayViewTab;
	}
	
//	public MainArrayViewTabBase getMainArrayViewTabByIndex(int vIndex) {
//		MainArrayViewType mainArrayViewType;
//		switch (vIndex)
//		{
//		case 0:
//			mainArrayViewType = MainArrayViewType.community;
//			break;
//		case 1:
//			mainArrayViewType = MainArrayViewType.marveltools;
//			break;
//		case 2:
//			mainArrayViewType = MainArrayViewType.discovery;
//			break;
//		case 3:
//			mainArrayViewType = MainArrayViewType.about_me;
//			break;
//		default:
//			LoggerUtils.e("unhandle type:" + vIndex);
//			mainArrayViewType = MainArrayViewType.community;
//			break;
//		}
//		return getMainArrayViewTab(mainArrayViewType);
//	}
}
